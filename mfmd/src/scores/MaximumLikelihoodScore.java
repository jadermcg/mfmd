package scores;

import interfaces.Score;

import java.util.List;

import sources.Background;
import sources.Profile;

/**
 * Implementation of the maximum likelihood Score (MLE) order 0
 * 
 * @author jadermcg
 *
 */
public class MaximumLikelihoodScore implements Score {

	private Background bg;

	public MaximumLikelihoodScore(Background bg) {
		this.bg = bg;
	}

	@Override
	public Double calculates(List<String> msa) {
		Double score = 0d;
		Profile profile = new Profile(msa, bg);
		for (String seq : msa) {
			score += profile.probSequenceGivenModel(seq);
		}

		return score;
	}

}
