package nl.gerete.tourspel.db;

import javax.persistence.*;

@Entity
@Table(name = "regions")
@SequenceGenerator(name = "sq", sequenceName = "region_id_seq")
public class Region {

	private Long m_id;

	private String m_region;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(length = 120, nullable = false)
	public String getRegion() {
		return m_region;
	}

	public void setRegion(String region) {
		m_region = region;
	}

}
