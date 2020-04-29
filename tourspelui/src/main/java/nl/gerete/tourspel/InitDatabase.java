package nl.gerete.tourspel;

import nl.gerete.tourspel.db.ApplicationRight;
import nl.gerete.tourspel.db.Country;
import nl.gerete.tourspel.db.Edition;
import nl.gerete.tourspel.db.EditionPhase;
import nl.gerete.tourspel.db.Etappe;
import nl.gerete.tourspel.db.EtappePhase;
import nl.gerete.tourspel.db.EtappeResult;
import nl.gerete.tourspel.db.EtappeType;
import nl.gerete.tourspel.db.Person;
import nl.gerete.tourspel.db.PersonRight;
import nl.gerete.tourspel.db.PlayList;
import nl.gerete.tourspel.db.PlayListEntry;
import nl.gerete.tourspel.db.PlayListType;
import nl.gerete.tourspel.db.Rider;
import nl.gerete.tourspel.db.Rider_;
import nl.gerete.tourspel.db.Team;
import nl.gerete.tourspel.logic.EditionBP;
import nl.gerete.tourspel.logic.PointsCalculator;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.hibernate.config.HibernateConfigurator;
import to.etc.util.DateUtil;
import to.etc.webapp.query.QCriteria;
import to.etc.webapp.query.QDataContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Initialiseren van de database voor het tourspel met de teams en de wielrenners.
 */
public class InitDatabase {
	private QDataContext m_dc;

	private boolean m_empty;

	private Person m_lastPerson;

	public void initialize() throws Exception {
		m_dc = HibernateConfigurator.getDataContextFactory().getDataContext();
		try {
			m_empty = isDbEmpty();
			if (m_empty) {
				fillTestData();
			}
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

		int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
		if(year < 2018 || year > 2300)
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

		createRider("Contador", "Alberto", "",esp, null, t, 1);
		createRider("Hernandez", "Jesus", "",esp, null, t, 2);
		createRider("Navarro", "Daniel", "",esp, null, t, 3);
		createRider("Noval", "Benjamin", "",esp, null, t, 4);
		createRider("Porte", "Richie" , "",aus, null, t, 5);
		createRider("Sörensen", "Chris Anker", "", den, null, t, 6);
		createRider("Sörensen", "Nicki", "",den, null, t, 7);
		createRider("Tossato", "Matteo", "",ita, null, t, 8);
		Rider rider1 = createRider("Vandborg", "Brian", "", den, null, t, 9);

		t = createTeam("Leopard-Trek", "Kim Andersen", ger);

		createRider("Schleck", "Andy", "", lux, null, t, 51);
		createRider("Cancellara", "Fabian", "", zwi, null, t, 52);
		createRider("Fuglsang", "Jakob", "", den, null, t, 53);
		createRider("Gerdemann", "Linus", "", ger, null, t, 54);
		createRider("Montfort", "Maxime", "", bel, null, t, 55);
		createRider("O'Grady", "Stuart", "", aus, null, t, 56);
		createRider("Posthuma", "Joost", "", ned, null, t, 57);
		createRider("Schleck", "Frank", "", lux, null, t, 58);
		createRider("Voigt", "Jens", "", ger, null, t, 59);

		t = createTeam("Euskatel-Euskadi", "Igor Gonzalez Galdeano", esp);

		createRider("Sanchez", "Samuel", "", esp, null, t, 101);
		createRider("Izagirre", "Gorka", "", esp, null, t, 102);
		createRider("Martinez", "Egoi", "", esp, null, t, 103);
		createRider("Perez", "Alan", "", esp, null, t, 104);
		createRider("Perez", "Ruben", "", esp, null, t, 105);
		createRider("Txurruka", "Amets", "", esp, null, t, 106);
		createRider("Urtasun", "Pablo", "", esp, null, t, 107);
		createRider("Velasco", "Ivan", "", esp, null, t, 108);
		createRider("Verdugo", "Gorka", "", esp, null, t, 109);

		t = createTeam("Omega Pharma - Lotto", "Herman Frison", ger);

		Rider rider2 = createRider("Broeck", "Jurgen", "van den", bel, null, t, 151);
		createRider("Gilbert", "Philippe", "", bel, null, t, 152);
		createRider("Greipel", "André", "", ger, null, t, 153);
		createRider("Lang", "Sebastian", "", ger, null, t, 154);
		createRider("Roelandts", "Jürgen", "", bel, null, t, 155);
		createRider("Sieberg", "Marcel", "", ger, null, t, 156);
		createRider("Walle", "Jurgen", "van de", bel, null, t, 157);
		createRider("Vanendert", "Jelle", "", bel, null, t, 158);
		createRider("Willems", "Frederik", "", bel, null, t, 159);

		t = createTeam("Rabobank", "Adri van Houwelingen", ned);

		createRider("Gesink", "Robert", "", ned, null, t, 201);
		Rider rider3 = createRider("Barredo", "Carlos", "", esp, null, t, 202);
		Rider rider8 = createRider("Boom", "Lars", "", ned, null, t, 203);
		createRider("Garate", "Juan Manuel", "", esp, null, t, 204);
		createRider("Mollema", "Bauke", "", ned, null, t, 205);
		createRider("Niermann", "Grischa", "", ger, null, t, 206);
		createRider("Sanchez", "Luis Leon", "", esp, null, t, 207);
		createRider("Dam", "Laurens", "ten", ned, null, t, 208);
		createRider("Tjallingii", "Maarten", "", ned, null, t, 209);

		t = createTeam("Garmin-Cervelo", "Jonathan Vaughters", vst);

		createRider("Hushovd", "Thor", "", noo, null, t, 251);
		createRider("Danielson", "Tom", "", vst, null, t, 252);
		createRider("Dean", "Julian", "", nze, null, t, 253);
		createRider("Farrar", "Tyler", "", vst, null, t, 254);
		Rider rider9 = createRider("Hesjedal", "Ryder", "", can, null, t, 255);
		createRider("Millar", "David", "", eng, null, t, 256);
		createRider("Navardauskas", "Ramunas", "", lit, null, t, 257);
		createRider("Vandevelde", "Christian", "", vst, null, t, 258);
		createRider("Zabriskie", "David", "", vst, null, t, 259);

		t = createTeam("Astana", "Aleksandr Sjefer", kaz);

		createRider("Vinokoerov", "Aleksandr", "", kaz, null, t, 301);
		createRider("Gregorio", "Remy", "di", fra, null, t, 302);
		createRider("Fofonov", "Dmitri", "", kaz, null, t, 303);
		Rider rider10 = createRider("Grivko", "Andrij", "", ukr, null, t, 304);
		createRider("Iglinskiy", "Maksim", "", kaz, null, t, 305);
		createRider("Kreuziger", "Roman", "", cze, DateUtil.dateFor(1986, 5, 6), t, 306);
		createRider("Tiralongo", "Paolo", "", ita, null, t, 307);
		createRider("Vaitkus", "Tomas", "", lit, null, t, 308);
		createRider("Zeits", "Andrei", "", kaz, null, t, 309);

		t = createTeam("Radioshack", "Johan Bruyneel", vst);

		createRider("Brajkovic", "Janez", "", slv, null, t, 351);
		createRider("Horner", "Chris", "", vst, null, t, 352);
		createRider("Irizar", "Markel", "", esp, null, t, 353);
		createRider("Klöden", "Andreas", "", ger, null, t, 354);
		createRider("Leipheimer", "Levi", "", vst, null, t, 355);
		createRider("Moeravjev", "Dmitri", "", kaz, null, t, 356);
		createRider("Paulinho", "Sérgio", "", por, null, t, 357);
		Rider rider5 = createRider("Popovitsj", "Jaroslav", "", ukr, null, t, 358);
		createRider("Zubeldia", "Haimar", "", esp, null, t, 359);

		t = createTeam("Movistar", "Yvon Ledanois", esp);

		createRider("Arroyo", "David", "", esp, null, t, 401);
		createRider("Amador", "Andrey", "", cri, null, t, 402);
		createRider("Pannekoek", "Joep", "", por, null, t, 403);
		createRider("Erviti", "Imanol", "", esp, null, t, 404);
		createRider("Gutierrez", "José Ivan", "", esp, null, t, 405);
		createRider("Intxausti", "Benat", "", esp, null, t, 406);
		createRider("Kirijenka", "Vasil", "", wru, null, t, 407);
		createRider("Rojas", "José Joaquin", "", esp, null, t, 408);
		createRider("Ventoso", "Francesco", "", esp, null, t, 409);

		t = createTeam("Liquigas-Cannondale", "Stefano Zanatta", ita);

		createRider("Basso", "Ivan", "", ita, null, t, 451);
		createRider("Bodnar", "Maciej", "", pol, null, t, 452);
		createRider("Koren", "Kristjan", "", slv, null, t, 453);
		createRider("Borghini", "Paolo Longo", "", ita, null, t, 454);
		createRider("Oss", "Daniel", "", ita, null, t, 455);
		createRider("Paterski", "Maciej", "", pol, null, t, 456);
		createRider("Sabatini", "Fabio", "", ita, null, t, 457);
		createRider("Szmyd", "Sylwester", "", pol, null, t, 458);
		createRider("Vanotti", "Alessandro", "", ita, null, t, 459);

		t = createTeam("Ag2R – La Mondiale", "Vincent Lavenu", fra);

		createRider("Roche", "Nicolas", "",irl, null, t, 501);
		createRider("Bouet", "Maxime", "", fra, null, t, 502);
		createRider("Dupont", "Hubert", "", fra, null, t, 503);
		createRider("Gadret", "John", "", fra, null, t, 504);
		createRider("Hinault", "Sébastien" , "", fra, null, t, 505);
		createRider("Kadri", "Blel", "", fra, null, t, 506);
		createRider("Minard", "Sébastien", "", fra, null, t, 507);
		createRider("Péraud", "Jean-Christophe", "", fra, null, t, 508);
		createRider("Riblon", "Christophe", "", fra, null, t, 509);

		t = createTeam("Team Sky", "Sean Yates", eng);

		createRider("Wiggins", "Bradley", "", eng, null, t, 551);
		createRider("Flecha", "Juan Antonio", "", esp, null, t, 552);
		createRider("Gerrans", "Simon", "", aus, null, t, 553);
		createRider("Hagen", "Edvald Boasson", "", noo, null, t, 554);
		createRider("Knees", "Christian", "", ger, null, t, 555);
		createRider("Swift", "Ben", "", eng, null, t, 556);
		createRider("Thomas", "Geraint", "", eng, null, t, 557);
		createRider("Uran", "Rigoberto", "", col, null, t, 558);
		createRider("Zandio", "Xabier", "", esp, null, t, 559);

		t = createTeam("Quick-Step", "Wilfried Peeters", bel);

		createRider("Chavanel", "Sylvain", "", fra, null, t, 600);
		createRider("Boonen", "Tom", "", bel, null, t, 601);
		createRider("Ciolek", "Gerald", "", ger, null, t, 602);
		createRider("Weert", "Kevin", "de", bel, null, t, 603);
		createRider("Devenyns", "Dries", "", bel, null, t, 604);
		createRider("Engels", "Addy", "", ned, null, t, 605);
		createRider("Pineau", "Jerome", "", fra, null, t, 606);
		createRider("Steegmans", "Gert", "", bel, null, t, 607);
		createRider("Terpstra", "Niki", "", ned, null, t, 608);

		t = createTeam("Francaise des jeux", "Thierry Bricaud", fra);

		createRider("Casar", "Sandy", "", fra, null, t, 651);
		createRider("Bonnet", "William", "", fra, null, t, 652);
		createRider("Delage", "Mickaël", "", fra, null, t, 653);
		createRider("Jeannesson", "Arnold", "", fra, null, t, 654);
		createRider("Meersman", "Gianni", "", bel, null, t, 655);
		createRider("Pauriol", "Rémy", "", fra, null, t, 656);
		createRider("Roux", "Anthony", "", fra, null, t, 657);
		createRider("Roy", "Jeremy", "", fra, null, t, 658);
		createRider("Vichot", "Arthur", "", fra, null, t, 659);

		t = createTeam("BMC Racing", "John Lelangue", zwi);

		Rider rider4 = createRider("Evans", "Cadel", "", aus, null, t, 701);
		createRider("Bookwalter", "Brett", "", vst, null, t, 702);
		createRider("Burghardt", "Marcus", "", ger, null, t, 703);
		createRider("Hincapie", "George", "", vst, null, t, 704);
		createRider("Moinard", "Amaël", "", fra, null, t, 705);
		createRider("Morabito", "Steve", "", zwi, null, t, 706);
		createRider("Quinziato", "Manuel", "", ita, null, t, 707);
		createRider("Santaromita", "Ivan", "", ita, null, t, 708);
		createRider("Schär", "Michael", "", zwi, null, t, 709);

		t = createTeam("Cofidis", "Didier Rous", est);

		createRider("Taaramae", "Rein", "", est, null, t, 751);
		createRider("Buffaz", "Mickaël", "", fra, null, t, 752);
		createRider("Dumoulin", "Samuel", "", fra, null, t, 753);
		createRider("Duque", "Leonardo", "", col, null, t, 754);
		createRider("Farès", "Julien", "El", fra, null, t, 755);
		createRider("Gallopin", "Tony", "", fra, null, t, 756);
		createRider("Moncoutié", "David", "", fra, null, t, 757);
		createRider("Valentin", "Tristan", "", fra, null, t, 758);
		createRider("Zingle", "Romain", "", bel, null, t, 759);

		t = createTeam("Lampre-ISD", "Orlando Maini", ita);

		createRider("Cunego", "Damiano", "", ita, null, t, 801);
		createRider("Bertagnolli", "Leonardo", "", ita, null, t, 802);
		createRider("Bole", "Grega", "", slv, null, t, 803);
		createRider("Bono", "Matteo", "", ita, null, t, 804);
		createRider("Hondo", "Danilo", "", ger, null, t, 805);
		createRider("Kostijoek", "Denis", "", ukr, null, t, 806);
		createRider("Loosli", "David", "", zwi, null, t, 807);
		createRider("Malori", "Adriano", "", ita, null, t, 808);
		createRider("Petacchi", "Alessandro", "", ita, null, t, 809);

		t = createTeam("HTC Highroad", "Brian Holm", eng);

		createRider("Cavendish", "Mark", "", eng, null, t, 851);
		createRider("Bak", "Lars", "", den, null, t, 852);
		createRider("Eisel", "Bernhard", "", oos, null, t, 853);
		createRider("Goss", "Matthew", "", aus, null, t, 854);
		createRider("Martin", "Tony", "", ger, null, t, 855);
		createRider("Pate", "Danny", "", vst, null, t, 856);
		createRider("Renshaw", "Mark", "", aus, null, t, 857);
		createRider("Garderen", "Tejay", "Van", vst, null, t, 858);
		createRider("Velits", "Peter", "", slw, null, t, 859);

		t = createTeam("Europcar", "Dominique Arnould", fra);

		createRider("Voeckler", "Thomas", "", fra, null, t, 901);
		createRider("Charteau", "Anthony", "", fra, null, t, 902);
		createRider("Gautier", "Cyril", "", fra, null, t, 903);
		createRider("Gène", "Yohann", "", fra, null, t, 904);
		createRider("Jérôme", "Vincent", "", fra, null, t, 905);
		createRider("Kern", "Christophe", "", fra, null, t, 906);
		Rider rider6 = createRider("Quemeneur", "Perrig", "", fra, null, t, 907);
		createRider("Rolland", "Pierre", "", fra, null, t, 908);
		createRider("Turgot", "Sébastien", "", fra, null, t, 909);

		t = createTeam("Katoesja", "Dmitri Konisjev", rus);

		createRider("Karpets", "Vladimir", "", rus, null, t, 951);
		createRider("Broett", "Pavel", "", rus, null, t, 952);
		createRider("Galimzjanov", "Denis", "", rus, null, t, 953);
		createRider("Goesev", "Vladimir", "", rus, null, t, 954);
		createRider("Ignatjev", "Michail", "", rus, null, t, 955);
		createRider("Isajtsjev", "Vladimir", "", rus, null, t, 956);
		createRider("Kolobnev", "Aleksandr", "", rus, null, t, 957);
		createRider("Silin", "Egor", "", rus, null, t, 958);
		createRider("Trofimov", "Joeri", "", rus, null, t, 959);

		t = createTeam("Vacansoleil-DCM", "Hilaire Van der Schueren", ned);

		createRider("Feillu", "Romain", "", fra, null, t, 1001);
		createRider("Bozic", "Borut", "", slv, null, t, 1002);
		createRider("Gendt", "Thomas", "De", bel, null, t, 1003);
		createRider("Hoogerland", "Johnny", "", ned, null, t, 1004);
		createRider("Leukemans", "Björn", "", bel, null, t, 1005);
		createRider("Marcato", "Marco", "", ita, null, t, 1006);
		createRider("Poels", "Wout", "", ned, null, t, 1007);
		createRider("Ruijgh", "Rob", "", ned, null, t, 1008);
		createRider("Westra", "Lieuwe", "", ned, null, t, 1009);

		t = createTeam("Saur-Sojasun", "Stephane Heulot", fra);

		createRider("Coppel", "Jérôme", "", fra, null, t, 1051);
		createRider("Coyot", "Arnaud", "", fra, null, t, 1052);
		Rider rider7 = createRider("Delaplace", "Anthony", "", fra, null, t, 1053);
		createRider("Engoulvent", "Jimmy", "", fra, null, t, 1054);
		createRider("Galland", "Jérémie", "", fra, null, t, 1055);
		createRider("Hivert", "Jonathan", "", fra, null, t, 1056);
		createRider("Jeandesboz", "Fabrice", "", fra, null, t, 1057);
		createRider("Mangel", "Laurent", "", fra, null, t, 1058);
		createRider("Talabardon", "Yannick", "", fra, null, t, 1059);

		dc().commit();

		//-- Etappes
		Etappe eprologue = createEtappe("1", EtappeType.Prologue, DateUtil.dateFor(2021, Calendar.JULY, 5), "Leeds", "Harrogate", 191, "http://www.letour.com/le-tour/2014/us/stage-1.html",
			m_ed);
		Etappe firstEtappe = createEtappe("2", EtappeType.MediumMountains, DateUtil.dateFor(2021, Calendar.JULY, 6), "York", "Sheffield", 198, "http://www.letour.com/le-tour/2014/us/stage-2.html",
			m_ed);
		createEtappe("3", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 7), "Cambridge", "Londen", 159, "http://www.letour.com/le-tour/2014/us/stage-3.html", m_ed);

		createEtappe("4", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 8), "Le Touquet", "Lille", 164, "http://www.letour.com/le-tour/2014/us/stage-4.html", m_ed);
		createEtappe("5", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 9), "Ieper (B)", "Arenberg", 156, "http://www.letour.com/le-tour/2014/us/stage-5.html", m_ed);
		createEtappe("6", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 10), "Arras", "Reims", 194, "http://www.letour.com/le-tour/2014/us/stage-6.html", m_ed);
		createEtappe("7", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 11), "Épernay", "Nancy", 233, "http://www.letour.com/le-tour/2014/us/stage-7.html", m_ed);
		createEtappe("8", EtappeType.MediumMountains, DateUtil.dateFor(2021, Calendar.JULY, 12), "Tomblaine", "Gérardmer", 161, "http://www.letour.com/le-tour/2014/us/stage-8.html", m_ed);
		createEtappe("9", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 13), "Gérardmer", "Mulhouse", 166, "http://www.letour.com/le-tour/2014/us/stage-9.html", m_ed);
		createEtappe("10", EtappeType.MediumMountains, DateUtil.dateFor(2021, Calendar.JULY, 14), "Mulhouse", "La Planche des Belles Filles", 161,
			"http://www.letour.com/le-tour/2014/us/stage-10.html",
			m_ed);
		createEtappe("11", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 16), "Besançon", "Oyonnax", 186, "http://www.letour.com/le-tour/2014/us/stage-11.html", m_ed);
		createEtappe("12", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 17), "Bourg-en-Bresse", "Saint-Etienne", 183, "http://www.letour.com/le-tour/2014/us/stage-12.html", m_ed);
		createEtappe("13", EtappeType.HighMountains, DateUtil.dateFor(2021, Calendar.JULY, 18), "Saint-Etienne", "Chamrousse", 200, "http://www.letour.com/le-tour/2014/us/stage-13.html", m_ed);
		createEtappe("14", EtappeType.HighMountains, DateUtil.dateFor(2021, Calendar.JULY, 19), "Grenoble", "Risoul", 177, "http://www.letour.com/le-tour/2014/us/stage-14.html", m_ed);
		createEtappe("15", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 20), "Tallard", "Nîmes", 222, "http://www.letour.com/le-tour/2014/us/stage-15.html", m_ed);
		createEtappe("16", EtappeType.HighMountains, DateUtil.dateFor(2021, Calendar.JULY, 22), "Carcassonne", "Bagnères-de-Luchon", 237, "http://www.letour.com/le-tour/2014/us/stage-16.html", m_ed);
		createEtappe("17", EtappeType.HighMountains, DateUtil.dateFor(2021, Calendar.JULY, 23), "Saint-Gaudens", "Soulan Pla d’Adet", 125, "http://www.letour.com/le-tour/2014/us/stage-17.html",
			m_ed);
		createEtappe("18", EtappeType.HighMountains, DateUtil.dateFor(2021, Calendar.JULY, 24), "Pau", "Hautacam", 145, "http://www.letour.com/le-tour/2014/us/stage-18.html", m_ed);
		createEtappe("19", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 25), "Maubourguet", "Bergerac", 208, "http://www.letour.com/le-tour/2014/us/stage-19.html", m_ed);
		createEtappe("20", EtappeType.IndividualTime, DateUtil.dateFor(2021, Calendar.JULY, 26), "Bergerac", "Périgueux", 54, "http://www.letour.com/le-tour/2014/us/stage-20.html", m_ed);
		createEtappe("21", EtappeType.Plain, DateUtil.dateFor(2021, Calendar.JULY, 27), "Évry", "Paris Champs-Élysées", 136, "http://www.letour.com/le-tour/2014/us/stage-21.html", m_ed);
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
			m_ed.setStartDate(DateUtil.dateFor(2021, Calendar.JULY, 5));
			eprologue.setPhase(EtappePhase.CALCULATING);

			List<Rider>	rlist = dc().query(QCriteria.create(Rider.class).eq(Rider_.edition(), m_ed).ascending(Rider_.lastName()));

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

	@NonNull
	private PersonRight createRight(@NonNull Person p, @NonNull ApplicationRight right, Date start, Date end) throws Exception {
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

	private PersonRight addRight(@NonNull ApplicationRight right, Date from, Date to) throws Exception {
		return createRight(m_lastPerson, right, from, to);
	}

	private PersonRight addRight(@NonNull ApplicationRight right) throws Exception {
		return createRight(m_lastPerson, right, DateUtil.dateFor(2010, 0, 1), null);
	}

	@Nullable
	private Date m_lastEtappe;

	private Edition m_ed;

	@NonNull
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

	@NonNull
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
