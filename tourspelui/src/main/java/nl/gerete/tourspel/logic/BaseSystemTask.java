package nl.gerete.tourspel.logic;

import org.eclipse.jdt.annotation.*;

import to.etc.webapp.query.*;

public abstract class BaseSystemTask implements ISystemTask {
	@Nullable
	private QDataContext m_dc;

	@NonNull
	protected QDataContext dc() throws Exception {
		QDataContext dc = m_dc;
		if(dc == null) {
			dc = QContextManager.createUnmanagedContext();
			dc.startTransaction();
			m_dc = dc;
		}
		return dc;
	}

	protected void silentCloseDc() {
		if(m_dc != null) {
			try {
				m_dc.close();
				m_dc = null;
			} catch(Exception e) {}
		}
	}
}
