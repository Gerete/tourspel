package nl.gerete.tourspel.pages.etappe;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.css.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;

import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.adm.*;
import nl.gerete.tourspel.pages.playlist.*;

/**
 * This handles the "daily" maintenance of an open edition.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 3, 2012
 */
public class AdminDailyPage extends BasicTourPage {
	private EtappeResultEditFragment m_etappeFragment;

	private QuittersFragment m_quittersFragment;

	private DefaultButton m_finishButton;

	private Etappe m_etappe;

	private List<EtappeResult> m_etappeResults;

	private List<StoppedRider> m_stoppedList;

	private SimpleListModel<Rider> m_ridersModel;

	private SimpleListModel<EtappeResult> m_etappeResultModel;

	@Override
	public void createContent() throws Exception {

		//-- 1. Make sure edition is open and running.
		if(!checkEdition(EditionPhase.RUNNING)) // The tour must be running.
			return;
		getTourFrame().add(new EditionInfoFragment(getEdition()));

		m_etappe = EditionBP.getCurrentEtappe(getSharedContext());
		if(m_etappe == null) {
			m_etappeResults = Collections.EMPTY_LIST;
		} else {
			m_etappeResults = EditionBP.getEtappeResults(m_etappe); // Get ordered etappe results.
		}

		createRidersByTeamFragment();

		TBody tBody = addTable();
		TD leftCell = tBody.addCell();
		leftCell.setVerticalAlign(VerticalAlignType.TOP);
		leftCell.setWidth("300px");
		RiderByTeamComponent teamComponent = new RiderByTeamComponent(m_ridersModel);
		leftCell.add(teamComponent);

		TD rightCell = tBody.addCell();
		rightCell.setVerticalAlign(VerticalAlignType.TOP);
		TabPanel tp = new TabPanel();
		rightCell.add(tp);
		createEtappeResultFragment(tp);

		createQuittersFragment(tp);

		addButtonBar();

		updateState();

		m_etappeFragment.addListener(new IValueChanged<NodeBase>() {
			@Override
			public void onValueChanged(NodeBase component) throws Exception {
				updateState();
			}
		});

		m_etappeFragment.addListener(new ITableModelListener<EtappeResult>() {

			@Override
			public void rowModified(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {}

			@Override public void rowsSorted(ITableModel<EtappeResult> model) throws Exception {}

			@Override
			public void rowDeleted(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {
				m_ridersModel.delete(value.getRider());
			}

			@Override
			public void rowAdded(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {

			}

			@Override
			public void modelChanged(ITableModel<EtappeResult> model) {}
		});
	}

	/**
	 * @param tp
	 * @throws Exception
	 */
	private void createEtappeResultFragment(@NonNull TabPanel tp) throws Exception {
		m_etappeResultModel = new SimpleListModel<EtappeResult>(m_etappeResults);
		m_etappeResultModel.addChangeListener(new ITableModelListener<EtappeResult>() {

			@Override
			public void rowModified(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {
			}

			@Override
			public void rowsSorted(ITableModel<EtappeResult> model) throws Exception {}

			@Override
			public void rowDeleted(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {
				m_ridersModel.delete(value.getRider());
				m_etappeResults.remove(value);
			}

			@Override
			public void rowAdded(ITableModel<EtappeResult> model, int index, EtappeResult value) throws Exception {
				m_ridersModel.add(value.getRider());
			}

			@Override
			public void modelChanged(ITableModel<EtappeResult> model) {
			}
		});

		m_etappeFragment = new EtappeResultEditFragment(getEdition(), m_etappe, m_etappeResultModel);
		tp.add(m_etappeFragment, "Etappe resultaat");
	}

	/**
	 *
	 */
	private void createRidersByTeamFragment() {
		final List<Rider> etappeResultRiders = new ArrayList<Rider>();
		for(EtappeResult er : m_etappeResults) {
			etappeResultRiders.add(er.getRider());
		}
		m_ridersModel = new SimpleListModel<Rider>(etappeResultRiders);
		m_ridersModel.addChangeListener(new ITableModelListener<Rider>() {

			@Override
			public void rowModified(ITableModel<Rider> model, int index, Rider value) throws Exception {
			}

			@Override
			public void rowsSorted(ITableModel<Rider> model) throws Exception {}

			@Override
			public void rowDeleted(ITableModel<Rider> model, int index, Rider value) throws Exception {
				removeRiderFromEtappe(value);
			}

			@Override
			public void rowAdded(ITableModel<Rider> model, int index, Rider value) throws Exception {
				addRiderToEtappe(value);
			}

			@Override
			public void modelChanged(ITableModel<Rider> model) {
			}
		});
	}

	protected void removeRiderFromEtappe(Rider rider) throws Exception {
		EtappeResult etappeResult = findEtappeResultByRider(rider);
		if(etappeResult != null) {
			m_etappeResultModel.delete(etappeResult);
		}
	}

	@Nullable
	private EtappeResult findEtappeResultByRider(@NonNull Rider rider) throws Exception {
		for(int i = m_etappeResultModel.getRows(); --i >= 0;) {
			EtappeResult er = m_etappeResultModel.getItem(i);
			if(rider.equals(er.getRider()))
				return er;
		}
		return null;
	}

	protected void addRiderToEtappe(Rider rider) throws Exception {
		for(EtappeResult etappeResult : m_etappeResults) {
			if(etappeResult.getRider().equals(rider)) {
				return;
			}
		}
		m_etappeFragment.addRider(rider);
	}

	/**
	 * @param tp
	 */
	private void createQuittersFragment(TabPanel tp) {
		m_quittersFragment = new QuittersFragment(getEdition());
		tp.add(m_quittersFragment, "Afstappers");
	}


	/**
	 *
	 */
	private void addButtonBar() {
		ButtonBar bb = new ButtonBar();
		add(bb);
		bb.addBackButton();

		DefaultButton okb = new DefaultButton("!Save", Icon.of("THEME/btnSave.png"), new IClicked<DefaultButton>() {
			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				save();
			}
		});
		bb.addButton(okb);

		m_finishButton = bb.addConfirmedButton("Etappe afsluiten", "Dit sluit de etappe en berekent de tussenstanden voor deze etappe. Weet je zeker dat alle gegevens juist zijn ingevuld?",
			new IClicked<DefaultButton>() {
				@Override
				public void clicked(DefaultButton clickednode) throws Exception {
					m_etappe.setPhase(EtappePhase.CALCULATING);
					getSharedContext().commit();
				}
			});
	}

	private void updateState() throws Exception {
		if(m_etappeResults.size() >= 10) {
			m_finishButton.setDisplay(DisplayType.INLINE_BLOCK);
		} else {
			m_finishButton.setDisplay(DisplayType.NONE);

		}
	}

	private void save() throws Exception {
		//-- Make sure the current etappe list does not contain a quitter.
		Set<Rider> stoppedSet = m_quittersFragment.getStoppedRiders();
		for(EtappeResult er : m_etappeResults) {
			if(stoppedSet.contains(er.getRider())) {
				MsgBox.error(this, "De renner " + er.getRider() + " staat in de etappe resultaten, maar zit ook in de afstaplijst.");
				return;
			}
		}

		m_etappeFragment.save(m_etappeResults);
		m_quittersFragment.save();
		getSharedContext().commit();
		MessageFlare.display(this, MsgType.INFO, "De gegevens zijn opgeslagen.");
	}



}
