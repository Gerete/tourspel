package nl.gerete.tourspel.adm;

import nl.gerete.tourspel.db.*;
import to.etc.domui.login.*;
import to.etc.domui.state.*;

import javax.annotation.*;
import java.util.*;

@DefaultNonNull
public class TourUser implements IUser {

	final private Person m_person;

	final private Set<ApplicationRight> m_rights;

	public TourUser(Person person, Set<ApplicationRight> rights) {
		m_person = person;
		m_rights = rights;
	}

	@Override
	public String getDisplayName() {
		return m_person.toString();
	}

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

	public Person getPerson() {
		return m_person;
	}

	static public TourUser getCurrent() {
		return (TourUser) UIContext.getLoggedInUser();
	}

	@Override
	public <T> boolean hasRight(String r, @Nullable T dataElement) {
		return false;
	}

}
