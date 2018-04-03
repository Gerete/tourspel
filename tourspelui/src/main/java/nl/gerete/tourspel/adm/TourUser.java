package nl.gerete.tourspel.adm;

import nl.gerete.tourspel.db.*;
import to.etc.domui.login.*;
import to.etc.domui.state.*;

import javax.annotation.*;
import java.util.*;

public class TourUser implements IUser {
	@Nonnull
	final private Person m_person;

	@Nonnull
	final private Set<ApplicationRight> m_rights;

	public TourUser(@Nonnull Person person, @Nonnull Set<ApplicationRight> rights) {
		m_person = person;
		m_rights = rights;
	}

	@Nonnull
	@Override
	public String getDisplayName() {
		return m_person.toString();
	}

	@Nonnull
	@Override
	public String getLoginID() {
		return m_person.getEmail();
	}

	@Override
	public boolean hasRight(String r) {
		return m_rights.contains(ApplicationRight.valueOf(r));
	}

	public boolean hasRight(ApplicationRight e) {
		return hasRight(e.toString());
	}

	@Nonnull
	public Person getPerson() {
		return m_person;
	}

	@Nonnull
	static public TourUser getCurrent() {
		return (TourUser) UIContext.getLoggedInUser();
	}

	@Override
	public <T> boolean hasRight(String r, T dataElement) {
		return false;
	}

}
