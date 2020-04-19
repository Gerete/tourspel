package nl.gerete.tourspel.componenten;

import org.eclipse.jdt.annotation.*;

@NonNullByDefault
public final class ChatManager {

	@Nullable
	private static String m_berichtVanRob;

	@Nullable
	static String getMessage() {
		return m_berichtVanRob;
	}

	public static void setMessage(String message) {
		m_berichtVanRob = message;
	}
}
