package nl.gerete.tourspel;

import nl.gerete.tourspel.db.*;

import javax.annotation.*;

final public class TourUtil {
	private TourUtil() {}

	@Nonnull
	static public String getFlag(@Nonnull Country c) {
		return "images/flags/" + c.getShortName() + ".png";
	}

}
