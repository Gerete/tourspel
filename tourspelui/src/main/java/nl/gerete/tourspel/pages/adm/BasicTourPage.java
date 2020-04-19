package nl.gerete.tourspel.pages.adm;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.layout.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.domui.trouble.*;
import to.etc.domui.util.*;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;

public class BasicTourPage extends UrlPage {
	private Div m_contentDiv;

	@Nullable
	private Edition m_edition;

	@Override
	public void createFrame() {

		Div borderDiv = new Div();
		borderDiv.setCssClass("borderdiv");
		add(borderDiv);

		Div headerDiv = new Div();
		headerDiv.setCssClass("headerdiv");
		borderDiv.add(headerDiv);

		Img img = new Img();
		img.setSrc("images/logo-tour.png");
		headerDiv.add(img);

		if(TourUser.getCurrent().hasRight(ApplicationRight.ADMIN.name()))
			headerDiv.add(new TaskRunningIndicator());

		m_contentDiv = new Div();
		m_contentDiv.setCssClass("contentdiv");
		borderDiv.add(m_contentDiv);

		if(UIContext.getCurrentUser() != null)
			m_contentDiv.add(new TourMenuBar());

		m_contentDiv.add(new VerticalSpacer(40));

		m_contentDiv.add(new ErrorMessageDiv(this));
		delegateTo(m_contentDiv);

	}

	/**
	 * DO NOT USE: Just add, and remove this later.
	 * @return
	 */
	@Deprecated
	protected Div getTourFrame() {
		return m_contentDiv;
	}

	@NonNull
	public Edition getEdition() {
		Edition edition = m_edition;
		if(edition == null)
			throw new ValidationException(Msgs.VERBATIM, "No edition");
		return edition;
	}

	/**
	 * Get an edition in at least one of these phases.
	 * @param phases
	 * @throws Exception
	 */
	public boolean checkEdition(EditionPhase... phases) throws Exception {
		Edition ed = EditionBP.getCurrentEdition(getSharedContext());
		if(phases.length == 0) {
			m_edition = ed;
			return true;
		}

		for(EditionPhase ep : phases) {
			if(ed.getPhase() == ep) {
				m_edition = ed;
				return true;
			}
		}
		getTourFrame().add(new InfoPanel("De editie " + ed.getYear() + " is in de fase " + ed.getPhase()));
		return false;
	}

}
