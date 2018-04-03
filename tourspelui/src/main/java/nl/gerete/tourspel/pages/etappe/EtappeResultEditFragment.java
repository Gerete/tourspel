package nl.gerete.tourspel.pages.etappe;

import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.css.*;
import to.etc.domui.dom.html.*;

import javax.annotation.*;
import java.util.*;

class EtappeResultEditFragment extends Div {
	private static final int MAX_ETAPPE_RESULTS = 10;

	@Nonnull
	final private Edition m_edition;

	@Nullable
	final private Etappe m_etappe;

	@Nonnull
	private List<EtappeResult> m_resultList = new ArrayList<EtappeResult>();

	@Nullable
	private OrderedRidersComponent<EtappeResult> m_ridersComponent;

	@Nonnull
	private EtappeResult m_selectedResult;

	private TR m_selectedRow;

	private TBody m_inputBody;

	@Nonnull
	private List<IValueChanged> m_changeListeners = Collections.EMPTY_LIST;

	@Nonnull
	private List<ITableModelListener<EtappeResult>> m_modelChangeListeners = Collections.EMPTY_LIST;

	@Nonnull
	private final SimpleListModel<EtappeResult> m_etappeResultModel;

	@Nonnull
	public Edition getEdition() {
		return m_edition;
	}

	public EtappeResultEditFragment(@Nonnull Edition edition, @Nullable Etappe et, @Nonnull SimpleListModel<EtappeResult> model) throws Exception {
		m_edition = edition;
		m_etappe = et;
		m_etappeResultModel = model;
		for(EtappeResult etappeResult : m_etappeResultModel.getItems(0, m_etappeResultModel.getRows())) {
			m_resultList.add(etappeResult);
		}
	}

	@Nullable
	private Etappe getEtappe() throws Exception {
		return m_etappe;
	}

	@Override
	public void createContent() throws Exception {
		if(null == getEtappe()) {
			add(new InfoPanel("Er zijn geen etappes in te vullen voor vandaag."));
			return;
		}

		//-- Etappe-resultaat.
		add(new BR());
		add(new Div("Vul het resultaat in (1e 10 posities) van etappe " + m_etappe.getStage() + " " + " van " + m_etappe.getStart() + " naar " + m_etappe.getEnd() + " (" + m_etappe.getType() + ")"));

		//-- We need to fill in 10 etappe results...
		m_ridersComponent = new OrderedRidersComponent<EtappeResult>(m_etappeResultModel, MAX_ETAPPE_RESULTS);
		add(m_ridersComponent);
		m_ridersComponent.setModel(m_etappeResultModel);
		m_ridersComponent.addListener(new IModelChangedListener<EtappeResult>() {

			@Override
			public void onValueRemoved(EtappeResult removedEntry) throws Exception {
				m_etappeResultModel.delete(removedEntry);
				updateState();
				fireRemoved(removedEntry);
			}

			@Override
			public void onValueAdded(EtappeResult addedEntry) throws Exception {
				updateState();
			}
		});
		m_inputBody = addTable();

		RiderLookupInput<EtappeResult> rin = new RiderLookupInput<EtappeResult>();
		rin.setAdded(new ICellClicked<Rider>() {

			@Override
			public void cellClicked(Rider rowval) throws Exception {
				addRider(rowval);
			}
		});
		rin.setSelectedRiders(m_resultList);
		m_inputBody.addCell().add(rin);
		updateState();
	}

	protected void fireRemoved(EtappeResult removedEntry) throws Exception {
		for(ITableModelListener<EtappeResult> tml : m_modelChangeListeners) {
			tml.rowDeleted(m_etappeResultModel, removedEntry.getPlace(), removedEntry);
		}

	}

	private void updateState() throws Exception {
		if(m_resultList.size() >= 10) {
			m_inputBody.getTable().setDisplay(DisplayType.NONE);
		} else {
			m_inputBody.getTable().setDisplay(DisplayType.BLOCK);
		}
		fireChanges();
	}



	/**
	 * Poor man's data table selection.
	 * @param tr
	 * @param rowval
	 */
	protected void handleSelection(NodeBase tr, EtappeResult rowval) {
		if(m_selectedRow != null) {
			m_selectedRow.removeCssClass("ui-selected");
			m_selectedResult = null;
			m_selectedRow = null;
		}
		if(rowval != null) {
			tr.addCssClass("ui-selected");
			m_selectedRow = (TR) tr;
			m_selectedResult = rowval;
		}

		updateButtons();

	}

	private void updateButtons() {}


	protected void addRider(Rider value) throws Exception {
		if(null == value || m_ridersComponent == null)
			return;

		//-- Ok: add new one
		EtappeResult er = new EtappeResult();
		er.setEtappe(m_etappe);
		er.setRider(value);
		getSharedContext().save(er);
		m_resultList.add(er);
		m_ridersComponent.addOrderedRider(er);
	}

	public void removeRider(Rider rider) throws Exception { //FIXME Wordt deze wel aangeroepen?
		if(rider == null) {
			return;
		}
		for(EtappeResult etappeResult : m_resultList) {
			if(etappeResult.getRider().equals(rider)) {
				m_resultList.remove(etappeResult);
				m_ridersComponent.removeRider(etappeResult);
			}
		}
	}

	public void save(@Nonnull List<EtappeResult> etappeResults) throws Exception {
		for(IOrderedRiders er : etappeResults) {
			getSharedContext().save(er);
		}
	}

	public void addListener(IValueChanged vc) {
		if(m_changeListeners == Collections.EMPTY_LIST)
			m_changeListeners = new ArrayList<IValueChanged>();
		m_changeListeners.add(vc);
	}

	public void removeListener(IValueChanged vc) {
		if(m_changeListeners.size() > 0)
			m_changeListeners.remove(vc);
	}

	private void fireChanges() throws Exception {
		for(IValueChanged vc : m_changeListeners)
			vc.onValueChanged(this);
	}

	public void addListener(ITableModelListener<EtappeResult> mcl) {
		if(m_modelChangeListeners == Collections.EMPTY_LIST) {
			m_modelChangeListeners = new ArrayList<ITableModelListener<EtappeResult>>();
		}
		m_modelChangeListeners.add(mcl);
	}

}
