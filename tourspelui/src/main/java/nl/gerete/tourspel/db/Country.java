package nl.gerete.tourspel.db;

import to.etc.domui.component.meta.*;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "name", defaultSortable = SortableType.SORTABLE_ASC)})
@SequenceGenerator(name = "sq", sequenceName = "country_id_seq")
public class Country extends TourspelEntity {

	private Long m_id;

	private String m_name;

	public static final String pNAME = "name";

	private String m_shortName;

	public static final String pSHORTNAME = "shortName";


	@Override
	@Id
	@Column(nullable = false, name = "country_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(length = 120, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	/**
	 * The 2-letter country code in lowercase according to http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
	 * @return String
	 */
	@Column(length = 5, nullable = false, unique = true)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public String getShortName() {
		return m_shortName;
	}

	public void setShortName(String shortName) {
		m_shortName = shortName.toLowerCase();
	}

}
