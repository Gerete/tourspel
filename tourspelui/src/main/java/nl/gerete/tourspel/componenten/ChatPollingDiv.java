package nl.gerete.tourspel.componenten;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.delayed.*;
import to.etc.domui.component.htmleditor.*;

@NonNullByDefault
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
