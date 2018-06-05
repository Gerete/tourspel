package nl.gerete.tourspel.pages.newpages;

import to.etc.domui.component.searchpanel.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

/**
 * Basic page for all administrative pages of the tour game
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
abstract class BasicTourPage<T> extends UrlPage {

	final private Class<T> m_clazz;

	private DataTable<T> m_table;

	public BasicTourPage(Class<T> clazz) {
		m_clazz = clazz;
	}

	protected void search(SearchPanel<T> lf) throws Exception {
		QCriteria<T> criteria = lf.getCriteria();
		if(criteria == null) {					// Nothing entered or error
			return;
		}
		search(criteria);
	}

	protected void search(QCriteria<T> criteria) {
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

	void addHeader() {

		Div borderDiv = new Div();
		borderDiv.setCssClass("borderdiv");
		add(borderDiv);

		Div headerDiv = new Div();
		headerDiv.setCssClass("headerdiv");
		borderDiv.add(headerDiv);

		Img img = new Img();
		img.setSrc("images/logo-tour.png");
		headerDiv.add(img);

	}
}
