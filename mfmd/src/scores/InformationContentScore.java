package scores;

import interfaces.Score;

import java.util.List;

import sources.Background;
import sources.Profile;

/**
 * Implementation of the Information Content Score (IC)
 * 
 * @author jadermcg
 *
 */
public class InformationContentScore implements Score {

	private Background bg;

	public InformationContentScore(Background bg) {
		this.bg = bg;
	}

	@Override
	public Double calculates(List<String> msa) {
		double score = 0d;
		Profile profile = new Profile(msa, bg);
		double pwm[][] = profile.getPwm();
		int pfm[][] = profile.getPfm();
		for (int j = 0; j < pwm[0].length; j++) {
			for (int i = 0; i < pwm.length; i++) {
				score += pfm[i][j] * pwm[i][j];
			}
		}

		return score;
	}

}
