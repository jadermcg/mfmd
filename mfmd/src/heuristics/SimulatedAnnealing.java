package heuristics;

import interfaces.Heuristics;
import interfaces.Score;
import sources.Solution;
import util.Dataset;

public class SimulatedAnnealing implements Heuristics {
	// ************************************************************************
	// attributes
	// ************************************************************************
	private Score score;
	private Dataset dataset;
	private int w;
	private int validPositions;
	private double T;
	private int nt;
	private double alpha;

	// ************************************************************************
	// public constructor
	// ************************************************************************
	public SimulatedAnnealing(Score score, Dataset dataset, int w, int validPositions, double T,
			int nt, double alpha) {
		this.score = score;
		this.dataset = dataset;
		this.w = w;
		this.validPositions = validPositions;
		this.T = T;
		this.nt = nt;
		this.alpha = alpha;

	}

	// ************************************************************************
	// main method
	// ************************************************************************
	@Override
	public Solution calculates(Solution actual) {

		Solution best = actual;

		while (T > 0.0001) {
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
				} else {
					double n = Math.random();
					if (n < acceptanceProbability(delta, T)) {
						best = temp;
					}
				}
				x++;
			}
			T = decreasingTemperature(T, alpha);
		}

		return (best.getScore() > actual.getScore()) ? best : actual;
	}

	// ************************************************************************
	// decreasing temperature
	// ************************************************************************
	private double decreasingTemperature(double T, double alpha) {
		return T * alpha;
	}

	// ************************************************************************
	// acceptance probability
	// ************************************************************************
	private double acceptanceProbability(double delta, double T) {
		return 1 / Math.exp(Math.abs(delta) / T);
	}

}
