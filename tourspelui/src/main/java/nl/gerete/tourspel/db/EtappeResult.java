package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A single result of an etappe.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 3, 2012
 */
@Entity
@Table(name = "etappe_results")
//@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "year", defaultSortable = SortableType.SORTABLE_ASC)})
@SequenceGenerator(name = "sq", sequenceName = "etapperesults_id_seq")
public class EtappeResult extends TourspelEntity implements Comparable<EtappeResult>, IOrderedRiders {
	private Long	m_id;

	@Nullable
	private Etappe	m_etappe;

	public final static String pETAPPE = "etappe";

	private int		m_place;

	public final static String pPLACE = "place";

	private Rider m_rider;

	public final static String pRIDER = "rider";

	@Override
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	@Id
	@Column(name = "etapperesult_id", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "er_etappe_id")
	@Nullable
	public Etappe getEtappe() {
		return m_etappe;
	}

	public void setEtappe(@NonNull Etappe etappe) {
		m_etappe = etappe;
	}

	@Override
	@Column(name = "et_place", precision = 2, nullable = false, scale = 0)
	public int getPlace() {
		return m_place;
	}

	@Override
	public void setPlace(int place) {
		m_place = place;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "er_rider_id")
	@NonNull
	public Rider getRider() {
		return m_rider;
	}

	@Override
	public void setRider(@NonNull Rider rider) {
		m_rider = rider;
	}

	@Override
	public int compareTo(EtappeResult o) {
		return getPlace() - o.getPlace();
	}
}
