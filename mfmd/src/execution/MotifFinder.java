package execution;

import interfaces.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sources.Background;
import sources.Join;
import sources.Solution;
import util.Dataset;

public class MotifFinder {

	// *************************************************************************
	// attributes
	// **************************************************************************
	private Dataset dataset;
	private Background bg;
	private Score score;
	private double significanceLevel;
	private Solution bestSolution;
	private Map<Integer, List<Integer>> patternMatchingPositions;
	private Map<Integer, List<Integer>> newPositions;
	private List<Integer> patternDiscoveryPositions;
	private boolean oops;
	private int nTrees;
	private boolean greedy;

	// *************************************************************************
	// public constructor
	// **************************************************************************
	public MotifFinder(Dataset dataset, Background bg, Score score, double significanceLevel,
			boolean oops, boolean greedy, int nTrees) {
		this.dataset = dataset;
		this.bg = bg;
		this.score = score;
		this.significanceLevel = significanceLevel;
		this.oops = oops;
		this.greedy = greedy;
		this.nTrees = nTrees;
		patternMatchingPositions = new LinkedHashMap<>();
		patternDiscoveryPositions = new ArrayList<>();
		newPositions = new LinkedHashMap<>();
	}

	// *************************************************************************
	// start method
	// **************************************************************************
	public void start() {
		preprocessing();
		patternDiscovery();
		// se modelo de busca for oops
		if (!oops)
			patternMatching();
	}

	// *************************************************************************
	// pre-processing
	// **************************************************************************
	private void preprocessing() {
		System.out.println("\npreprocessing stage...");
		Preprocessing preprocessing = new Preprocessing(dataset, bg);
		dataset = preprocessing.getDataset();
		System.out.println("done!");
	}

	// *************************************************************************
	// pattern discovery
	// **************************************************************************
	private void patternDiscovery() {
		System.out.println("\npattern discovery stage...\n");

		// aplica estapa de pattern discovery
		PatternDiscovery pd = new PatternDiscovery(greedy, dataset, bg, score, nTrees);
		pd.start();

		// pega melhor solucao
		bestSolution = pd.getBest();

		// copia posicoes encontradas para lista novas de posicoes
		patternDiscoveryPositions = bestSolution.getPositions();
		// add positions found in pattern discovery phase into a new positions
		// map
		int k = 0;
		for (int pos : patternDiscoveryPositions) {
			newPositions.put(k++, Arrays.asList(pos));
		}
		System.out.println("done!");
	}

	// *************************************************************************
	// pattern matching
	// **************************************************************************
	private void patternMatching() {
		System.out.println("\npattern matching stage...\n");

		// inicia etapa de pattern matching
		PatternMatching matching = new PatternMatching(dataset, dataset.getMsa(
				bestSolution.getPositions(), dataset.getW()), bg, significanceLevel);

		// copia posicoes encontradas para lista de posicoes
		patternMatchingPositions = matching.getNewPositions();

		// add the best positions found in the pattern discovery into new
		// positions found in pattern matching
		newPositions = Join.patternDiscoveryPositions_patternMatchingPositions(
				patternDiscoveryPositions, patternMatchingPositions);

		System.out.println("done!");
	}

	// *************************************************************************
	// get positions found
	// **************************************************************************
	public Map<Integer, List<Integer>> getPositions() {
		return newPositions;
	}

}
