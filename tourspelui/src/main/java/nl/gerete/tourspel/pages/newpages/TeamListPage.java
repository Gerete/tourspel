package nl.gerete.tourspel.pages.newpages;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.searchpanel.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.domui.themes.*;
import to.etc.domui.util.*;

import javax.annotation.*;

/**
 * Pagina voor het tonen van alle teams
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

	private void createTeamsTable() throws Exception {

		SearchPanel<Team> lf = new SearchPanel<>(Team.class);
		add(lf);
		DefaultButton b = new DefaultButton(Msgs.BUNDLE.getString("ui.buttonbar.back"), Theme.BTN_CANCEL, new IClicked<DefaultButton>() {
			@Override
			public void clicked(final @Nonnull DefaultButton bxx) throws Exception {
				UIGoto.back();
			}
		});
		lf.addButton(b, 999);
		search(lf.getCriteria());
	}

	@Override
	protected RowRenderer<Team> createRowRenderer() {
		RowRenderer<Team> rr = new RowRenderer<>(Team.class);
		rr.column(Team_.name()).ascending();
		rr.column(Team_.teamCaptainName()).ascending().sortdefault();
		rr.column(Team_.country().name()).ascending();
		rr.column(Team_.country()).renderer(new FlagRenderer<>(Country -> Country));
		rr.setRowClicked(TeamEditPage::open);
		return rr;
	}

	@Override
	protected void onShelve() throws Exception {
		resetAllSharedContexts();
	}
}
