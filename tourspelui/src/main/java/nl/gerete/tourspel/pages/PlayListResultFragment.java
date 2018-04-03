package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class PlayListResultFragment extends Div {

	private int m_nrOfListToShow;

	private PlayListType m_playListType;

	public PlayListType getPlayListType() {
		return m_playListType;
	}

	public PlayListResultFragment(@Nonnull PlayListType playListType, int nrOfListToShow) {
		m_playListType = playListType;
		m_nrOfListToShow = nrOfListToShow;
		// css hier op geven.
		setCssClass("portalFragment");
	}

	@Override
	public void createContent() throws Exception {

		if(m_playListType.equals(PlayListType.KLASSEMENT)) {
			add(new Label($("klassement")));
		} else {
			add(new Label($("poedel")));
		}

		QCriteria<PlayList> qpl = QCriteria.create(PlayList.class);
		qpl.add(new QOrder(QSortOrderDirection.DESC, PlayList.pCURRENTPOINTS));
		List<PlayList> playLists = getSharedContext().query(qpl);

		TBody tBody = addTable($("plaats"), $("lijst"), $("punten"));

		// Calculate the position to show from.
		PlayList last = null;
		int teTonenAantal = playLists.size() < m_nrOfListToShow ? playLists.size() : m_nrOfListToShow;
		if(m_playListType.equals(PlayListType.POEDEL)) {
			getPoedelLijst(playLists, tBody, teTonenAantal, last);
		} else {
			getKlassementLijst(playLists, tBody, teTonenAantal, last);
		}
	}

	private void getPoedelLijst(List<PlayList> playLists, TBody tBody, int teTonenAantal, PlayList last) {
		int i = 1;
		for(int j = playLists.size(); j > playLists.size() - teTonenAantal; j--) {
			PlayList pl = playLists.get(j - 1);
			displayPlayList(tBody, pl, last, i++);
			last = pl;
		}
	}

	private void getKlassementLijst(List<PlayList> playLists, TBody tBody, int teTonenAantal, PlayList last) {
		for(int j = 0; j < teTonenAantal; j++) {
			PlayList pl = playLists.get(j);
			displayPlayList(tBody, pl, last, j + 1);
			last = pl;
		}
	}

	/**
	 * Display one list. If the position is the same, a dash will be printed instead of the number.
	 *
	 * @param tBody
	 * @param current
	 * @param last
	 * @param position
	 */
	private void displayPlayList(@Nonnull TBody tBody, @Nonnull PlayList current, @Nullable PlayList last, int position) {

		TR tr = tBody.addRow();
		TD td = tr.addCell();
		if(last == null || current.getCurrentPoints() != last.getCurrentPoints())
			td.add(Integer.toString(position));
		else
			td.add("-");
		td = tr.addCell();
		td.add(current.getListName());
		td = tr.addCell();
		td.setAlign(TDAlignType.CENTER);
		td.add("" + current.getCurrentPoints());
	}
}
