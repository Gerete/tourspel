package nl.gerete.tourspel.components;


import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.component.ntbl.*;
import to.etc.domui.component.tbl.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.db.*;

/**
 * Component waarmee renners weergegeven worden met de mogelijkheid om deze
 * toe te voegen, te verwijderen en te sorteren.
 *
 *
 * @author <a href="mailto:ben.schoen@itris.nl">Ben Schoen</a>
 * Created on May 8, 2012
 */
@NonNullByDefault
public class OrderedRidersComponent<T extends IOrderedRiders> extends Div {

	private SimpleListModel<T> m_model;

	@Nullable
	private DataTable<T> m_table;

	private int m_maxRows;

	@NonNull
	private List<IModelChangedListener<T>> m_changeListeners = new ArrayList<>();

	public OrderedRidersComponent(SimpleListModel<T> model, int maxRows) throws Exception {
		m_model = model;
		m_maxRows = maxRows;
	}

	@NonNull
	public SimpleListModel<T> getModel() {
		SimpleListModel<T> model = m_model;
		if(null == model)
			throw new IllegalStateException("The model is not set");
		return model;
	}

	public void setModel(@NonNull SimpleListModel<T> model) {
		if(m_model == model)
			return;
		m_model = model;
		forceRebuild();
	}

	@Override
	public void createContent() throws Exception {
		reorder();

		INodeContentRenderer<Integer> placer = new INodeContentRenderer<Integer>() {
			@Override
			public void renderNodeContent(@NonNull NodeBase component, @NonNull NodeContainer node, @Nullable Integer object, @Nullable Object parameters) throws Exception {
				if(null == object)
					return;
				int pl = object;
				if(pl <= 10) {
					node.add(object.toString());
					return;
				}
				node.add("Reserve " + (pl - 10));
			}
		};

		BasicRowRenderer<IOrderedRiders> brr = new BasicRowRenderer<IOrderedRiders>(IOrderedRiders.class, // TODO Waarom kon ik deze IOrderedRiders.class niet in een Class<T> veranderen?
			"place", "^Plaats", "%5", placer //
			, "", new FlagRenderer<IOrderedRiders>(o -> Objects.requireNonNull(o.getRider()).getCountry()) //
			, "rider.displayName", "^Renner", "%30", BasicRowRenderer.NOWRAP //
			, "rider.team.name", "^Team", "%20", BasicRowRenderer.NOWRAP //
		);

		brr.setRowButtonFactory((IRowButtonFactory<IOrderedRiders>) new IRowButtonFactory<T>() {
			@Override
			public void addButtonsFor(RowButtonContainer c, final T data) throws Exception {
				c.addLinkButton("Verwijder", Icon.of("THEME/btnDelete.png"), new IClicked<LinkButton>() {
					@Override
					public void clicked(LinkButton clickednode) throws Exception {
						m_model.delete(data);
						fireRemoved(data);
					}
				});

				c.addLinkButton("Omhoog", Icon.of("images/btnUp.png"), new IClicked<LinkButton>() {
					@Override
					public void clicked(LinkButton clickednode) throws Exception {
						move(data, -1);
					}
				});

				c.addLinkButton("Omlaag", Icon.of("images/btnDown.png"), new IClicked<LinkButton>() {
					@Override
					public void clicked(LinkButton clickednode) throws Exception {
						move(data, 1);
					}
				});
			}
		});

		m_table = new DataTable<T>(m_model, (IRowRenderer<T>) brr);
		add(m_table);
	}

	private void move(IOrderedRiders data, int dir) throws Exception {
		int ix = getResultList().indexOf(data);
		if(ix < 0)
			return;
		int nix = ix + dir;
		if(nix < 0)
			return;
		if(nix >= getResultList().size())
			return;
		m_model.move(nix, ix);
		reorder();
	}

	@NonNull
	private List<T> getResultList() throws Exception {
		List<T> items = new ArrayList<T>();
		for(int i = m_model.getRows(); --i >= 0;) {
			items.add(m_model.getItem(i));
		}
		Collections.reverse(items);
		return items;
	}

	private void reorder() throws Exception {
		int ix = 1;
		for(T er : getResultList()) {
			if(er.getPlace() != ix) {
				er.setPlace(ix);
				m_model.modified(er);
			}
			ix++;
		}
	}

	public void addOrderedRider(T entry) throws Exception {
		if(m_model.getRows() >= m_maxRows) {
			MsgBox.error(this, "Er zijn al " + m_maxRows + " renners geselecteerd.");
			return;
		}
		entry.setPlace(m_model.getRows() + 1);
		m_model.add(entry);
		fireAdded(entry);
	}

	public void removeRider(T entry) throws Exception {
		m_model.delete(entry);
		fireRemoved(entry);
	}

	public void addListener(IModelChangedListener<T> vc) {
		if(m_changeListeners == Collections.EMPTY_LIST)
			m_changeListeners = new ArrayList<IModelChangedListener<T>>();
		m_changeListeners.add(vc);
	}

	public void removeListener(IModelChangedListener<T> vc) {
		if(m_changeListeners.size() > 0) {
			m_changeListeners.remove(vc);
		}
	}

	private void fireAdded(T addedEntry) throws Exception {
		for(IModelChangedListener<T> vc : m_changeListeners) {
			vc.onValueAdded(addedEntry);
		}
		forceRebuild();
	}

	private void fireRemoved(T removedEntry) throws Exception {
		for(IModelChangedListener<T> vc : m_changeListeners) {
			vc.onValueRemoved(removedEntry);
		}
		reorder();
		forceRebuild();
	}

}
