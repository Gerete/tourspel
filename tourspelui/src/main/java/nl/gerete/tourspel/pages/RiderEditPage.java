package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;

@UIRights(value = "ADMIN")
public class RiderEditPage extends BasicEditPage<Rider> {

	public RiderEditPage() {
		super(Rider.class, "lastName", "firstName", "middleName", "team", "number", "country");
	}

}
