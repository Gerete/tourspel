package nl.gerete.tourspel.logic;

import to.etc.domui.converter.*;
import to.etc.util.*;

import java.util.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 19-9-17.
 */
public class SportmenAgeValidatorParams implements IParameterizedValidator<Date> {

	private Date toOld;

	private Date toYoung;

	@Override public void setParameters(String[] parameters) {
		toOld = DateUtil.incrementDate(new Date(), Calendar.YEAR, Integer.valueOf(parameters[0]));
		toYoung = DateUtil.incrementDate(new Date(), Calendar.YEAR, Integer.valueOf(parameters[1]));
	}

	@Override public void validate(Date input) throws Exception {
		if(input.before(toOld)) {
			throw new Exception("Renner is te oud (parameter)");
		}
		if(input.after(toYoung)) {
			throw new Exception("Renner is te jong (parameter)");
		}
	}
}
