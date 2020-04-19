package nl.gerete.tourspel.components;

import java.util.*;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.*;

/**
 *
 * @author <a href="mailto:ben.schoen@itris.nl">Ben Schoen</a>
 * Created on Mar 5, 2012
 */
public class TeamRiderEditFragment extends Div implements ISavableFragment {

	private Team m_team;

	private List<Rider> m_deletedRiders = new ArrayList<Rider>();

	private List<Rider> m_riders = new ArrayList<Rider>();

	TBody m_riderTable;

	private ButtonBar m_buttonBar;

	public TeamRiderEditFragment(Team team) {
		m_team = team;
	}

	@Override
	public void createContent() throws Exception {

		createRiderList();

		m_buttonBar = new ButtonBar();
		add(m_buttonBar);
		createAddButton();
	}

	private void createRiderList() throws Exception {
		List<Rider> riders = m_team.getRiders();
		m_riders.addAll(riders);
		m_riderTable = addTable("Achternaam", "Voornaam", "Tussenvoegsel", "Geboortedatum", "Rugnummer", "Land");
		for(Rider rider : riders) {

			addRiderRow(rider);
		}
		for(int i = 10 - m_team.getRiders().size(); i > 0; i--) {
			Rider r = new Rider(m_team);
			r.setEdition(m_team.getEdition());
			m_riders.add(r);
			addRiderRow(r);
		}
	}

	private void addRiderRow(final Rider rider) {
		final RiderEditRow riderRow = new RiderEditRow(rider);
		Button deleteBtn = MsgBox.areYouSureButton("Verwijderen", null, $("delete.confirmation"), new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				m_riders.remove(rider);
				m_deletedRiders.add(rider);
				addRiderToTable();
				m_riderTable.removeChild(riderRow);
			}
		});
		riderRow.setRowButton(deleteBtn);
		m_riderTable.add(riderRow);
	}

	private void addRiderToTable() {
		Rider r = new Rider(m_team);
		m_riders.add(r);
		addRiderRow(r);
	}

	private void createAddButton() {
		if(isEditable()) {
			DefaultButton addButton = m_buttonBar.addButton("Toevoegen", Icon.of("THEME/btnNew.png"), new IClicked<DefaultButton>() {
				@Override
				public void clicked(DefaultButton b) throws Exception {
					addRiderToTable();
				}

			});
		}
	}

	@Override
	public void save(QDataContext dc) throws Exception {
		for (Iterator<Rider> iterator = m_riders.iterator(); iterator.hasNext(); ) {
			Rider rider =  iterator.next();
			if(rider.getLastName()==null) {
				iterator.remove();
			}
		}
		for(Rider rider : m_riders) {
			dc.save(rider);
		}

		for(Rider rider : m_deletedRiders) {
			dc.delete(rider);
		}
	}

	private boolean isEditable() {
		return true;
	}
}
