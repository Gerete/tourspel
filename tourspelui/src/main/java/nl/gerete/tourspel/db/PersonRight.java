package nl.gerete.tourspel.db;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "personrights")
@SequenceGenerator(name = "sq", sequenceName = "personrights_id_seq")
public class PersonRight extends TourspelEntity {
	private Long m_id;

	private Person m_person;

	private ApplicationRight m_right;

	private Date m_startDate;

	private Date m_endDate;

	@Override
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}


	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return m_person;
	}

	public void setPerson(Person person) {
		m_person = person;
	}

	@Column(name = "application_right", length = 64, nullable = false)
	@Enumerated(EnumType.STRING)
	public ApplicationRight getRight() {
		return m_right;
	}

	public void setRight(ApplicationRight right) {
		m_right = right;
	}

	/**
	 * @return the startDate
	 */
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return m_startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		m_startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return m_endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		m_endDate = endDate;
	}
}


