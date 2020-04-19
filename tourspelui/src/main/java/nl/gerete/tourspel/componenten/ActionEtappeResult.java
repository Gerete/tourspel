package nl.gerete.tourspel.componenten;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.menu.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import nl.gerete.tourspel.pages.*;

@NonNullByDefault
public class ActionEtappeResult implements IUIAction<Void> {

	private static final String MENUNAME = "Etapperesultaten";

	private static final String TITLE = "De resultaten per etappe";

	private static final String ICON = "images/podium123.png";

	@Override
	@Nullable
	public String getDisableReason(@Nullable Void instance) throws Exception {
		// TODO Auto-generated method stub
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
		UIGoto.moveSub(EtappeListPage.class);
	}

}
