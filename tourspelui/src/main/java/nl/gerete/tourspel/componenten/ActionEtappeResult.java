package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.pages.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import javax.annotation.*;

public class ActionEtappeResult implements IUIAction<Void> {

	private static final String MENUNAME = "Etapperesultaten";

	private static final String TITLE = "De resultaten per etappe";

	private static final String ICON = "images/podium123.png";

	@Override
	@Nullable
	public String getDisableReason(Void instance) throws Exception {
		// TODO Auto-generated method stub
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
		UIGoto.moveSub(EtappeListPage.class);
	}

}
