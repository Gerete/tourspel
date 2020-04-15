package nl.gerete.tourspel.pages.adm;

import nl.gerete.tourspel.adm.TourUser;
import nl.gerete.tourspel.db.ApplicationRight;
import to.etc.domui.component.menu.IUIAction;
import to.etc.domui.component.misc.IIconRef;
import to.etc.domui.component.misc.Icon;
import to.etc.domui.dom.html.NodeBase;
import to.etc.domui.state.UIGoto;

public class ActionEdition implements IUIAction<Void> {

	private final static String MENUNAME = "Editiebeheer";

	private final static IIconRef ICON = Icon.of("images/action-edition.gif");

	@Override
	public String getDisableReason(Void instance) throws Exception {
		if(!TourUser.getCurrent().hasRight(ApplicationRight.ADMIN.toString()))
			return "You do not have rights";

		return null;
	}

	@Override
	public String getName(Void instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public IIconRef getIcon(Void instance) throws Exception {
		return ICON;
	}

	@Override
	public void execute(NodeBase component, Void instance) throws Exception {
		UIGoto.moveSub(AdminEditionPage.class);
	}

	@Override
	public String getTitle(Void instance) throws Exception {
		// TODO Auto-generated method stub
		return MENUNAME;
	}
}
