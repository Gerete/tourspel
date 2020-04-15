package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.component.meta.MetaDisplayProperty;
import to.etc.domui.component.meta.MetaObject;
import to.etc.domui.component.meta.MetaProperty;
import to.etc.domui.component.meta.MetaSearch;
import to.etc.domui.component.meta.NumericPresentation;
import to.etc.domui.component.meta.SearchPropertyType;
import to.etc.domui.component.meta.SortableType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * One "Edition" of the tour, meaning the tour for a given year.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 3, 2012
 */
@Entity
@Table(name = "editions")
@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "year", defaultSortable = SortableType.SORTABLE_ASC)})
@SequenceGenerator(name = "sq", sequenceName = "editions_id_seq")
public class Edition extends TourspelEntity {
	private Long m_id;

	public static final String pYEAR = "year";
	private int m_year;

	@NonNull
	private EditionPhase m_phase = EditionPhase.FUTURE;

	private List<Etappe> m_etappeList = new ArrayList<>();

	@Nullable
	private Date m_startDate;

	@Nullable
	private Date m_endDate;

	@Nullable
	private Date m_registrationDeadline;

	@Nullable
	private Date m_changeRidersDeadline;

	@Nullable
	private Date m_payRegistrationFeeDeadline;

	@Nullable
	private Date m_payPriceMoneyDeadline;

	@Override
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	@Id
	@Column(name = "edition_id", nullable = false, precision = 16)
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@Column(name = "ed_year", precision = 4, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@MetaProperty(numericPresentation = NumericPresentation.NUMBER_SCALED)
	public int getYear() {
		return m_year;
	}

	public void setYear(int year) {
		m_year = year;
	}

	@NonNull
	@Column(name = "ed_phase", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	public EditionPhase getPhase() {
		return m_phase;
	}

	public void setPhase(@NonNull EditionPhase phase) {
		m_phase = phase;
	}

	@OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
	public List<Etappe> getEtappeList() {
		return m_etappeList;
	}

	public void setEtappeList(List<Etappe> etappeList) {
		m_etappeList = etappeList;
	}

	@Column(name = "ed_startdate", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getStartDate() {
		return m_startDate;
	}

	@Transient
	public void setStartDate(@NonNull Date start) {
		m_startDate = start;
	}

	@Column(name = "ed_enddate", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getEndDate() {
		return m_endDate;
	}

	public void setEndDate(@NonNull Date endDate) {
		m_endDate = endDate;
	}

	@Column(name = "ed_registrationDeadline", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getRegistrationDeadline() {
		return m_registrationDeadline;
	}

	public void setRegistrationDeadline(@NonNull Date registrationDeadline) {
		m_registrationDeadline = registrationDeadline;
	}

	@Column(name = "ed_changedRidersDeadline", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getChangeRidersDeadline() {
		return m_changeRidersDeadline;
	}

	public void setChangeRidersDeadline(@NonNull Date changeRidersDeadline) {
		m_changeRidersDeadline = changeRidersDeadline;
	}

	@Column(name = "ed_payRegistrationFeeDeadline", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getPayRegistrationFeeDeadline() {
		return m_payRegistrationFeeDeadline;
	}

	public void setPayRegistrationFeeDeadline(@NonNull Date payRegistrationFeeDeadline) {
		m_payRegistrationFeeDeadline = payRegistrationFeeDeadline;
	}

	@Column(name = "ed_payPriceMoneyDeadline", nullable = false)
	@Temporal(TemporalType.DATE)
	@Nullable
	public Date getPayPriceMoneyDeadline() {
		return m_payPriceMoneyDeadline;
	}

	public void setPayPriceMoneyDeadline(@NonNull Date payPriceMoneyDeadline) {
		m_payPriceMoneyDeadline = payPriceMoneyDeadline;
	}
}
