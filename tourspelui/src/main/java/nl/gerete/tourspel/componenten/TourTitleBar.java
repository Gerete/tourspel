package nl.gerete.tourspel.componenten;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.layout.title.*;
import to.etc.domui.component.menu.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

import javax.annotation.*;

/**
 * The Tour title bar. This consists of an image in the left corner, a string describing the
 * module's functionality and a set of standard buttons.
 *
 */
@DefaultNonNull
public class TourTitleBar extends AppPageTitleBar {

	private boolean m_breadCrumb = true;

	@Nullable
	private IErrorFence m_errorFence;

	private ErrorMessageDiv m_errorThingy = new ErrorMessageDiv();

	public TourTitleBar() {
		super(true);
	}

	public TourTitleBar(String icon, String title) {
		super(icon, title, true);
	}

	public TourTitleBar(String title) {
		super(title, true);
	}

	private boolean isBreadCrumb() {
		if("true".equals(getPage().getPageParameters().getString("breadcrumb", null)))
			return true;

		return m_breadCrumb;
	}

	public void setBreadCrumb(boolean breadCrumb) {
		m_breadCrumb = breadCrumb;
	}

	@Override
	public void createContent() throws Exception {
		super.createContent();

		//VpErrorPanel is used as error handler
		m_errorThingy.setTestID("errorVpTitleBar");

		//fuzzy but cannot add div to table so had to change this, messages became narrow sometimes
		int cspan = calcColSpan(getBody());
		TD c = getBody().addRowAndCell();
		c.add(m_errorThingy);
		c.setColspan(cspan);

		if (isBreadCrumb()) {
			TD d = getBody().addRowAndCell();
			d.add(new BreadCrumb());
			d.setColspan(cspan);
		}

	}

	@Override
	public String getDefaultIcon() {
		return "/imgages/fietsbel.png";
		//			return Images.BTNMODULE;
	}

	/**
	 * Calculate the largest colspan from all rows.
	 * @param b
	 * @return
	 */
	static private int calcColSpan(TBody b) {
		int maxcol = 1;
		for(NodeBase nb : b) {
			TR row = (TR) nb;
			int thiscol = 0;
			for(NodeBase ntd : row) {
				TD td = (TD) ntd;
				if(td.getColspan() <= 1)
					thiscol++;
				else
					thiscol += td.getColspan();
			}
			if(thiscol > maxcol)
				maxcol = thiscol;
		}

		return maxcol;
	}

	/**
	 * Add the default Tour title bar buttons to the page.
	 * @see to.etc.domui.components.basic.AppPageTitle#addDefaultButtons(to.etc.domui.dom.html.NodeContainer)
	 */
	@Override
	protected void addDefaultButtons(@Nullable final NodeContainer nc) {
		if (null == nc) {
			return;
		}
		nc.add(new TourMenuBar());
	}

	private LinkButton adminsMenu() {
		final PopupMenu pm = new PopupMenu();
		//			if(role.equals(ApplicationRoleEnum.CLERK)) {
		pm.addItem("Etappe uitslag", "img/btnSmileySmiley.gif", null);
		//			if(role.equals(ApplicationRoleEnum.ADMIN)) {
		pm.addItem("------------", "img/btnSmileySmiley.gif", null);
		pm.addItem("Ploegen", "img/btnSmileySad.gif", null);
		pm.addItem("Renners", "img/btnSmileySad.gif", null);
		pm.addItem("Valideren inschrijflijsten", "img/btnSmileySad.gif", null);
		pm.addItem("Etcetera, etcetera ...", "img/btnSmileySad.gif", null);
		pm.addItem("Start inschrijving", "img/btnSmileySmiley.gif", null);
		pm.addItem("Start tourspel", "img/btnSmileySmiley.gif", null);

		LinkButton lb = new LinkButton("Clerk", (IClicked<LinkButton>) clickednode -> pm.show(clickednode, null));
		return lb;
	}

	private LinkButton playersMenu() {
		final PopupMenu pm = new PopupMenu();
		pm.addItem("Account gegevens", "img/btnSmileySmiley.gif", null);
		pm.addItem("Uitloggen", "img/btnSmileySad.gif", null);

		LinkButton lb = new LinkButton("Menu", (IClicked<LinkButton>) clickednode -> pm.show(clickednode, null));
		return lb;
	}


	/*--------------------------------------------------------------*/
	/*	CODING:	Error panel handling.								*/
	/*--------------------------------------------------------------*/

	/**
	 * When I'm added to a page register m_errorThingy as an error listener for that page.
	 * @see to.etc.domui.dom.html.NodeBase#onAddedToPage(to.etc.domui.dom.html.Page)
	 */
	@Override
	public void onAddedToPage(@Nullable Page p) {
		super.onAddedToPage(p);
		m_errorFence = DomUtil.getMessageFence(this);
		m_errorFence.addErrorListener(m_errorThingy);
	}

	/**
	 * When I'm removed from a page m_errorThingy may no longer handle it's errors, so remove
	 * m_errorThingy from the error listener chain.
	 *
	 * @see to.etc.domui.dom.html.NodeBase#onRemoveFromPage(to.etc.domui.dom.html.Page)
	 */
	@Override
	public void onRemoveFromPage(@Nullable Page p) {
		super.onRemoveFromPage(p);
		IErrorFence errorFence = m_errorFence;
		if (null == errorFence) {
			return;
		}
		errorFence.removeErrorListener(m_errorThingy);
	}
}
