package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.text.*;
import java.util.*;

/**
 * Fragment which is used to show the most recent riders that quit.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 22 mei 2012
 */
public class StoppedRidersFragment extends Div {

	private int m_nrOfRidersToShow;

	public StoppedRidersFragment(int nrOfRidersToShow) {
		m_nrOfRidersToShow = nrOfRidersToShow;
		setCssClass("portalFragment");
	}

	@Override
	public void createContent() throws Exception {
		QCriteria<StoppedRider> qpl = QCriteria.create(StoppedRider.class);
		qpl.add(new QOrder(QSortOrderDirection.DESC, StoppedRider.pSTOPDATE));
		List<StoppedRider> stoppedRiders = getSharedContext().query(qpl);

		if(stoppedRiders.size() == 0) {
			add(new Label($("noSteppedOut")));
			return;
		}

		add(new Label($("steppedOut")));

		TBody tBody = addTable($("number"), $("name"), $("etappe"));

		int i = 0;
		for(StoppedRider sr : stoppedRiders) {
			addRiderToList(tBody, sr);
			i++;
			if(i > m_nrOfRidersToShow)
				break;
		}
	}

	private void addRiderToList(@NonNull TBody tBody, @NonNull StoppedRider stoppedRider) {

		Rider rider = stoppedRider.getRider();

		TR tr = tBody.addRow();
		TD td = tr.addCell();
		td.add(rider.getNumber().toString());
		td = tr.addCell();
		td.setAlign(TDAlignType.CENTER);
		td.add(rider.getDisplayName());
		td = tr.addCell();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		td.add(sdf.format(stoppedRider.getStopDate()));
	}
}
