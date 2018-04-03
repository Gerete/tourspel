package nl.gerete.tourspel.componenten;

public final class ChatManager {

	private static String m_berichtVanRob;

	public static String getMessage() {
		return m_berichtVanRob;
	}

	public static void setMessage(String message) {
		m_berichtVanRob = message;
	}
}
