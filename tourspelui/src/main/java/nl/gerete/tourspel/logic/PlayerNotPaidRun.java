package nl.gerete.tourspel.logic;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import to.etc.smtp.*;
import to.etc.util.*;
import to.etc.webapp.mailer.*;
import to.etc.webapp.pendingoperations.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 *
 *
 *
 * @author <a href="mailto:ben.schoen@itris.nl">Ben Schoen</a>
 * Created on May 14, 2013
 */
public class PlayerNotPaidRun extends BaseSystemTask {
	List<PlayList> m_notPaidPlayLists;

	Map<Person, List<PlayList>> m_userNotPaidLists;

	private TourMailer m_tourMailer;

	private Edition m_edition;

	PlayerNotPaidRun(@NonNull Edition edition) {
		m_edition = edition;
	}

	@Override
	public String getName() {
		return "Dagtaken";
	}


	@Override
	public void execute(Progress p) throws Exception {
		try {
			m_edition = dc().find(Edition.class, Objects.requireNonNull(m_edition.getId())); // Reload in this session.
			if(m_edition.getPhase() != EditionPhase.OPEN) {
				System.out.println(m_edition + ": in phase " + m_edition.getPhase() + ", no email to send.");
				return;
			}
			findPlayListNotPaidFor();
			groupPlayListPerPerson();
			mailUsers();
		} finally {
			silentCloseDc();
		}
	}


	private void mailUsers() throws Exception {
		//zou je deze loop in deze methode doen, of zou je dit in de execute doen en
		//de persoon en playlist als argument meegeven?
		m_tourMailer = new TourMailer();
		for(Map.Entry<Person, List<PlayList>> entry : m_userNotPaidLists.entrySet()) {
			emailNotPaidLists(entry.getKey(), entry.getValue());
		}

	}

	private void emailNotPaidLists(Person currentUser, List<PlayList> playLists) throws Exception {
		m_tourMailer.start(currentUser);				// Start, and set subject later.

		m_tourMailer.generate(getClass(), "prestart-playlist-notpaid.tpl.html", "person", currentUser, "notPaidLists", playLists, "daysLeft", Integer.valueOf(EditionBP.getDaysToStartEdition(dc())));

		m_tourMailer.setSubject("Foei! Je heb nog niet alles betaald voor het tourspel!");
		m_tourMailer.send(dc());
	}



	private void groupPlayListPerPerson() {
		m_userNotPaidLists = new HashMap<Person, List<PlayList>>();
		for(PlayList notPaidPlayList : m_notPaidPlayLists) {
			List<PlayList> playLists = m_userNotPaidLists.get(notPaidPlayList.getPerson());
			if(playLists == null) {
				playLists = new ArrayList<PlayList>();
				m_userNotPaidLists.put(notPaidPlayList.getPerson(), playLists);
			}
			playLists.add(notPaidPlayList);
		}
	}


	private void findPlayListNotPaidFor() throws Exception {
		QCriteria<PlayList> crit = QCriteria.create(PlayList.class);
		crit.eq(PlayList.pPAID, Boolean.FALSE);
		m_notPaidPlayLists = dc().query(crit);
	}


	public static void main(String[] args) {
		try {
			Application.initDatabase(new File("WebContent/WEB-INF/" + DeveloperOptions.getString("tourspel", "tourspel.properties")));
			PollingWorkerQueue.initialize();
			BulkMailer.initialize(Application.getDataSource(), new SmtpTransport("localhost"));

			Edition ed = new Edition();
			ed.setId(Long.valueOf(50));

			new PlayerNotPaidRun(ed).test();
		} catch(Exception e) {
			e.printStackTrace();
		}


	}


	private void test() throws Exception {
		System.out.println("PlayerNotPaidRun is gestart...");
		Progress p = new Progress(null);
		execute(p);
		System.out.println("PlayerNotPaidRun is gestopt.");
	}

}
