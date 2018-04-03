package nl.gerete.tourspel.pages.adm;

import nl.gerete.tourspel.components.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.*;
import to.etc.domui.component.buttons.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.util.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

/**
 * This handles the phase transitions et al for the current edition.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 3, 2012
 */
public class AdminEditionPage extends BasicTourPage {
	@Override
	public void createContent() throws Exception {
		if(!checkEdition())
			return;

		//-- Show the current edition.
		getTourFrame().add(new EditionInfoFragment(getEdition()));

		ButtonBar bb = new ButtonBar();
		getTourFrame().add(bb);

		switch(getEdition().getPhase()){
			default:
				getTourFrame().add(new InfoPanel("Phase " + getEdition().getPhase() + " nog niet gebouwd"));
				break;

			case FUTURE:
				bb.addConfirmedButton("Open Editie", "Openen van de editie betekent dat deelnemers kunnen beginnen met invullen van hun lijsten... Weet je het zeker?", new IClicked<DefaultButton>() {
					@Override
					public void clicked(DefaultButton clickednode) throws Exception {
						try {
							EditionBP.open(getSharedContext(), getEdition());
							getSharedContext().commit();
							UIGoto.clearPageAndReload(getPage(), "Editie geopend");
						} catch(Exception x) {
							MsgBox.error(AdminEditionPage.this, "Openen editie mislukt: " + x);
							x.printStackTrace();
						}
					}
				});
				break;

			case OPEN:
				bb.addConfirmedButton("Start tour", "Starten van de tour betekent dat inschrijvingen gesloten worden en de eerste etappe actief wordt. Weet je het zeker?", new IClicked<DefaultButton>() {
					@Override
					public void clicked(DefaultButton clickednode) throws Exception {
						try {
							EditionBP.startTour(getSharedContext(), getEdition());
							getSharedContext().commit();
								UIGoto.clearPageAndReload(getPage(), "De tour " + getEdition().getYear() + " is gestart!");
						} catch(Exception x) {
								MsgBox.error(AdminEditionPage.this, "Starten van de tour mislukt: " + x);
							x.printStackTrace();
						}
					}
				});
				break;

		}
		showEditionButtons(bb, getEdition().getPhase());


	}

	private void showEditionButtons(@Nonnull ButtonBar bb, @Nonnull EditionPhase phase) throws Exception {
		switch(phase){
			default:
				break;
			case FUTURE:
				break;
			case OPEN:
				bb.addConfirmedButton("Mail niet-betaalde spelers", "images/mail.png", "Wil je mail versturen naar alle spelers die onbetaalde lijsten hebben ingevuld.",
					new IClicked<DefaultButton>() {

					@Override
					public void clicked(DefaultButton clickednode) throws Exception {
							ISystemTaskProvider provider = PlayerNotPaidProvider.INSTANCE;
							ISystemTask task = provider.getTask();
							if(task == null) {
								return;
							}
							Progress p = new Progress(task.getName());
							System.out.println("De taak " + task.getName() + " wordt nu gestart.....");
							task.execute(p);
					}
				});
				add(new VerticalSpacer(20));
				QCriteria<PlayList> crit = QCriteria.create(PlayList.class);
				crit.eq(PlayList.pPAID, Boolean.FALSE);
				List<PlayList> list = getSharedContext().query(crit);
				if(list.isEmpty()) {
					add(new DisplayValue<String>("Alle spelers hebben betaald"));
				} else {
					add(new DisplayValue<String>("Er zijn " + list.size() + " spelerslijsten nog niet betaald"));
				}
				break;
		}
		bb.addButton("Etappes bewerken", new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				UIGoto.moveNew(EtappeListPage.class);
			}

		});

	}
}
