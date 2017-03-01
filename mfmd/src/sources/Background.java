package sources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.core.sequence.DNASequence;

import util.Dataset;
import util.Functions;

/**
 * class to works background probabilities
 * 
 * @author jadermcg
 *
 */
public class Background {

	// *************************************************
	// attributes
	// *************************************************
	private Map<String, Integer> frequencies;
	private Map<String, Double> probabilities;
	private int order;
	private List<List<String>> permutations;
	private Dataset dataset;
	private int pseudocounters;
	private static final String[] alphabet = { "a", "c", "g", "t" };

	// *************************************************
	// construtores
	// *************************************************
	public Background(Dataset dataset, int order, int pseudocounters) {
		this.dataset = dataset;
		this.order = order;
		this.pseudocounters = pseudocounters;
		probabilities = new LinkedHashMap<>();
		frequencies = new LinkedHashMap<>();
		permutations = new ArrayList<>();
		start();
	}

	public Background(Map<String, Double> manualProbabilities, int order) {
		this.probabilities = manualProbabilities;
		this.order = order;
	}

	public Background(double[] manualProbabilities) {
		this.probabilities = new LinkedHashMap<>();
		this.probabilities.put("a", manualProbabilities[0]);
		this.probabilities.put("c", manualProbabilities[1]);
		this.probabilities.put("g", manualProbabilities[2]);
		this.probabilities.put("t", manualProbabilities[3]);
		this.order = 0;
	}

	// ******************************************************************
	// initiates the attributes and call the methods in the correct order
	// ******************************************************************
	private void start() {
		symbolPermutation();
		symbolCounts();
		addPseudoCounters();
		symbolProbabilities();
	}

	// ******************************************************************
	// symbol permutations
	// ******************************************************************
	private void symbolPermutation() {
		int n = 1;
		while (n <= order) {
			List<String> temp = Functions.permutation(alphabet, n);
			permutations.add(temp);
			n++;
		}

		permutations.forEach(v -> {
			v.forEach(symbol -> {
				frequencies.put(symbol, 0);
				probabilities.put(symbol, 0d);
			});
		});
	}

	// ******************************************************************
	// count symbols into dataset
	// ******************************************************************
	private void symbolCounts() {
		frequencies.forEach((k, v) -> {
			List<String> seqs = dataset.getSequences();
			int j = k.length();
			for (String seq : seqs) {
				for (int i = 0; i <= seq.length() - j; i++) {
					String sub = seq.substring(i, i + j);
					DNASequence s = null;
					try {
						s = new DNASequence(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String normal = s.getSequenceAsString();
					String revComp = s.getReverseComplement().getSequenceAsString();
					if (normal.equalsIgnoreCase(k))
						frequencies.compute(k, (a, b) -> b + 1);

					if (revComp.equalsIgnoreCase(k))
						frequencies.compute(k, (a, b) -> b + 1);
				}
			}
		});
	}

	// ******************************************************************
	// add pseudocounters
	// ******************************************************************
	private void addPseudoCounters() {
		frequencies.forEach((k, v) -> {
			frequencies.compute(k, (a, b) -> b + pseudocounters);
		});

	}

	// ******************************************************************
	// calculates symbols probabilities
	// ******************************************************************
	private void symbolProbabilities() {
		ArrayList<Integer> sums = new ArrayList<>();

		int j = 0;
		int sum = 0;
		for (String symbol : frequencies.keySet()) {
			int frequency = frequencies.get(symbol);
			if (j < alphabet.length) {
				sum += frequency;
				j++;
			}

			if (j == alphabet.length) {
				sums.add(sum);
				j = 0;
				sum = 0;
			}
		}

		j = 0;
		int k = 0;
		for (String symbol : frequencies.keySet()) {
			int frequency = frequencies.get(symbol);
			if (j < alphabet.length) {
				probabilities.replace(symbol, (double) frequency / sums.get(k));
				j++;
			}

			if (j == alphabet.length) {
				j = 0;
				k++;
			}
		}
	}

	// ******************************************************************
	// calculates probability sequence given a background
	// ******************************************************************
	public Double probSequenceGivenBackground(String seq) {
		Double probability = 1d;
		ArrayList<String> sequenceSplit = new ArrayList<>();

		for (int i = 0, j = 1; j < order; j++) {
			sequenceSplit.add(seq.substring(i, j));
		}

		for (int i = 0, j = order; j <= seq.length(); i++, j++) {
			sequenceSplit.add(seq.substring(i, j));
		}

		for (String symbols : sequenceSplit) {
			probability *= probabilities.get(symbols);
		}

		return probability;
	}

	// ******************************************************************
	// get frequencies
	// ******************************************************************
	public Map<String, Integer> getFrequencies() {
		return frequencies;
	}

	// ******************************************************************
	// get probabilities
	// ******************************************************************
	public Map<String, Double> getProbabilities() {
		return probabilities;
	}

	// ******************************************************************
	// get order
	// ******************************************************************
	public int getOrder() {
		return order;
	}

	// ******************************************************************
	// get pseudocontadores
	// ******************************************************************
	public int getPseudocounters() {
		return pseudocounters;
	}

	// ******************************************************************
	// get permutations
	// ******************************************************************
	public List<List<String>> getSymbols() {
		return permutations;
	}

	// ******************************************************************
	// get alphabet
	// ******************************************************************
	public static String[] getAlphabet() {
		return alphabet;
	}
}
