package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.pages.adm.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import javax.annotation.*;

public class ActionPortalPage implements IUIAction<Void> {

	private static final String MENUNAME = "Deelnemers";

	private static final String TITLE = "Deelnemers";

	private static final String ICON = "images/list_all_participants.png";

	@Override
	@Nullable
	public String getDisableReason(Void instance) throws Exception {
		return null;
	}

	@Override
	@Nonnull
	public String getName(Void instance) throws Exception {
		return MENUNAME;
	}

	@Override
	@Nullable
	public String getTitle(Void instance) throws Exception {
		return TITLE;
	}

	@Override
	@Nullable
	public String getIcon(Void instance) throws Exception {
		return ICON;
	}

	@Override
	public void execute(NodeBase component, Void instance) throws Exception {
		UIGoto.moveSub(TourPortalPage.class);
	}

}
