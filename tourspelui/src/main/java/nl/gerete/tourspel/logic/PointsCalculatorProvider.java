package nl.gerete.tourspel.logic;

import nl.gerete.tourspel.db.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

/**
 * Called every 5 minutes, this checks to see if an etappe is "CALCULATING"; if so it will return
 * the {@link PointsCalculatorRun} to calculate that etappe.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on May 22, 2012
 */
final public class PointsCalculatorProvider implements ISystemTaskProvider {
	@NonNull
	static public final PointsCalculatorProvider INSTANCE = new PointsCalculatorProvider();

	private PointsCalculatorProvider() {}

	@Override
	public ISystemTask getTask() throws Exception {
		QDataContext dc = QContextManager.createUnmanagedContext();
		try {
			Edition current = EditionBP.getCurrentEdition(dc);
			List<Etappe> etappes = EditionBP.getEtappeList(current);		// Get all etappes for this edition.
			for(Etappe etappe : etappes) {
				if(etappe.getPhase() == EtappePhase.CALCULATING) {			// Set to calculating -> then calculate this one!
					return new PointsCalculatorRun(etappe);					// Run for that etappe.
				}
			}
			return null;
		} finally {
			try {
				dc.close();
			} catch(Exception x) {}
		}
	}

	/**
	 * Try to force the task executor to execute this task immediately.
	 */
	public void punch() {
		SystemTask.getInstance().punch(this);
	}
}
