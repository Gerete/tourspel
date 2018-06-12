package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.newpages.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import javax.annotation.*;

@DefaultNonNull
public class ActionAdminCountries implements IUIAction< Void > {

	private final static String MENUNAME = "Landen";

	private final static String ICON = "images/fietsbel.png";

	@Override
	@Nullable
	public String getDisableReason(@Nullable Void instance) throws Exception {
		if(!TourUser.getCurrent().hasRight(ApplicationRight.ADMIN.toString()))
			return "You do not have rights";

		return null;
	}

	@Override
	public String getName(@Nullable Void instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public String getIcon(@Nullable Void instance) throws Exception {
		return ICON;
	}

	@Override
	public void execute(NodeBase component, @Nullable Void instance) throws Exception {
		UIGoto.moveSub(CountryListPage.class);
	}

	@Override
	public String getTitle(@Nullable Void instance) throws Exception {
		return MENUNAME; // Dit is geen officiele retour waarde die eigenlijk in de string had moeten staan. Jo bedoelt eigenlijk......
	}

}
