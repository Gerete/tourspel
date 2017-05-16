package nl.gerete.tourspel.db;

import org.hibernate.annotations.*;
import to.etc.domui.component.meta.*;

import javax.annotation.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@MetaObject(defaultColumns = {@MetaDisplayProperty(name = "listName", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 75),
	@MetaDisplayProperty(name = "paid", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 10),
	@MetaDisplayProperty(name = "playListType", defaultSortable = SortableType.SORTABLE_ASC, displayLength = 40)})
@Entity
@Table(name = "playlist")
@SequenceGenerator(name = "sq", sequenceName = "playlist_id_seq")
public class PlayList extends TourspelEntity {

	private Long m_id;

	public static final String pPERSON = "person";
	private Person m_person;

	public static final String pLISTNAME = "listName";
	private String m_listName;

	public static final String pPLAYLISTTYPE = "playListType";
	private PlayListType m_playListType;

	public static final String pPLAYLISTENTRIES = "playListEntries";
	private List<PlayListEntry> m_playListEntries = new ArrayList<>();

	public static final String pPAID = "paid";
	private boolean m_paid;

	private Edition m_edition;

	@Nonnull
	private List<PlayListResult> m_resultList = new ArrayList<>();

	private Etappe m_lastScoredEtappe;

	private Etappe m_lastMailedEtappe;

	public static final String pCURRENTPOINTS = "currentPoints";

	private long m_currentPoints;

	public PlayList() {}

	@Override
	@Id
	@Column(name = "playlist_id", nullable = false, precision = 16)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return m_person;
	}

	public void setPerson(Person person) {
		m_person = person;
	}

	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Column(name = "pl_listname", length = 40, nullable = false)
	public String getListName() {
		return m_listName;
	}

	public void setListName(String name) {
		m_listName = name;
	}

	@Column(name = "pl_playlisttype", length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	public PlayListType getPlayListType() {
		return m_playListType;
	}

	public void setPlayListType(PlayListType playListType) {
		m_playListType = playListType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = PlayListEntry.pPLAYLIST)
	public List<PlayListEntry> getPlayListEntries() {
		return m_playListEntries;
	}

	public void setPlayListEntries(List<PlayListEntry> playListEntries) {
		m_playListEntries = playListEntries;
	}

	@MetaSearch(searchType = SearchPropertyType.BOTH)
	@Column(name = "pl_paid", length = 1, nullable = false)
	@Type(type = "to.etc.domui.hibernate.types.BooleanPrimitiveYNType")
	public boolean isPaid() {
		return m_paid;
	}

	public void setPaid(boolean paid) {
		m_paid = paid;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Edition getEdition() {
		return m_edition;
	}

	public void setEdition(@Nonnull Edition edition) {
		m_edition = edition;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Etappe getLastScoredEtappe() {
		return m_lastScoredEtappe;
	}

	public void setLastScoredEtappe(Etappe lastScoredEtappe) {
		m_lastScoredEtappe = lastScoredEtappe;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Etappe getLastMailedEtappe() {
		return m_lastMailedEtappe;
	}

	public void setLastMailedEtappe(Etappe lastScoredEtappe) {
		m_lastMailedEtappe = lastScoredEtappe;
	}

	@Column(precision = 10, nullable = false)
	public long getCurrentPoints() {
		return m_currentPoints;
	}

	public void setCurrentPoints(long currentPoints) {
		m_currentPoints = currentPoints;
	}

	@Nonnull
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "playList")
	public List<PlayListResult> getResultList() {
		return m_resultList;
	}

	public void setResultList(@Nonnull List<PlayListResult> resultList) {
		m_resultList = resultList;
	}

	@Nullable
	@Transient
	public PlayListEntry findRiderInPlayList(@Nonnull Rider r) {
		for(PlayListEntry ple : getPlayListEntries()) {
			if(r.equals(ple.getRider()))
				return ple;
		}
		return null;
	}


}
