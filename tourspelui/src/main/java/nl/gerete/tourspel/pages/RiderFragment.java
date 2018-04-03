package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component.form.*;
import to.etc.domui.component.ntbl.*;
import to.etc.domui.dom.html.*;

public class RiderFragment extends Div implements IEditor {
	private Rider m_rider;

	public RiderFragment(Rider rider) {
		m_rider = rider;
	}

	@Override
	public boolean validate(boolean isnew) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void createContent() throws Exception {
		HorizontalFormBuilder hfb = new HorizontalFormBuilder(m_rider);
		hfb.addProp("lastName", true, true);
		hfb.addProp("firstName", true, true);
		hfb.addProp("middleName", true, false);
		hfb.addProp("dateOfBirth", true, false);
		hfb.addProp("number", true, true);
		hfb.addProp("country", true, false);
		add(hfb.finish());
	}

}
