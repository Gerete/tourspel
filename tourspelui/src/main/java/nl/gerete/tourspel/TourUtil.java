package nl.gerete.tourspel;

import nl.gerete.tourspel.db.Country;
import org.eclipse.jdt.annotation.NonNull;

final public class TourUtil {
	private TourUtil() {}

	@NonNull
	static public String getFlag(@NonNull Country c) {
		return "images/flags/" + c.getShortName() + ".png";
	}

}
