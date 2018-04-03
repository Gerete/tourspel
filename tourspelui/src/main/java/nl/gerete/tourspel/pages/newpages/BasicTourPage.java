package nl.gerete.tourspel.pages.newpages;

import to.etc.domui.dom.html.*;

/**
 * Basic page for all administrative pages of the tour game
 *
 * @author <a href="mailto:marc@gerete.nl">Marc Mol</a>
 * Created on Jun 2, 2017
 */
abstract class BasicTourPage extends UrlPage {

	void addHeader() {

		Div borderDiv = new Div();
		borderDiv.setCssClass("borderdiv");
		add(borderDiv);

		Div headerDiv = new Div();
		headerDiv.setCssClass("headerdiv");
		borderDiv.add(headerDiv);

		Img img = new Img();
		img.setSrc("images/logo-tour.png");
		headerDiv.add(img);

	}
}
