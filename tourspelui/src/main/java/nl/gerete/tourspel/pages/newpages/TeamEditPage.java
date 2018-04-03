package nl.gerete.tourspel.pages.newpages;

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
public class TeamEditPage extends BasicTourPage {

	private final static String ID_PARAM = "teamID";

	@Nullable
	private Team m_team;

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
		q.eq(Rider.pTEAM, getTeam());

		SimpleSearchModel<Rider> sm = new SimpleSearchModel<>(this, q);
		RowRenderer<Rider> rr = new RowRenderer<>(Rider.class);
		rr.column(Rider.pNUMBER).ascending().sortdefault();
		rr.column(Rider.pLASTNAME).ascending();
		rr.column(Rider.pDATEOFBIRTH).ascending();

		DataTable<Rider> dt = new DataTable<>(sm, rr);
		add(dt);
		dt.setPageSize(25);
		add(new DataPager(dt));

		rr.setRowClicked(this::clickedOne);
	}

	private void createTeamForm() throws Exception {
		FormBuilder fb = new FormBuilder(this);
		fb.property(getTeam(), Team.pNAME).mandatory().control();
		fb.property(getTeam(), Team.pTEAMCAPTAINNAME).control();
		fb.property(getTeam(), Team.pEDITION).control();
		fb.property(getTeam(), Team.pCOUNTRY).control();
	}

	private void clickedOne(@Nonnull final Rider rider) {
		RiderEditPage.open(rider);
	}

	@Override
	protected void onShelve() throws Exception {
		forceReloadData();
	}

	static void open(Team team) {

		/**
		 * See remark at the setter of the team. It took me some time to find out
		 * I declared the setTeam private instead of public.
		 */
		UIGoto.moveSub(TeamEditPage.class, ID_PARAM, team.getId());
	}
}
