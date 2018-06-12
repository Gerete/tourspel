package nl.gerete.tourspel.pages.newpages;

import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.state.*;

import javax.annotation.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
@DefaultNonNull
public class BasicEditPage<T> extends BasicTourPage {

	final static String ID_PARAM = "ID";

	@Nullable
	private T m_entity;

	public BasicEditPage() {}

	@UIUrlParameter(name = ID_PARAM)
	public T getEntity() {
		T entity = m_entity;
		if(null == entity)
			throw new IllegalStateException("Missing entity");
		return entity;
	}

	public void setEntity(@Nullable T entity) {
		m_entity = entity;
	}

	@Override
	public void createContent() throws Exception {
		super.createContent();
		addButtons();
	}

	private void addButtons() {
		ButtonBar bb = new ButtonBar();
		DefaultButton saveButton = new DefaultButton("Save");
		bb.addButton(saveButton);
		bb.setClicked(clickednode -> save());
		add(bb);
		bb.addBackButton();  // A backbutton can only be added after you added the ButtonBar to the page.
	}

	void save() throws Exception {
		if (bindErrors()) {
			return;
		}

		getSharedContext().commit();
		UIGoto.back();
	}

	@Override
	protected void onShelve() {
		resetAllSharedContexts();
	}
}
