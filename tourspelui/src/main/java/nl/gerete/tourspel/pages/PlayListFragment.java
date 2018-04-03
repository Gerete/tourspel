package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.dom.css.*;
import to.etc.domui.dom.html.*;

import javax.annotation.*;
import java.util.*;

public class PlayListFragment extends Div {

	@Nonnull
	private PlayList m_playList;

	private Table m_playListTable = new Table();
	private List<PlayListFragment> m_playListGroup;

	public PlayListFragment(@Nonnull PlayList pl, @Nonnull List<PlayListFragment> playListGroup) {
		setCssClass("portalFragment");
		m_playList = pl;
		m_playListGroup = playListGroup;
		m_playListTable.setTableHead("Nr", "Renner", "Bonus punten", "Totaal punten");
	}

	@Override
	public void createContent() throws Exception {
		Div name = new Div();
		add(name);

		name.setText(m_playList.getListName() + " (" + m_playList.getPlayListType().getValue() + ")");
		name.setCssClass("portal-left");

		Div punten = new Div();
		add(punten);
		punten.setText("Totaal punten:" + m_playList.getCurrentPoints());
		punten.setCssClass("portal-right");

		add(m_playListTable);
		TBody tb = m_playListTable.addBody();
		setClicked(new IClicked<Div>() {
			@Override
			public void clicked(Div clickednode) throws Exception {
				togglePlayLists();
			}
		});
		getPlayListTable().setDisplay(DisplayType.NONE);

		List<PlayListEntry> plel = m_playList.getPlayListEntries();
		for(PlayListEntry ple : plel) {
			Rider rider = ple.getRider();
			if(rider == null)
				continue;
			System.out.println(ple.getPlace() + " " + rider.getDisplayName());
		}
		Collections.sort(plel, new Comparator<PlayListEntry>() {
			@Override
			public int compare(PlayListEntry o1, PlayListEntry o2) {
				int a = o1.getPlace();
				int b = o2.getPlace();
				return (a < b ? 0 : 1);
			}
		});

		for(PlayListEntry ple : plel) {
			Rider rider = ple.getRider();
			if(rider == null)
				continue;
			System.out.println(ple.getPlace() + " " + rider.getDisplayName());
		}
		PlayerPointsCalculator playerPointsCalc = new PlayerPointsCalculator(getSharedContext(), m_playList);
		playerPointsCalc.initialize();

		int i =0;
		for(PlayListEntry ple : plel) {
			TD td = tb.addRowAndCell();
			td.setText(ple.getPlace() + "");
			td = tb.addCell();
			Rider rider = ple.getRider();
			if(rider == null)
				throw new IllegalStateException("The Rider cannot be null here!");
			else
				td.setText(rider.getDisplayName());
			addScoreListPoints(tb, playerPointsCalc.getTotalBonusScorePoints(rider));
			addScoreListPoints(tb, playerPointsCalc.getTotalScorePoints(rider));
			i++;
		}
		m_playListGroup.add(this);
	}

	private void togglePlayLists() {
		for(PlayListFragment playList : m_playListGroup) {
			if(playList == this) {
				getPlayListTable().setDisplay(DisplayType.BLOCK);
			} else {
				playList.getPlayListTable().setDisplay(DisplayType.NONE);
			}
		}
	}

	@Nonnull
	protected Table getPlayListTable() {
		return m_playListTable;
	}

	private void addScoreListPoints(@Nonnull TBody tb, int scorePoints) {
		TD td;
		td = tb.addCell();
		Label label = new Label(scorePoints + "");
		label.setCssClass("portal-right");
		label.setFontSize("100%");
		td.add(label);
	}
}
