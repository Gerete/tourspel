package nl.gerete.tourspel.logic;

import to.etc.util.*;

public interface ISystemTask {
	String getName();
	void execute(Progress p) throws Exception;

}
