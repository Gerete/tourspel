package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.meta.*;

@UIRights(value = "ADMIN")
public class CountryListPage extends BasicListPage<Country> {

	public CountryListPage() {
		super(Country.class, CountryEditPage.class);
		setDisplayFields(Country.pSHORTNAME, "%2", SortableType.SORTABLE_ASC, "", new FlagRenderer<Country>(country -> country), "%1", Country.pNAME, "%30", SortableType.SORTABLE_ASC);
	}

}
