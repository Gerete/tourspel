package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.playlist.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class PersonDetailListPage extends BasicEditPage<Person> {

	private SimpleListModel<PlayList> m_slm;

	@Nonnull
	private Person m_person;

	@Nonnull
	@UIUrlParameter(name = "id", mandatory = true)
	public Person getPerson() {
		return m_person;
	}

	public void setPerson(Person person) {
		m_person = person;
	}

	public PersonDetailListPage() {
		super(Person.class);
	}

	@Override
	public void createContent() throws Exception {

		TourUser user = (TourUser) UIContext.getLoggedInUser();
		if(user.hasRight(ApplicationRight.ADMIN)) {
			setProperties("firstName", "lastName", "prefix", "email", "phoneNumber", Person.pPASSWORD);
		} else {
			setProperties("firstName", "lastName", "prefix", "phoneNumber");
		}

		super.createContent();

		if(!user.hasRight(ApplicationRight.ADMIN)) {
			if(!user.getPerson().getId().equals(getPerson().getId())) {
				throw new IllegalStateException("The page was accessed illegally (or at least without a person_id)");
			}
		}

		List<PlayList> playLists = new ArrayList<PlayList>();
		if(getPerson().getId() != null) {
			QCriteria<PlayList> q = QCriteria.create(PlayList.class).eq(PlayList.pPERSON, getPerson());
			playLists = getSharedContext().query(q);
		}

		if(playLists.isEmpty()) {
			add(new Div("geen lijst aanwezig"));
		} else {
			m_slm = new SimpleListModel<PlayList>(playLists);
			BasicRowRenderer<PlayList> brr = new BasicRowRenderer<PlayList>(PlayList.class);
			brr.setRowClicked(new ICellClicked<PlayList>() {
				@Override
				public void cellClicked(PlayList rowval) throws Exception {
					UIGoto.moveSub(PlayListEditPage.class, new PageParameters("id", rowval.getId()));
				}
			});
			DataTable<PlayList> dt = new DataTable<PlayList>(m_slm, brr);
			add(dt);
		}
	}

	@Override
	@Nullable
	public DefaultButton getAdditionalButton() {
		TourUser user = (TourUser) UIContext.getLoggedInUser();
		// FIXME Waarom moet ik hier toString() vergelijken?
		if(getPerson().toString().equals(user.getPerson().toString())) {
			DefaultButton playListAddButton = new DefaultButton("Nieuwe lijst", new IClicked<DefaultButton>() {

				@Override
				public void clicked(DefaultButton clickednode) throws Exception {
					UIGoto.moveSub(PlayListEditPage.class, "id", "NEW", "person_id", getPage().getPageParameters().getLongW("id"));
				}
			});
			return playListAddButton;
		} else
			return null;
	}

	@Override
	protected void onShelve() throws Exception {
		/*
		 * Close the context so the query will be re-executed when the page is re-entered
		 */
		QContextManager.closeSharedContexts(getPage().getConversation());
	}

	@Override
	protected void onUnshelve() throws Exception {
		forceRebuild();
	}
}
