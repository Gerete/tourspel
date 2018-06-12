package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.tbl.*;

/**
 * Pagina voor het tonen van alle teams
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 1, 2017
 */
public class TeamListPage extends BasicListPage<Team> {

	public TeamListPage() {
		super(Team.class);
	}

	@Override
	protected RowRenderer<Team> createRowRenderer() {
		RowRenderer<Team> rr = new RowRenderer<>(Team.class);
		rr.column(Team_.country()).renderer(new FlagRenderer<>(Country -> Country));
		rr.column(Team_.name()).ascending();
		rr.column(Team_.teamCaptainName()).ascending().sortdefault();
		rr.column(Team_.country().name()).ascending();
		rr.setRowClicked(TeamEditPage::open);
		return rr;
	}

	@Override
	protected void onShelve() throws Exception {
		resetAllSharedContexts();
	}
}
