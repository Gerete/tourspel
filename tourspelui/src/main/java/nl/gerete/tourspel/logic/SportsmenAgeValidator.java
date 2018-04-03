package nl.gerete.tourspel.logic;

import to.etc.domui.converter.*;
import to.etc.util.*;

import java.util.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 19-9-17.
 */
public class SportsmenAgeValidator implements IValueValidator<Date>{

	private final Date toOld = DateUtil.incrementDate(new Date(), Calendar.YEAR, -50);

	private final Date toYoung = DateUtil.incrementDate(new Date(), Calendar.YEAR, -10);

	@Override public void validate(Date input) throws Exception {
		if(input.before(toOld)) {
			throw new Exception("Renner is te oud (50)");
		}
		if(input.after(toYoung)) {
			throw new Exception("Renner is te jong (10)");
		}
	}
}
