package nl.gerete.tourspel.db;

import to.etc.domui.component.meta.*;

import javax.persistence.*;
import java.util.*;

/**
 * A rider that stops his tour is registered in here (ETAP US-003).
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 25, 2012
 */
@Entity
@Table(name = "stopped_rider")
@SequenceGenerator(name = "sq", sequenceName = "stopped_rider_id_seq")
@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "rider", displayLength = 20) //
	, @MetaDisplayProperty(name = "stopDate", displayLength = 5) //
	, @MetaDisplayProperty(name = "rider.team.country.shortName", displayLength = 2) //
})
public class StoppedRider extends TourspelEntity {
	private Long m_id;

	public static final String pRIDER = "rider";

	private Rider m_rider;

	public static final String pEDITION = "edition";

	private Edition m_edition;

	public static final String pETAPPE = "etappe";

	private Etappe m_etappe;

	public static final String pSTOPDATE = "stopDate";

	private Date m_stopDate;

	@Override
	@Id
	@Column(name = "stopped_rider_id", nullable = false, precision = 16)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Rider getRider() {
		return m_rider;
	}

	public void setRider(Rider rider) {
		m_rider = rider;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Edition getEdition() {
		return m_edition;
	}

	public void setEdition(Edition edition) {
		m_edition = edition;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Etappe getEtappe() {
		return m_etappe;
	}

	public void setEtappe(Etappe etappe) {
		m_etappe = etappe;
	}

	@Column(name = "stopDate", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getStopDate() {
		return m_stopDate;
	}

	public void setStopDate(Date stopDate) {
		m_stopDate = stopDate;
	}
}
