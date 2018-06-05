package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.state.*;
import to.etc.webapp.query.*;

import javax.annotation.*;

/**
 * Edit page for manipulating a team
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
@DefaultNonNull
public class TeamEditPage extends BasicTourPage<Team> {

	private final static String ID_PARAM = "teamID";

	@Nullable
	private Team m_team;

	public TeamEditPage() {
		super(Team.class);
	}

	@UIUrlParameter(name = ID_PARAM)
	public Team getTeam() {
		Team team = m_team;
		if(null == team)
			throw new IllegalStateException("Missing team");
		return team;
	}

	/**
	 * If this method isn't declared public, you don't get to see the detail page. It's empty.
	 * It would be nice if some kind of error is shown
	 */
	public void setTeam(Team team) {
		m_team = team;
	}

	@Override
	public void createContent() throws Exception {
		addHeader();
		addButtons();
		createTeamForm();
		createRidersTable();
	}

	private void addButtons() {
		ButtonBar bb = new ButtonBar();
		DefaultButton saveButton = new DefaultButton("Save");
		bb.addButton(saveButton);
		bb.setClicked(clickednode -> save());
		add(bb);
		bb.addBackButton();  // A backbutton can only be added after you added the ButtonBar to the page.
	}

	private void save() throws Exception {

		if (bindErrors()) {
			return;
		}

		getSharedContext().commit();
		UIGoto.back();
	}

	private void createRidersTable() {
		QCriteria<Rider> q = QCriteria.create(Rider.class);
		q.eq(Rider_.team(), getTeam());

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
		fb.property(getTeam(), Team_.name()).mandatory().control();
		fb.property(getTeam(), Team_.teamCaptainName()).control();
		fb.property(getTeam(), Team_.edition()).control();
		fb.property(getTeam(), Team_.country()).control();
	}

	private void clickedOne(@Nonnull final Rider rider) {
		RiderEditPage.open(rider);
	}

	@Override
	protected void onShelve() throws Exception {
		resetAllSharedContexts();
	}

	static void open(Team team) {
		UIGoto.moveSub(TeamEditPage.class, ID_PARAM, team.getId());
	}
}
