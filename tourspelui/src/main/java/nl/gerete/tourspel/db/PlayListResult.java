package nl.gerete.tourspel.db;

import javax.persistence.*;

/**
 * A single result (points) for a playlist for a given Etappe and a given Rider in there.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 26, 2012
 */
@Entity
@Table(name = "playlist_result")
@SequenceGenerator(name = "sq", sequenceName = "playlist_result_id_seq")
public class PlayListResult extends TourspelEntity {
	private Long m_id;

	private PlayList m_playList;

	private Etappe m_etappe;

	private int m_score;

	@Override
	@Id
	@Column(name = "playresult_id", nullable = false, precision = 16)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq")
	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public PlayList getPlayList() {
		return m_playList;
	}

	public void setPlayList(PlayList playList) {
		m_playList = playList;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Etappe getEtappe() {
		return m_etappe;
	}

	public void setEtappe(Etappe etappe) {
		m_etappe = etappe;
	}

	@Column(precision = 6, nullable = false)
	public int getScore() {
		return m_score;
	}

	public void setScore(int score) {
		m_score = score;
	}
}
