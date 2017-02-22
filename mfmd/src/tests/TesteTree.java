package tests;

import interfaces.Score;

import java.util.List;
import java.util.Map;

import scores.InformationContentScore;
import sources.Background;
import sources.TreeSolution;
import util.Dataset;

public class TesteTree {

	public static void main(String[] args) {

		String datasetFile = System.getProperty("user.home")
				+ "/datasets/final/test/dataset_arnt/dataset.fa";
		int w = 6;
		boolean greedy = false;

		Dataset dataset = new Dataset(datasetFile, false, w);
		Background bg = new Background(dataset, 1, 1);
		Score ic = new InformationContentScore(bg);

		TreeSolution tree = new TreeSolution(196, w, dataset, ic, greedy);

		Map<Integer, List<Integer>> p = tree.getPaths();

		p.forEach((k, v) -> System.out.println(k + "=" + v));

	}

}
