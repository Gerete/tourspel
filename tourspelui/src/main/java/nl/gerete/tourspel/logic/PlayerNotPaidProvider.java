package nl.gerete.tourspel.logic;

import org.eclipse.jdt.annotation.*;

import to.etc.util.*;
import to.etc.webapp.query.*;

/**
 *
 *
 * @author <a href="mailto:ben.schoen@itris.nl">Ben Schoen</a>
 * @since May 18, 2013
 */
public class PlayerNotPaidProvider implements ISystemTaskProvider {

	public static final PlayerNotPaidProvider INSTANCE = new PlayerNotPaidProvider();

	private PlayerNotPaidProvider() {}
	/**
	 * @see nl.tourspel.logic.ISystemTaskProvider#getTask()
	 */
	@Override
	@Nullable
	public ISystemTask getTask() throws Exception {
		QDataContext dc = QContextManager.createUnmanagedContext();

		try {
			return new PlayerNotPaidRun(EditionBP.getCurrentEdition(dc));
		} finally {
			FileTool.closeAll(dc);
		}
	}

}
