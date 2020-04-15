package nl.gerete.tourspel.pages.etappe;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class EtappeResultFragment extends Div {

	private Etappe m_etappe;

	public EtappeResultFragment(Etappe et) {
		m_etappe = et;
		setCssClass("portalFragment");
	}

	@Nullable
	private Etappe getEtappe() throws Exception {
		return m_etappe;
	}

	private void setEtappe(@NonNull Etappe etappe) {
		m_etappe = etappe;
		forceRebuild();
	}

	@Override
	public void createContent() throws Exception {

		if(m_etappe == null) {
			add(new Div($("geen.etappeuitslag")));
			return;
		}

		add(new Label($("etappeuitslag")));
		add(new Div(m_etappe.getDisplayName()));

		QCriteria<EtappeResult> qc = QCriteria.create(EtappeResult.class).eq(EtappeResult.pETAPPE, getEtappe()).ascending(EtappeResult.pPLACE);
		List<EtappeResult> resultList = getSharedContext().query(qc);

		TBody tb = addTable($("plaats"), $("naam"));
		for(EtappeResult result : resultList) {
			TR tr = tb.addRow();
			TD td = tr.addCell();
			td.setText("" + (result.getPlace()));
			td = tr.addCell();
			td.setText(getRidersName(result));
		}

		addNavigation();

	}

	private void addNavigation() {
		Button btnPrev = new SmallImgButton("images/go-left.png", new IClicked<SmallImgButton>() {

			@Override
			public void clicked(SmallImgButton clickednode) throws Exception {
				calculateNextEtappe(-1);
			}
		});
		add(btnPrev);

		Button btnNext = new SmallImgButton("images/go-right.png", new IClicked<SmallImgButton>() {

			@Override
			public void clicked(SmallImgButton clickednode) throws Exception {
				calculateNextEtappe(1);
			}
		});
		add(btnNext);
	}

	protected void calculateNextEtappe(int delta) throws Exception {
		List<Etappe> etappeList = EditionBP.getEtappeList(EditionBP.getCurrentEdition(getSharedContext()));
		int newEtappe = etappeList.indexOf(getEtappe()) + delta;
		if(newEtappe >= 0 && newEtappe < etappeList.size()) {
			setEtappe(etappeList.get(newEtappe));
			return;
		}
		if(delta > 0) {
			setEtappe(etappeList.get(0));
			return;
		}
		setEtappe(etappeList.get(etappeList.size() - 1));
	}

	private String getRidersName(EtappeResult result) {
		String middle = "";
		if(!(result.getRider().getMiddleName() == null || result.getRider().getMiddleName().equals(""))) {
			middle = " " + result.getRider().getMiddleName();
		}
		return result.getRider().getFirstName() + middle + " " + result.getRider().getLastName();
	}
}
