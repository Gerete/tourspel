package nl.gerete.tourspel.db;

import nl.gerete.tourspel.logic.*;
import to.etc.domui.component.meta.*;

import javax.persistence.*;
import java.util.*;

@to.etc.annotations.GenerateProperties @Entity
@Table(name = "rider")
@SequenceGenerator(name = "sq", sequenceName = "rider_id_seq")
@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "firstName", join = " "), @MetaDisplayProperty(name = "lastName")})
public class Rider extends TourspelEntity {
	private Long m_id;

	private String m_lastName;

	public static final String pLASTNAME = "lastName";

	private String m_firstName;

	public static final String pFIRSTNAME = "firstName";

	private String m_middleName;

	public static final String pMIDDLENAME = "middleName";

	private Country m_country;

	public static final String pCOUNTRY = "country";

	private Date m_dateOfBirth;

	public static final String pDATEOFBIRTH = "dateOfBirth";

	public static final String pTEAM = "team";

	private Team m_team;

	private Integer m_number;

	public static final String pNUMBER = "number";

	private Edition m_edition;

	public static final String pEDITION = "edition";

	public final static Comparator<Rider> C_BYNUMBER = (a, b) -> {
		Integer ia = a.getNumber();
		Integer ib = b.getNumber();
		if(ia == null && ib == null)
			return 0;
		if(ia != null && ib != null)
			return ia - ib;
		return ia != null ? 1 : -1;
	};


	public Rider() {
	}

	public Rider(Team team) {
		m_team = team;
		team.getRiders().add(this);
	}

	@Override
	@Id
	@Column(name = "rider_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(name = "last_name", length = 60)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getLastName() {
		return m_lastName;
	}

	public void setLastName(String lastName) {
		m_lastName = lastName;
	}

	@Column(name = "first_name", length = 25)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getFirstName() {
		return m_firstName;
	}

	public void setFirstName(String firstName) {
		m_firstName = firstName;
	}

	@Column(name = "middle_name", length = 10)
	public String getMiddleName() {
		return m_middleName;
	}

	public void setMiddleName(String middleName) {
		m_middleName = middleName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", nullable = true)
	public Country getCountry() {
		return m_country;
	}

	public void setCountry(Country country) {
		m_country = country;
	}

	@Column(name = "date_of_birth", nullable = true)
	@Temporal(TemporalType.DATE)
	@MetaProperty(validator = { SportsmenAgeValidator.class }, parameterizedValidator = {@MetaValueValidator( validator = SportmenAgeValidatorParams.class, parameters = {"-45", "-15"})})
	public Date getDateOfBirth() {
		return m_dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		m_dateOfBirth = dateOfBirth;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	public Team getTeam() {
		return m_team;
	}

	public void setTeam(Team team) {
		m_team = team;
	}

	@Column(name = "number", length = 3, nullable = false)
	@MetaSearch
	public Integer getNumber() {
		return m_number;
	}

	public void setNumber(Integer number) {
		m_number = number;
	}

	@Transient
	public String getDisplayName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFirstName());
		if(getMiddleName() != null) {
			sb.append(' ');
			sb.append(getMiddleName());
		}
		sb.append(' ');
		sb.append(getLastName());
		return sb.toString();
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Edition getEdition() {
		return m_edition;
	}

	public void setEdition(Edition edition) {
		m_edition = edition;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(getFirstName() != null)
			sb.append(getFirstName());
		if(getMiddleName() != null) {
			if(sb.length() > 0)
				sb.append(' ');
			sb.append(getMiddleName());
		}
		if(getLastName() != null) {
			if(sb.length() > 0)
				sb.append(' ');
			sb.append(getLastName());
		}
		return sb.toString();
	}
}
