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

	@NonNull
	final private Edition m_edition;

	@Nullable
	final private Etappe m_etappe;

	@NonNull
	private List<EtappeResult> m_resultList = new ArrayList<EtappeResult>();

	@Nullable
	private OrderedRidersComponent<EtappeResult> m_ridersComponent;

	private TR m_selectedRow;

	private TBody m_inputBody;

	@NonNull
	private List<IValueChanged<?>> m_changeListeners = new ArrayList<>();

	@NonNull
	private List<ITableModelListener<EtappeResult>> m_modelChangeListeners = new ArrayList<>();

	@NonNull
	private final SimpleListModel<EtappeResult> m_etappeResultModel;

	@NonNull
	public Edition getEdition() {
		return m_edition;
	}

	public EtappeResultEditFragment(@NonNull Edition edition, @Nullable Etappe et, @NonNull SimpleListModel<EtappeResult> model) throws Exception {
		m_edition = edition;
		m_etappe = et;
		m_etappeResultModel = model;
		m_resultList.addAll(m_etappeResultModel.getItems(0, m_etappeResultModel.getRows()));
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
		add(new Div("Vul het resultaat in (1e 10 posities) van etappe " + Objects.requireNonNull(m_etappe).getStage() + " " + " van " + Objects.requireNonNull(m_etappe).getStart() + " naar " + Objects.requireNonNull(m_etappe).getEnd() + " (" + Objects.requireNonNull(m_etappe).getType() + ")"));

		//-- We need to fill in 10 etappe results...
		OrderedRidersComponent<EtappeResult> ridersComponent = m_ridersComponent = new OrderedRidersComponent<EtappeResult>(m_etappeResultModel, MAX_ETAPPE_RESULTS);
		add(ridersComponent);
		ridersComponent.setModel(m_etappeResultModel);
		ridersComponent.addListener(new IModelChangedListener<EtappeResult>() {

			@Override
			public void onValueRemoved(@NonNull EtappeResult removedEntry) throws Exception {
				m_etappeResultModel.delete(removedEntry);
				updateState();
				fireRemoved(removedEntry);
			}

			@Override
			public void onValueAdded(@NonNull EtappeResult addedEntry) throws Exception {
				updateState();
			}
		});
		m_inputBody = addTable();

		RiderLookupInput<EtappeResult> rin = new RiderLookupInput<EtappeResult>();
		rin.setAdded(this::addRider);
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
			m_selectedRow = null;
		}
		if(rowval != null) {
			tr.addCssClass("ui-selected");
			m_selectedRow = (TR) tr;
		}
		updateButtons();
	}

	private void updateButtons() {}


	protected void addRider(Rider value) throws Exception {
		if(null == value || m_ridersComponent == null)
			return;

		//-- Ok: add new one
		EtappeResult er = new EtappeResult();
		er.setEtappe(Objects.requireNonNull(m_etappe));
		er.setRider(value);
		getSharedContext().save(er);
		m_resultList.add(er);
		Objects.requireNonNull(m_ridersComponent).addOrderedRider(er);
	}

	public void removeRider(Rider rider) throws Exception { //FIXME Wordt deze wel aangeroepen?
		if(rider == null) {
			return;
		}
		for(EtappeResult etappeResult : m_resultList) {
			if(etappeResult.getRider().equals(rider)) {
				m_resultList.remove(etappeResult);
				Objects.requireNonNull(m_ridersComponent).removeRider(etappeResult);
			}
		}
	}

	public void save(@NonNull List<EtappeResult> etappeResults) throws Exception {
		for(IOrderedRiders er : etappeResults) {
			getSharedContext().save(er);
		}
	}

	public void addListener(IValueChanged<?> vc) {
		if(m_changeListeners == Collections.EMPTY_LIST)
			m_changeListeners = new ArrayList<>();
		m_changeListeners.add(vc);
	}

	public void removeListener(IValueChanged<?> vc) {
		if(m_changeListeners.size() > 0)
			m_changeListeners.remove(vc);
	}

	private void fireChanges() throws Exception {
		for(IValueChanged<?> vc : m_changeListeners)
			((IValueChanged<EtappeResultEditFragment>)vc).onValueChanged(this);
	}

	void addListener(ITableModelListener<EtappeResult> mcl) {
		if(m_modelChangeListeners == Collections.EMPTY_LIST) {
			m_modelChangeListeners = new ArrayList<>();
		}
		m_modelChangeListeners.add(mcl);
	}

}
