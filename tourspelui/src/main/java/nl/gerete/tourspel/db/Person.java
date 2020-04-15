package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import to.etc.domui.component.meta.MetaDisplayProperty;
import to.etc.domui.component.meta.MetaObject;
import to.etc.domui.component.meta.MetaSearch;
import to.etc.domui.component.meta.SearchPropertyType;
import to.etc.domui.component.meta.SortableType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "lastName", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 20),
	@MetaDisplayProperty(name = "prefix", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 20),
	@MetaDisplayProperty(name = "firstName", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 50),
	@MetaDisplayProperty(name = "email", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 50),
	@MetaDisplayProperty(name = "phoneNumber", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 20)})
@Entity
@Table(name = "persons")
@SequenceGenerator(name = "sq", sequenceName = "person_id_seq")
public class Person extends TourspelEntity {
	private Long m_id;

	public final static String pLASTNAME = "lastName";
	private String m_lastName;

	public final static String pPREFIX = "prefix";
	private String m_prefix;

	public final static String pFIRSTNAME = "firstName";
	private String m_firstName;

	public final static String pEMAIL = "email";
	private String m_email;

	public final static String pPHONENUMBER = "phoneNumber";
	private String m_phoneNumber;

	public final static String pPASSWORD = "password";
	private String m_password;

	public final static String pPLAYLISTLIST = "playListList";

	private List<PlayList> m_playListList = new ArrayList<PlayList>();

	private List<PersonRight> m_rightList = new ArrayList<PersonRight>();

	@Override
	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(length = 16, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getLastName() {
		return m_lastName;
	}

	public void setLastName(String lastName) {
		m_lastName = lastName;
	}

	@Column(length = 120, nullable = true)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getFirstName() {
		return m_firstName;
	}

	public void setFirstName(String firstName) {
		m_firstName = firstName;
	}

	@Column(length = 120, nullable = false, unique = true)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getEmail() {
		return m_email;
	}

	public void setEmail(String email) {
		m_email = email;
	}

	@Column(length = 24, nullable = true)
	public String getPrefix() {
		return m_prefix;
	}

	public void setPrefix(String prefix) {
		m_prefix = prefix;
	}

	@Column(length = 16, nullable = false)
	public String getPassword() {
		return m_password;
	}

	public void setPassword(String password) {
		m_password = password;
	}

	@Column(length = 20, nullable = true)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getPhoneNumber() {
		return m_phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		m_phoneNumber = phoneNumber;
	}

	@OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
	@NonNull
	public List<PlayList> getPlayListList() throws Exception {
		return m_playListList;
	}

	public void setPlayListList(List<PlayList> playListList) {
		m_playListList = playListList;
	}

	/**
	 * Returns false if all playlists are paid. True if one or more playlists
	 * should be paid and null if no playlists are present.
	 * @return
	 */
	@Transient
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public Boolean isLatePayment() {

		if(m_playListList.isEmpty())
			return null;

		for(PlayList pl : m_playListList) {
			if(!pl.isPaid()) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	@NonNull
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	public List<PersonRight> getRightList() {
		return m_rightList;
	}

	public void setRightList(@NonNull List<PersonRight> rightList) {
		m_rightList = rightList;
	}

	@NonNull
	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}
