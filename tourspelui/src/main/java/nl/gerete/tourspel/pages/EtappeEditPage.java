package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.pages.playlist.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

@UIRights(value = "ADMIN")
public class EtappeEditPage extends BasicEditPage<Etappe> {


	public EtappeEditPage() {
		super(Etappe.class, Etappe.pEDITION, Etappe.pSTAGE, Etappe.pDATE, Etappe.pTYPE, Etappe.pSTART, Etappe.pEND, Etappe.pLENGTH);
	}


	@Override
	public DefaultButton getAdditionalButton() {

		DefaultButton etappeEditButton = new DefaultButton("Bewerk etappe", new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				UIGoto.moveSub(PlayListEditPage.class, "id", "NEW", "person_id", getPage().getPageParameters().getLongW("id"));
			}
		});
		return etappeEditButton;

	}
}
