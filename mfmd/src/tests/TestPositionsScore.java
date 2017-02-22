package tests;

import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import sources.Solution;
import util.Dataset;

public class TestPositionsScore {

	public static void main(String[] args) {

		Dataset dataset = new Dataset(System.getProperty("user.home")
				+ "/datasets/final/semi-syntetic/dataset_che-1/dataset.fa", true, 6);
		Background bg = new Background(dataset, 1, 1);
		Score ic = new InformationContentScore(bg);

		List<Integer> initialPositions = Arrays.asList(276, 93, 396, 420, 85, 338, 410, 370, 479,
				451, 302, 401, 438, 232, 340, 184, 384, 180, 72, 168, 473, 143, 24, 224, 319, 190,
				137);
		double initialScore = ic.calculates(dataset.getMsa(initialPositions, 22));

		Solution s = new Solution(initialPositions, initialScore);

		System.out.println(s.getScore());
		System.out.println(s.getPositions());

	}
}
