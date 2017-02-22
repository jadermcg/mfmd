package sources;

import java.util.List;

import util.Functions;

/**
 * Class which represents a probabilistic profile
 * 
 * @author Jader M. Caldonazzo Garbelini
 *
 */

public class Profile implements Cloneable {

	// *************************************************
	// attributes
	// *************************************************
	private int pfm[][];
	private double ppm[][];
	private double pwm[][];
	private List<String> msa;
	private String consensus;
	private Background bg;

	// *************************************************
	// public constructores method
	// *************************************************
	public Profile(List<String> msa, Background bg) {
		this.msa = msa;
		this.bg = bg;
		pfm = new int[4][msa.get(0).length()];
		ppm = new double[4][msa.get(0).length()];
		pwm = new double[4][msa.get(0).length()];
		consensus = new String();
		start();
	}

	// *************************************************
	// class initialization method
	// *************************************************
	private void start() {
		pfm();
		pseudoCounters();
		ppm();
		pwm();
		logPwm();
		informationContent();
		consensusSequenceCalculates();
	}

	// *************************************************
	// calculate pfm
	// *************************************************
	private void pfm() {
		msa.forEach(seq -> {
			for (int i = 0; i < seq.length(); i++) {
				char c = seq.charAt(i);
				switch (c) {
				case 'a':
					pfm[0][i]++;
					break;
				case 'c':
					pfm[1][i]++;
					break;
				case 'g':
					pfm[2][i]++;
					break;
				case 't':
					pfm[3][i]++;
					break;
				}
			}
		});
	}

	// *************************************************
	// insert pseudoconunters into pfm matrix
	// *************************************************
	private void pseudoCounters() {
		for (int i = 0; i < pfm.length; i++) {
			for (int j = 0; j < pfm[0].length; j++) {
				pfm[i][j]++;
			}
		}
	}

	// *************************************************
	// calculate ppm
	// *************************************************
	private void ppm() {
		int sum = msa.size() + 4;
		for (int i = 0; i < pfm.length; i++) {
			for (int j = 0; j < pfm[0].length; j++) {
				ppm[i][j] = (double) pfm[i][j] / sum;
			}
		}
	}

	// *************************************************
	// calculate pwm
	// *************************************************
	private void pwm() {
		double background[] = bg.getProbabilities().values().stream()
				.mapToDouble(Double::doubleValue).toArray();
		for (int j = 0; j < pwm[0].length; j++) {
			for (int i = 0; i < pwm.length; i++) {
				pwm[i][j] = ppm[i][j] / background[i];
			}
		}
	}

	// *************************************************
	// calculate log pwm
	// *************************************************
	private void logPwm() {
		for (int i = 0; i < pfm.length; i++) {
			for (int j = 0; j < pfm[0].length; j++) {
				pwm[i][j] = Functions.logarithm(pwm[i][j], 2);
			}
		}
	}

	// *************************************************
	// calculate information content
	// *************************************************
	private void informationContent() {
		for (int i = 0; i < pwm.length; i++) {
			for (int j = 0; j < pwm[0].length; j++) {
				pwm[i][j] = ppm[i][j] * pwm[i][j];
			}
		}
	}

	// *************************************************
	// calcula sequencia consensus
	// *************************************************
	private void consensusSequenceCalculates() {

		for (int j = 0; j < pfm[0].length; j++) {
			int value = 0;
			int idx = 0;
			for (int i = 0; i < pfm.length; i++) {
				if (pfm[i][j] > value) {
					value = pfm[i][j];
					idx = i;
				}
			}
			switch (idx) {
			case 0:
				consensus = consensus.concat("a");
				break;
			case 1:
				consensus = consensus.concat("c");
				break;
			case 2:
				consensus = consensus.concat("g");
				break;
			case 3:
				consensus = consensus.concat("t");
				break;
			}
		}
	}

	// ******************************************************************
	// calculates probability sequence given a model
	// ******************************************************************
	public Double probSequenceGivenModel(String seq) {
		Double probability = 1d;
		for (int i = 0; i < seq.length(); i++) {
			char c = seq.charAt(i);
			switch (c) {
			case 'a':
				probability *= ppm[0][i];
				break;
			case 'c':
				probability *= ppm[1][i];
				break;
			case 'g':
				probability *= ppm[2][i];
				break;
			case 't':
				probability *= ppm[3][i];
				break;
			}
		}
		return probability;
	}

	// ******************************************************************
	// calculates probability sequence given a model
	// ******************************************************************
	public Double probSequenceGivenModel(String sequence, int p) {
		double probability = 0;
		int w = pfm[0].length;
		for (int i = 0; i < sequence.length(); i++) {
			char c = sequence.charAt(i);
			if (i >= p && i < p + w) {
				probability += Math.log(probSequenceGivenModel(String.valueOf(c)));
			}

			else {
				probability += Math.log(bg.probSequenceGivenBackground(String.valueOf(c)));
			}
		}

		return probability;
	}

	/**
	 * getters and setters
	 * 
	 * @param args
	 */

	public String getConsensus() {
		return consensus;
	}

	public int[][] getPfm() {
		return pfm;
	}

	public double[][] getPpm() {
		return ppm;
	}

	public double[][] getPwm() {
		return pwm;
	}

	public List<String> getMsa() {
		return msa;
	}

}
