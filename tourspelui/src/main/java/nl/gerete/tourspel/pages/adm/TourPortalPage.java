package nl.gerete.tourspel.pages.adm;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

public class TourPortalPage extends BasicTourPage {

	private Div m_content;

	@Override
	public void createContent() throws Exception {

		m_content = getTourFrame();

		TourUser user = (TourUser) UIContext.getLoggedInUser();
		EditionPhase ep = EditionBP.getCurrentEdition(getSharedContext()).getPhase();

		if(user.hasRight(ApplicationRight.ADMIN)) {
			UIGoto.moveSub(PersonListPage.class);
		} else {
			handlePlayersPages(user, ep);
		}

	}

	private void handlePlayersPages(TourUser user, EditionPhase ep) throws Exception {
		if(ep.equals(EditionPhase.OPEN)) {
			UIGoto.moveSub(PersonDetailListPage.class, new PageParameters("id", user.getPerson().getId()));
		} else {
			if(ep.equals(EditionPhase.RUNNING)) {
				UIGoto.moveSub(EtappePlayersPage.class);
			} else {
				UIGoto.moveSub(HelloTour.class);
			}
		}
	}

}
