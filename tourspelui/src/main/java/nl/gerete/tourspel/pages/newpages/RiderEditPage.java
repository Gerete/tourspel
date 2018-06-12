package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.state.*;

/**
 * Edit page for manipulating a rider
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
public class RiderEditPage extends BasicEditPage<Rider> {

	public RiderEditPage() {}

	@Override
	public void createContent() throws Exception {
		super.createContent();
		createRiderForm();
	}

	private void createRiderForm() throws Exception {
		FormBuilder fb = new FormBuilder(this);
		fb.property(getEntity(), Rider_.number()).control();
		fb.property(getEntity(), Rider_.firstName()).control();
		fb.property(getEntity(), Rider_.middleName()).control();
		fb.property(getEntity(), Rider_.lastName()).control();
		fb.property(getEntity(), Rider_.dateOfBirth()).control();
		fb.property(getEntity(), Rider_.country()).control();
	}

	static void open(Rider rider) {
		UIGoto.moveSub(RiderEditPage.class, ID_PARAM, rider.getId());
	}
}
