package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.adm.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.controlfactory.*;
import to.etc.domui.component.form.*;
import to.etc.domui.component.input.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.converter.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.domui.trouble.*;
import to.etc.domui.util.*;
import to.etc.webapp.nls.*;
import to.etc.webapp.query.*;

public class PersonPasswordEditPage extends BasicTourPage {

	public PersonPasswordEditPage() {
	}

	protected static final BundleRef BUNDLE_SHARED_MSG = SharedMsg.BUNDLE;

	private HiddenText<String> m_oldPasswordControl;

	private HiddenText<String> m_newPasswordControl;

	private String m_oldPassword;

	private String m_newPassword;

	private String m_newPasswordAgain;

	private ModelBindings m_modelBindings;

	public String getOldPassword() {
		return m_oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		m_oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return m_newPassword;
	}

	public void setNewPassword(String newPassword) {
		m_newPassword = newPassword;
	}

	public String getNewPasswordAgain() {
		return m_newPasswordAgain;
	}

	public void setNewPasswordAgain(String newPasswordAgain) {
		m_newPasswordAgain = newPasswordAgain;
	}

	@Override
	public void createContent() throws Exception {
		TabularFormBuilder tabFormBuilder = new TabularFormBuilder(this);

		m_oldPasswordControl = getPasswordInput();
		tabFormBuilder.addProp("oldPassword", m_oldPasswordControl);
		m_newPasswordControl = getPasswordInput();
		tabFormBuilder.addProp("newPassword", m_newPasswordControl);
		tabFormBuilder.addProp("newPasswordAgain", getPasswordInput());

		m_modelBindings = tabFormBuilder.getBindings();
		m_modelBindings.moveModelToControl();
		add(tabFormBuilder.finish());
		addButtonBar();
	}

	private HiddenText<String> getPasswordInput() {
		final HiddenText<String> password = new HiddenText<String>(String.class);
		password.setMaxLength(16);
		password.setWidth("90px");
		password.setMandatory(true);
		password.addValidator(new IValueValidator<String>() {
			@Override
			public void validate(String input) throws Exception {
				if(input != null) {
					if(input.length() < PersonEditPage.PASSWORD_MIN_LENGTH) {
						throw new ValidationException(Msgs.UI_VALIDATION_FAILED);
					}
				}
			}
		});
		return password;
	}

	protected void addButtonBar() {
		ButtonBar buttonBar = new ButtonBar();
		add(buttonBar);

		buttonBar.addBackButton();
		buttonBar.addButton("Opslaan", new IClicked<DefaultButton>() {
			@Override
			public void clicked(DefaultButton btn) throws Exception {
				save();
			}
		});
	}

	protected void save() throws Exception {

		m_modelBindings.moveControlToModel();

		Person person = ((TourUser) UIContext.getCurrentUser()).getPerson();

		// Controle of het oude wachtwoord correct is.
		if(!getOldPassword().equalsIgnoreCase(person.getPassword())) {
			m_oldPasswordControl.setMessage(UIMessage.error(BUNDLE_SHARED_MSG, SharedMsg.PASSWORD_INCORRECT));
			return;
		}

		// Controle of nieuwe wachtwoord en controle nieuwe wachtwoord gelijk zijn.
		if(!getNewPassword().equalsIgnoreCase(getNewPasswordAgain())) {
			m_newPasswordControl.setMessage(UIMessage.error(BUNDLE_SHARED_MSG, SharedMsg.PASSWORD_NOT_SAME));
			return;
		}

		// Controle oude en nieuwe wachtwoord mogen niet gelijk zijn.
		if(getNewPassword().equalsIgnoreCase(getOldPassword())) {
			m_newPasswordControl.setMessage(UIMessage.error(BUNDLE_SHARED_MSG, SharedMsg.PASSWORD_NOT_SAME_OLD));
			return;
		}

		QDataContext dc = getSharedContext();
		dc.startTransaction();
		dc.refresh(person);   // @TODO Blijkbaar is hier de link met de database verdwenen en moeten we een refresh doen waarom?
		person.setPassword(getNewPassword());
		dc.commit();
		UIGoto.moveNew(TourPortalPage.class);
	}

}
