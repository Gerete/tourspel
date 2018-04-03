package nl.gerete.tourspel.pages;

import nl.gerete.tourspel.db.*;
import nl.gerete.tourspel.logic.*;
import nl.gerete.tourspel.logic.PointsCalculator.*;
import to.etc.webapp.query.*;

import javax.annotation.*;
import java.util.*;

public class PlayerPointsCalculator {

	@Nonnull
	private QDataContext m_dc;

	@Nonnull
	private PlayList m_playList;

	private Map<Rider, Score> m_scoreList = new HashMap<Rider, Score>();

	public PlayerPointsCalculator(@Nonnull QDataContext dc, @Nonnull PlayList playList) {
		m_dc = dc;
		m_playList = playList;
	}

	public void initialize() throws Exception {
		List<Etappe> etappes = EditionBP.getEtappeList(EditionBP.getCurrentEdition(m_dc));
		for(Etappe e : etappes) {
			if(!e.isCompleted())
				continue;
			PointsCalculator pc = new PointsCalculator();
			pc.initialize(m_dc, e);
			pc.calculateScore(m_dc, m_playList);

			List<Score> scoreList = pc.getScoreList();
			for(Score score : scoreList) {
				if(m_scoreList.containsKey(score.getRider())) {
					Score s = m_scoreList.get(score.getRider());
					s.setScorePoints(s.getScorePoints() + score.getScorePoints());
					s.setBonusScorePoints(s.getBonusScorePoints() + score.getBonusScorePoints());
				} else {
					m_scoreList.put(score.getRider(), score);
				}
			}
//			for(int i = 0; i < PointsCalculator.NUM_RIDERS && i < scoreList.size(); i++) {
//				scoreListTotalPoints[i] = scoreListTotalPoints[i] + scoreList.get(i).getScorePoints();
//			}
//
//			List<Score> bonusScoreList = pc.getBonusScoreList();
//			for(int i = 0; i < PointsCalculator.NUM_RIDERS && i < bonusScoreList.size(); i++) {
//				bonusScoreListTotalPoints[i] = bonusScoreListTotalPoints[i] + bonusScoreList.get(i).getScorePoints();
//			}
		}

	}

	public int getTotalScorePoints(Rider rider) {
		Score score = m_scoreList.get(rider);
		if(score == null) {
			return 0;
		}
		return score.getScorePoints();
	}

	public int getTotalBonusScorePoints(Rider rider) {
		Score score = m_scoreList.get(rider);
		if(score == null) {
			return 0;
		}
		return score.getBonusScorePoints();
	}
}
