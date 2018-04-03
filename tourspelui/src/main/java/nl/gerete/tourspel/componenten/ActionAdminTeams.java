package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.newpages.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import javax.annotation.*;

public class ActionAdminTeams implements IUIAction< Void > {

	private final static String MENUNAME = "Ploegen";

	private final static String ICON = "images/fietsbel.png";

	@Override
	public String getDisableReason(Void instance) throws Exception {
		if(!TourUser.getCurrent().hasRight(ApplicationRight.ADMIN.toString()) && !TourUser.getCurrent().hasRight(ApplicationRight.CLERK.toString()))
			return "You do not have rights";

		return null;
	}

	@Override
	public String getName(Void instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public String getIcon(Void instance) throws Exception {
		return ICON;
	}

	@Override
	public void execute(NodeBase component, Void instance) throws Exception {
		UIGoto.moveSub(TeamListPage.class);
	}

	@Override
	@Nullable
	public String getTitle(Void instance) throws Exception {
		// TODO Auto-generated method stub
		return MENUNAME;
	}

}
