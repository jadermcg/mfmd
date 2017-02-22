package tests;

import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.ComplexityScore;
import sources.Background;
import util.Dataset;

public class TestComplexityScore {

	public static void main(String[] args) {

		String datasetName = System.getProperty("user.home")
				+ "/datasets/final/real/dataset_crp/dataset.fa";

		int w = 22;

		Dataset dataset = new Dataset(datasetName, false, w);

		Background bg = new Background(dataset, 1, 1);

		List<Integer> pos = Arrays.asList(60, 54, 75, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
				17, 18);

		List<String> msa = dataset.getMsa(pos, w);
		System.out.println(msa);
		// msa = Arrays.asList("aaaaaaaattaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa",
		// "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");
		// System.out.println(msa);

		Score cs = new ComplexityScore(bg);

		System.out.println(cs.calculates(msa));

	}
}
