package tests;

import heuristics.HillClimbing;
import interfaces.Heuristics;
import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import sources.Solution;
import util.Dataset;

public class TestSa {

	public static void main(String[] args) {

		Dataset dataset = new Dataset(System.getProperty("user.home")
				+ "/datasets/final/real/dataset_crp/dataset.fa", true, 22);
		Background bg = new Background(dataset, 1, 1);
		Score ic = new InformationContentScore(bg);

		List<Integer> initialPositions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 15, 16, 17, 18);
		double initialScore = ic.calculates(dataset.getMsa(initialPositions, 22));

		Solution s = new Solution(initialPositions, initialScore);

		System.out.println(s.getScore());

		// Heuristics sa = new SimulatedAnnealing(ic, dataset, 22,
		// dataset.getValidPositions(), 100,
		// 300, 0.9);

		Heuristics vns = new HillClimbing(ic, dataset, 22, dataset.getValidPositions(), 10000);

		s = vns.calculates(s);

		System.out.println(s.getScore());
		System.out.println(s.getPositions());

	}
}
