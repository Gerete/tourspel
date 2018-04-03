package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import javax.annotation.*;

public class ActionEditPerson implements IUIAction<Person> {

	private final static String MENUNAME = "Wijzig gegevens";

	@Override
	public String getDisableReason(Person instance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(Person instance) throws Exception {
		return MENUNAME;
	}

	@Override
	public String getIcon(Person instance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(NodeBase component, Person instance) throws Exception {
		UIGoto.moveSub(PersonEditPage.class, "id", instance.getId());
	}

	@Override
	@Nullable
	public String getTitle(Person instance) throws Exception {
		// TODO Auto-generated method stub
		return MENUNAME;
	}

}
