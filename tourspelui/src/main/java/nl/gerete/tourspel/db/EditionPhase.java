package nl.gerete.tourspel.db;

public enum EditionPhase {
	/* The edition is being defined for the coming year; user editing is forbidden */
	FUTURE

	/* The edition is open for entry (before the tour has started) */
	, OPEN

	/* The tour has started */
	, RUNNING

	/* The tour has ended */
	, HISTORIC
}
