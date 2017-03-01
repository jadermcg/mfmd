package tests;

import heuristics.HillClimbing;
import heuristics.ILS;
import heuristics.Vnd;
import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import sources.Solution;
import util.Dataset;

public class TestVnd {

	public static void main(String[] args) {

		String file = System.getProperty("user.home")
				+ "/datasets/final/real/dataset_crp/dataset.fa";
		int w = 22;
		boolean palindrome = true;

		Dataset dataset = new Dataset(file, palindrome, w);
		Background bg = new Background(new double[] { 0.25, 0.25, 0.25, 0.25 });

		List<Integer> positions = Arrays.asList(55, 45, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
				15, 16, 17, 18);

		Score ic = new InformationContentScore(bg);

		Solution s = new Solution(positions, ic.calculates(dataset.getMsa(positions, w)));

		System.out.println(s.getScore());

		Vnd vnd = new Vnd(dataset, ic, 10, w);

		HillClimbing hc = new HillClimbing(ic, dataset, w, dataset.getValidPositions(), 1000);

		ILS ils = new ILS(dataset, ic, 10, 22);
		Solution s1 = ils.calculates(s);
		// Solution s1 = vnd.calculates(s);
		// Solution s1 = hc.calculates(s);

		System.out.println(s1.getScore());

		System.out.println(s1.getPositions());

	}

}
