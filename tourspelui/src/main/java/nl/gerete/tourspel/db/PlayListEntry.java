package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.component.meta.MetaDisplayProperty;
import to.etc.domui.component.meta.SortableType;

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

@Entity
@Table(name = "playlistentry")
@MetaDisplayProperty(name = PlayListEntry.pPLACE, defaultSortable = SortableType.SORTABLE_ASC)
@SequenceGenerator(name = "sq", sequenceName = "playlistentry_id_seq")
public class PlayListEntry extends TourspelEntity implements IOrderedRiders, Comparable<PlayListEntry> {

	private Long m_id;

	public static final String pPLAYLIST = "playList";

	@Nullable
	private PlayList m_playList;

	public static final String pRIDER = "rider";

	@Nullable
	private Rider m_rider;

	public static final String pPLACE = "place";

	private int m_place;

	public PlayListEntry() {}

	public PlayListEntry(@NonNull PlayList playList, @NonNull Rider rider) {
		m_playList = playList;
		m_rider = rider;
	}

	public PlayListEntry(@NonNull PlayList playList, @NonNull Rider rider, int place) {
		super();
		m_playList = playList;
		m_rider = rider;
		m_place = place;
	}

	@Override
	@Id
	@Column(name = "playlistentry_id", nullable = false, precision = 16)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playlist_id", nullable = false)
	@Nullable
	public PlayList getPlayList() {
		return m_playList;
	}

	public void setPlayList(@NonNull PlayList playList) {
		m_playList = playList;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rider_id", nullable = false)
	@Nullable
	public Rider getRider() {
		return m_rider;
	}

	@Override
	public void setRider(@NonNull Rider rider) {
		m_rider = rider;
	}

	@Override
	@Column(name = "ple_place", nullable = false)
	public int getPlace() {
		return m_place;
	}

	@Override
	public void setPlace(int position) {
		m_place = position;
	}

	/**
	 * Natural order is by ascending position.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PlayListEntry o) {
		if(o == null)
			return 0;
		return getPlace() - o.getPlace();
	}


}
