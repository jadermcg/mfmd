package tests;

import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import sources.TreeSolution;
import util.Dataset;

public class TestEvalue {

	public static void main(String[] args) {

		String datasetName = System.getProperty("user.home")
				+ "/datasets/final/real/dataset_crp/dataset.fa";
		int w = 5;
		boolean pal = false;
		Dataset dataset = new Dataset(datasetName, pal, w);
		Background bg = new Background(dataset, 1, 1);
		Score ic = new InformationContentScore(bg);

		TreeSolution tree = new TreeSolution(60, w, dataset, ic, true);
		List<Integer> positions = tree.getPaths().get(0);

		double score = ic.calculates(dataset.getMsa(positions, w));

		System.out.println(score / w);

		List<String> msa = Arrays.asList("aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa",
				"aaaaaaaaaaaaaaaaaaaaaa");

		System.out.println(ic.calculates(msa));

	}
}
