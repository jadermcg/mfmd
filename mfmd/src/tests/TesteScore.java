package tests;

import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import util.Dataset;

public class TesteScore {

	public static void main(String[] args) {

		String datasetFile = System.getProperty("user.home")
				+ "/datasets/final/test/dataset_arnt/dataset.fa";
		int w = 6;

		Dataset dataset = new Dataset(datasetFile, false, w);
		Background bg = new Background(dataset, 1, 1);
		Score ic = new InformationContentScore(bg);

		List<Integer> real_positions = Arrays.asList(196, 254, 106, 465, 124, 287, 343, 372, 71,
				329, 289, 247, 411, 294, 146, 186, 316, 138, 421, 297);

		// List<Integer> dmma2_positions = Arrays.asList(196, 227, 106, 465,
		// 124, 287, 324, 372, 71,
		// 329, 289, 247, 140, 87, 146, 186, 316, 138, 421, 88);

		// List<Integer> gibbs_positions = Arrays.asList(197, 255, 107, 466,
		// 125, 288, 325, 373, 72,
		// 330, 290, 248, 141, 295, 147, 187, 317, 139, 422, 298);

		List<Integer> test_positions = Arrays.asList(196, 254, 106, 465, 124, 287, 343, 372, 71,
				329, 289, 247, 411, 87, 146, 186, 316, 487, 421, 297);

		List<String> msa = dataset.getMsa(real_positions, w);
		List<String> msa_test = dataset.getMsa(test_positions, w);

		System.out.println("Real -> " + ic.calculates(msa));
		System.out.println("Test -> " + ic.calculates(msa_test));
	}
}
