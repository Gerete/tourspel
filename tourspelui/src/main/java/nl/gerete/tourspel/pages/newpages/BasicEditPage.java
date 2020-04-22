package nl.gerete.tourspel.pages.newpages;

import org.eclipse.jdt.annotation.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.state.*;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-6-18.
 */
@NonNullByDefault
public class BasicEditPage<T> extends BasicTourPage {

	final static String ID_PARAM = "ID";

	@Nullable
	protected T m_entity;

	public BasicEditPage() {}

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
