package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.Edition;
import nl.gerete.tourspel.db.Etappe;
import nl.gerete.tourspel.logic.EditionBP;
import org.eclipse.jdt.annotation.Nullable;

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
