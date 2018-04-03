package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;

import javax.annotation.*;

public class EtappeListPage extends BasicListPage<Etappe> {

	public EtappeListPage() {
		super(Etappe.class, EtappeEditPage.class);
	}

	@Override
	public void createContent() throws Exception {
		setEnableNewButton(true);
		super.createContent();
	}

	@Override
	@Nullable
	protected Edition restrictEdition() throws Exception {
		return EditionBP.getCurrentEdition(getSharedContext());
	}
}
