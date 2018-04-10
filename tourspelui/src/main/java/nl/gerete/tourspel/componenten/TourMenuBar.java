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

import javax.annotation.*;
import java.util.*;

@DefaultNonNull
public class TourMenuBar extends Div {

	@Nullable
	private PopupMenu m_adminPopupMenu;

	@Nullable
	private PopupMenu m_playerPopupMenu;

	@Nullable
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
		DisplaySpan<String> daysLeft = new DisplaySpan<>(String.class);
		add(daysLeft);
		daysLeft.setCssClass("l-menu-item");
		Edition currentEdition = EditionBP.getCurrentEdition(getSharedContext());
		daysLeft.setValue("Nog " + EditionBP.getDaysToStartEdition(getSharedContext()) + " dagen tot de start van editie " + currentEdition.getYear());

	}

	private void addLogOut() {

		LinkButton logOut = new LinkButton("Uitloggen", "images/fietsbel.png", (IClicked<LinkButton>) clickednode -> {
			UILogin.logout();
			//-- !!! I must return to application root!!
			UIGoto.redirect(UIContext.getRequestContext().getRelativePath(""));
		});
		logOut.addCssClass("r-menu-uitlog");
		add(logOut);
	}

	private void addPlayersMenu() {
		PopupMenu popupMenu = m_playerPopupMenu = new PopupMenu();
		popupMenu.addAction(new ActionEditPerson());
		popupMenu.addAction(new ActionEditPassword());

		LinkButton playersMenu = new LinkButton("Deelnemers", (IClicked<LinkButton>) clickednode -> {
			togglePlayerMenu(TourUser.getCurrent().getPerson(), clickednode);
			//				togglePlayerMenu(null, clickednode.getParent());
		});
		playersMenu.setCssClass("r-menu-item");
		add(playersMenu);


	}

	private void togglePlayerMenu(Object data, NodeContainer nc) {
		Objects.requireNonNull(m_playerPopupMenu).show(nc, data);
	}

	private void addAdminMenu() {
		PopupMenu popupMenu = m_adminPopupMenu = new PopupMenu();
		popupMenu.addAction(new ActionAdminTeams());
		popupMenu.addAction(new ActionAdminCountries());
		popupMenu.addAction(new ActionEdition());
		popupMenu.addAction(new ActionDaily());

		LinkButton adminMenu = new LinkButton("Admin", new IClicked<LinkButton>() {
			@Override
			public void clicked(@Nonnull LinkButton clickednode) throws Exception {
				toggleAdminMenu(null, clickednode);
			}
		});
		adminMenu.setCssClass("r-menu-item");
		add(adminMenu);
	}

	private void toggleAdminMenu(@Nullable Object data, NodeContainer nc) {
		Objects.requireNonNull(m_adminPopupMenu).show(nc, data);
	}

	private void addPortalMenu() {
		PopupMenu popupMenu = m_portalPopupMenu = new PopupMenu();
		popupMenu.addAction(new ActionPortalPage());
		popupMenu.addAction(new ActionEtappeResult());

		LinkButton portal = new LinkButton("Portaal", (IClicked<LinkButton>) clickednode -> togglePortalMenu(null, clickednode));
		portal.setCssClass("r-menu-item");
		add(portal);
	}

	private void togglePortalMenu(@Nullable Object data, NodeContainer nc) {
		Objects.requireNonNull(m_portalPopupMenu).show(nc, data);
	}
}
