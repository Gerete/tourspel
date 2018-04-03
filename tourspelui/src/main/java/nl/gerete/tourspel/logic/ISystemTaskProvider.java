package nl.gerete.tourspel.logic;

import javax.annotation.*;

/**
 * Provides tasks to execute.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on May 22, 2012
 */
public interface ISystemTaskProvider {
	@Nullable
	ISystemTask getTask() throws Exception;
}
