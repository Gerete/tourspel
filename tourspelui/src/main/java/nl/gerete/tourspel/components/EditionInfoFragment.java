package nl.gerete.tourspel.components;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import org.eclipse.jdt.annotation.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.component2.form4.*;
import to.etc.domui.dom.html.*;

public class EditionInfoFragment extends Div {
	@NonNull
	private Edition m_edition;

	public EditionInfoFragment(@NonNull Edition edition) {
		m_edition = edition;
	}

	@NonNull
	public Edition getEdition() {
		return m_edition;
	}

	@Override
	public void createContent() throws Exception {
		//-- Show the current edition.
		FormBuilder fb = new FormBuilder(this);
		fb.property(m_edition, Edition_.year()).control();
		fb.property(m_edition, Edition_.startDate()).control();
		fb.property(m_edition, Edition_.phase()).control();

		Etappe et = EditionBP.getNextEtappe(getEdition(), Application.getNow());
		if(et == null) {
			fb.label("Huidige/Volgende etappe").control(new DisplayValue<String>("Onbekend"));
		} else {
			fb.label("Huidige/Volgende etappe").control(new DisplayValue<String>(et.getStage() + ": " + et.getStart() + " > " + et.getEnd()));
			fb.property(m_edition, Edition_.startDate()).control();
		}
	}
}
