package nl.gerete.tourspel.pages.etappe;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.input.*;
import to.etc.domui.component.ntbl.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

/**
 * Edit the quitter's list for the current edition/etappe.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 25, 2012
 */
class QuittersFragment extends Div {
	private Edition m_edition;

	private List<StoppedRider> m_stoppedList;

	private SimpleListModel<StoppedRider> m_stoppedModel;

	private DataTable<StoppedRider> m_table;

	private TBody m_inputBody;

	private Etappe m_etappe;

	private DateInput m_stopDateInput;

	public QuittersFragment(Edition edition) {
		m_edition = edition;
	}

	/**
	 * Get a set of all riders that are currently defined as "stopped".
	 * @return
	 */
	public Set<Rider> getStoppedRiders() {
		Set<Rider> res = new HashSet<Rider>();
		for(StoppedRider sr : m_stoppedList)
			res.add(sr.getRider());
		return res;
	}

	@Override
	public void createContent() throws Exception {
		m_etappe = EditionBP.getCurrentEtappe(getSharedContext());

		m_stoppedModel = new SimpleListModel<StoppedRider>(getStoppedList());
		BasicRowRenderer<StoppedRider> brr = new BasicRowRenderer<StoppedRider>(StoppedRider.class, //
			"rider", new FlagRenderer<>(Rider::getCountry), "%1", "^" //
			, "stopDate", "^Gestopt op", "%5", BasicRowRenderer.NOWRAP //
			, "rider", "^Renner", "%30", BasicRowRenderer.NOWRAP //
			, "rider.team.name", "^Team", "%20", BasicRowRenderer.NOWRAP //
			, "etappe.displayName", "^Afgestapt voor/tijdens", "%30", BasicRowRenderer.NOWRAP //
		);

		brr.setRowButtonFactory(new IRowButtonFactory<StoppedRider>() {
			@Override
			public void addButtonsFor(RowButtonContainer c, final StoppedRider data) throws Exception {
				if(m_etappe != data.getEtappe())					// Only riders newly added this etappe can be removed.
					return;

				c.addLinkButton("Verwijder", "THEME/btnDelete.png", new IClicked<LinkButton>() {
					@Override
					public void clicked(LinkButton clickednode) throws Exception {
						m_stoppedModel.delete(data);
						getSharedContext().delete(data);
					}
				});
			}
		});

		m_table = new DataTable<StoppedRider>(m_stoppedModel, brr);
		add(m_table);

		m_inputBody = addTable();

		final LookupInput<Rider> rin = new LookupInput<Rider>(Rider.class);
		m_inputBody.addRowAndCell().add("Renner");
		TD td = m_inputBody.addCell();
		td.add(rin);
		td.setCellWidth("200");

		//-- Stop date
		m_stopDateInput = new DateInput();
		m_stopDateInput.setValue(Application.getNow());
		m_stopDateInput.setMandatory(true);
		td = m_inputBody.addCell();
		td.add(m_stopDateInput);
		td.setCellWidth("200");

		LinkButton lb = new LinkButton("Toevoegen", "THEME/btnNew.png", new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				Rider rider = rin.getValue();
				rin.setValue(null);
				addRider(rider, m_stopDateInput.getValue());
			}
		});
		td = m_inputBody.addCell();
		td.add(lb);

		rin.setQueryManipulator(createManipulator(m_stoppedList));
	}

	private IQueryManipulator<Rider> createManipulator(@Nonnull final List<StoppedRider> resultList) {
		return new IQueryManipulator<Rider>() {

			@Override
			public QCriteria<Rider> adjustQuery(QCriteria<Rider> c) {
				//-- Remove all riders that have already stopped
				for(StoppedRider sr : resultList)
					c.ne("id", sr.getRider().getId());
				return c;
			}
		};
	}


	private void addRider(Rider value, Date stop) throws Exception {
		if(null == value)
			return;

		//-- Ok: add new one
		StoppedRider er = new StoppedRider();
		er.setEdition(getEdition());
		er.setEtappe(m_etappe);
		er.setRider(value);

		if(stop.getTime() > Objects.requireNonNull(m_etappe.getDate()).getTime())
			stop = m_etappe.getDate();

		er.setStopDate(stop);
		m_stoppedModel.add(er);
	}


	public List<StoppedRider> getStoppedList() throws Exception {
		if(null == m_stoppedList) {
			m_stoppedList = getSharedContext().query(QCriteria.create(StoppedRider.class).eq("edition", getEdition()).descending("stopDate"));
		}
		return m_stoppedList;
	}

	public Edition getEdition() {
		return m_edition;
	}

	public void save() throws Exception {
		for(StoppedRider sr : m_stoppedList) {
			getSharedContext().save(sr);
		}
	}

}
