package tests;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import sources.Read;
import util.Dataset;

public class TestMsaOut {

	public static void main(String[] args) throws Exception {

		String resultDirectory = System.getProperty("user.home") + "/datasets/final/chip/MA0014.2/";
		String datasetFile = resultDirectory + "dataset.fa";
		String msaFile = resultDirectory + "dmma2/msa.out";
		String foundFile = resultDirectory + "dmma2/positions.out";

		Map<Integer, List<Integer>> found = Read.file(foundFile);
		Dataset dataset = new Dataset(datasetFile, false, 19);

		// *****************************************************************
		// create msa file
		// *****************************************************************
		File pathMsa = new File(msaFile);
		PrintStream pw_msa = new PrintStream(pathMsa);

		List<String> msa = dataset.getMsa(found, 19);
		int seq = 1;
		for (String str : msa) {
			pw_msa.println("seq>" + seq++);
			pw_msa.println(str + "\n");
		}

		pw_msa.close();

	}
}
