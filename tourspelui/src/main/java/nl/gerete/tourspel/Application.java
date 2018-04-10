package nl.gerete.tourspel;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.adm.*;
import to.etc.dbpool.*;
import to.etc.domui.dom.header.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.hibernate.config.*;
import to.etc.domui.hibernate.config.HibernateConfigurator.*;
import to.etc.domui.login.*;
import to.etc.domui.server.*;
import to.etc.domui.themes.sass.*;
import to.etc.smtp.Address;
import to.etc.util.*;
import to.etc.webapp.pendingoperations.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import javax.servlet.*;
import javax.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class Application extends DomApplication {

	static private boolean m_dateInited;

	static private Date m_baseDate;

	private static DataSource m_dataSource;

	private static to.etc.smtp.Address m_fromAddress;

	static synchronized public to.etc.smtp.Address getFromAddress() {
		if(m_fromAddress == null)
			m_fromAddress = new to.etc.smtp.Address("jal@etc.to", "test-tourspel");
		return m_fromAddress;
	}

	public static DataSource getDataSource() {
		return m_dataSource;
	}

	@Override
	public Class< ? extends UrlPage> getRootPage() {
		return TourPortalPage.class;
	}

	@Override
	protected void initialize(@Nonnull ConfigParameters pp) throws Exception {
		addHeaderContributor(HeaderContributor.loadStylesheet("css/style.css"), 100);
		addHeaderContributor(HeaderContributor.loadStylesheet("css/tourspel.css"), 100);

		//-- Handle basic config.
		String tourspelProperties = DeveloperOptions.getString("tourspel", "tourspel.properties");
		File configFile = getAppFile("WEB-INF/" + tourspelProperties);
		PropertyFile.initialize(configFile);
		initDatabase(configFile);
		setDefaultThemeFactory(SassThemeFactory.INSTANCE);

		setLoginAuthenticator(new TourLoginAuthenticator());
		setLoginAuthenticator(new TourLoginAuthenticator());

		setLoginDialogFactory(new ILoginDialogFactory() {
			@Override
			public String getLoginRURL(final String originalTarget) {
				StringBuilder sb = new StringBuilder();
				//                              sb.append("login/login.jsp?target=");
				sb.append(LoginPage.class.getName() + ".ui?target=");
				StringTool.encodeURLEncoded(sb, originalTarget);
				return sb.toString();
			}

			@Override
			public String getAccessDeniedURL() {
				return null; // Just use the built-in DomUI access denied page
			}
		});

		// Bulk mailer.
		PollingWorkerQueue.initialize();
		PropertyFile propertyfile = PropertyFile.getInstance();
		String host = propertyfile.getProperty("smtp.host");
		if(null == host)
			host = "localhost";
		String fromName = propertyfile.getProperty("smtp.from");
		if(null == fromName)
			fromName = "noreply@itris.nl";
		m_fromAddress = new to.etc.smtp.Address(fromName, "Tourspel");

	//	SmtpTransport st = new SmtpTransport(host);
	//	BulkMailer.initialize(m_dataSource, st);
	//
	//	//-- Start the job executor.
	//	SystemTask.getInstance().register(PointsCalculatorProvider.INSTANCE);
	//	SystemTask.getInstance().register(new TestTaskProvider());
	//	SystemTask.getInstance().initialize();
	}

	public static void initTest() throws Exception {
		//-- Handle basic config.
		String tourspelProperties = DeveloperOptions.getString("tourspel", "WEB-INF/tourspel.properties");
		File configFile = new File("WebContent/WEB-INF/" + tourspelProperties);
		PropertyFile.initialize(configFile);
		initDatabase(configFile);
	}

	public static void initDatabase(File dbProperties) throws Exception {

		File pf = getAppFile("WEB-INF/pool.xml");
		if(!pf.exists())
			throw new UnavailableException("Missing file WEB-INF/pool.xml containing the database to use");
		//-- 1. Get a datasource using the to.etc.dbpool pool manager.
		ConnectionPool p = PoolManager.getInstance().initializePool(pf, "tourspel");
		m_dataSource = p.getUnpooledDataSource();

		//-- 2. Test for an existing db
		PropertyFile propertyfile = PropertyFile.getInstance();
		String m = propertyfile.getProperty("createdb");
		if(null == m)
			m = "false";

		Mode mode = "true".equalsIgnoreCase(m) ? Mode.CREATE : Mode.UPDATE;

		//-- 3. Use the HibernateConfigurator utility class to configure easily.
		HibernateConfigurator.addClasses( //
			// Op alfabetische volgorde aub; NB '//' zorgt voor correcte uitlijning
			Address.class, //
			Country.class, //
			Edition.class, //
			Etappe.class, //
			EtappeResult.class, //
			Person.class, //
			PersonRight.class, //
			PlayList.class, //
			PlayListEntry.class, //
			Region.class, //
			Rider.class, //
			Team.class //
			, StoppedRider.class //
			, PlayListResult.class //
		);
//		HibernateConfigurator.showSQL(true);

		//-- 4. Initialize and create Hibernate objects eq. create db (if necessary)
		HibernateConfigurator.schemaUpdate(mode);
		HibernateConfigurator.initialize(m_dataSource);

		//-- 5. Tell the generic layer how to create default DataContext's.
		QContextManager.setImplementation(QContextManager.DEFAULT, HibernateConfigurator.getDataContextFactory()); // Prime factory with connection source

		//-- 6. Check for testdata
		InitDatabase ib = new InitDatabase();
		ib.initialize();
	}

	@Override
	protected void destroy() {
//		SystemTask.getInstance().terminate();
	}

	static public synchronized Date getNow() {
		if(!m_dateInited) {
			try {
				PropertyFile propertyfile = PropertyFile.getInstance();
				String dts = propertyfile.getProperty("tourspel.datum");
				if(null != dts) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					df.setLenient(false);
					Date dt = df.parse(dts);
					m_baseDate = dt;
				}
				m_dateInited = true;
			} catch(Exception x) {
				throw WrappedException.wrap(x);
			}
		}
		if(m_baseDate == null)
			return new Date();

		Calendar cal = Calendar.getInstance();
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);

		cal.setTime(m_baseDate);
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		cal.set(Calendar.SECOND, s);
		cal.set(Calendar.MILLISECOND, ms);
		return cal.getTime();
	}


}

