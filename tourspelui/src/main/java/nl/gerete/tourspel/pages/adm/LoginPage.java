package nl.gerete.tourspel.pages.adm;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.input.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.header.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.login.*;
import to.etc.domui.state.*;
import to.etc.domui.trouble.*;
import to.etc.util.*;

import javax.annotation.*;
import java.util.*;

@DefaultNonNull
public class LoginPage extends UrlPage {
	static private final String LOGO = "images/tour-de-france-logo.jpg";

	private final TextStr m_loginid = new TextStr();

	private final HiddenText<String> m_password = new HiddenText<String>(String.class);

	private final Checkbox m_keeplogin = new Checkbox();

	//	Div	m_failed;
	private Img m_logo = new Img();

	@Nullable
	private TD m_blurb;

	@Nullable
	private TD m_error;

	private int m_failcount;

	static public final String FORCE_TOP = "\nif(parent.location.href != self.location.href) {\n" // Make sure we're not in a frame
		//	+	"  alert('a='+parent.location.href+', b='+self.location.href);\n"
		+ "  parent.window.location.href = self.location.href;\n" + "}";

	@Override
	public void createContent() throws Exception {
		setTitle("Welkom bij het tourspel 2018.");
		setCssClass("l-body");
		add(new VerticalSpacer(40));
		getPage().addHeaderContributor(HeaderContributor.loadStylesheet("css/login.css"), 1000);
		getPage().addHeaderContributor(HeaderContributor.loadJavaScriptlet(FORCE_TOP), 1000);
		Div d = new Div();
		add(d);
		d.setCssClass("l-root");

		d.setReturnPressed((IReturnPressed<? extends NodeBase>) node -> doLogin());

		Table t = new Table();
		d.add(t);
		TBody b = t.addBody();

		//-- Image box to the left
		TD td = b.addRowAndCell();
		m_logo.setSrc(LOGO);
		m_logo.setAlign(ImgAlign.CENTER);
		td.add(m_logo);
		td.setCssClass("l-logo");

		//-- Login box to the right
		td = b.addCell();
		t = new Table();
		td.add(t);
		b = t.addBody();

		//-- New table containing the login box.
		m_blurb = b.addRowAndCell();
		setBlurb();

		m_error = b.addRowAndCell();

		td = b.addRowAndCell();
		td.add("Email");

		td = b.addRowAndCell();
		//		m_loginid = new TextStr();
		m_loginid.setMaxLength(32);
		m_loginid.setMandatory(true);
		m_loginid.setTestID("login");
		td.add(m_loginid);

		td = b.addRowAndCell();
		td.add("Password");

		td = b.addRowAndCell();
		//		m_password = new TextStr();
		m_password.setMaxLength(32);
		m_password.setMandatory(true);
		m_password.setTestID("password");
		td.add(m_password);

		if(DeveloperOptions.isDeveloperWorkstation()) {
			String du = PropertyFile.getInstance().getProperty("loginuser");
			if(null != du)
				m_loginid.setValue(du);
			du = PropertyFile.getInstance().getProperty("loginpw");
			if(null != du)
				m_password.setValue(du);
//			m_loginid.setValue("jal@etc.to");
//			m_password.setValue("frits");
			m_loginid.setValue("benshoe@gmail.com");
			m_password.setValue("ben");
		}

		td = b.addRowAndCell();
		//		m_keeplogin = new Checkbox();
		td.add(m_keeplogin);
		td.add(" Keep me logged in");

		td = b.addRowAndCell();
		td.add(new VerticalSpacer(20));

		DefaultButton defaultButton = new DefaultButton("Log in", new IClicked<DefaultButton>() {
			@Override
			public void clicked(final DefaultButton xb) throws Exception {
				doLogin();
			}
		});
		defaultButton.setTestID("loginButton");
		td.add(defaultButton);
		td.add(new DefaultButton("Registreren", new IClicked<DefaultButton>() {
			@Override
			public void clicked(final DefaultButton xb) throws Exception {
				doRegister();
			}
		}));
		td = b.addRowAndCell();
		td.add(new VerticalSpacer(20));
		td.add(new LinkButton("Wachtwoord vergeten", "images/fietsbel.png", new IClicked<LinkButton>() {
			@Override
			public void clicked(final LinkButton xb) throws Exception {
				doForgottenPassword();
			}
		}));
	}

	protected void doRegister() throws Exception {
		EditionPhase ep = EditionBP.getCurrentEdition(getSharedContext()).getPhase();
		if(ep.equals(EditionPhase.OPEN)) {
			UIGoto.moveSub(PersonEditPage.class, new PageParameters("id", "NEW"));
			return;
		}
		setFailed("Cannot register; the game is not opened for new competitors");
	}

	protected void doForgottenPassword() {
		setFailed("A new password will be send to your email address");
	}

	void doLogin() {
		try {
			if(!UILogin.login(Objects.requireNonNull(m_loginid.getValue()), Objects.requireNonNull(m_password.getValue()))) {
				m_loginid.setCssClass("l-bad");
				m_loginid.setTestID("login");
				m_password.setCssClass("l-bad");
				setFailed("Login failed");
			} else {
				if(m_keeplogin.isChecked()) {
					//-- Create a LOGIN cookie
					UILogin.createLoginCookie(System.currentTimeMillis() + 30l * 1000 * 24 * 60 * 60);
				}

				setBlurb();
				m_logo.setSrc(LOGO);

				//-- Redirect to root frame, and ask it to re-render the source page causing the login.
				/*
				 * We put the page to return-to in the session to prevent putting shit in the root url; it will
				 * be cleared from the session in the menu.
				 */
				String tgt = getPage().getPageParameters().getString("target");
				UIGoto.redirect(tgt);
				System.out.println("LOGIN WORKED: " + tgt);
				return;
			}
		} catch(ValidationException x) {} catch(Exception x) {
			setFailed(x.toString());
			x.printStackTrace();
		}
	}

	void setBlurb() {
		Objects.requireNonNull(m_blurb).setText("Welkom bij het Tourspel 2018");
		Objects.requireNonNull(m_blurb).setCssClass("l-blurb");
	}

	static private final int MAXIMAGE = 4;

	void setFailed(final String what) {
		Objects.requireNonNull(m_error).setText(what);
		Objects.requireNonNull(m_error).setCssClass("l-error");
		System.out.println("LOGIN failed: " + what);
		m_failcount++;
		String name;
		if(m_failcount > MAXIMAGE)
			name = "images/loginfailed" + MAXIMAGE + ".png";
		else
			name = "images/loginfailed" + m_failcount + ".png";
		m_logo.setSrc(name);
		if(m_failcount == 3) {
			Objects.requireNonNull(m_blurb).setText("Hmmm.. Not sure if you're welcome, but try again..");

		}
	}
}
