package nl.gerete.tourspel.logic;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.meta.*;
import to.etc.smtp.*;
import to.etc.util.*;
import to.etc.webapp.mailer.*;
import to.etc.webapp.pendingoperations.*;
import to.etc.webapp.query.*;

import java.io.*;
import java.util.*;

/**
 * Batch thing to calculate points and do other stuff.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 26, 2012
 */
public class PointsCalculatorRun extends BaseSystemTask implements ISystemTask {
	private Etappe m_etappe;

	/** Calculate in runs of xxxx playlists @ a time. */
	private static final int RUNSIZE = 100;

	private PointsCalculator m_calculator = new PointsCalculator();

	private TourMailer m_tourMailer;

	PointsCalculatorRun(Etappe et) {
		m_etappe = et;
	}

	@Override
	public String getName() {
		return "Etappe doorrekenen";
	}

	private Edition getEdition() {
		return m_etappe.getEdition();
	}

	/**
	 * Calculate all scores for the current etappe. Because we need to mail the final ranking too we
	 * need to do this by passing all playlists twice:
	 * <ul>
	 *	<li>In run 1 we calculate the new total score for all playlists.</li>
	 *	<li>In run 2 we generate all emails, detailing the scores AND showing the current ranking of all playlists.</li>
	 * </ul>
	 * @see nl.tourspel.logic.ISystemTask#execute(to.etc.util.Progress)
	 */
	@Override
	public void execute(Progress p) throws Exception {
		try {
			m_etappe = dc().find(Etappe.class, m_etappe.getId()); // Reload in this session.
			if(m_etappe.getPhase() != EtappePhase.CALCULATING) {
				System.out.println(m_etappe + ": in phase " + m_etappe.getPhase() + ", no calculation possible");
				return;
			}

			//-- Prepare the email template.
			m_tourMailer = new TourMailer();

			//-- Prepare the results calculator.
			m_calculator.initialize(dc(), m_etappe);

			p.setTotalWork(2);
			Progress sub = p.createSubProgress("Calculate etappe score", 1);

			//-- Calculate all new scores.
			scoreAllLists(sub);
			sub.complete();

			//-- Get total ranking.
			loadRanking();

			sub = p.createSubProgress("Mail results", 1);
			mailResults(sub);
			sub.complete();

			//-- Everything done -> set etappe to done
			m_etappe = dc().find(Etappe.class, m_etappe.getId());
			m_etappe.setPhase(EtappePhase.CLOSED);
			dc().commit();
		} finally {
			silentCloseDc();
		}
	}

	private void mailResults(Progress p) throws Exception {
		List<Long> allList = getUnmailedLists();
		p.setTotalWork(allList.size());
		System.out.println("Mailing " + allList.size() + " playlists");

		int plix = 0;
		List<PlayList> reslist = new ArrayList<PlayList>();

		while(plix < allList.size()) {
			int plex = plix + RUNSIZE;
			if(plex > allList.size())
				plex = allList.size();

			/*
			 * Do a run. We will collect all playlists of a single user together, even if this causes
			 * us to exceed the run.
			 */
			Person currentUser = null;
			while(plix < plex) {
				//-- Collect all playlists of the current user.
				reslist.clear();
				while(plix < allList.size()) {
					Long theId = allList.get(plix);
					PlayList pl = dc().find(PlayList.class, theId);			// Get the playlist.
					if(pl == null)
						throw new IllegalStateException("The playlist with the following id was not found in the database?? " + theId);
					if(currentUser == null || MetaManager.areObjectsEqual(pl.getPerson(), currentUser)) {
						//-- New playlist or playlist of same person: add to list
						currentUser = pl.getPerson();
						reslist.add(pl);
						plix++;
					} else {
						//-- We have a new one... First calculate the previous run.
						emailUserScore(currentUser, reslist);
						p.increment(reslist.size());
						currentUser = null;
						reslist.clear();
						break;
					}
				}
			}
			if(reslist.size() > 0) {
				emailUserScore(currentUser, reslist);
				p.increment(reslist.size());
			}

			//-- Run completed!
			dc().commit(); 		// Commit this-run
			silentCloseDc();	// Close the session

			if(plix >= allList.size()) // Reached the end?
				break;
		}
	}

	static public final class RankItem {
		private int m_rank;

		private List<PlayList> m_playListList = new ArrayList<PlayList>();

		RankItem(int rank) {
			m_rank = rank;
		}

		private List<PlayList> getContenders() {
			return m_playListList;
		}

		public int getRank() {
			return m_rank;
		}
	}

	private List<RankItem> m_rankList = new ArrayList<RankItem>();

	private Map<Long, RankItem> m_rankMap = new HashMap<Long, RankItem>();

	private List<TopItem> m_topList = new ArrayList<TopItem>();

	static public final class TopItem {
		private int m_rank;

		private int m_score;

		private Person m_person;

		private PlayList m_playList;

		public TopItem(int rank, int score, Person person, PlayList playList) {
			m_rank = rank;
			m_score = score;
			m_person = person;
			m_playList = playList;
		}

		public int getRank() {
			return m_rank;
		}

		public int getScore() {
			return m_score;
		}

		public Person getPerson() {
			return m_person;
		}

		public PlayList getPlayList() {
			return m_playList;
		}
	}

	/**
	 * Load the ranking for all playlists.
	 * @throws Exception
	 */
	private void loadRanking() throws Exception {
		//-- Get all playlists by descending points.
		List<PlayList> res = dc().query(QCriteria.create(PlayList.class).eq("edition", getEdition()).descending("currentPoints"));
		int place = 0;
		long curscore = -1;
		RankItem rank = null;
		m_rankMap.clear();
		m_rankList.clear();
		for(PlayList pl : res) {
			long score = pl.getCurrentPoints();
			if(score != curscore) {
				//-- New "points" - increment place.
				place++;
				curscore = score;
				rank = new RankItem(place);
				m_rankList.add(rank);
			}

			if(rank == null)
				//What ish problem?
			rank.getContenders().add(pl);
			m_rankMap.put(pl.getId(), rank);
		}

		//-- Create the top-10 list
		m_topList.clear();
		for(RankItem ri : m_rankList) {
			for(PlayList pl : ri.getContenders()) {
				m_topList.add(new TopItem(ri.getRank(), (int) pl.getCurrentPoints(), pl.getPerson(), pl));
			}
			if(m_topList.size() >= 10)
				break;
		}
	}

	/**
	 *
	 * @param allList
	 */
	private void scoreAllLists(Progress p) throws Exception {
		List<Long> allList = getUncalculatedLists();
		p.setTotalWork(allList.size());
		System.out.println("Calculating " + allList.size() + " playlists");

		int plix = 0;
		while(plix < allList.size()) {
			int plex = plix + RUNSIZE;
			if(plex > allList.size())
				plex = allList.size();

			/*
			 * Do a run. We will collect all playlists of a single user together, even if this causes
			 * us to exceed the run.
			 */
			Person currentUser = null;
			while(plix < plex) {
				Long theId = allList.get(plix++);
				PlayList pl = dc().find(PlayList.class, theId);			// Get the playlist.
				if(pl == null)
					throw new IllegalStateException("The playlist with the following id was not found in the database?? " + theId);
				m_calculator.calculateScore(dc(), pl);
				pl.setLastScoredEtappe(m_etappe);
				pl.setCurrentPoints(pl.getCurrentPoints() + m_calculator.getScore());		// Increment score
				p.increment(1);
			}
			dc().commit();
			silentCloseDc();	// Close the session
		}
		p.complete();
	}


	/**
	 * Get all playlists to score, ordered by person.
	 * @return
	 * @throws Exception
	 */
	private List<Long> getUncalculatedLists() throws Exception {
		QSelection<PlayList> qs = QSelection.create(PlayList.class).eq("edition", m_etappe.getEdition()).selectProperty("id").ascending("person.id");
		qs.or().ne("lastScoredEtappe", m_etappe).isnull("lastScoredEtappe");

		List<Object[]> query = dc().query(qs);
		List<Long> res = new ArrayList<Long>(query.size());
		for(Object[] oar : query) {
			res.add((Long) oar[0]);
		}
		return res;
	}

	/**
	 * Get all playlists to email still.
	 * @return
	 * @throws Exception
	 */
	private List<Long> getUnmailedLists() throws Exception {
		QSelection<PlayList> qs = QSelection.create(PlayList.class).eq("edition", m_etappe.getEdition()).selectProperty("id").ascending("person.id");
		qs.or().ne("lastMailedEtappe", m_etappe).isnull("lastMailedEtappe");

		List<Object[]> query = dc().query(qs);
		List<Long> res = new ArrayList<Long>(query.size());
		for(Object[] oar : query) {
			res.add((Long) oar[0]);
		}
		return res;
	}


	/**
	 * Calculate the score(s) for all playlist(s) of a user, and mail the result.
	 * @param currentUser
	 * @param reslist
	 * @throws Exception
	 */
	private void emailUserScore(Person currentUser, List<PlayList> reslist) throws Exception {
		m_tourMailer.start(currentUser);				// Start, and set subject later.
		m_tourMailer.generate(getClass(), "etapperesult-welcome.tpl.html", "person", currentUser, "etappe", m_etappe, "result", m_calculator.getResultList());		// Welcome part.

		int toprank = Integer.MAX_VALUE;
		boolean shared = false;
		for(PlayList pl : reslist) {
			RankItem ri = m_rankMap.get(pl.getId());
			if(ri.getRank() < toprank) {
				toprank = ri.getRank();
				shared = ri.getContenders().size() > 1;
			}
			calculatePlaylist(currentUser, pl, ri);
		}

		m_tourMailer.generate(getClass(), "etapperesult-highscore.tpl.html", "person", currentUser, "etappe", m_etappe, "top", m_topList);


		String s = "[tourspel] Etappe-resultaat " + m_etappe.getDisplayName() + (shared ? ": gedeelde " : ": ") + toprank + "e plaats";
		s = StringTool.removeAccents(s);
		m_tourMailer.setSubject(s);
		m_tourMailer.send(dc());
	}

	/**
	 * Score, store and email.
	 * @param theId
	 */
	private void calculatePlaylist(Person who, PlayList pl, RankItem ri) throws Exception {
		m_calculator.calculateScore(dc(), pl);
		pl.setLastMailedEtappe(m_etappe);
		m_tourMailer.generate(getClass(), "etapperesult-playlist.tpl.html", "person", who, "etappe", m_etappe, "scoreList", m_calculator.getScoreList(), "list", pl, "score", m_calculator.getScore(),
			"rank", ri);		// Welcome part.
	}


	public static void main(String[] args) {
		try {
			Application.initDatabase(new File("WebContent/WEB-INF/" + DeveloperOptions.getString("tourspel", "tourspel.properties")));
			PollingWorkerQueue.initialize();
			BulkMailer.initialize(Application.getDataSource(), new SmtpTransport("localhost"));

			Etappe et = new Etappe();
			et.setId(Long.valueOf(50));

			new PointsCalculatorRun(et).test();

		} catch(Exception x) {
			x.printStackTrace();
		}
	}

	private void test() throws Exception {
		Progress p = new Progress(null);
		execute(p);

	}


}
