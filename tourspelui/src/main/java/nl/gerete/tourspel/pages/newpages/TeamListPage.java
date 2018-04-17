package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.searchpanel.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.state.*;

/**
 * Page to list all teams
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 1, 2017
 */
public class TeamListPage extends BasicTourPage<Team> {

	public TeamListPage() {
		super(Team.class);
	}

	@Override
	public void createContent() throws Exception {
		addHeader();
		createTeamsTable();
	}

	private void createTeamsTable() {

		SearchPanel<Team> lf = new SearchPanel<>(Team.class);
		add(lf);
		lf.setClicked(a -> search(lf.getCriteria()));
	}

	@Override
	protected RowRenderer<Team> createRowRenderer() {
		RowRenderer<Team> rr = new RowRenderer<>(Team.class);
		rr.column(Team_.name()).ascending();
		rr.column(Team_.teamCaptainName()).ascending().sortdefault();
		rr.column(Team_.country().name()).ascending();
		rr.column(Team_.country()).renderer(new FlagRenderer<>(Country -> Country));
		rr.setRowClicked(row -> UIGoto.moveSub(TeamEditPage.class, new PageParameters("teamID", row.getId())));
		return rr;
	}

}
