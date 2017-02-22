package execution;

import interfaces.Score;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import scores.InformationContentScore;
import sources.Background;
import sources.PerformanceCalculations;
import sources.Read;
import util.Dataset;

public class Mfmd2Run {

	public static void main(String[] args) throws Exception {

		// *********************************************************
		// attributes
		// *********************************************************
		boolean oops = false;
		boolean greedy = false;
		String type = "test";
		String pathname = System.getProperty("user.home") + "/" + "datasets/final/" + type + "/";
		File root = new File(pathname);
		String datasets[] = root.list();
		int nTrees = 5;

		// *********************************************************
		// foreach dataset
		// *********************************************************
		for (String datasetName : datasets) {
			File temp = new File(root.getPath() + "/" + datasetName);
			if (temp.isDirectory()) {
				String datasetpath = temp.getPath() + "/";
				System.out.println(temp.toString());
				// *************************************************************************
				// parameters
				// *************************************************************************
				List<String> param = Files.lines(Paths.get(datasetpath + "param.conf")).collect(
						Collectors.toList());
				int w = Integer.parseInt(param.get(0));
				double significanceLevel = Double.parseDouble(param.get(1));
				boolean dimmer = Boolean.parseBoolean(param.get(4));

				// *************************************************************************
				// Dataset and Score objetc build
				// *************************************************************************
				Dataset dataset = new Dataset(datasetpath + "dataset.fa", dimmer, w);
				Map<String, Double> fundo = new LinkedHashMap<>();
				fundo.put("a", .25);
				fundo.put("c", .25);
				fundo.put("g", .25);
				fundo.put("t", .25);
				Background bg = new Background(fundo, 1);
				Score score = new InformationContentScore(bg);

				// *************************************************************************
				// Time settings
				// *************************************************************************
				long initialTime = 0l;
				long finalTime = 0l;
				double totalTime = 0l;

				// *****************************************************************
				// execution algorithm
				// *****************************************************************
				initialTime = System.currentTimeMillis();
				MotifFinder exec = new MotifFinder(dataset, bg, score, significanceLevel, oops,
						greedy, nTrees);
				exec.start();

				Map<Integer, List<Integer>> found = exec.getPositions();

				finalTime = System.currentTimeMillis();
				totalTime = (double) (finalTime - initialTime) / 1000;

				// *************************************************************************
				// Create result directory
				// *************************************************************************
				String directory = "dmma2/";
				new File(datasetpath + directory).mkdir();
				String resultDirectory = datasetpath + directory;

				// *****************************************************************
				// save time
				// *****************************************************************
				File pathTime = new File(resultDirectory + "time.out");
				new File(resultDirectory).mkdir();
				PrintStream pw_time = new PrintStream(pathTime);
				pw_time.println(totalTime);
				pw_time.close();

				// *****************************************************************
				// save positions
				// *****************************************************************
				File pathPositions = new File(resultDirectory + "positions.out");
				PrintStream pw_positions = new PrintStream(pathPositions);

				for (int k : found.keySet()) {
					List<Integer> pos = found.get(k);
					pw_positions.print(k + 1 + "=");
					for (int i = 0; i < pos.size(); i++) {
						if (i < pos.size() - 1)
							pw_positions.print(pos.get(i) + ",");
						else
							pw_positions.print(pos.get(i) + "\n");
					}
				}
				pw_positions.close();

				// *****************************************************************
				// performance measures
				// *****************************************************************
				Map<Integer, List<Integer>> real = Read.file(datasetpath + "positions.conf");
				found = Read.file(resultDirectory + "positions.out");
				int nPos = Integer.parseInt(param.get(2));
				int nNeg = Integer.parseInt(param.get(3));
				PerformanceCalculations pc = new PerformanceCalculations(real, found, nPos, nNeg, 4);
				Map<String, Double> measures = pc.getMeasures();
				File pathMeasures = new File(resultDirectory + "measures.out");
				PrintStream pw_measures = new PrintStream(pathMeasures);
				measures.forEach((k, v) -> pw_measures.println(v));
				pw_measures.close();

				// *****************************************************************
				// create msa file
				// *****************************************************************
				File pathMsa = new File(resultDirectory + "msa.out");
				PrintStream pw_msa = new PrintStream(pathMsa);

				List<String> msa = dataset.getMsa(found, w);
				int seq = 1;
				for (String str : msa) {
					pw_msa.println(">seq" + seq++);
					pw_msa.println(str + "\n");
				}

				pw_msa.close();

				// *****************************************************************
				// create logo
				// *****************************************************************
				String[] command = { "weblogo", "-f", "msa.out", "-D", "fasta", "-F", "pdf", "-o",
						"logo.pdf", "-A", "dna", "-c", "classic", "-P", "' '", "-s", "large" };
				Process p = Runtime.getRuntime().exec(command, null,
						Paths.get(resultDirectory).toFile());
				p.waitFor();
				p.destroy();

				// *****************************************************************
				// next iteration
				// *****************************************************************
				System.out
						.println("---------------------------------------------------------------");
			}
		}
	}
}
