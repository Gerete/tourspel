package nl.gerete.tourspel.pages.admin;

import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.state.*;

import javax.annotation.*;

/**
 * Edit page for manipulating a rider
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
public class RiderEditPage extends BasicTourPage {

	private final static String ID_PARAM = "riderID";

	@Nullable
	private Rider m_rider;

	@UIUrlParameter(name = ID_PARAM)
	public Rider getRider() {
		Rider rider = m_rider;
		if(null == rider)
			throw new IllegalStateException("Missing rider");
		return rider;
	}

	public void setRider(Rider rider) {
		m_rider = rider;
	}

	@Override
	public void createContent() throws Exception {
		addHeader();
		ButtonBar bb = new ButtonBar();
		DefaultButton saveButton = new DefaultButton("Save", clickednode -> save());
		bb.addButton(saveButton);
		add(bb);
		bb.addBackButton();  // A backbutton can only be added after you added the ButtonBar to the page.
		createRiderForm();
	}

	private void save() throws Exception {

		if(bindErrors()) {
			return;
		}
		getSharedContext().commit();

		UIGoto.back();
	}

	private void createRiderForm() throws Exception {
		FormBuilder fb = new FormBuilder(this);
		fb.property(getRider(), Rider.pNUMBER).control();
		fb.property(getRider(), Rider.pFIRSTNAME).control();
		fb.property(getRider(), Rider.pMIDDLENAME).control();
		fb.property(getRider(), Rider.pLASTNAME).control();
		fb.property(getRider(), Rider.pDATEOFBIRTH).control();
		fb.property(getRider(), Rider.pCOUNTRY).control();
	}

	static void open(Rider rider) {
		UIGoto.moveSub(RiderEditPage.class, ID_PARAM, rider.getId());
	}
}
