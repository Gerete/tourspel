package nl.gerete.tourspel.components;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component.input.*;
import to.etc.domui.dom.html.*;

public class RiderEditRow extends TR {
	private Rider m_rider;

	private Button m_rowButton;

	RiderEditRow(Rider rider) {
		m_rider = rider;
	}

	@Override
	public void createContent() throws Exception {
		TD td = addCell();
		TextStr lastName = new TextStr();
		lastName.bind().to(m_rider, Rider.pLASTNAME);
		td.add(lastName);
		td = addCell();
		TextStr firstName = new TextStr();
		firstName.bind().to(m_rider, Rider.pFIRSTNAME);
		td.add(firstName);
		td = addCell();
		TextStr middleName = new TextStr();
		middleName.bind().to(m_rider, Rider.pMIDDLENAME);
		td.add(middleName);
		td = addCell();
		DateInput dob = new DateInput();
		dob.bind().to(m_rider, Rider.pDATEOFBIRTH);
		td.add(dob);
		td = addCell();
		Text<Integer> number = new Text<Integer>(Integer.class);
		number.bind().to(m_rider, Rider.pNUMBER);
		td.add(number);
		td = addCell();
		LookupInput<Country> country = new LookupInput<Country>(Country.class);
		country.bind().to(m_rider, Rider.pCOUNTRY);
		td.add(country);
		if(m_rowButton != null) {
			td = addCell();
			td.add(m_rowButton);
		}
	}

	public void setRowButton(Button deleteBtn) {
		m_rowButton = deleteBtn;

	}

}
