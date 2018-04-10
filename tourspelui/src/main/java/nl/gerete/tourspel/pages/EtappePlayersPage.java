package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.adm.*;
import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.pages.adm.*;
import nl.gerete.tourspel.pages.etappe.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;

import java.util.*;

public class EtappePlayersPage extends BasicTourPage {

	@Override
	public void createContent() throws Exception {
		Table t = new Table();
		add(t);
		t.setHeight("auto");
		TBody tb = t.addBody();
		TR tr = tb.addRow();
		TD etappeResult = tr.addCell();
		etappeResult.setCssClass("v-top portal-side");

		Etappe etappe = EditionBP.getLastFinishedEtappe(getSharedContext());
		EtappeResultFragment erf = new EtappeResultFragment(etappe);
		etappeResult.add(erf);

		TD portal = tr.addCell();
		PersonsDetailsFragment pdf = new PersonsDetailsFragment();
		portal.add(pdf);
		portal.setCssClass("v-top");

		TourUser usr = (TourUser) UIContext.getCurrentUser();
		if(usr == null)
			throw new IllegalStateException("The TourUser should not be null here!");
		//FIXME Waarom voer je hier een query uit als je al usr.getPerson() hebt?
		Person p = getSharedContext().get(Person.class, Objects.requireNonNull(usr.getPerson().getId()));
		List<PlayListFragment> playListGroup = new ArrayList<PlayListFragment>();

		for(PlayList pl : p.getPlayListList()) {
			PlayListFragment plf = new PlayListFragment(pl, playListGroup);
			portal.add(plf);
			portal.add(new BR());
		}

		TD playListResult = tr.addCell();
		playListResult.setCssClass("v-top portal-side");
		PlayListResultFragment topResultFragment = new PlayListResultFragment(PlayListType.KLASSEMENT, 10);
		playListResult.add(topResultFragment);
		playListResult.add(new BR());
		topResultFragment.setClicked(new IClicked<PlayListResultFragment>() {

			@Override
			public void clicked(PlayListResultFragment clickednode) throws Exception {
				PlayListResult plr = new PlayListResult(PlayListType.KLASSEMENT);
				add(plr);
			}
		});

		PlayListResultFragment loosersResultFragment = new PlayListResultFragment(PlayListType.POEDEL, 4);
		playListResult.add(loosersResultFragment);
		playListResult.add(new BR());
		loosersResultFragment.setClicked(new IClicked<PlayListResultFragment>() {

			@Override
			public void clicked(PlayListResultFragment clickednode) throws Exception {
				PlayListResult plr = new PlayListResult(PlayListType.POEDEL);
				add(plr);
			}
		});
		StoppedRidersFragment stoppedRidersFragment = new StoppedRidersFragment(10);
		playListResult.add(stoppedRidersFragment);


	}
}
