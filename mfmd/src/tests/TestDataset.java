package tests;

import util.Dataset;

public class TestDataset {

	public static void main(String[] args) {

		int w = 16;
		String datasetName = "../../datasets/final/real/dataset_creb/dataset_creb.fa";
		boolean dimmer = false;
		Dataset dataset = new Dataset(datasetName, dimmer, w);
		//System.out.println(dataset.getSize());
		System.out.println(dataset.getSubsequences(0));
		// System.out.println(dataset.getSubsequence(1, 1000));
		// System.out.println(dataset.getSequenceSize(15));
		// Background bg = new Background(dataset, 1, new String[] { "a", "c",
		// "g", "t" }, 1l);
	}

}
