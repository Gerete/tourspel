package nl.gerete.tourspel.componenten;

import to.etc.domui.component.delayed.*;
import to.etc.domui.component.htmleditor.*;

import javax.annotation.*;
import java.util.*;

@DefaultNonNull
public class ChatPollingDiv extends PollingDiv {

	@Nullable
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
		Objects.requireNonNull(m_dh).add(message);
	}


}
