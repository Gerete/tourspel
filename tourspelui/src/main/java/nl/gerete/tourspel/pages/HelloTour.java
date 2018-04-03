package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.adm.*;
import to.etc.domui.annotations.*;
import to.etc.domui.dom.css.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

@UIRights(value = "PLAYER")
public class HelloTour extends BasicTourPage {

	@Override
	public void createContent() {
		Div tekst = new Div();
		getTourFrame().add(tekst);
		TourUser user = (TourUser) UIContext.getCurrentUser();
		if(user == null)
			throw new IllegalStateException("The user is at this point not known, while it should be!");
		tekst.setText("Hoi " + user.getPerson().getFirstName() + ", welkom bij het Tourspel 2014!");
		tekst.setTextAlign(TextAlign.CENTER);
		tekst.setFontSize("20px");
		Div wr = new Div();
		getTourFrame().add(wr);
		Img wrImg = new Img("images/wiel005.gif");
		wrImg.setWidth("100%");
		wr.add(wrImg);

		PlayListResultFragment topResultFragment = new PlayListResultFragment(PlayListType.KLASSEMENT, 10);
		add(topResultFragment);
		add(new BR());
		add(new BR());
		PlayListResultFragment loosersResultFragment = new PlayListResultFragment(PlayListType.POEDEL, 2);
		add(loosersResultFragment);
		add(new BR());
		add(new BR());
		StoppedRidersFragment stoppedRidersFragment = new StoppedRidersFragment(10);
		add(stoppedRidersFragment);
	}
}
