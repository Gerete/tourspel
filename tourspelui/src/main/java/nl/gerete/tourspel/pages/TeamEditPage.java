package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import to.etc.domui.annotations.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.webapp.nls.*;

@UIRights(value = "ADMIN")
public class TeamEditPage extends BasicEditPage<Team> {

	public TeamEditPage() {
		super(Team.class, "name", "teamCaptainName", "country");
	}

	@Override
	public void createContent() throws Exception {
		super.createContent();

		TeamRiderEditFragment fragment = new TeamRiderEditFragment(getObject());
		add(fragment);
		addSavableFragment(fragment);
	}

	@Override
	public DefaultButton getAdditionalButton() {
		DefaultButton button = new DefaultButton("Opslaan en nieuw", new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				save();
				UIGoto.replace(getPage(), TeamEditPage.class, new PageParameters("id", "NEW"), UIMessage.info(BundleRef.create(BasicEditPage.class, "messages"), "global.saved"));
			}
		});
		return button;
	}

	@Override
	protected void initializeNewInstance(Team instance) throws Exception {
		//-- Set the team's edition.
		instance.setEdition(EditionBP.getCurrentEdition(getSharedContext()));
	}
}
