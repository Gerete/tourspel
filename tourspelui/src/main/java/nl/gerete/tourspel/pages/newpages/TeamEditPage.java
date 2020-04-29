package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import org.eclipse.jdt.annotation.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.state.*;
import to.etc.webapp.query.*;

/**
 * Edit page for manipulating a team
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
@NonNullByDefault
public class TeamEditPage extends BasicEditPage<Team> {

	public TeamEditPage() {}

	@Override
	public void createContent() throws Exception {
		super.createContent();
		createTeamForm();
		createRidersTable();
	}

	private void createRidersTable() {
		QCriteria<Rider> q = QCriteria.create(Rider.class);
		q.eq(Rider_.team(), getEntity());

		SimpleSearchModel<Rider> sm = new SimpleSearchModel<>(this, q);
		RowRenderer<Rider> rr = new RowRenderer<>(Rider.class);
		rr.column(Rider_.number()).ascending().sortdefault();
		rr.column(Rider_.lastName()).ascending();
		rr.column(Rider_.dateOfBirth()).ascending();
		rr.column(Team_.country()).renderer(new FlagRenderer<>(Country -> Country));

		DataTable<Rider> dt = new DataTable<>(sm, rr);
		add(dt);
		dt.setPageSize(25);
		add(new DataPager(dt));

		rr.setRowClicked(this::clickedOne);
	}

	private void createTeamForm() throws Exception {
		FormBuilder fb = new FormBuilder(this);
		fb.property(getEntity(), Team_.name()).mandatory().control();
		fb.property(getEntity(), Team_.teamCaptainName()).control();
		fb.property(getEntity(), Team_.edition()).control();
		fb.property(getEntity(), Team_.country()).control();
	}

	private void clickedOne(@NonNull final Rider rider) {
		RiderEditPage.open(rider);
	}

	static void open(Team team) {
		UIGoto.moveSub(TeamEditPage.class, ID_PARAM, team.getId());
	}
}
