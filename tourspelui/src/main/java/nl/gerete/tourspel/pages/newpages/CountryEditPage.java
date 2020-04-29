package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.db.Country;
import nl.gerete.tourspel.db.Country_;
import org.eclipse.jdt.annotation.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component2.form4.FormBuilder;
import to.etc.domui.state.UIGoto;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
@NonNullByDefault
public class CountryEditPage extends BasicEditPage<Country> {

	public CountryEditPage() {
	}

	@Override
	public void createContent() throws Exception {
		super.createContent();
		createCountryForm();
	}

	private void createCountryForm() throws Exception {
		FormBuilder fb = new FormBuilder(this);
		fb.property(getEntity(), Country_.name()).mandatory().control();
		fb.property(getEntity(), Country_.shortName()).control();
	}

	static void open(Country country) {
		UIGoto.moveSub(CountryEditPage.class, ID_PARAM, country.getId());
	}
}
