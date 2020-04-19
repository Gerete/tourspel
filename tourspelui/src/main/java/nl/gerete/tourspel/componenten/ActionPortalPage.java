package nl.gerete.tourspel.componenten;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.menu.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import nl.gerete.tourspel.pages.adm.*;

@NonNullByDefault
public class ActionPortalPage implements IUIAction<Void> {

	private static final String MENUNAME = "Deelnemers";

	private static final String TITLE = "Deelnemers";

	private static final String ICON = "images/list_all_participants.png";

	@Override
	@Nullable
	public String getDisableReason(@Nullable Void instance) throws Exception {
		return null;
	}

	@Override
	public String getName(@Nullable Void instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public String getTitle(@Nullable Void instance) throws Exception {
		return TITLE;
	}

	@Override
	public IIconRef getIcon(@Nullable Void instance) throws Exception {
		return Icon.of(ICON);
	}

	@Override
	public void execute(NodeBase component, @Nullable Void instance) throws Exception {
		UIGoto.moveSub(TourPortalPage.class);
	}

}
