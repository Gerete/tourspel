package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.lookup.*;
import to.etc.domui.component.meta.*;
import to.etc.domui.dom.html.*;
import to.etc.webapp.query.*;

@UIRights(value = "ADMIN")
public class PersonListPage extends BasicListPage<Person> {

	public PersonListPage() {
		super(Person.class, PersonDetailListPage.class);
		setEnableNewButton(true);
		setDisplayFields(Person.pLASTNAME, "%30", SortableType.SORTABLE_ASC, Person.pFIRSTNAME, "%20", SortableType.SORTABLE_ASC, Person.pEMAIL, "%30", SortableType.SORTABLE_ASC, Person.pPHONENUMBER, "%10",
			SortableType.SORTABLE_ASC, "latePayment");
	}

	@Override
	public void createContent() throws Exception {
		super.createContent();
		createPaidField();
	}

	private void createPaidField() {
		LookupForm<Person> lf = getLookupForm();

		final Checkbox cb = new Checkbox();

		AbstractLookupControlImpl lookupControlThingy = new AbstractLookupControlImpl(cb) {
			@Override
			public AppendCriteriaResult appendCriteria(QCriteria< ? > crit) throws Exception {
				boolean userEntry = false;

				if(cb.getValue().booleanValue()) {
					crit.exists(PlayList.class, Person.pPLAYLISTLIST).ne(PlayList.pPAID, cb.getValue());
					userEntry = true;
				}
				if(userEntry) {
					return AppendCriteriaResult.VALID;
				}
				return AppendCriteriaResult.EMPTY;
			}
		};

		lf.addProperty(Person.pLASTNAME);
		lf.addProperty(Person.pFIRSTNAME);
		lf.addProperty(Person.pEMAIL);
		lf.addManualPropertyLabel(Person.pPLAYLISTLIST, lookupControlThingy);
	}
}
