package nl.gerete.tourspel.db;

import to.etc.domui.component.meta.*;

import javax.persistence.*;
import java.util.*;

@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "name", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 75),
	@MetaDisplayProperty(name = "teamCaptainName", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 75),
	@MetaDisplayProperty(name = "country.shortName", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 5),
	@MetaDisplayProperty(name = "country.name", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 40)})
@Entity
@Table(name = "team")
@SequenceGenerator(name = "sq", sequenceName = "team_id_seq")
public class Team extends TourspelEntity {
	private Long m_id;

	public static final String TEAM_ID = "team_id";

	private String m_name;

	public static final String pNAME = "name";

	private String m_teamCaptainName;

	public static final String pTEAMCAPTAINNAME = "teamCaptainName";

	private Country m_country;

	public static final String pCOUNTRY = "country";

	private List<Rider> m_riders = new ArrayList<>();

	public static final String pRIDERS = "riders";

	private Edition m_edition;

	public static final String pEDITION = "edition";

	@Override
	@Id
	@Column(nullable = false, name = TEAM_ID)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(name = "name", length = 40)
	@MetaSearch
	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	@Column(name = "team_captain_name", length = 40)
	@MetaSearch
	public String getTeamCaptainName() {
		return m_teamCaptainName;
	}

	public void setTeamCaptainName(String teamCaptainName) {
		m_teamCaptainName = teamCaptainName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	public Country getCountry() {
		return m_country;
	}

	public void setCountry(Country country) {
		m_country = country;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
	public List<Rider> getRiders() {
		return m_riders;
	}

	public void setRiders(List<Rider> riders) {
		m_riders = riders;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Edition getEdition() {
		return m_edition;
	}

	public void setEdition(Edition edition) {
		m_edition = edition;
	}
}
