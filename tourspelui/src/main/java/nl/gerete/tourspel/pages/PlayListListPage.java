package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.playlist.*;
import to.etc.webapp.query.*;

public class PlayListListPage extends BasicListPage<PlayList> {

	public PlayListListPage() {
		super(PlayList.class, PlayListEditPage.class);

		setSearchFields(PlayList_.listName());

	}

	@Override
	public void adjustCriteria(QCriteria<PlayList> q) {
		q.eq(PlayList.pPERSON, TourUser.getCurrent().getPerson());
	}


}
