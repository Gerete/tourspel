package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.tbl.*;
import to.etc.webapp.query.*;

/**
 * Page to list all teams
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 1, 2017
 */
public class TeamListPage extends BasicTourPage {

	@Override
	public void createContent() throws Exception {
		addHeader();
		createTeamsTable();
	}

	private void createTeamsTable() {
		QCriteria<Team> q = QCriteria.create(Team.class);

		SimpleSearchModel<Team> sm = new SimpleSearchModel<>(this, q);
		RowRenderer<Team> rr = new RowRenderer<>(Team.class);
		rr.column(Team.pNAME).ascending();
		rr.column(Team.pTEAMCAPTAINNAME).ascending().sortdefault();
		rr.column(Team.pCOUNTRY + "." + Country.pNAME).ascending();
		rr.column(Team.pCOUNTRY);
		rr.setNodeRenderer(3, new FlagRenderer());

		DataTable<Team> dt = new DataTable<>(sm, rr);
		add(dt);
		dt.setPageSize(25);
		add(new DataPager(dt));

		rr.setRowClicked(TeamEditPage::open);
	}

	@Override
	protected void onShelve() throws Exception {
		forceReloadData();
	}
}
