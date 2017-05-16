package nl.gerete.tourspel.db;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "addresses")
@SequenceGenerator(name = "sq", sequenceName = "address_id_seq")
public class Address {

	private Long m_id;

	private String m_street;

	private String m_housenr;

	private String m_housnrSuffix;

	private String m_city;

	private Region m_region;

	private String m_postcode;

	private Country m_country;

	private AddressType m_type;

	private Date m_startDate;

	private Date m_endDate;

	private Person m_person;

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
	public String getStreet() {
		return m_street;
	}

	public void setStreet(String street) {
		m_street = street;
	}

	@Column(length = 120, nullable = false)
	public String getHousenr() {
		return m_housenr;
	}

	public void setHousenr(String housenr) {
		m_housenr = housenr;
	}

	@Column(length = 120, nullable = false)
	public String getHousnrSuffix() {
		return m_housnrSuffix;
	}

	public void setHousnrSuffix(String housnrSuffix) {
		m_housnrSuffix = housnrSuffix;
	}

	@Column(length = 120, nullable = false)
	public String getCity() {
		return m_city;
	}

	public void setCity(String city) {
		m_city = city;
	}

	@ManyToOne
	public Region getRegion() {
		return m_region;
	}

	public void setRegion(Region region) {
		m_region = region;
	}

	@Column(length = 120, nullable = false)
	public String getPostcode() {
		return m_postcode;
	}

	public void setPostcode(String postcode) {
		m_postcode = postcode;
	}

	@ManyToOne
	public Country getCountry() {
		return m_country;
	}

	public void setCountry(Country country) {
		m_country = country;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public AddressType getType() {
		return m_type;
	}

	public void setType(AddressType type) {
		m_type = type;
	}

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return m_startDate;
	}

	public void setStartDate(Date startDate) {
		m_startDate = startDate;
	}

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return m_endDate;
	}

	public void setEndDate(Date endDate) {
		m_endDate = endDate;
	}

	@ManyToOne()
	public Person getPerson() {
		return m_person;
	}

	public void setPerson(Person person) {
		m_person = person;
	}

}

