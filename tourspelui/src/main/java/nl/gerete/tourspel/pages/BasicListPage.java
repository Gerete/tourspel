package nl.gerete.tourspel.pages;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.ntbl.*;
import to.etc.domui.component.searchpanel.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.webapp.qsql.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.adm.*;

public class BasicListPage<T extends ILongIdentifyable> extends BasicTourPage {

	private SearchPanel<T> m_lookupForm;

	private DataTable<T> m_datatable;

	private Class<T> m_class;

	private Class< ? extends UrlPage> m_editPageClass;

	private QField<T,?>[] m_searchFields = new QField[0];

	private Object[] m_displayFields = new Object[0];

	private Div m_content;

	private IRowRenderer<T> m_rowRenderer;

	private boolean m_enableNewButton;

	public BasicListPage(Class<T> clz, Class< ? extends UrlPage> clzPage) {
		m_class = clz;
		m_editPageClass = clzPage;
	}

	protected void setSearchFields(QField<T,?>... searchFields) {
		m_searchFields = searchFields;
	}

	protected void setDisplayFields(Object... displayFields) {
		m_displayFields = displayFields;
	}

	@Override
	public void createContent() throws Exception {
		m_content = getTourFrame();

		m_lookupForm = new SearchPanel<>(m_class);
		m_lookupForm.setMargin("5%");
		m_lookupForm.setCssClass("lookup");
		if(m_searchFields.length > 0) {
			for(QField<T,?> s : m_searchFields) {
				m_lookupForm.add().property(s).control();
			}
		}

		if(m_enableNewButton) {
			m_lookupForm.setOnNew(clickednode -> UIGoto.moveSub(m_editPageClass, new PageParameters("id", "NEW")));
		}

		m_content.add(m_lookupForm);

		m_lookupForm.setClicked(new IClicked<SearchPanel<T>>() {
			@Override
			public void clicked(SearchPanel<T> b) throws Exception {
				search(b.getCriteria());
			}
		});

		search(m_lookupForm.getCriteria());
	}

	protected void adjustCriteria(QCriteria<T> crit) {}

	protected void search(QCriteria<T> enteredCriteria) throws Exception {
		adjustCriteria(enteredCriteria);

		Edition ed = restrictEdition();
		if(null != ed) {
			enteredCriteria.eq("edition", ed);
		}

		SimpleSearchModel<T> ssm = new SimpleSearchModel<T>(this, enteredCriteria);
		if(m_datatable != null) {
			/**
			 * The datatable already exists and only needs to be updated.
			 */
			m_datatable.setModel(ssm);
			return;
		}
		/**
		 * If the datatable is not yet created it will be now!
		 */
		BasicRowRenderer<T> brr = new BasicRowRenderer<T>(m_class);
		if(m_displayFields.length > 0) {
			brr.addColumns(m_displayFields);
		}

		brr.setRowClicked(new ICellClicked<T>() {
			@Override
			public void cellClicked(T rowval) throws Exception {
				UIGoto.moveSub(m_editPageClass, new PageParameters("id", rowval.getId()));
			}
		});

		brr.setRowButtonFactory(getRowButtonFactory());

		m_datatable = new DataTable<T>(ssm, brr);
		m_datatable.setPageSize(10);
//		m_datatable.setWidth("95%");
//		m_datatable.setMargin("5%");
		m_datatable.setCssClass("testtest");

		m_content.add(m_datatable);
		m_content.add(new DataPager(m_datatable));
	}

	@Override
	protected void onShelve() throws Exception {
		/*
		 * Close the context so the query will be re-executed when the page is re-entered
		 */
		QContextManager.closeSharedContexts(getPage().getConversation());
	}

	public DataTable<T> getDatatable() {
		return m_datatable;
	}

	public void setDatatable(DataTable<T> datatable) {
		m_datatable = datatable;
	}

	public void onSelect(T rcord) throws Exception {}

	/**
	 * Get the row renderer used for the request. This returns either the instance created by {@link #provideRowRenderer()} or
	 * the instance that was automatically created.
	 * @return
	 */
	public IRowRenderer<T> getRowRenderer() throws Exception {
		if(m_rowRenderer == null) {
			m_rowRenderer = new BasicRowRenderer<T>(m_class); // Create a default one
		}

		//-- jal 20091111 It is required that any search result has clickable rows. If no row click handler is set set one to call onNew.
		if(m_rowRenderer instanceof AbstractRowRenderer< ? >) { // Silly ? is needed even though cast cant do anything with it. Idiots.
			AbstractRowRenderer<T> arrh = (AbstractRowRenderer<T>) m_rowRenderer;
			if(arrh.getRowClicked() == null) {
				arrh.setRowClicked(new ICellClicked<T>() {
					@Override
					public void cellClicked(T val) throws Exception {
						onSelect(val);
					}
				});
			}
		}
		return m_rowRenderer;
	}

	public IRowButtonFactory<T> getRowButtonFactory() {
		return null;
	}

	/**
	 * Override to provide your own Row Renderer. If not set a BasicRowRenderer with reasonable
	 * defaults will be created for you.
	 * @throws Exception
	 */
	public void setRowRenderer(IRowRenderer<T> rr) throws Exception {
		m_rowRenderer = rr;
	}

	/**
	 * Disables the New button for this page
	 * @param enableNewButton
	 */
	public void setEnableNewButton(boolean enableNewButton) {
		m_enableNewButton = enableNewButton;
	}

	protected SearchPanel<T> getLookupForm() {
		return m_lookupForm;
	}

	/**
	 *
	 * @return
	 */
	@Nullable
	protected Edition	restrictEdition() throws Exception {
		return null;
	}
}
