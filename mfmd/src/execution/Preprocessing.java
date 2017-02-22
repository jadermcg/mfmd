package execution;

import sources.Background;
import util.Dataset;
import util.Functions;

/**
 * normalize dataset $$ run before the pattern discovery $$
 * 
 * @author jadermcg
 *
 */
public class Preprocessing {

	// **************************************************************
	// attributes
	// **************************************************************
	private Dataset dataset;
	private int maior;
	private Background bg;

	// **************************************************************
	// public constructor
	// **************************************************************
	public Preprocessing(Dataset dataset, Background bg) {
		this.dataset = dataset;
		this.bg = bg;
		maior = Integer.MIN_VALUE;
		start();
	}

	// **************************************************************
	// start method
	// **************************************************************
	private void start() {
		findLargerSequence();
		normalizeDataset();
		findAndReplace();
	}

	// **************************************************************
	// find base N or n and replace to background probability
	// **************************************************************
	private void findAndReplace() {

		for (int i = 0; i < dataset.getSize(); i++) {
			StringBuilder seq = new StringBuilder(dataset.getSequence(i));
			for (int j = 0; j < seq.length(); j++) {
				char c = seq.charAt(j);
				String r = null;

				switch (c) {
				case 'N':
					r = Functions.generateNucleotideFromBgProbability(bg);
					seq.replace(j, j + 1, r);
					break;
				case 'n':
					r = Functions.generateNucleotideFromBgProbability(bg);
					seq.replace(j, j + 1, r);
					break;
				}
			}
			dataset.setSequence(i, seq.toString());
		}

	}

	// **************************************************************
	// find the bigger sequence
	// **************************************************************
	private void findLargerSequence() {
		for (int i = 0; i < dataset.getSize(); i++) {
			int temp = dataset.getSequenceSize(i);
			if (temp > maior)
				maior = temp;
		}
	}

	// **************************************************************
	// normalize dataset by the size of the largest sequence
	// **************************************************************
	private void normalizeDataset() {
		for (int i = 0; i < dataset.getSize(); i++) {
			String seq = dataset.getSequence(i);
			while (seq.length() < maior) {
				seq = seq.concat(Functions.generateNucleotideFromBgProbability(bg));
			}
			dataset.setSequence(i, seq);
		}
	}

	// **************************************************************
	// returns normalized dataset
	// **************************************************************
	public Dataset getDataset() {
		return dataset;
	}

}
