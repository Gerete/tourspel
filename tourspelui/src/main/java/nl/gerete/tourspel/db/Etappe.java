package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.component.meta.MetaDisplayProperty;
import to.etc.domui.component.meta.MetaObject;
import to.etc.domui.component.meta.MetaSearch;
import to.etc.domui.component.meta.SearchPropertyType;
import to.etc.domui.component.meta.SortableType;
import to.etc.domui.converter.ConverterRegistry;
import to.etc.domui.converter.DateConverter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "etappes")
@MetaObject(defaultColumns = {@MetaDisplayProperty(name = Etappe.pSTAGE),
	@MetaDisplayProperty(name = Etappe.pDATE, defaultSortable = SortableType.SORTABLE_ASC),
	@MetaDisplayProperty(name = Etappe.pTYPE),
	@MetaDisplayProperty(name = Etappe.pSTART),
	@MetaDisplayProperty(name = Etappe.pEND),
	@MetaDisplayProperty(name = Etappe.pLENGTH)})
//@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "year", defaultSortable = SortableType.SORTABLE_ASC)})
@SequenceGenerator(name = "sq", sequenceName = "etappes_id_seq")
public class Etappe extends TourspelEntity implements Comparable<Etappe> {
	private Long m_id;

	private String m_stage;

	public final static String pSTAGE = "stage";

	private EtappeType m_type = EtappeType.Plain;

	public final static String pTYPE = "type";

	private Date m_date;

	public final static String pDATE = "date";

	private String m_start;

	public final static String pSTART = "start";

	private String m_end;

	public final static String pEND = "end";

	private double m_length;

	public final static String pLENGTH = "length";

	/** External details about the etappe */
	private String m_url;

	public final static String pURL = "url";

	private Edition m_edition;

	public final static String pEDITION = "edition";

	private EtappePhase m_phase = EtappePhase.OPEN;

	public final static String pPHASE = "phase";

	public final static String pRESULTLIST = "resultList";

	private List<EtappeResult> m_resultList = new ArrayList<>();

	@Override
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	@Id
	@Column(name = "etappe_id", nullable = false, precision = 16)
	public Long getId() {
		return m_id;
	}

	public void setId(@NonNull Long id) {
		m_id = id;
	}

	@Column(name = "et_stage", length = 5, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Nullable
	public String getStage() {
		return m_stage;
	}

	public void setStage(@NonNull String stage) {
		m_stage = stage;
	}

	@NonNull
	@Column(name = "et_type", length = 32, nullable = false)
	@Enumerated(EnumType.STRING)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	public EtappeType getType() {
		return m_type;
	}

	public void setType(@NonNull EtappeType type) {
		m_type = type;
	}

	@Column(name = "et_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Nullable
	public Date getDate() {
		return m_date;
	}

	public void setDate(@NonNull Date date) {
		m_date = date;
	}

	@Column(name = "et_start", length = 64, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Nullable
	public String getStart() {
		return m_start;
	}

	public void setStart(@NonNull String start) {
		m_start = start;
	}

	@Column(name = "et_end", length = 64, nullable = false)
	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Nullable
	public String getEnd() {
		return m_end;
	}

	public void setEnd(@NonNull String end) {
		m_end = end;
	}

	@Column(name = "et_length", precision = 6, scale = 1, nullable = false)
	public double getLength() {
		return m_length;
	}

	public void setLength(double length) {
		m_length = length;
	}

	@Nullable
	@Column(name = "et_url", length = 256, nullable = true)
	public String getUrl() {
		return m_url;
	}

	public void setUrl(@Nullable String url) {
		m_url = url;
	}

	@Nullable
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "et_edition_id")
	public Edition getEdition() {
		return m_edition;
	}

	public void setEdition(@NonNull Edition edition) {
		m_edition = edition;
	}

	@Override
	public int compareTo(Etappe o) {
		if(o.getEdition() != getEdition())
			return Objects.requireNonNull(getEdition()).getYear() - Objects.requireNonNull(o.getEdition()).getYear();
		return Objects.requireNonNull(getDate()).compareTo(Objects.requireNonNull(o.getDate()));
	}

	@NonNull
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "etappe")
	public List<EtappeResult> getResultList() {
		return m_resultList;
	}

	public void setResultList(@NonNull List<EtappeResult> resultList) {
		m_resultList = resultList;
	}

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	@NonNull
	public EtappePhase getPhase() {
		return m_phase;
	}

	public void setPhase(@NonNull EtappePhase phase) {
		m_phase = phase;
	}

	@Transient
	public boolean isCompleted() {
		return getPhase() == EtappePhase.CLOSED;
	}

	@Transient
	public String getDisplayName() {
		return toString();
	}

	@Override
	public String toString() {
		try {
			return getStart() + " -> " + getEnd() + " - " + ConverterRegistry.convertValueToString(DateConverter.class, getDate());
		} catch(Exception x) {
			return x.toString();
		}
	}
}
