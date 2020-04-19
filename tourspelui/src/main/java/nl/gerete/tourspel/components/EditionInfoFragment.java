package nl.gerete.tourspel.components;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.form.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;

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
		HorizontalFormBuilder fb = new HorizontalFormBuilder(getEdition());
		fb.addDisplayProp("year", "Editie jaar");
		fb.addDisplayProp("startDate", "Start");
		fb.addDisplayProp("phase", "Huidige fase");

		Etappe et = EditionBP.getNextEtappe(getEdition(), Application.getNow());
		if(et == null) {
			fb.addLabelAndControl("Huidige/Volgende etappe", new DisplayValue<String>("Onbekend"), false);
		} else {
			fb.addLabelAndControl("Huidige/Volgende etappe", new DisplayValue<String>(et.getStage() + ": " + et.getStart() + " > " + et.getEnd()), false);
			fb.setInstance(et);
			fb.addDisplayProp("date", "Etappe datum");
		}

		add(fb.finish());
		fb.getBindings().moveModelToControl();
	}
}
