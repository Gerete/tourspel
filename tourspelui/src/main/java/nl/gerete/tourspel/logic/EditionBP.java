package nl.gerete.tourspel.logic;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.util.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;

/**
 * Edition-related logic.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 3, 2012
 */
public class EditionBP {
	@NonNull
	static public Edition createEdition(@NonNull QDataContext dc, int year) throws Exception {
		if(year < 2018 || year > 2300)
			throw new IllegalArgumentException(year + ": year must be between 2018 and 2300");
		Edition cur = findCurrentEdition(dc);
		if(null != cur)
			throw new IllegalStateException("Another edition (" + cur.getYear() + ") is still in phase " + cur.getPhase());
		cur = new Edition();
		cur.setYear(year);
		cur.setStartDate(DateUtil.dateFor(year, Calendar.JULY, 5, 16, 00, 00, 00));            //5 July 2014 is a real start date
		cur.setChangeRidersDeadline(DateUtil.dateFor(year, Calendar.JULY, 5, 12, 00, 00, 00));
		cur.setEndDate(DateUtil.dateFor(year, Calendar.JULY, 27));
		cur.setPayPriceMoneyDeadline(DateUtil.dateFor(year, Calendar.AUGUST, 21));
		cur.setPayRegistrationFeeDeadline(DateUtil.dateFor(year, Calendar.JULY, 1));
		cur.setRegistrationDeadline(DateUtil.dateFor(year, Calendar.JULY, 1));
		cur.setPhase(EditionPhase.FUTURE);
		dc.save(cur);
		return cur;
	}

	/**
	 * Get the current edition: the one that is in any other state than HISTORIC. If no edition is open it will create one
	 * for the "current" year, or the year after the last "closed" one.
	 * @return
	 * @throws Exception
	 */
	@NonNull
	static public Edition getCurrentEdition(@NonNull QDataContext dc) throws Exception {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		Edition edition = dc.queryOne(QCriteria.create(Edition.class).ne("phase", EditionPhase.HISTORIC).eq(Edition.pYEAR, Integer.valueOf(year)));
		if(null == edition) {
			edition = dc.queryOne(QCriteria.create(Edition.class).eq(Edition.pYEAR, Integer.valueOf(year)));
			if(edition != null) {
				//-- The tour for this year has finished... So open the year after.
				year = edition.getYear() + 1;
			}
			edition = createEdition(dc, year);
		}
		return edition;
	}

	static private Edition findCurrentEdition(@NonNull QDataContext dc) throws Exception {
		return dc.queryOne(QCriteria.create(Edition.class).ne("phase", EditionPhase.HISTORIC));
	}

	/**
	 * @return the number of days to the start of the Edition
	 * @throws Exception
	 */
	static public int getDaysToStartEdition(@NonNull QDataContext dc) throws Exception {
		return DateUtil.deltaInDays(Application.getNow(), getCurrentEdition(dc).getStartDate());
	}


	/**
	 * Open an edition. This checks whether all etappes and riders are present, then moves the edition state to OPEN. It
	 * throws an exception when some precondition is not met.
	 *
	 * @param edition
	 * @throws Exception
	 */
	public static void open(@NonNull QDataContext dc, @NonNull Edition edition) throws Exception {
		if(edition.getPhase() != EditionPhase.FUTURE)
			throw new RuntimeException("De editie is in de fase " + edition.getPhase());

		//-- There should be approx. 20 etappes
		List<Etappe> el = dc.query(QCriteria.create(Etappe.class).eq("edition", edition));
		if(el.size() < 20)
			throw new RuntimeException("Er zijn " + el.size() + " etappes opgevoerd, ik verwacht er 20");

		//-- FIXME Check teams and riders.


		//-- All worked: move to OPEN.
		edition.setPhase(EditionPhase.OPEN);
	}

	/**
	 * Make an edition running.
	 * @param sharedContext
	 * @param edition
	 */
	public static void startTour(QDataContext sharedContext, Edition edition) {
		if(edition.getPhase() != EditionPhase.OPEN)
			throw new RuntimeException("De editie is in de fase " + edition.getPhase());

		//-- FIXME What should we check?


		edition.setPhase(EditionPhase.RUNNING);
	}

	/**
	 * Get a sorted list of etappes.
	 * @param ed
	 * @return
	 */
	@NonNull
	public static List<Etappe> getEtappeList(Edition ed) {
		List<Etappe> res = new ArrayList<Etappe>(ed.getEtappeList());
		Collections.sort(res);
		return res;
	}

	@NonNull
	private static List<Etappe> getEditableEtappes(@NonNull Edition ed, @NonNull Date dt) {
		if(ed.getPhase() != EditionPhase.RUNNING)
			throw new IllegalStateException("Edition is not running but " + ed.getPhase());
		List<Etappe> el = getEtappeList(ed);
		for(int i = el.size(); --i >= 0;) {
			Etappe et = el.get(i);
			if(!isEditableEtappe(et, dt))
				el.remove(i);
		}
		return el;
	}

	static public Etappe getCurrentEtappe(QDataContext dc) throws Exception {
		List<Etappe> el = EditionBP.getEditableEtappes(getCurrentEdition(dc), Application.getNow());
		if(el.size() == 0)
			return null;
		return el.get(0);
	}

	/**
	 * Get the next etappe that will be run.
	 * @param ed
	 * @param dt
	 * @return
	 */
	@Nullable
	public static Etappe getNextEtappe(@NonNull Edition ed, @NonNull Date dt) {
		if(ed.getPhase() != EditionPhase.RUNNING)
			return null;
		List<Etappe> el = getEtappeList(ed);
		for(int i = 0; i < el.size(); i++) {
			Etappe et = el.get(i);
			if(!et.isCompleted())
				return et;
		}
		return null;
	}

	private static boolean isEditableEtappe(@NonNull Etappe et, @NonNull Date dt) {
		if(et.isCompleted())
			return false;
		if(Objects.requireNonNull(et.getDate()).getTime() > dt.getTime()) // Etappe is later than the date specified
			return false;
		return true;
	}

	public static List<EtappeResult> getEtappeResults(Etappe et) {
		List<EtappeResult> res = new ArrayList<EtappeResult>(et.getResultList());
		Collections.sort(res);
		return res;
	}

	public static Etappe getLastFinishedEtappe(QDataContext dc) throws Exception {
		List<Etappe> res = getEtappeList(getCurrentEdition(dc));
		for(int j = res.size() - 1; j >= 0; j--) {
			Etappe etappe = res.get(j);
			if(etappe.getPhase().equals(EtappePhase.CLOSED)) {
				return etappe;
			}
		}

		return null;
	}
}

