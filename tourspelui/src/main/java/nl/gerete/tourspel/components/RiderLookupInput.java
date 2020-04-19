package nl.gerete.tourspel.components;

import java.util.*;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.input.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.db.*;


/**
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 15 mei 2012
 */
public class RiderLookupInput<T extends IOrderedRiders> extends Div {


	private Collection<T> m_selectedRiders = Collections.EMPTY_LIST;

	public void setSelectedRiders(Collection<T> selectedRiders) {
		m_selectedRiders = selectedRiders;
	}

	private ICellClicked<Rider> m_riderAdded;

	@Override
	public void createContent() throws Exception {
		TBody container = addTable();
		final LookupInput<Rider> rin = new LookupInput<Rider>(Rider.class);
		container.addRowAndCell().add("Renner");
		TD td = container.addCell();
		td.add(rin);
		td.setCellWidth("200");
		LinkButton lb = new LinkButton("Toevoegen", Icon.of("THEME/btnNew.png"), new IClicked<LinkButton>() {
			@Override
			public void clicked(LinkButton clickednode) throws Exception {
				if(rin.getValue() != null) {
					Rider rider = rin.getValue();
					rin.setValue(null);
					m_riderAdded.cellClicked(Objects.requireNonNull(rider));
				}
			}
		});
		td = container.addCell();
		td.add(lb);

		rin.setQueryManipulator(createManipulator());
	}

	public void setAdded(ICellClicked<Rider> iCellClicked) {
		m_riderAdded = iCellClicked;
	}

	private IQueryManipulator<Rider> createManipulator() {
		return new IQueryManipulator<Rider>() {

			@Override
			public QCriteria<Rider> adjustQuery(QCriteria<Rider> c) {
				//-- Remove all earlier added riders
				for(T r : m_selectedRiders) {
					c.ne("id", Objects.requireNonNull(r.getRider()).getId());
				}
				return c;
			}
		};
	}

}
