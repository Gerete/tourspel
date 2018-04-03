package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.meta.*;

@UIRights(value = "ADMIN")
public class TeamListPage extends BasicListPage<Team> {

	public TeamListPage() throws Exception {
		super(Team.class, TeamEditPage.class);
		setSearchFields(Team.pNAME, Team.pTEAMCAPTAINNAME, Team.pCOUNTRY + "." + Country.pNAME);
		setDisplayFields(Team.pNAME, Team.pTEAMCAPTAINNAME, Team.pCOUNTRY + "." + Country.pNAME, Team.pCOUNTRY + "." + Country.pSHORTNAME, "%2", SortableType.SORTABLE_ASC, "", new FlagRenderer());
	}

	@Override
	protected Edition restrictEdition() throws Exception {
		return EditionBP.getCurrentEdition(getSharedContext());
	}
}
