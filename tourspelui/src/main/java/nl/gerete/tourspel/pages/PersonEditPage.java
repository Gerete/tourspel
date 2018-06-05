package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.adm.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.controlfactory.*;
import to.etc.domui.component.form.*;
import to.etc.domui.component.input.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.converter.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.login.*;
import to.etc.domui.state.*;
import to.etc.domui.trouble.*;
import to.etc.domui.util.*;
import to.etc.util.*;
import to.etc.webapp.query.*;

import java.text.*;

public class PersonEditPage extends BasicEditPage<Person> {
	public PersonEditPage() {
		super(Person.class);
	}

	private String m_id;
	public static final int PASSWORD_MIN_LENGTH = 8;

	@UIUrlParameter(name = "id")
	public String getId() {
		return m_id;
	}

	public void setId(String id) {
		m_id = id;
	}


	@Override
	public void createContent() throws Exception {
		Person person = getObject();

		TabularFormBuilder tabFormBuilder = new TabularFormBuilder(person);

		Text<String> email = getEmailInput();
		tabFormBuilder.addProp("email", email);
		if(person.getId() == null) {
			tabFormBuilder.addProp("password", getPasswordInput());
		} else {
			email.setReadOnly(true);
		}
		tabFormBuilder.addProp("firstName");
		tabFormBuilder.addProp("prefix");
		tabFormBuilder.addProp("lastName");
		tabFormBuilder.addProp("phoneNumber");

		final ModelBindings modelBindings = tabFormBuilder.getBindings();
		modelBindings.moveModelToControl();
		add(tabFormBuilder.finish());
		add(getButtonBar(modelBindings));
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
					if(input.length() < PASSWORD_MIN_LENGTH) {
						throw new ValidationException(Msgs.UI_VALIDATION_FAILED);
					}
				}
			}
		});
		return password;
	}

	private Text<String> getEmailInput() {
		final Text<String> email = new Text<String>(String.class);
		email.setMaxLength(120);
		email.setWidth("200px");
		email.setMandatory(true);
		email.addValidator(new IValueValidator<String>() {
			@Override
			public void validate(String input) throws Exception {
				if(!StringTool.isValidEmail(input)) {
					throw new ValidationException(Msgs.NOT_VALID, input);
				}
			}
		});
		return email;
	}

	@Override
	protected ButtonBar getButtonBar(final ModelBindings modelBindings) {
		ButtonBar buttonBar = new ButtonBar();
		add(buttonBar);

		buttonBar.addBackButton();
		buttonBar.addButton("Opslaan", new IClicked<DefaultButton>() {
			@Override
			public void clicked(DefaultButton btn) throws Exception {
				modelBindings.moveControlToModel();
				QDataContext dc = getSharedContext();
				dc.startTransaction();
				dc.save(getObject());

				/*
				 * jal 20120515: een bestaand persoon moet niet steeds weer een nieuw recht krijgen....
				 */
				if(getObject().getRightList().size() == 0) {
					PersonRight rp = new PersonRight();
					rp.setPerson(getObject());
					rp.setRight(ApplicationRight.PLAYER);
					rp.setStartDate(Application.getNow());
					getObject().getRightList().add(rp);
					dc.save(rp);
					dc.commit();
					sendWelcomeMail(rp);
				} else {
					dc.commit();
				}

				UILogin.login(getObject().getEmail(), getObject().getPassword());
				UIGoto.moveNew(TourPortalPage.class);
			}

		});
		return buttonBar;
	}

	private void sendWelcomeMail(PersonRight rp) throws Exception {
		if(!checkEdition(EditionPhase.OPEN)) {
			throw new IllegalStateException("Edition not Open for registration - should be");
		}
		SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy");
		SimpleDateFormat dtTime = new SimpleDateFormat("EEE, d MMM yyyy mm:ss");
		TourMailer tourMailer = new TourMailer();
		String startDate = dt.format(getEdition().getStartDate());
		String registrationDeadline = dt.format(getEdition().getRegistrationDeadline());
		String changeRidersDeadline = dtTime.format(getEdition().getChangeRidersDeadline());
		String payRegistrationFeeDeadline = dt.format(getEdition().getPayRegistrationFeeDeadline());
		String payPriceMoneyDeadline = dt.format(getEdition().getPayPriceMoneyDeadline());
		String endDate = dt.format(getEdition().getEndDate());

		tourMailer.start(getObject(), "Welkom bij het Tourspel 2019");
		tourMailer.generate(getClass(), "register-welcome.tpl.html", "person", getObject(), "startDate", startDate, "registrationDeadline", registrationDeadline, "changeRidersDeadline",
			changeRidersDeadline, "payRegistrationFeeDeadline", payRegistrationFeeDeadline, "payPriceMoneyDeadline", payPriceMoneyDeadline, "endDate", endDate);
		tourMailer.send(getSharedContext());
	}
}
