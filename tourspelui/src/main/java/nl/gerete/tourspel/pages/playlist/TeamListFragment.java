package nl.gerete.tourspel.pages.playlist;

import nl.gerete.tourspel.db.Rider;
import nl.gerete.tourspel.db.Team;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.component.tbl.ITableModel;
import to.etc.domui.component.tbl.ITableModelListener;
import to.etc.domui.component.tbl.SimpleListModel;
import to.etc.domui.dom.css.DisplayType;
import to.etc.domui.dom.html.Div;
import to.etc.domui.dom.html.IClicked;
import to.etc.domui.dom.html.Img;

import java.util.HashMap;
import java.util.Map;

public class TeamListFragment extends Div {
	@NonNull
	final private Team m_team;

	@NonNull
	private Div m_riders = new Div();

	@NonNull
	private Map<Rider, Div> m_riderDivMap = new HashMap<Rider, Div>();

	@NonNull
	final private SimpleListModel<Rider> m_model;

	private Div m_teamDiv;

	private Img m_expandImg = new Img("images/arrow-right.png");

	private Img m_collapseImg = new Img("images/arrow-down.png");

	public TeamListFragment(@NonNull Team team, @NonNull SimpleListModel<Rider> selectedRiders) {
		m_team = team;
		m_model = selectedRiders;
		m_model.addChangeListener(new ITableModelListener<Rider>() {
			@Override
			public void rowModified(@NonNull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
			}

			@Override
			public void rowDeleted(@NonNull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
				Div rider = m_riderDivMap.get(value);
				if(rider != null) {
					rider.removeCssClass("selected");
				}
			}

			@Override
			public void rowAdded(@NonNull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
				Div rider = m_riderDivMap.get(value);
				if(rider != null) {
					rider.addCssClass("selected");
				}
			}

			@Override
			public void modelChanged(@Nullable ITableModel<Rider> model) {
				forceRebuild();
			}
		});
	}

	@Override
	public void createContent() throws Exception {
		m_teamDiv = new Div(m_expandImg);
		m_teamDiv.add(m_team.getName());
		m_teamDiv.setCssClass("headerdiv");
		add(m_teamDiv);
		m_teamDiv.setClicked(new IClicked<Div>() {
			@Override
			public void clicked(Div clickednode) throws Exception {
				toggleTeamVisibility();
			}
		});

		//-- Create riders list.
		m_riders.setDisplay(DisplayType.NONE);
		add(m_riders);
		for(final Rider rider : m_team.getRiders()) {
			final Div riderDiv = new Div();
			m_riderDivMap.put(rider, riderDiv);

			if(isInModel(rider)) {
				riderDiv.addCssClass("selected");
			}

			riderDiv.setClicked(new IClicked<Div>() {
				@Override
				public void clicked(Div clickednode) throws Exception {
					toggleSelection(rider);
				}
			});
			riderDiv.setText((rider.getDisplayName()));
			riderDiv.addCssClass("rider");
			m_riders.add(riderDiv);
		}
	}

	private void toggleSelection(@NonNull Rider rider) throws Exception {
		boolean wasthere = isInModel(rider);
		if(wasthere)
			m_model.delete(rider);
		else
			m_model.add(rider);
	}

	private boolean isInModel(@NonNull Rider r) throws Exception {
		for(int i = m_model.getRows(); --i >= 0;) {
			if(r.equals(m_model.getItem(i)))
				return true;
		}
		return false;
	}

	protected void toggleTeamVisibility() {
		if(DisplayType.NONE.equals(m_riders.getDisplay())) {
			m_riders.setDisplay(DisplayType.BLOCK);
			m_teamDiv.replaceChild(m_expandImg, m_collapseImg);
		} else {
			m_riders.setDisplay(DisplayType.NONE);
			m_teamDiv.replaceChild(m_collapseImg, m_expandImg);
		}
	}

}
