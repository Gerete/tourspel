package nl.gerete.tourspel.logic;

import org.eclipse.jdt.annotation.*;

import to.etc.util.*;

@NonNullByDefault
public interface ISystemTask {
	String getName();
	void execute(Progress p) throws Exception;

}
