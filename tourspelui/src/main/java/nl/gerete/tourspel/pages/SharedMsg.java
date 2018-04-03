package nl.gerete.tourspel.pages;

import to.etc.webapp.nls.*;

public interface SharedMsg {
	public static final BundleRef BUNDLE = BundleRef.create(SharedMsg.class, "sharedmsg");

	public static final String BUNDLE_KEY = "sharedmsg";

	//Tour general
	public static final String PASSWORD_INCORRECT = "password.incorrect";

	public static final String PASSWORD_NOT_SAME = "password.not.same";

	public static final String PASSWORD_NOT_SAME_OLD = "password.not.same.old";
}
