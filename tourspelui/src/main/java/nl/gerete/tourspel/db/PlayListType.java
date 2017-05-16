package nl.gerete.tourspel.db;

public enum PlayListType {

	KLASSEMENT("Klassement"), POEDEL("Poedel");

	private String m_value;

	PlayListType(String s) {
		m_value = s;
	}

	public String getValue() {
		return m_value;
	}
}
