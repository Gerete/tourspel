package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.adm.*;
import nl.gerete.tourspel.pages.etappe.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.login.*;
import to.etc.domui.state.*;

public class TourMenuBar extends Div {

	private PopupMenu m_adminPopupMenu;

	private PopupMenu m_playerPopupMenu;

	private PopupMenu m_portalPopupMenu;

	@Override
	public void createContent() throws Exception {

		setCssClass("r-menu");

		addNumberOfDaysLeft();

		if(TourUser.getCurrent().hasRight(ApplicationRight.ADMIN.toString()) || TourUser.getCurrent().hasRight(ApplicationRight.CLERK.toString()))
			addAdminMenu();

		addPlayersMenu();

		addPortalMenu();

		addLogOut();
	}

	private void addNumberOfDaysLeft() throws Exception {
		DisplaySpan daysLeft = new DisplaySpan(String.class);
		add(daysLeft);
		daysLeft.setCssClass("l-menu-item");
		Edition currentEdition = EditionBP.getCurrentEdition(getSharedContext());
		daysLeft.setValue("Nog " + EditionBP.getDaysToStartEdition(getSharedContext()) + " dagen tot de start van editie " + currentEdition.getYear());

	}

	private void addLogOut() {

		LinkButton logOut = new LinkButton("Uitloggen", "images/fietsbel.png", new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				UILogin.logout();
				//-- !!! I must return to application root!!
				UIGoto.redirect(UIContext.getRequestContext().getRelativePath(""));
			}
		});
		logOut.addCssClass("r-menu-uitlog");
		add(logOut);
	}

	private void addPlayersMenu() {
		m_playerPopupMenu = new PopupMenu();
		m_playerPopupMenu.addAction(new ActionEditPerson());
		m_playerPopupMenu.addAction(new ActionEditPassword());

		LinkButton playersMenu = new LinkButton("Deelnemers", new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				togglePlayerMenu(TourUser.getCurrent().getPerson(), clickednode);
				//				togglePlayerMenu(null, clickednode.getParent());
			}
		});
		playersMenu.setCssClass("r-menu-item");
		add(playersMenu);


	}

	void togglePlayerMenu(Object data, NodeContainer nc) {
		m_playerPopupMenu.show(nc, data);
	}

	private void addAdminMenu() {
		m_adminPopupMenu = new PopupMenu();
		m_adminPopupMenu.addAction(new ActionAdminTeams());
		m_adminPopupMenu.addAction(new ActionAdminCountries());
		m_adminPopupMenu.addAction(new ActionEdition());
		m_adminPopupMenu.addAction(new ActionDaily());

		LinkButton adminMenu = new LinkButton("Admin", new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				toggleAdminMenu(null, clickednode);
			}
		});
		adminMenu.setCssClass("r-menu-item");
		add(adminMenu);
	}

	void toggleAdminMenu(Object data, NodeContainer nc) {
		m_adminPopupMenu.show(nc, data);
	}

	private void addPortalMenu() {
		m_portalPopupMenu = new PopupMenu();
		m_portalPopupMenu.addAction(new ActionPortalPage());
		m_portalPopupMenu.addAction(new ActionEtappeResult());

		LinkButton portal = new LinkButton("Portaal", new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				togglePortalMenu(null, clickednode);
			}
		});
		portal.setCssClass("r-menu-item");
		add(portal);
	}

	protected void togglePortalMenu(Object data, NodeContainer nc) {
		m_portalPopupMenu.show(nc, data);
	}
}
