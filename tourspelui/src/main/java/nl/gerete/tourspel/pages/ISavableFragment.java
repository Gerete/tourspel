package nl.gerete.tourspel.pages;

import to.etc.webapp.query.*;

public interface ISavableFragment {
	void save(QDataContext dc) throws Exception;
}
