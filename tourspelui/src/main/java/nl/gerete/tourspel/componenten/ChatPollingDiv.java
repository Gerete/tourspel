package nl.gerete.tourspel.componenten;

import to.etc.domui.component.delayed.*;
import to.etc.domui.component.htmleditor.*;

public class ChatPollingDiv extends PollingDiv {

	private DisplayHtml m_dh;

	@Override
	public void createContent() {
		m_dh = new DisplayHtml();
		add(m_dh);
	}

	@Override
	public void checkForChanges() throws Exception {
		String message = ChatManager.getMessage();
		if(message == null)
			return;
		m_dh.add(message);
	}


}
