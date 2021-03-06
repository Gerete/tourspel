package nl.gerete.tourspel.componenten;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.menu.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.*;

@NonNullByDefault
public class ActionEditPassword implements IUIAction<Person> {

	private final static String MENUNAME = "Wijzig wachtwoord";

	private final static String ICON = "images/fietsbel.png";

	@Override
	@Nullable
	public String getDisableReason(@Nullable Person instance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(@Nullable Person instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public IIconRef getIcon(@Nullable Person instance) throws Exception {
	return Icon.of(ICON);
	}

	@Override
	public void execute(NodeBase component, @Nullable Person instance) throws Exception {
		if(null == instance) {
			return;
		}
		UIGoto.moveSub(PersonPasswordEditPage.class, "id", instance.getId());
	}

	@Override
	public String getTitle(@Nullable Person instance) throws Exception {
		return MENUNAME;
	}

}
