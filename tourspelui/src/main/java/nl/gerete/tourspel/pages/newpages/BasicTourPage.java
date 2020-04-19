package nl.gerete.tourspel.pages.newpages;

import to.etc.domui.component.layout.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.componenten.*;
import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;

/**
 * Basic page for all administrative pages of the tour game
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
public class BasicTourPage extends UrlPage {

	private Div m_contentDiv;

	public BasicTourPage() {}

	@Override
	public void createContent() throws Exception {

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

		m_contentDiv.add(new TourTitleBar());
		m_contentDiv.add(new VerticalSpacer(40));
		m_contentDiv.add(new ErrorMessageDiv(this));

		delegateTo(m_contentDiv);
	}
}
