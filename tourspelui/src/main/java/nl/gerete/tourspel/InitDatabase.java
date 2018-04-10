package nl.gerete.tourspel;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import to.etc.domui.hibernate.config.*;
import to.etc.util.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class InitDatabase {
	private QDataContext m_dc;

	private boolean m_empty;

	private Person m_lastPerson;

	public void initialize() throws Exception {
		m_dc = HibernateConfigurator.getDataContextFactory().getDataContext();
		try {
			m_empty = isDbEmpty();
			fillTestData();
		} finally {
			try {
				m_dc.close();
			} catch(Exception x) {}
		}
	}


	private QDataContext dc() {
		return m_dc;
	}


	private boolean isDbEmpty() throws Exception {
		QCriteria<Person> qc = QCriteria.create(Person.class);
		List<Person> lp = dc().query(qc);
		return (lp.size() < 1);
	}

	private void fillTestData() throws Exception {
		dc().startTransaction();

		int year = Calendar.getInstance().get(Calendar.YEAR);
		if(year < 2014 || year > 2300)
			throw new IllegalStateException("Make a new one");
		m_ed = EditionBP.getCurrentEdition(dc());

		PropertyFile propertyfile = PropertyFile.getInstance();
		String what = propertyfile.getProperty("tourspel.dbinit", DbInitType.UNSTARTED.name());
		DbInitType itype = DbInitType.valueOf(what);

		//-- Flags: see http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
		Country ned = createCountry("Nederland", "nl");
		Country esp = createCountry("Spanje", "es");
		Country ger = createCountry("Duitsland", "de");
		Country cze = createCountry("Tjechië", "cz");
		Country kaz = createCountry("Kazachstan", "kz");
		Country ukr = createCountry("Ukraine", "ua");
		Country ita = createCountry("Italië", "it");
		Country lit = createCountry("Litouwen", "li");
		Country fra = createCountry("Frankrijk", "fr");
		Country rus = createCountry("Rusland", "ru");
		Country lux = createCountry("Luxemburg", "lu");
		Country irl = createCountry("Ierland", "ie");
		Country den = createCountry("Denemarken", "dk");
		Country aus = createCountry("Australië", "au");
		Country bel = createCountry("België", "be");
		Country zwi = createCountry("Zwitserland", "ch");
		Country slv = createCountry("Slovenië", "si");
		Country vst = createCountry("Verenigde Staten", "us");
		Country noo = createCountry("Noorwegen", "no");
		Country can = createCountry("Canada", "ca");
		Country eng = createCountry("Verenigd Koninkrijk", "gb");
		Country nze = createCountry("Nieuw Zeeland", "nz");
		Country por = createCountry("Portugal", "pt");
		Country wru = createCountry("Wit Rusland", "by");
		Country pol = createCountry("Polen", "pl");
		Country cri = createCountry("Costa Rica", "cr");
		Country col = createCountry("Colombia", "co");
		Country oos = createCountry("Oostenrijk", "at");
		Country slw = createCountry("Slowakije", "sk");
		Country est = createCountry("Estland", "ee");



		Team t = createTeam("Saxo Bank-Sungard", "Bradley McGee", eng);

		createRider("Contador", "Alberto", "",esp, null, t, Integer.valueOf(1));
		createRider("Hernandez", "Jesus", "",esp, null, t, Integer.valueOf(2));
		createRider("Navarro", "Daniel", "",esp, null, t, Integer.valueOf(3));
		createRider("Noval", "Benjamin", "",esp, null, t, Integer.valueOf(4));
		createRider("Porte", "Richie" , "",aus, null, t, Integer.valueOf(5));
		createRider("Sörensen", "Chris Anker", "", den, null, t, Integer.valueOf(6));
		createRider("Sörensen", "Nicki", "",den, null, t, Integer.valueOf(7));
		createRider("Tossato", "Matteo", "",ita, null, t, Integer.valueOf(8));
		Rider rider1 = createRider("Vandborg", "Brian", "", den, null, t, Integer.valueOf(9));

		t = createTeam("Leopard-Trek", "Kim Andersen", ger);

		createRider("Schleck", "Andy", "", lux, null, t, Integer.valueOf(51));
		createRider("Cancellara", "Fabian", "", zwi, null, t, Integer.valueOf(52));
		createRider("Fuglsang", "Jakob", "", den, null, t, Integer.valueOf(53));
		createRider("Gerdemann", "Linus", "", ger, null, t, Integer.valueOf(54));
		createRider("Montfort", "Maxime", "", bel, null, t, Integer.valueOf(55));
		createRider("O'Grady", "Stuart", "", aus, null, t, Integer.valueOf(56));
		createRider("Posthuma", "Joost", "", ned, null, t, Integer.valueOf(57));
		createRider("Schleck", "Frank", "", lux, null, t, Integer.valueOf(58));
		createRider("Voigt", "Jens", "", ger, null, t, Integer.valueOf(59));

		t = createTeam("Euskatel-Euskadi", "Igor Gonzalez Galdeano", esp);

		createRider("Sanchez", "Samuel", "", esp, null, t, Integer.valueOf(101));
		createRider("Izagirre", "Gorka", "", esp, null, t, Integer.valueOf(102));
		createRider("Martinez", "Egoi", "", esp, null, t, Integer.valueOf(103));
		createRider("Perez", "Alan", "", esp, null, t, Integer.valueOf(104));
		createRider("Perez", "Ruben", "", esp, null, t, Integer.valueOf(105));
		createRider("Txurruka", "Amets", "", esp, null, t, Integer.valueOf(106));
		createRider("Urtasun", "Pablo", "", esp, null, t, Integer.valueOf(107));
		createRider("Velasco", "Ivan", "", esp, null, t, Integer.valueOf(108));
		createRider("Verdugo", "Gorka", "", esp, null, t, Integer.valueOf(109));

		t = createTeam("Omega Pharma - Lotto", "Herman Frison", ger);

		Rider rider2 = createRider("Broeck", "Jurgen", "van den", bel, null, t, Integer.valueOf(151));
		createRider("Gilbert", "Philippe", "", bel, null, t, Integer.valueOf(152));
		createRider("Greipel", "André", "", ger, null, t, Integer.valueOf(153));
		createRider("Lang", "Sebastian", "", ger, null, t, Integer.valueOf(154));
		createRider("Roelandts", "Jürgen", "", bel, null, t, Integer.valueOf(155));
		createRider("Sieberg", "Marcel", "", ger, null, t, Integer.valueOf(156));
		createRider("Walle", "Jurgen", "van de", bel, null, t, Integer.valueOf(157));
		createRider("Vanendert", "Jelle", "", bel, null, t, Integer.valueOf(158));
		createRider("Willems", "Frederik", "", bel, null, t, Integer.valueOf(159));

		t = createTeam("Rabobank", "Adri van Houwelingen", ned);

		createRider("Gesink", "Robert", "", ned, null, t, Integer.valueOf(201));
		Rider rider3 = createRider("Barredo", "Carlos", "", esp, null, t, Integer.valueOf(202));
		Rider rider8 = createRider("Boom", "Lars", "", ned, null, t, Integer.valueOf(203));
		createRider("Garate", "Juan Manuel", "", esp, null, t, Integer.valueOf(204));
		createRider("Mollema", "Bauke", "", ned, null, t, Integer.valueOf(205));
		createRider("Niermann", "Grischa", "", ger, null, t, Integer.valueOf(206));
		createRider("Sanchez", "Luis Leon", "", esp, null, t, Integer.valueOf(207));
		createRider("Dam", "Laurens", "ten", ned, null, t, Integer.valueOf(208));
		createRider("Tjallingii", "Maarten", "", ned, null, t, Integer.valueOf(209));

		t = createTeam("Garmin-Cervelo", "Jonathan Vaughters", vst);

		createRider("Hushovd", "Thor", "", noo, null, t, Integer.valueOf(251));
		createRider("Danielson", "Tom", "", vst, null, t, Integer.valueOf(252));
		createRider("Dean", "Julian", "", nze, null, t, Integer.valueOf(253));
		createRider("Farrar", "Tyler", "", vst, null, t, Integer.valueOf(254));
		Rider rider9 = createRider("Hesjedal", "Ryder", "", can, null, t, Integer.valueOf(255));
		createRider("Millar", "David", "", eng, null, t, Integer.valueOf(256));
		createRider("Navardauskas", "Ramunas", "", lit, null, t, Integer.valueOf(257));
		createRider("Vandevelde", "Christian", "", vst, null, t, Integer.valueOf(258));
		createRider("Zabriskie", "David", "", vst, null, t, Integer.valueOf(259));

		t = createTeam("Astana", "Aleksandr Sjefer", kaz);

		createRider("Vinokoerov", "Aleksandr", "", kaz, null, t, Integer.valueOf(301));
		createRider("Gregorio", "Remy", "di", fra, null, t, Integer.valueOf(302));
		createRider("Fofonov", "Dmitri", "", kaz, null, t, Integer.valueOf(303));
		Rider rider10 = createRider("Grivko", "Andrij", "", ukr, null, t, Integer.valueOf(304));
		createRider("Iglinskiy", "Maksim", "", kaz, null, t, Integer.valueOf(305));
		createRider("Kreuziger", "Roman", "", cze, DateUtil.dateFor(1986, 5, 6), t, Integer.valueOf(306));
		createRider("Tiralongo", "Paolo", "", ita, null, t, Integer.valueOf(307));
		createRider("Vaitkus", "Tomas", "", lit, null, t, Integer.valueOf(308));
		createRider("Zeits", "Andrei", "", kaz, null, t, Integer.valueOf(309));

		t = createTeam("Radioshack", "Johan Bruyneel", vst);

		createRider("Brajkovic", "Janez", "", slv, null, t, Integer.valueOf(351));
		createRider("Horner", "Chris", "", vst, null, t, Integer.valueOf(352));
		createRider("Irizar", "Markel", "", esp, null, t, Integer.valueOf(353));
		createRider("Klöden", "Andreas", "", ger, null, t, Integer.valueOf(354));
		createRider("Leipheimer", "Levi", "", vst, null, t, Integer.valueOf(355));
		createRider("Moeravjev", "Dmitri", "", kaz, null, t, Integer.valueOf(356));
		createRider("Paulinho", "Sérgio", "", por, null, t, Integer.valueOf(357));
		Rider rider5 = createRider("Popovitsj", "Jaroslav", "", ukr, null, t, Integer.valueOf(358));
		createRider("Zubeldia", "Haimar", "", esp, null, t, Integer.valueOf(359));

		t = createTeam("Movistar", "Yvon Ledanois", esp);

		createRider("Arroyo", "David", "", esp, null, t, Integer.valueOf(401));
		createRider("Amador", "Andrey", "", cri, null, t, Integer.valueOf(402));
		createRider("Costa", "Rui", "", por, null, t, Integer.valueOf(403));
		createRider("Erviti", "Imanol", "", esp, null, t, Integer.valueOf(404));
		createRider("Gutierrez", "José Ivan", "", esp, null, t, Integer.valueOf(405));
		createRider("Intxausti", "Benat", "", esp, null, t, Integer.valueOf(406));
		createRider("Kirijenka", "Vasil", "", wru, null, t, Integer.valueOf(407));
		createRider("Rojas", "José Joaquin", "", esp, null, t, Integer.valueOf(408));
		createRider("Ventoso", "Francesco", "", esp, null, t, Integer.valueOf(409));

		t = createTeam("Liquigas-Cannondale", "Stefano Zanatta", ita);

		createRider("Basso", "Ivan", "", ita, null, t, Integer.valueOf(451));
		createRider("Bodnar", "Maciej", "", pol, null, t, Integer.valueOf(452));
		createRider("Koren", "Kristjan", "", slv, null, t, Integer.valueOf(453));
		createRider("Borghini", "Paolo Longo", "", ita, null, t, Integer.valueOf(454));
		createRider("Oss", "Daniel", "", ita, null, t, Integer.valueOf(455));
		createRider("Paterski", "Maciej", "", pol, null, t, Integer.valueOf(456));
		createRider("Sabatini", "Fabio", "", ita, null, t, Integer.valueOf(457));
		createRider("Szmyd", "Sylwester", "", pol, null, t, Integer.valueOf(458));
		createRider("Vanotti", "Alessandro", "", ita, null, t, Integer.valueOf(459));

		t = createTeam("Ag2R – La Mondiale", "Vincent Lavenu", fra);

		createRider("Roche", "Nicolas", "",irl, null, t, Integer.valueOf(501));
		createRider("Bouet", "Maxime", "", fra, null, t, Integer.valueOf(502));
		createRider("Dupont", "Hubert", "", fra, null, t, Integer.valueOf(503));
		createRider("Gadret", "John", "", fra, null, t, Integer.valueOf(504));
		createRider("Hinault", "Sébastien" , "", fra, null, t, Integer.valueOf(505));
		createRider("Kadri", "Blel", "", fra, null, t, Integer.valueOf(506));
		createRider("Minard", "Sébastien", "", fra, null, t, Integer.valueOf(507));
		createRider("Péraud", "Jean-Christophe", "", fra, null, t, Integer.valueOf(508));
		createRider("Riblon", "Christophe", "", fra, null, t, Integer.valueOf(509));

		t = createTeam("Team Sky", "Sean Yates", eng);

		createRider("Wiggins", "Bradley", "", eng, null, t, Integer.valueOf(551));
		createRider("Flecha", "Juan Antonio", "", esp, null, t, Integer.valueOf(552));
		createRider("Gerrans", "Simon", "", aus, null, t, Integer.valueOf(553));
		createRider("Hagen", "Edvald Boasson", "", noo, null, t, Integer.valueOf(554));
		createRider("Knees", "Christian", "", ger, null, t, Integer.valueOf(555));
		createRider("Swift", "Ben", "", eng, null, t, Integer.valueOf(556));
		createRider("Thomas", "Geraint", "", eng, null, t, Integer.valueOf(557));
		createRider("Uran", "Rigoberto", "", col, null, t, Integer.valueOf(558));
		createRider("Zandio", "Xabier", "", esp, null, t, Integer.valueOf(559));

		t = createTeam("Quick-Step", "Wilfried Peeters", bel);

		createRider("Chavanel", "Sylvain", "", fra, null, t, Integer.valueOf(600));
		createRider("Boonen", "Tom", "", bel, null, t, Integer.valueOf(601));
		createRider("Ciolek", "Gerald", "", ger, null, t, Integer.valueOf(602));
		createRider("Weert", "Kevin", "de", bel, null, t, Integer.valueOf(603));
		createRider("Devenyns", "Dries", "", bel, null, t, Integer.valueOf(604));
		createRider("Engels", "Addy", "", ned, null, t, Integer.valueOf(605));
		createRider("Pineau", "Jerome", "", fra, null, t, Integer.valueOf(606));
		createRider("Steegmans", "Gert", "", bel, null, t, Integer.valueOf(607));
		createRider("Terpstra", "Niki", "", ned, null, t, Integer.valueOf(608));

		t = createTeam("Francaise des jeux", "Thierry Bricaud", fra);

		createRider("Casar", "Sandy", "", fra, null, t, Integer.valueOf(651));
		createRider("Bonnet", "William", "", fra, null, t, Integer.valueOf(652));
		createRider("Delage", "Mickaël", "", fra, null, t, Integer.valueOf(653));
		createRider("Jeannesson", "Arnold", "", fra, null, t, Integer.valueOf(654));
		createRider("Meersman", "Gianni", "", bel, null, t, Integer.valueOf(655));
		createRider("Pauriol", "Rémy", "", fra, null, t, Integer.valueOf(656));
		createRider("Roux", "Anthony", "", fra, null, t, Integer.valueOf(657));
		createRider("Roy", "Jeremy", "", fra, null, t, Integer.valueOf(658));
		createRider("Vichot", "Arthur", "", fra, null, t, Integer.valueOf(659));

		t = createTeam("BMC Racing", "John Lelangue", zwi);

		Rider rider4 = createRider("Evans", "Cadel", "", aus, null, t, Integer.valueOf(701));
		createRider("Bookwalter", "Brett", "", vst, null, t, Integer.valueOf(702));
		createRider("Burghardt", "Marcus", "", ger, null, t, Integer.valueOf(703));
		createRider("Hincapie", "George", "", vst, null, t, Integer.valueOf(704));
		createRider("Moinard", "Amaël", "", fra, null, t, Integer.valueOf(705));
		createRider("Morabito", "Steve", "", zwi, null, t, Integer.valueOf(706));
		createRider("Quinziato", "Manuel", "", ita, null, t, Integer.valueOf(707));
		createRider("Santaromita", "Ivan", "", ita, null, t, Integer.valueOf(708));
		createRider("Schär", "Michael", "", zwi, null, t, Integer.valueOf(709));

		t = createTeam("Cofidis", "Didier Rous", est);

		createRider("Taaramae", "Rein", "", est, null, t, Integer.valueOf(751));
		createRider("Buffaz", "Mickaël", "", fra, null, t, Integer.valueOf(752));
		createRider("Dumoulin", "Samuel", "", fra, null, t, Integer.valueOf(753));
		createRider("Duque", "Leonardo", "", col, null, t, Integer.valueOf(754));
		createRider("Farès", "Julien", "El", fra, null, t, Integer.valueOf(755));
		createRider("Gallopin", "Tony", "", fra, null, t, Integer.valueOf(756));
		createRider("Moncoutié", "David", "", fra, null, t, Integer.valueOf(757));
		createRider("Valentin", "Tristan", "", fra, null, t, Integer.valueOf(758));
		createRider("Zingle", "Romain", "", bel, null, t, Integer.valueOf(759));

		t = createTeam("Lampre-ISD", "Orlando Maini", ita);

		createRider("Cunego", "Damiano", "", ita, null, t, Integer.valueOf(801));
		createRider("Bertagnolli", "Leonardo", "", ita, null, t, Integer.valueOf(802));
		createRider("Bole", "Grega", "", slv, null, t, Integer.valueOf(803));
		createRider("Bono", "Matteo", "", ita, null, t, Integer.valueOf(804));
		createRider("Hondo", "Danilo", "", ger, null, t, Integer.valueOf(805));
		createRider("Kostijoek", "Denis", "", ukr, null, t, Integer.valueOf(806));
		createRider("Loosli", "David", "", zwi, null, t, Integer.valueOf(807));
		createRider("Malori", "Adriano", "", ita, null, t, Integer.valueOf(808));
		createRider("Petacchi", "Alessandro", "", ita, null, t, Integer.valueOf(809));

		t = createTeam("HTC Highroad", "Brian Holm", eng);

		createRider("Cavendish", "Mark", "", eng, null, t, Integer.valueOf(851));
		createRider("Bak", "Lars", "", den, null, t, Integer.valueOf(852));
		createRider("Eisel", "Bernhard", "", oos, null, t, Integer.valueOf(853));
		createRider("Goss", "Matthew", "", aus, null, t, Integer.valueOf(854));
		createRider("Martin", "Tony", "", ger, null, t, Integer.valueOf(855));
		createRider("Pate", "Danny", "", vst, null, t, Integer.valueOf(856));
		createRider("Renshaw", "Mark", "", aus, null, t, Integer.valueOf(857));
		createRider("Garderen", "Tejay", "Van", vst, null, t, Integer.valueOf(858));
		createRider("Velits", "Peter", "", slw, null, t, Integer.valueOf(859));

		t = createTeam("Europcar", "Dominique Arnould", fra);

		createRider("Voeckler", "Thomas", "", fra, null, t, Integer.valueOf(901));
		createRider("Charteau", "Anthony", "", fra, null, t, Integer.valueOf(902));
		createRider("Gautier", "Cyril", "", fra, null, t, Integer.valueOf(903));
		createRider("Gène", "Yohann", "", fra, null, t, Integer.valueOf(904));
		createRider("Jérôme", "Vincent", "", fra, null, t, Integer.valueOf(905));
		createRider("Kern", "Christophe", "", fra, null, t, Integer.valueOf(906));
		Rider rider6 = createRider("Quemeneur", "Perrig", "", fra, null, t, Integer.valueOf(907));
		createRider("Rolland", "Pierre", "", fra, null, t, Integer.valueOf(908));
		createRider("Turgot", "Sébastien", "", fra, null, t, Integer.valueOf(909));

		t = createTeam("Katoesja", "Dmitri Konisjev", rus);

		createRider("Karpets", "Vladimir", "", rus, null, t, Integer.valueOf(951));
		createRider("Broett", "Pavel", "", rus, null, t, Integer.valueOf(952));
		createRider("Galimzjanov", "Denis", "", rus, null, t, Integer.valueOf(953));
		createRider("Goesev", "Vladimir", "", rus, null, t, Integer.valueOf(954));
		createRider("Ignatjev", "Michail", "", rus, null, t, Integer.valueOf(955));
		createRider("Isajtsjev", "Vladimir", "", rus, null, t, Integer.valueOf(956));
		createRider("Kolobnev", "Aleksandr", "", rus, null, t, Integer.valueOf(957));
		createRider("Silin", "Egor", "", rus, null, t, Integer.valueOf(958));
		createRider("Trofimov", "Joeri", "", rus, null, t, Integer.valueOf(959));

		t = createTeam("Vacansoleil-DCM", "Hilaire Van der Schueren", ned);

		createRider("Feillu", "Romain", "", fra, null, t, Integer.valueOf(1001));
		createRider("Bozic", "Borut", "", slv, null, t, Integer.valueOf(1002));
		createRider("Gendt", "Thomas", "De", bel, null, t, Integer.valueOf(1003));
		createRider("Hoogerland", "Johnny", "", ned, null, t, Integer.valueOf(1004));
		createRider("Leukemans", "Björn", "", bel, null, t, Integer.valueOf(1005));
		createRider("Marcato", "Marco", "", ita, null, t, Integer.valueOf(1006));
		createRider("Poels", "Wout", "", ned, null, t, Integer.valueOf(1007));
		createRider("Ruijgh", "Rob", "", ned, null, t, Integer.valueOf(1008));
		createRider("Westra", "Lieuwe", "", ned, null, t, Integer.valueOf(1009));

		t = createTeam("Saur-Sojasun", "Stephane Heulot", fra);

		createRider("Coppel", "Jérôme", "", fra, null, t, Integer.valueOf(1051));
		createRider("Coyot", "Arnaud", "", fra, null, t, Integer.valueOf(1052));
		Rider rider7 = createRider("Delaplace", "Anthony", "", fra, null, t, Integer.valueOf(1053));
		createRider("Engoulvent", "Jimmy", "", fra, null, t, Integer.valueOf(1054));
		createRider("Galland", "Jérémie", "", fra, null, t, Integer.valueOf(1055));
		createRider("Hivert", "Jonathan", "", fra, null, t, Integer.valueOf(1056));
		createRider("Jeandesboz", "Fabrice", "", fra, null, t, Integer.valueOf(1057));
		createRider("Mangel", "Laurent", "", fra, null, t, Integer.valueOf(1058));
		createRider("Talabardon", "Yannick", "", fra, null, t, Integer.valueOf(1059));

		dc().commit();

		//-- Etappes
		Etappe eprologue = createEtappe("1", EtappeType.Prologue, DateUtil.dateFor(2014, Calendar.JANUARY, 21), "Leeds", "Harrogate", 191, "http://www.letour.com/le-tour/2014/us/stage-1.html",
			m_ed);
		Etappe firstEtappe = createEtappe("2", EtappeType.MediumMountains, DateUtil.dateFor(2014, Calendar.JULY, 6), "York", "Sheffield", 198, "http://www.letour.com/le-tour/2014/us/stage-2.html",
			m_ed);
		createEtappe("3", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 7), "Cambridge", "Londen", 159, "http://www.letour.com/le-tour/2014/us/stage-3.html", m_ed);

		createEtappe("4", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 8), "Le Touquet", "Lille", 164, "http://www.letour.com/le-tour/2014/us/stage-4.html", m_ed);
		createEtappe("5", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 9), "Ieper (B)", "Arenberg", 156, "http://www.letour.com/le-tour/2014/us/stage-5.html", m_ed);
		createEtappe("6", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 10), "Arras", "Reims", 194, "http://www.letour.com/le-tour/2014/us/stage-6.html", m_ed);
		createEtappe("7", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 11), "Épernay", "Nancy", 233, "http://www.letour.com/le-tour/2014/us/stage-7.html", m_ed);
		createEtappe("8", EtappeType.MediumMountains, DateUtil.dateFor(2014, Calendar.JULY, 12), "Tomblaine", "Gérardmer", 161, "http://www.letour.com/le-tour/2014/us/stage-8.html", m_ed);
		createEtappe("9", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 13), "Gérardmer", "Mulhouse", 166, "http://www.letour.com/le-tour/2014/us/stage-9.html", m_ed);
		createEtappe("10", EtappeType.MediumMountains, DateUtil.dateFor(2014, Calendar.JULY, 14), "Mulhouse", "La Planche des Belles Filles", 161,
			"http://www.letour.com/le-tour/2014/us/stage-10.html",
			m_ed);
		createEtappe("11", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 16), "Besançon", "Oyonnax", 186, "http://www.letour.com/le-tour/2014/us/stage-11.html", m_ed);
		createEtappe("12", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 17), "Bourg-en-Bresse", "Saint-Etienne", 183, "http://www.letour.com/le-tour/2014/us/stage-12.html", m_ed);
		createEtappe("13", EtappeType.HighMountains, DateUtil.dateFor(2014, Calendar.JULY, 18), "Saint-Etienne", "Chamrousse", 200, "http://www.letour.com/le-tour/2014/us/stage-13.html", m_ed);
		createEtappe("14", EtappeType.HighMountains, DateUtil.dateFor(2014, Calendar.JULY, 19), "Grenoble", "Risoul", 177, "http://www.letour.com/le-tour/2014/us/stage-14.html", m_ed);
		createEtappe("15", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 20), "Tallard", "Nîmes", 222, "http://www.letour.com/le-tour/2014/us/stage-15.html", m_ed);
		createEtappe("16", EtappeType.HighMountains, DateUtil.dateFor(2014, Calendar.JULY, 22), "Carcassonne", "Bagnères-de-Luchon", 237, "http://www.letour.com/le-tour/2014/us/stage-16.html", m_ed);
		createEtappe("17", EtappeType.HighMountains, DateUtil.dateFor(2014, Calendar.JULY, 23), "Saint-Gaudens", "Soulan Pla d’Adet", 125, "http://www.letour.com/le-tour/2014/us/stage-17.html",
			m_ed);
		createEtappe("18", EtappeType.HighMountains, DateUtil.dateFor(2014, Calendar.JULY, 24), "Pau", "Hautacam", 145, "http://www.letour.com/le-tour/2014/us/stage-18.html", m_ed);
		createEtappe("19", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 25), "Maubourguet", "Bergerac", 208, "http://www.letour.com/le-tour/2014/us/stage-19.html", m_ed);
		createEtappe("20", EtappeType.IndividualTime, DateUtil.dateFor(2014, Calendar.JULY, 26), "Bergerac", "Périgueux", 54, "http://www.letour.com/le-tour/2014/us/stage-20.html", m_ed);
		createEtappe("21", EtappeType.Plain, DateUtil.dateFor(2014, Calendar.JULY, 27), "Évry", "Paris Champs-Élysées", 136, "http://www.letour.com/le-tour/2014/us/stage-21.html", m_ed);
		dc().commit();

		Person p = createPerson("Rob", "Gersteling", "degerst@gmail.com", "Degerst");
//		addRight(ApplicationRight.ADMIN);
		addRight(ApplicationRight.PLAYER);
		createPlayList(p, "De eerste", PlayListType.KLASSEMENT, 1, true);
		createPlayList(p, "Bicycle", PlayListType.KLASSEMENT, 2, false);
		createPlayList(p, "Poedelig", PlayListType.POEDEL, 2, true);

		p = createPerson("Rob", "Gersteling", "aa", "aaaaaaaa");
		addRight(ApplicationRight.ADMIN);
		addRight(ApplicationRight.PLAYER);

		p = createPerson("Marc", "Mol", "marc@gerete.nl", "marc");
		addRight(ApplicationRight.ADMIN);
		addRight(ApplicationRight.PLAYER);
		addRight(ApplicationRight.CLERK);
		createPlayList(p, "Remspoor", PlayListType.KLASSEMENT, 1, true);
		createPlayList(p, "Bicycle Repairman", PlayListType.KLASSEMENT, 2, false);

		p = createPerson("Ben", "Schoen", "benshoe@gmail.com", "ben");
		createPlayList(p, "Schoensmeer", PlayListType.KLASSEMENT, 1, false);
		addRight(ApplicationRight.ADMIN);
		addRight(ApplicationRight.PLAYER);
		addRight(ApplicationRight.CLERK);

		p = createPerson("Frits", "Jalvink", "jal@etc.to", "frits");
		createPlayList(p, "Kale nicht a", PlayListType.POEDEL, 2, true);
		addRight(ApplicationRight.PLAYER);
		p = createPerson("Dennis", "vd Meer", "dennis.van.der.meer@itris.nl", "dennis");
		addRight(ApplicationRight.PLAYER);
		p = createPerson("Menno", "v Eyck", "menno.van.eyck@itris.nl", "menno");
		addRight(ApplicationRight.PLAYER);

		p = createPerson("Frits", "Jalvingh", "frits.jalvingh@itris.nl", "frits");
		createPlayList(p, "Florpen", PlayListType.KLASSEMENT, 1, false);
		addRight(ApplicationRight.ADMIN);
		addRight(ApplicationRight.PLAYER);
		addRight(ApplicationRight.CLERK);


		dc().commit();

		if(itype == DbInitType.ONE_ETAPPE && m_empty) {
			//-- Finish the prologue.
			m_ed.setPhase(EditionPhase.RUNNING);
			m_ed.setStartDate(DateUtil.dateFor(2014, Calendar.JULY, 5));
			eprologue.setPhase(EtappePhase.CALCULATING);

			List<Rider>		rlist = dc().query(QCriteria.create(Rider.class).eq("edition", m_ed).ascending("lastName"));

			//-- Create the results for it as a seeded random set.
			Random r = new Random();
			r.setSeed(12345678);
			Set<Rider> doneset = new HashSet<Rider>();
			while(eprologue.getResultList().size() < PointsCalculator.NUM_RIDERS) {
				Rider rider = rlist.get(r.nextInt(rlist.size()));
				if(doneset.contains(rider))
					continue;
				doneset.add(rider);

				createEtappeResult(eprologue, rider);
			}
			firstEtappe.setPhase(EtappePhase.CALCULATING);
			createEtappeResult(firstEtappe, rider1);
			createEtappeResult(firstEtappe, rider2);
			createEtappeResult(firstEtappe, rider3);
			createEtappeResult(firstEtappe, rider4);
			createEtappeResult(firstEtappe, rider5);
			createEtappeResult(firstEtappe, rider6);
			createEtappeResult(firstEtappe, rider7);
			createEtappeResult(firstEtappe, rider8);
			createEtappeResult(firstEtappe, rider9);
			createEtappeResult(firstEtappe, rider10);

		}



		dc().commit();
		dc().close();
	}

	private void createEtappeResult(Etappe et, Rider rider) throws Exception {
		EtappeResult er = new EtappeResult();
		er.setRider(rider);
		er.setEtappe(et);
		er.setPlace(et.getResultList().size() + 1);
		et.getResultList().add(er);
		dc().save(er);
	}


	private List<PlayListEntry> getPlayListEntries(int i, PlayList playList) throws Exception {

		List<PlayListEntry> plel = new ArrayList<PlayListEntry>();
		switch(i){
			case 1:
				plel.add(createPlayListEntry(1, 9, playList));
				plel.add(createPlayListEntry(2, 54, playList));
				plel.add(createPlayListEntry(3, 151, playList));
				plel.add(createPlayListEntry(4, 154, playList));
				plel.add(createPlayListEntry(5, 351, playList));
				plel.add(createPlayListEntry(6, 403, playList));
				plel.add(createPlayListEntry(7, 101, playList));
				plel.add(createPlayListEntry(8, 951, playList));
				plel.add(createPlayListEntry(9, 1003, playList));
				plel.add(createPlayListEntry(10, 651, playList));
				break;
			case 2:
				plel.add(createPlayListEntry(10, 9, playList));
				plel.add(createPlayListEntry(9, 4, playList));
				plel.add(createPlayListEntry(8, 151, playList));
				plel.add(createPlayListEntry(7, 305, playList));
				plel.add(createPlayListEntry(6, 354, playList));
				plel.add(createPlayListEntry(5, 358, playList));
				plel.add(createPlayListEntry(4, 751, playList));
				plel.add(createPlayListEntry(3, 752, playList));
				plel.add(createPlayListEntry(2, 651, playList));
				plel.add(createPlayListEntry(1, 701, playList));
				break;
			default:
				plel.add(createPlayListEntry(10, 1, playList));
				plel.add(createPlayListEntry(9, 51, playList));
				plel.add(createPlayListEntry(8, 151, playList));
				plel.add(createPlayListEntry(7, 551, playList));
				plel.add(createPlayListEntry(6, 351, playList));
				plel.add(createPlayListEntry(5, 301, playList));
				plel.add(createPlayListEntry(4, 751, playList));
				plel.add(createPlayListEntry(3, 951, playList));
				plel.add(createPlayListEntry(2, 901, playList));
				plel.add(createPlayListEntry(1, 801, playList));
		}
		return plel;
	}


	private PlayListEntry createPlayListEntry(int position, int rider, PlayList playList) throws Exception {
		Rider r = dc().queryOne(QCriteria.create(Rider.class).eq(Rider.pNUMBER, Integer.valueOf(rider)));
		if(r == null)
			throw new IllegalArgumentException("Rider " + rider + " does not exist");
		PlayListEntry ple = new PlayListEntry(playList, r, position);
		dc().save(ple);
		return ple;
	}

	private Country createCountry(String country, String shortName) throws Exception {
		Country c = dc().queryOne(QCriteria.create(Country.class).eq(Country.pSHORTNAME, shortName));
		if(null != c)
			return c;
		c = new Country();
		c.setName(country);
		c.setShortName(shortName);
		dc().save(c);
		return c;
	}

	private Rider createRider(String lastName, String firstName, String middleName, Country country, Date dateOfBirth, Team team, Integer startNumber) throws Exception {
		Rider r = dc().queryOne(QCriteria.create(Rider.class).eq(Rider.pNUMBER, startNumber));
		if(null != r)
			return r;
		r = new Rider();
		r.setEdition(m_ed);
		r.setLastName(lastName);
		r.setFirstName(firstName);
		r.setMiddleName(middleName);
		r.setDateOfBirth(dateOfBirth);
		r.setNumber(startNumber);
		r.setTeam(team);
		r.setCountry(country);
		dc().save(r);
		return r;
	}

	private Person createPerson(String firstname, String lastname, String email, String password) throws Exception {

		Person p = dc().queryOne(QCriteria.create(Person.class).eq(Person.pEMAIL, email));
		if(null != p)
			return (m_lastPerson = p);
		p = new Person();
		p.setLastName(lastname);
		p.setFirstName(firstname);
		p.setEmail(email);
		p.setPassword(password);
		dc().save(p);
		m_lastPerson = p;
		return p;
	}

	private Team createTeam(String name, String teamCaptain, Country country) throws Exception {

		Team t = dc().queryOne(QCriteria.create(Team.class).eq(Team.pNAME, name));
		if(null != t)
			return t;
		t = new Team();
		t.setEdition(m_ed);
		t.setName(name);
		t.setTeamCaptainName(teamCaptain);
		t.setCountry(country);
		dc().save(t);

		return t;
	}

	@Nonnull
	private PersonRight createRight(@Nonnull Person p, @Nonnull ApplicationRight right, Date start, Date end) throws Exception {
		PersonRight pr = dc().queryOne(QCriteria.create(PersonRight.class).eq("person", p).eq("right", right));
		if(null != pr)
			return pr;
		pr = new PersonRight();
		pr.setPerson(p);
		pr.setRight(right);
		pr.setStartDate(start);
		pr.setEndDate(end);
		p.getRightList().add(pr);
		dc().save(pr);
		return pr;
	}

	private PersonRight addRight(@Nonnull ApplicationRight right, Date from, Date to) throws Exception {
		return createRight(m_lastPerson, right, from, to);
	}

	private PersonRight addRight(@Nonnull ApplicationRight right) throws Exception {
		return createRight(m_lastPerson, right, DateUtil.dateFor(2010, 0, 1), null);
	}

	@Nullable
	private Date m_lastEtappe;

	private Edition m_ed;

	@Nonnull
	private Etappe createEtappe(String stage, EtappeType ty, Date dt, String start, String end, double dist, String url, Edition ed) throws Exception {
		Etappe et = dc().queryOne(QCriteria.create(Etappe.class).eq("edition", ed).eq("stage", stage));
		if(null != et)
			return et;
		et = new Etappe();
		et.setEdition(ed);
		et.setEnd(end);
		et.setStage(stage);
		et.setStart(start);
		et.setLength(dist);
		et.setType(ty);
		et.setUrl(url);

		Calendar cal = Calendar.getInstance();
		if(dt != null)
			cal.setTime(dt);
		else {
			if(m_lastEtappe == null)
				throw new IllegalStateException("No last etappe date");
			cal.setTime(m_lastEtappe);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		m_lastEtappe = cal.getTime();
		et.setDate(Objects.requireNonNull(m_lastEtappe));
		dc().save(et);
		return et;
	}

	@Nonnull
	private PlayList createPlayList(Person person, String listName, PlayListType playListType, int listNumber, boolean paid) throws Exception {
		PlayList pl = dc().queryOne(QCriteria.create(PlayList.class).eq(PlayList.pPERSON, person).eq(PlayList.pLISTNAME, listName).eq("edition", m_ed));
		if(null != pl)
			return pl;
		pl = new PlayList();
		pl.setEdition(m_ed);
		pl.setListName(listName);
		pl.setPerson(person);
		pl.setPlayListType(playListType);
		pl.setPaid(paid);
		dc().save(pl);
		pl.setPlayListEntries(getPlayListEntries(listNumber, pl));
		return pl;
	}
}
