package nl.gerete.tourspel.pages.playlist;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

/**
 * This component shows all teams. By opening a team you see all riders that can then be "selected" to be included in
 * the result of this component.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Feb 3, 2014
 */
public class RiderByTeamComponent extends Div {
	@Nonnull
	private Map<Team, TeamListFragment> m_teamListFragmentMap = new HashMap<Team, TeamListFragment>();

	@Nonnull
	private SimpleListModel<Rider> m_model;

	public RiderByTeamComponent(@Nonnull SimpleListModel<Rider> ridersModel) {
		m_model = ridersModel;
	}

	@Override
	public void createContent() throws Exception {
		QCriteria<Team> q = QCriteria.create(Team.class);
		List<Team> teams = getSharedContext().query(q);
		createTeamListFragments(teams);
	}

	private void createTeamListFragments(@Nonnull List<Team> teams) {
		for(Team team : teams) {
			TeamListFragment teamListFragment = new TeamListFragment(team, m_model);
			m_teamListFragmentMap.put(team, teamListFragment);
			add(teamListFragment);
		}
	}
}
