package nl.gerete.tourspel.pages.newpages;

import to.etc.domui.component.searchpanel.*;
import to.etc.domui.component.tbl.*;
import to.etc.webapp.query.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
public class BasicListPage<T> extends BasicTourPage {

	final private Class<T> m_clazz;

	private DataTable<T> m_table;

	public BasicListPage(Class<T> clazz) {
		m_clazz = clazz;
	}

	@Override
	public void createContent() throws Exception {

		super.createContent();
		SearchPanel<T> lf = new SearchPanel<>(m_clazz);
		add(lf);
		lf.getButtonFactory().addBackButton();
		search(lf.getCriteria());
	}

	private void search(QCriteria<T> criteria) {
		if(null == criteria)
			return;
		SimpleSearchModel<T> model = new SimpleSearchModel<T>(this, criteria);

		DataTable<T> table = m_table;
		if(null == table) {
			RowRenderer<T> rr = createRowRenderer();
			table = m_table = new DataTable<>(model, rr);
			add(table);
			add(new DataPager(table));
			table.setPageSize(10);
		} else {
			table.setModel(model);
		}
	}

	protected RowRenderer<T> createRowRenderer() {
		return new RowRenderer<>(m_clazz);
	}
}
