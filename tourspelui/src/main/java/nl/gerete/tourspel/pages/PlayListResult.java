package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.component.layout.*;

public class PlayListResult extends Window {

	private PlayListType m_playListType;

	public PlayListResult(PlayListType playListType) {
		super("Totaal overzicht");
		m_playListType = playListType;
	}

	@Override
	public void createContent() throws Exception {
		add(new PlayListResultFragment(m_playListType, Integer.MAX_VALUE));
	}
}
