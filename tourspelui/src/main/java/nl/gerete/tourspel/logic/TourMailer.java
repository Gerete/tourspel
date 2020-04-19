package nl.gerete.tourspel.logic;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.server.*;
import to.etc.domui.util.*;
import to.etc.domui.util.resources.*;
import to.etc.smtp.Address;
import to.etc.smtp.*;
import to.etc.template.*;
import to.etc.util.*;
import to.etc.webapp.mailer.*;
import to.etc.webapp.pendingoperations.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;

@NonNullByDefault
public class TourMailer {
	@NonNull
	final private Map<String, JSTemplate> m_templateMap = new HashMap<String, JSTemplate>();

	@Nullable
	private MailHelper m_mailer;

	@Nullable
	private Person m_who;

	public void start(Person who, String subject) {
		m_who = who;
		Address a = new Address(who.getEmail(), who.toString());
		getMailer().start(a, subj(subject));
	}

	public void start(Person who) {
		m_who = who;
		Address a = new Address(who.getEmail(), who.toString());
		getMailer().start(a, null);
	}

	private String subj(@NonNull String s) {
		String header = "[tourspel] ";
		if(DeveloperOptions.isDeveloperWorkstation()) {
			if(s.startsWith(header)) {
				s = s.substring(header.length()).trim();
				String user = System.getProperty("java.user");
				s = "[" + user + " tourspel]" + s;
			}
		}
		return s;
	}

	public void setSubject(String s) {
		getMailer().setSubject(subj(s));
	}

	@NonNull
	private JSTemplate getTemplate(Class< ? > clz, String tplname) throws Exception {
		JSTemplate tpl = m_templateMap.get(tplname);
		if(null == tpl) {
			JSTemplateCompiler tc = new JSTemplateCompiler();
			tpl = tc.compile(clz, tplname, "utf-8");
			m_templateMap.put(tplname, tpl);
		}
		return tpl;
	}

	public void generate(Class< ? > clz, String tplname, Object... vars) throws Exception {
		JSTemplate tpl = getTemplate(clz, tplname);
		StringBuilder sb = new StringBuilder();
		tpl.execute(sb, vars);

		//-- Append to mesage
		MailHelper mh = getMailer();
		mh.getHtmlBuffer().append(sb);					// Add expansion of HTML template.

		//-- Create the non-html version.
		String text = DomUtil.htmlRemoveAll(sb.toString(), true);
		mh.getTextBuffer().append(text);
	}

//	public void generate(QDataContext dc, Person psn, String subject, Object... vars) throws Exception {
//		JSTemplate tpl = getTemplate(clz, tplname);
//		StringBuilder sb = new StringBuilder();
//		tpl.execute(sb, vars);
//
//		//-- Mail the thing..
//		MailHelper mh = createMailer();
//		mh.start(a, subject);
//		mh.getHtmlBuffer().append(sb);					// Add expansion of HTML template.
//
//		//-- Create the non-html version.
//		String text = DomUtil.htmlRemoveAll(sb.toString(), false);
//		mh.getTextBuffer().append(text);
//
//		appendTourTrailer(mh);
//		mh.send(dc);
//	}

	private MailHelper getMailer() {
		MailHelper mailHelper = m_mailer;
		if(mailHelper == null) {
			mailHelper = m_mailer = new MailHelper() {
				@Override
				@Nullable
				public String getApplicationURL() {
					// FIXME We must have a public URL here!!
					return "http://www.tourspel.nl/tour";
				}

				@Override
				protected InputStream getApplicationResource(String name) throws Exception {
					IResourceRef rr = null;
					try {
						rr = DomApplication.get().getResource(name, ResourceDependencyList.NULL);
					} catch(Exception x) {}
					if(rr != null) {
						if(!rr.exists())
							throw new IllegalStateException("The application resource '" + name + "' does not exist.");
						return Objects.requireNonNull(rr.getInputStream());
					}

					File f = new File("WebContent/" + name);
					if(!f.exists()) {
						throw new IllegalStateException("The application resource '" + name + "' does not exist.");
					}
					return new FileInputStream(f);
				}
			};
			mailHelper.setFrom(Application.getFromAddress());
		}
		return mailHelper;
	}

	public void send(@NonNull QDataContext dc) throws Exception {
		appendTourTrailer(getMailer());
//		getMailer().send(dc);
		m_mailer = null;
	}

	public void send(@NonNull SmtpTransport tr) throws Exception {
		appendTourTrailer(getMailer());
//		getMailer().send(tr);
		m_mailer = null;
	}

	/**
	 * Create a nice tour trailer thingerydoo.
	 * @param mh
	 * @throws Exception
	 */
	private void appendTourTrailer(MailHelper mh) throws Exception {
		mh.nl();
		mh.nl();
		mh.i("Tourspel");
		mh.nl();
		mh.image("logo", "images/logo-tour.png");
	}


	public static void main(String[] args) throws Exception {
		Application.initTest();

		TourMailer m = new TourMailer();

		Person p = new Person();
		p.setFirstName("Frits");
		p.setEmail("jal@etc.to");
		m.start(p, "Test your template mailer");

		Etappe e = new Etappe();
		e.setStart("Lelysad");
		e.setEnd("Huizen");


		m.generate(TourMailer.class, "etapperesult-welcome.tpl.html", "person", p, "etappe", e);

		SmtpTransport tr = new SmtpTransport("itris05.hosts.itris.nl");

		if(false) {
			m.send(tr);
		} else {
			PollingWorkerQueue.initialize();
			BulkMailer.initialize(Application.getDataSource(), tr);

			QDataContext dc = QContextManager.createUnmanagedContext();
			try {
				m.send(dc);
			} finally {
				FileTool.closeAll(dc);
			}

			Thread.sleep(120000);

		}



	}
}
