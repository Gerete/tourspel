package nl.gerete.tourspel.logic;

import to.etc.util.*;

import javax.annotation.*;

@DefaultNonNull
public interface ISystemTask {
	String getName();
	void execute(Progress p) throws Exception;

}
