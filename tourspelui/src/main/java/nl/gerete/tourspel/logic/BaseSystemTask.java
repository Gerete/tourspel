package nl.gerete.tourspel.logic;

import to.etc.webapp.query.*;

import javax.annotation.*;

public abstract class BaseSystemTask implements ISystemTask {
	@Nullable
	private QDataContext m_dc;

	@Nonnull
	protected QDataContext dc() throws Exception {
		if(m_dc == null) {
			m_dc = QContextManager.createUnmanagedContext();
			m_dc.startTransaction();
		}

		return m_dc;
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
