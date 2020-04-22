package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.db.*;
import org.eclipse.jdt.annotation.*;
import to.etc.domui.annotations.*;
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

	@UIUrlParameter(name = ID_PARAM)
	public Rider getEntity() {
		Rider entity = m_entity;
		if(null == entity)
			throw new IllegalStateException("Missing entity");
		return entity;
	}

	public void setEntity(@Nullable Rider entity) {
		m_entity = entity;
	}

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
