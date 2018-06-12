package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.state.*;

import javax.annotation.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
@DefaultNonNull
public class CountryEditPage extends BasicEditPage<Country>{

	public CountryEditPage() {}

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
