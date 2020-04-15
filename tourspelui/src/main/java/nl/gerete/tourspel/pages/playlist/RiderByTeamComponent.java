package nl.gerete.tourspel.pages.playlist;

import nl.gerete.tourspel.db.Rider;
import nl.gerete.tourspel.db.Team;
import org.eclipse.jdt.annotation.NonNull;
import to.etc.domui.component.tbl.SimpleListModel;
import to.etc.domui.dom.html.Div;
import to.etc.webapp.query.QCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component shows all teams. By opening a team you see all riders that can then be "selected" to be included in
 * the result of this component.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Feb 3, 2014
 */
public class RiderByTeamComponent extends Div {
	@NonNull
	private Map<Team, TeamListFragment> m_teamListFragmentMap = new HashMap<Team, TeamListFragment>();

	@NonNull
	private SimpleListModel<Rider> m_model;

	public RiderByTeamComponent(@NonNull SimpleListModel<Rider> ridersModel) {
		m_model = ridersModel;
	}

	@Override
	public void createContent() throws Exception {
		QCriteria<Team> q = QCriteria.create(Team.class);
		List<Team> teams = getSharedContext().query(q);
		createTeamListFragments(teams);
	}

	private void createTeamListFragments(@NonNull List<Team> teams) {
		for(Team team : teams) {
			TeamListFragment teamListFragment = new TeamListFragment(team, m_model);
			m_teamListFragmentMap.put(team, teamListFragment);
			add(teamListFragment);
		}
	}
}
