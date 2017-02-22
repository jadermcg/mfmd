package scores;

import interfaces.Score;

import java.util.List;

import sources.Background;
import sources.Profile;

public class BayesianScore implements Score {
	private Background bg;

	public BayesianScore(Background bg) {
		this.bg = bg;
	}

	@Override
	public Double calculates(List<String> msa) {
		double score = 0d;
		Profile profile = new Profile(msa, bg);
		double pwm[][] = profile.getPwm();
		int pfm[][] = profile.getPfm();
		double A = 18;
		double L = 105 * A;
		double p0 = A / L;
		double w = 22;
		for (int j = 0; j < pwm[0].length; j++) {
			for (int i = 0; i < pwm.length; i++) {
				score += pfm[i][j] * pwm[i][j] + Math.log(p0 / (1 - p0) - 1) - 3 / 2 * w
						* Math.log(A - 1);

			}
		}

		System.out.println(score);

		return score;
	}
}
