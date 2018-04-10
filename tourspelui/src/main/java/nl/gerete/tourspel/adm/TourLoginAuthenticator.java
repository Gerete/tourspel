package nl.gerete.tourspel.adm;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.login.*;
import to.etc.util.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class TourLoginAuthenticator implements ILoginAuthenticator {

	@Override
	public IUser authenticateUser(String userid, String credentials) throws Exception {
		try(QDataContext dc = QContextManager.createUnmanagedContext()) {
			QCriteria<Person> crits = QCriteria.create(Person.class).eq("email", userid).eq("password", credentials);
			Person p = dc.queryOne(crits);
			if(p == null)
				return null;
			else {
				return createUserInstance(p);
			}
		}
	}

	private IUser createUserInstance(@Nonnull Person p) {
		Set<ApplicationRight> set = new HashSet<ApplicationRight>();
		long now = Application.getNow().getTime();
		for(PersonRight pr : p.getRightList()) {
			if(pr.getStartDate().getTime() < now) {
				if(pr.getEndDate() == null || pr.getEndDate().getTime() > now) {
					set.add(pr.getRight());
				}
			}
		}
		return new TourUser(p, set);
	}

	@Override
	public IUser authenticateByCookie(String userid, long ts, String hashcode) throws Exception {
		if(userid == null || userid.length() == 0)
			return null;

		try(QDataContext dc = QContextManager.createUnmanagedContext()) {
			QCriteria<Person> q = QCriteria.create(Person.class).eq(Person.pEMAIL, userid);
			Person p = dc.queryOne(q);
			if(p == null)
				return null;

			//-- Calculate a hash from password:userid
			String s = p.getPassword() + ":" + p.getEmail();
			byte[] data = s.getBytes("utf-8");
			data = FileTool.hashBuffers(new byte[][]{data});
			s = StringTool.toHex(data);
			if(!s.equals(hashcode))
				return null;
			return createUserInstance(p);
		}
	}

	@Override
	public String calcCookieHash(String userid, long ts) throws Exception {
		if(userid == null || userid.length() == 0)
			return null;

		try(QDataContext dc = QContextManager.createUnmanagedContext()) {
			QCriteria<Person> q = QCriteria.create(Person.class).eq(Person.pEMAIL, userid);
			Person p = dc.queryOne(q);
			if(p == null)
				return null;

			//-- Calculate a hash from password:userid
			String s = p.getPassword() + ":" + p.getEmail();
			byte[] data = s.getBytes("utf-8");
			data = FileTool.hashBuffers(new byte[][]{data});
			return StringTool.toHex(data);
		}
	}

}
