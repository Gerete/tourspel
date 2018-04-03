package nl.gerete.tourspel.components;

import nl.gerete.tourspel.logic.*;
import to.etc.domui.component.delayed.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.util.*;

/**
 * This polling div will display progress and data when a system task is running. It will show nothing if
 * no task is active.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on May 22, 2012
 */
public class TaskRunningIndicator extends PollingDiv {
	private PercentageCompleteRuler m_ruler;

	private Span m_text;

	public TaskRunningIndicator() {
		setCssClass("ts-trin");
	}


	@Override
	public void createContent() throws Exception {
		checkForChanges();
	}

	@Override
	public void checkForChanges() throws Exception {
		Progress p = SystemTask.getInstance().getProgress();
		if(null == p) {
			if(m_ruler != null) {
				m_ruler.remove();
				m_ruler = null;
			}
			if(m_text != null) {
				m_text.remove();
				m_text = null;
			}
			return;
		}

		//-- We have something....
		if(m_ruler == null) {
			m_ruler = new PercentageCompleteRuler();
			add(m_ruler);
			m_text = new Span();
			add(m_text);
			m_text.setCssClass("ts-trin-txt");
		}

		m_ruler.setPercentage(p.getPercentage());
		m_text.setText(p.getActionPath(4));
	}

}
