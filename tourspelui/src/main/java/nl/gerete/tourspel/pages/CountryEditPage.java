package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;

import java.lang.reflect.Method;

@UIRights(value = "ADMIN")
public class CountryEditPage extends BasicEditPage<Country> {

	public CountryEditPage() {
		super(Country.class, Country.pNAME, Country.pSHORTNAME);
	}
}
