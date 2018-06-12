package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.tbl.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
public class CountryListPage extends BasicListPage<Country> {

	public CountryListPage() {
		super(Country.class);
	}

	@Override
	protected RowRenderer<Country> createRowRenderer() {
		RowRenderer<Country> rr = new RowRenderer<>(Country.class);
		rr.column().renderer(new FlagRenderer<>(Country -> Country));
		rr.column(Country_.shortName()).ascending().sortdefault().nowrap();
		rr.column(Country_.name()).ascending();
		rr.setRowClicked(CountryEditPage::open);
		return rr;
	}

	@Override
	protected void onShelve() {
		resetAllSharedContexts();
	}
}
