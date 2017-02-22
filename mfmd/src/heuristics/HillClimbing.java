package heuristics;

import interfaces.Heuristics;
import interfaces.Score;
import sources.Solution;
import util.Dataset;

public class HillClimbing implements Heuristics {

	private Score score;
	private Dataset dataset;
	private int w;
	private int validPositions;
	private double nt;

	public HillClimbing(Score score, Dataset dataset, int w, int validPositions, double nt) {
		this.score = score;
		this.dataset = dataset;
		this.w = w;
		this.validPositions = validPositions;
		this.nt = nt;
	}

	@Override
	public Solution calculates(Solution actual) {
		Solution best = actual.clone();

		int x = 0;
		while (x < nt) {
			Solution temp = neighborhood(best, validPositions);

			double bestScore = score.calculates(dataset.getMsa(best.getPositions(), w));
			double tempScore = score.calculates(dataset.getMsa(temp.getPositions(), w));
			best.setScore(bestScore);
			temp.setScore(tempScore);
			double delta = bestScore - tempScore;
			if (delta < 0) {
				best = temp;
			}
			x++;
		}

		return (best.getScore() > actual.getScore()) ? best : actual;
	}

}
