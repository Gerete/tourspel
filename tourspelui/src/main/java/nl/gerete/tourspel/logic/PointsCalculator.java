package nl.gerete.tourspel.logic;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.webapp.query.*;

import nl.gerete.tourspel.db.*;

/**
 * Utility class which can calculate points for any etappe.
 *
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 29, 2012
 */
public class PointsCalculator {
	/** #of scored riders on a list. */
	public static final int NUM_RIDERS = 10;

	private Etappe m_etappe;

	/** All riders indexed by PK that stopped. */
	private HashMap<Long, StoppedRider> m_stoppedRiderMap;

	/** All riders indexed by PK that stopped before the prologue. */
	private HashMap<Long, StoppedRider> m_stoppedBeforePrologueMap;

	private boolean m_isprologue;

	private Date m_prologueDate;

	private List<EtappeResult> m_resultList;

	private HashMap<Rider, Rider> m_replacementMap;

	private List<Rider> m_runningList;

	private int m_score;

	static public final class Score {
		private Rider m_rider;

		private int m_scorePoints;

		private int m_bonusScorePoints;

		private int m_etappePosition;

		private int m_guessedPosition;

		public Score(Rider rider, int score, int bonusScore, int etappePosition, int guessedPosition) {
			m_rider = rider;
			m_scorePoints = score;
			m_bonusScorePoints = bonusScore;
			m_etappePosition = etappePosition;
			m_guessedPosition = guessedPosition;
		}

		public Rider getRider() {
			return m_rider;
		}

		public int getScorePoints() {
			return m_scorePoints;
		}

		public void setScorePoints(int scorePoints) {
			m_scorePoints = scorePoints;
		}

		public void setBonusScorePoints(int bonusScorePoints) {
			m_bonusScorePoints = bonusScorePoints;
		}

		public int getBonusScorePoints() {
			return m_bonusScorePoints;
		}

		public int getEtappePosition() {
			return m_etappePosition;
		}

		public int getGuessedPosition() {
			return m_guessedPosition;
		}
	}

	@Nullable
	private List<Score> m_scoreList;

	@Nullable
	private List<Score> m_bonusScoreList;

	/** The PK's of all etappe winners linked to their 0-based etappe position. */
	private Map<Long, Integer> m_winnerMap;

	/** When needed this will contain the team list in order of team number as determined by the rider's number. It is only filled when needed to fill replacement lists. */
	@Nullable
	private List<Team> m_teamList;

	/** The datacontext used while calculating. */
	private QDataContext m_dc;

	/** While calculating this holds all of the playlist entries in ascending order. */
	private List<PlayListEntry> m_currentEntries;

	/** When replacing, the first "reserve" rider to use */
	private int m_currentReplacementIndex;

	private int m_riderIndex;

	private int m_teamIndex;

	private PlayList m_currentPlaylist;

	public void initialize(QDataContext dc, Etappe etappe) throws Exception {
		m_etappe = etappe;

		//-- Find the prologue
		if(etappe.getType() == EtappeType.Prologue) {
			m_isprologue = true;
			m_prologueDate = etappe.getDate();
		} else {
			List<Etappe> etlist = Objects.requireNonNull(etappe.getEdition()).getEtappeList(); // Get all etappes.
			for(Etappe et : etlist) {
				if(et.getType() == EtappeType.Prologue) {
					m_prologueDate = et.getDate();
					break;
				}
			}
		}
		if(null == m_prologueDate)
			throw new IllegalStateException("No prologue found");

		//-- Get the etappe result list. 0 = 1st place, 1=2nd place et al.
		m_resultList = m_etappe.getResultList(); // The results
		Collections.sort(m_resultList);
		if(m_resultList.size() < NUM_RIDERS)
			throw new IllegalStateException("The etappe result list must have " + NUM_RIDERS + " riders, it has only " + m_resultList.size());

		loadStoppedRiders(dc);

		m_stoppedBeforePrologueMap = new HashMap<Long, StoppedRider>();
		for(StoppedRider sr : m_stoppedRiderMap.values()) {
			if(sr.getStopDate().getTime() <= m_prologueDate.getTime()) {
				m_stoppedBeforePrologueMap.put(sr.getRider().getId(), sr);
			}
		}

		//-- Create a hashset of all etappe winners, to quickly find them when scoring.
		m_winnerMap = new HashMap<Long, Integer>(NUM_RIDERS);
		for(int etappePosition = 0; etappePosition < NUM_RIDERS; etappePosition++) {
			m_winnerMap.put(m_resultList.get(etappePosition).getRider().getId(), Integer.valueOf(etappePosition));
		}
	}

	/**
	 * Load all rides that have stopped before or in the etappe specified.
	 * @throws Exception
	 */
	private void loadStoppedRiders(QDataContext dc) throws Exception {
		List<StoppedRider> sl = dc.query(QCriteria.create(StoppedRider.class).eq("edition", getEdition()).le("stopDate", Objects.requireNonNull(m_etappe.getDate())));
		m_stoppedRiderMap = new HashMap<Long, StoppedRider>();
		for(StoppedRider sr : sl) {
			m_stoppedRiderMap.put(sr.getRider().getId(), sr); // Stopped rider by rider's PK
		}
	}

	/**
	 * Lazy-loads the Teams list with teams in ascending rider order, for assigning riders when a list does not have enough riders.
	 * @return
	 * @throws Exception
	 */
	private List<Team> getTeamList() throws Exception {
		if(null == m_teamList) {
			List<Team> teamList = m_dc.query(QCriteria.create(Team.class).eq("edition", getEdition()));		// Dummy load to prevent n+1 queries.
			List<Rider> riderList = m_dc.query(QCriteria.create(Rider.class).eq("edition", getEdition()).ascending("number"));

			//-- Create the real team list, in ascending order, and sort the team-riderlist too.
			Team last = null;
			teamList.clear();
			for(Rider r : riderList) {
				if(r.getTeam() == last)
					continue;
				last = r.getTeam();						// This is a new team...
				if(teamList.contains(last))
					throw new IllegalStateException("Rider number problem: team " + last + " has riders with wrong numbers...");
				teamList.add(last);
				Collections.sort(last.getRiders(), Rider.C_BYNUMBER);
			}
		}
		return m_teamList;
	}


	/**
	 * Men vult 10 renners in (verplicht) en hiermee speelt men het gehele tourspel. Je moet ook een aantal
	 * reserve renners opgeven (geen minimum, maximaal 15). getTeamListDeze zijn alleen van belang voor de start. Na de
	 * start van de proloog is de reserve lijst niet meer van belang. Waarom? Bij de start van de proloog
	 * wordt er gekeken welke renners er allemaal starten. Heb jij een renner op je lijst die niet start,
	 * dan wordt deze vervangen door een renner uit de reserve lijst.  Let op: valt een renner, die jij
	 * op je lijst hebt, uit tijdens de proloog, dan is deze renner gestart en wordt deze niet vervangen
	 * door een renner van de reserve lijst. Als er een renner uitvalt tijdens een ettape, dan hou je
	 * een open plek op de lijst. De overige renners blijven op de plek staan waar ze stonden. Ze
	 * schuiven dus niet op.
	 *
	 * <h1>Te weinig reserverrijders</h1>
	 * Als de lijst te weinig rijders heeft door uitval dan worden de rijders aangevuld afhankelijk van het type lijst:
	 *getTeamList
	 * <ul>
	 *	<li>Bij een klassementslijst worden ontbrekende rijders aangevuld door eerst alle nummer-1 rijders van
	 *		iedere ploeg toe te voegen (mits nog niet geselecteerd en niet uitgevallen). Daarna alle nummer-2's
	 *		van iedere ploeg, enzovoorts. De ploegen worden in volgorde van ploegnummer afgelopen.</li>
	 *
	 *	<li>Voor een poedellijst wordt hetzelfde mechanisme gebruikt maar in plaats van de eerste renner wordt eerst
	 *		iedere laatste renner gepakt, daarna de voorlaatste enz.</li>
	 * </ul>
	 *
	 *
	 * Puntentelling
	 * Bij elke etappe zijn de eerste 10 renners die finishen belangrijk. Hiervoor kun je punten krijgen
	 * als je ze op je lijst hebt staan. De punten worden als volgt verdeeld:
	 * De renner die de etappe wint levert 10 punten op. De renner die als tweede over de meet komt levert
	 * 9 punten op en dat loopt zo terug tot 1 punt voor de renner die als 10de over de meet komt. Heb je
	 * de renner ook op de goede plek staan, dan krijg je er 5 bonuspunten bij. Stel bijvoorbeeld dat
	 * Geesink in een etappe 4de wordt. Als je Geesink op je lijst hebt staan, dan krijg je hiervoor 7
	 * punten. Heb je op je lijst Geesink op de 4de plek staan, dan krijg je ook nog eens 5 bonuspunten.
	 * Totaal dus 12 punten.
	 * @throws Exception
	 *
	 */
	public void calculateScore(@NonNull QDataContext dc, @NonNull PlayList pl) throws Exception {
		if(m_etappe == null)
			throw new IllegalStateException("Call init first");
		m_replacementMap = new HashMap<Rider, Rider>();
		m_score = 0;
		m_dc = dc;

		calculateRunningList(pl);

		//-- Walk the user's list so we can score.
		List<Score> scores = m_scoreList = new ArrayList<Score>();
		m_bonusScoreList = new ArrayList<Score>();

		if(m_runningList.size() < 1) {
			scores = Collections.emptyList();
			m_bonusScoreList = Collections.emptyList();
		}
		for(int position = 0; position < NUM_RIDERS && position < m_runningList.size(); position++) {
			Rider r = m_runningList.get(position);			// Get the user's guess for this position.
			int score = 0;
			int bonusScore = 0;
			Integer epos = m_winnerMap.get(r.getId());		// This rider is in etappe results?
			if(null != epos) {
				//-- Scored. Count points,
				score = 10 - epos.intValue();				// Score is 10 for 1st place, 9 for 2nd etc

				//-- If this rider is also at the same place as guessed by the user- add 5 bonus points
				if(epos.intValue() == position) {
					bonusScore = 5;
					score += 5;
				}
				m_score += score;
			}
			scores.add(new Score(r, score, bonusScore, epos == null ? -1 : epos.intValue() + 1, position + 1));
		}

//		System.out.println("Resultaat:");
//		int ix = 1;
//		for(Score s : m_scoreList) {
//			System.out.println("#" + (ix++) + ": " + s.getRider().getDisplayName() + " in team: " + s.getRider().getTeam().getName() + " score " + s.getScore() + ": guessed " + s.getGuessedPosition()
//				+ ", actual " + s.getEtappePosition());
//		}

//		System.out.println(pl.getId() + ": total score " + m_score);
	}

	private void calculateRunningList(PlayList pl) throws Exception {
		m_currentPlaylist = pl;
		m_currentEntries = pl.getPlayListEntries();
		m_currentReplacementIndex = NUM_RIDERS;			// When replacing, the first "reserve" rider to use
		m_riderIndex = 0;
		m_teamIndex = 0;
		Collections.sort(m_currentEntries);							// Sort by ascending "position"

		if(m_currentEntries.size() < NUM_RIDERS)
			System.out.println(pl.getId() + ": list has only " + m_currentEntries.size() + " entries.");

		/*
		 * 1. Get the running list: the list after all replacements before the prologue are done.
		 */
		m_runningList = new ArrayList<Rider>();			// The list we'll run with.
		int pes = 0;
		int pxtra = NUM_RIDERS; 						// To 1st reserve position in playlist.
		for(PlayListEntry pe : m_currentEntries) {
			Rider rider = pe.getRider();
			if(rider == null)
				throw new IllegalStateException("The rider is not known?");
			StoppedRider sr = m_stoppedBeforePrologueMap.get(rider.getId());
			if(sr == null) {
				//-- Not stopped before prologue.
				m_runningList.add(rider);
			} else {
				//-- Stopped in prologue. Get a replacement.
				Rider replacement = calculateReplacement();
				m_replacementMap.put(rider, replacement); //
				m_runningList.add(replacement);			// Add replacement or null if no more available
			}
			if(m_runningList.size() >= NUM_RIDERS)
				break;
		}
	}

	private Rider calculateReplacement() throws Exception {
		while(m_currentReplacementIndex < m_currentEntries.size()) {
			PlayListEntry rpe = m_currentEntries.get(m_currentReplacementIndex++);	// Get 1st replacement
			Rider rider = rpe.getRider();
			if(rider == null)
				throw new IllegalStateException("The rider should not be null at this point!");
			if(null == m_stoppedBeforePrologueMap.get(rider.getId())) {
				//-- This one did not stop -> use it as a replacement.
				return rider;
			}
		}

		//-- Not enough replacement riders.. We need to walk the teams, depending on the "mode".
		while(m_riderIndex < 50) {
			while(m_teamIndex < getTeamList().size()) {
				Team tm = getTeamList().get(m_teamIndex);

				//-- Get the [riderindex] rider from the start or the end of the list.
				int realindex = m_currentPlaylist.getPlayListType() == PlayListType.KLASSEMENT ? m_riderIndex : tm.getRiders().size() - m_riderIndex - 1;
				m_teamIndex++;

				if(realindex < 0 || realindex >= tm.getRiders().size())
					continue;

				//-- Try this dude....
				Rider r = tm.getRiders().get(realindex);

				//-- He should not have stopped....
				if(m_stoppedRiderMap.containsKey(r.getId()))			// Stopped
					continue;

				//-- User should not already have this dude
				boolean exists = false;
				for(PlayListEntry pe : m_currentEntries) {
					Rider rider = pe.getRider();
					if(rider == null)
						throw new IllegalStateException("The rider should not be null here!");
					if(Objects.requireNonNull(rider.getId()).equals(r.getId())) {
						exists = true;
						break;
					}
				}

				if(!exists)
					return r;
			}

			m_riderIndex++;						// Go one rank higher / lower (all number 2's / all number 8's).
		}

		throw new IllegalStateException("I cannot find replacement riders!!");
	}


	private Edition getEdition() {
		return m_etappe.getEdition();
	}

	public int getScore() {
		return m_score;
	}

	public List<EtappeResult> getResultList() {
		return m_resultList;
	}

	public List<Score> getScoreList() {
		return m_scoreList;
	}

	public List<Score> getBonusScoreList() {
		return m_bonusScoreList;
	}

}

