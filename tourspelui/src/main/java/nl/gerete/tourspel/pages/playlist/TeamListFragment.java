package nl.gerete.tourspel.pages.playlist;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.css.*;
import to.etc.domui.dom.html.*;

import javax.annotation.*;
import java.util.*;

public class TeamListFragment extends Div {
	@Nonnull
	final private Team m_team;

	@Nonnull
	private Div m_riders = new Div();

	@Nonnull
	private Map<Rider, Div> m_riderDivMap = new HashMap<Rider, Div>();

	@Nonnull
	final private SimpleListModel<Rider> m_model;

	private Div m_teamDiv;

	private Img m_expandImg = new Img("images/arrow-right.png");

	private Img m_collapseImg = new Img("images/arrow-down.png");

	public TeamListFragment(@Nonnull Team team, @Nonnull SimpleListModel<Rider> selectedRiders) {
		m_team = team;
		m_model = selectedRiders;
		m_model.addChangeListener(new ITableModelListener<Rider>() {
			@Override
			public void rowModified(@Nonnull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
			}

			@Override
			public void rowDeleted(@Nonnull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
				Div rider = m_riderDivMap.get(value);
				if(rider != null) {
					rider.removeCssClass("selected");
				}
			}

			@Override
			public void rowAdded(@Nonnull ITableModel<Rider> model, int index, @Nullable Rider value) throws Exception {
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

	private void toggleSelection(@Nonnull Rider rider) throws Exception {
		boolean wasthere = isInModel(rider);
		if(wasthere)
			m_model.delete(rider);
		else
			m_model.add(rider);
	}

	private boolean isInModel(@Nonnull Rider r) throws Exception {
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
