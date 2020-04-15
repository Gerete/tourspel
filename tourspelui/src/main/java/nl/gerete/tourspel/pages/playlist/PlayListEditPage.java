package nl.gerete.tourspel.pages.playlist;

import nl.gerete.tourspel.adm.TourUser;
import nl.gerete.tourspel.components.OrderedRidersComponent;
import nl.gerete.tourspel.components.RiderLookupInput;
import nl.gerete.tourspel.db.ApplicationRight;
import nl.gerete.tourspel.db.IOrderedRiders;
import nl.gerete.tourspel.db.Person;
import nl.gerete.tourspel.db.PlayList;
import nl.gerete.tourspel.db.PlayListEntry;
import nl.gerete.tourspel.db.Rider;
import nl.gerete.tourspel.logic.EditionBP;
import nl.gerete.tourspel.pages.adm.BasicTourPage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.annotations.UIUrlParameter;
import to.etc.domui.component.buttons.DefaultButton;
import to.etc.domui.component.controlfactory.ModelBindings;
import to.etc.domui.component.form.TabularFormBuilder;
import to.etc.domui.component.layout.ButtonBar;
import to.etc.domui.component.layout.CaptionedHeader;
import to.etc.domui.component.misc.MsgBox;
import to.etc.domui.component.misc.VerticalSpacer;
import to.etc.domui.component.tbl.ICellClicked;
import to.etc.domui.component.tbl.ITableModel;
import to.etc.domui.component.tbl.ITableModelListener;
import to.etc.domui.component.tbl.SimpleListModel;
import to.etc.domui.dom.css.VerticalAlignType;
import to.etc.domui.dom.errors.UIMessage;
import to.etc.domui.dom.html.IClicked;
import to.etc.domui.dom.html.NodeContainer;
import to.etc.domui.dom.html.TBody;
import to.etc.domui.dom.html.TD;
import to.etc.domui.login.IUser;
import to.etc.domui.state.UIContext;
import to.etc.domui.state.UIGoto;
import to.etc.webapp.nls.BundleRef;
import to.etc.webapp.query.QDataContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PlayListEditPage extends BasicTourPage {

	private static final int MAX_RIDERS_IN_PLAYLIST = 25;

	private PlayList m_playList;

	private OrderedRidersComponent<PlayListEntry> m_ridersComponent;

	private ModelBindings m_mb;

	private SimpleListModel<PlayListEntry> m_playListModel;

	private SimpleListModel<Rider> m_ridersModel;

	private static final BundleRef BUNDLE = BundleRef.create(PlayListEditPage.class, "messages");

	private List<PlayListEntry> m_initialPlayListEntries;

	/**
	 * TODO @UIUrlParameter in BasicEditPage toevoegen
	 * Het scherm moet scrollbaar zijn - container pagina moet worden aangepast
	 *
	 * @see to.etc.domui.dom.html.NodeBase#createContent()
	 */

	@UIUrlParameter(name = "id", mandatory = true)
	@NonNull
	public PlayList getPlayList() {
		PlayList pl = m_playList;
		if(pl == null)
			throw new IllegalStateException("The PlayList is empty at this point and that shouldn't be!");
		return pl;
	}

	public void setPlayList(PlayList playList) {
		m_playList = playList;
	}

	@Override
	public void createContent() throws Exception {
		TourUser usr = (TourUser) UIContext.getCurrentUser();
		if(usr != null && getPlayList().getId() != null && !usr.hasRight(ApplicationRight.ADMIN)) {
			if(!Objects.requireNonNull(usr.getPerson().getId()).equals(getPlayList().getPerson().getId())) {
				throw new IllegalStateException("The page was accessed illegally (or at least without a person_id)");
			}
		}

		final QDataContext dc = getSharedContext();
		if(getPlayList().getId() == null) {
			Long personId = getPage().getPageParameters().getLongW("person_id");
			if(personId == null)
				throw new IllegalStateException("The page was accessed illegally (or at least without a person_id)");
			Person person = dc.find(Person.class, personId);
			getPlayList().setPerson(person);
			getPlayList().setEdition(EditionBP.getCurrentEdition(dc));
		}

		final List<PlayListEntry> plel = getPlayList().getPlayListEntries();
		m_initialPlayListEntries = getPlayList().getPlayListEntries();

		createButtonBar(dc, plel);
		createEditFields();

		add(new VerticalSpacer(25));

		TBody tBody = addTable();
		TD leftCell = tBody.addCell();
		leftCell.setVerticalAlign(VerticalAlignType.TOP);
		TD rightCell = tBody.addCell();
		rightCell.setVerticalAlign(VerticalAlignType.TOP);

		leftCell.add(new CaptionedHeader("Selecteer team"));
		rightCell.add(new CaptionedHeader("Gekozen spelerslijst"));

		createTeamTree(leftCell, plel);
		createPlayListResult(rightCell, plel);

		m_ridersModel.addChangeListener(new ITableModelListener<Rider>() {
			@Override
			public void rowModified(@NonNull ITableModel<Rider> model, int index, @NonNull Rider value) throws Exception {}

			@Override
			public void rowDeleted(@NonNull ITableModel<Rider> model, int index, @NonNull Rider value) throws Exception {
				deletePlaylistEntry(value);
			}

			@Override
			public void rowAdded(@NonNull ITableModel<Rider> model, int index, @NonNull Rider value) throws Exception {
				addPlaylistEntry(value);
			}

			@Override
			public void modelChanged(@Nullable ITableModel<Rider> model) {}
		});

		m_playListModel.addChangeListener(new ITableModelListener<PlayListEntry>() {

			@Override
			public void rowModified(@NonNull ITableModel<PlayListEntry> model, int index, @NonNull PlayListEntry value) throws Exception {}

			@Override
			public void rowDeleted(@NonNull ITableModel<PlayListEntry> model, int index, @NonNull PlayListEntry value) throws Exception {
				m_ridersModel.delete(Objects.requireNonNull(value.getRider()));
			}

			@Override
			public void rowAdded(@NonNull ITableModel<PlayListEntry> model, int index, @NonNull PlayListEntry value) throws Exception {
				m_ridersModel.add(Objects.requireNonNull(value.getRider()));
			}

			@Override
			public void modelChanged(ITableModel<PlayListEntry> model) {}
		});

	}

	private void addPlaylistEntry(@NonNull Rider value) throws Exception {
		if(findRiderInPlayList(value) != null) {
			return;
		}
		PlayListEntry pe = new PlayListEntry();
		pe.setRider(value);
		pe.setPlayList(getPlayList());
		m_ridersComponent.addOrderedRider(pe);
	}

	private void deletePlaylistEntry(@NonNull Rider value) throws Exception {
		PlayListEntry pe = findRiderInPlayList(value);
		if(null != pe)
			m_ridersComponent.removeRider(pe);
	}

	private void createPlayListResult(NodeContainer right, final List<PlayListEntry> plel) throws Exception {
		Collections.sort(plel, new Comparator<PlayListEntry>() {
			@Override
			public int compare(PlayListEntry o1, PlayListEntry o2) {
				int a = o1.getPlace();
				int b = o2.getPlace();
				return (a < b ? 0 : 1);
			}
		});

		m_playListModel = new SimpleListModel<PlayListEntry>(plel);

		m_ridersComponent = new OrderedRidersComponent<PlayListEntry>(m_playListModel, MAX_RIDERS_IN_PLAYLIST);
		right.add(m_ridersComponent);

//		m_ridersComponent.addListener(new IModelChangedListener<PlayListEntry>() {
//
//			@Override
//			public void onValueRemoved(@NonNull PlayListEntry removedEntry) throws Exception {
//				Rider rider = removedEntry.getRider();
//				if(rider == null)
//					throw new IllegalStateException("Dat kan helemaal niet!?");
//				m_ridersModel.delete(rider);
//			}
//
//			@Override
//			public void onValueAdded(@NonNull PlayListEntry addedEntry) {
//				Rider rider = addedEntry.getRider();
//				if(rider == null)
//					throw new IllegalStateException("Dat kan helemaal niet!?");
//			}
//		});

		right.add(new CaptionedHeader("Zoek renner en voeg toe"));

		final RiderLookupInput<PlayListEntry> lookupRider = new RiderLookupInput<PlayListEntry>();

		lookupRider.setSelectedRiders(plel);
		lookupRider.setAdded(new ICellClicked<Rider>() {
			@Override
			public void cellClicked(Rider rowval) throws Exception {
				if(rowval != null) {
					addPlaylistEntry(rowval);
				}
			}
		});
		right.add(lookupRider);
	}

	private void createTeamTree(@NonNull NodeContainer left, @NonNull final List<PlayListEntry> plel) throws Exception {
		List<Rider> riderList = new ArrayList<Rider>();
		for(PlayListEntry pe : plel) {
			riderList.add(pe.getRider());
		}

		m_ridersModel = new SimpleListModel<Rider>(riderList);
		left.add(new RiderByTeamComponent(m_ridersModel));
	}

	private void createEditFields() throws Exception {
		TabularFormBuilder tfb = new TabularFormBuilder(getPlayList());
		tfb.addProp(PlayList.pLISTNAME);
		tfb.addProp(PlayList.pPLAYLISTTYPE);
		tfb.addProp(PlayList.pPAID, isAdminUser());
		m_mb = tfb.getBindings();
		m_mb.moveModelToControl();
		add(tfb.finish());
	}

	private void createButtonBar(@NonNull final QDataContext dc, @NonNull final List<PlayListEntry> plel) {
		final ButtonBar bb = new ButtonBar();
		add(bb);
		bb.addButton($("save"), new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				m_mb.moveControlToModel();

				dc.save(getPlayList());
				//FIXME Saving the new list doesn't work properly
				for(IOrderedRiders iOrderedRider : plel) {
					dc.save(iOrderedRider);
				}

				dc.commit();
				setMessage(UIMessage.info(BUNDLE, "global.saved"));
			}
		});
		bb.addBackButton();
		bb.addButton($("button.delete"), "THEME/btnDelete.png", new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				MsgBox.continueCancel(bb, $("button.delete.playlist.confirmation.message"), new IClicked<MsgBox>() {
					@Override
					public void clicked(MsgBox clickednode1) throws Exception {
						for(PlayListEntry ple : plel) {
							dc.delete(ple);
						}
						dc.delete(getPlayList());
						dc.commit();
						UIGoto.back();
					}
				});
			}
		});
	}

	/**
	 * @return
	 */
	private boolean isAdminUser() {
		IUser usr = UIContext.getCurrentUser();
		if(usr != null && usr.hasRight(ApplicationRight.ADMIN.name())) {
			return true;
		}
		return false;
	}

	@Nullable
	private PlayListEntry findRiderInPlayList(@NonNull Rider r) throws Exception {
		for(int i = m_playListModel.getRows(); --i >= 0;) {
			PlayListEntry ple = m_playListModel.getItem(i);
			if(r.equals(ple.getRider()))
				return ple;
		}
		return null;
	}

}
