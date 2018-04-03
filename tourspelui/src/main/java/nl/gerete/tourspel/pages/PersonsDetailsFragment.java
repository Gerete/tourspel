package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

public class PersonsDetailsFragment extends Div {

	public PersonsDetailsFragment() {
		setCssClass("portalFragment");
	}

	@Override
	public void createContent() throws Exception {
		TourUser usr = (TourUser) UIContext.getCurrentUser();
		if(usr == null)
			throw new IllegalStateException("Where is my user???");

		Person person = usr.getPerson();
		add(new Label("Welkom " + person.getFirstName() + " " + (person.getPrefix() == null ? "" : person.getPrefix()) + " " + person.getLastName()));

		Img wrImg = new Img("/images/wiel005.gif");
		wrImg.setWidth("100%");
	}
}
