package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 * Class to works dataset
 * 
 * @author jadermcg
 *
 */
public class Dataset {

	// ************************************************
	// attributes
	// ************************************************
	private ArrayList<String> sequences;
	private String file;
	private boolean dimmer;
	private int w;

	// ************************************************
	// default public constructor
	// ************************************************
	public Dataset(String file, boolean dimmer, int w) {
		this.file = file;
		this.dimmer = dimmer;
		this.w = w;
		start();
	}

	// ************************************************
	// initiates class attributes
	// ************************************************
	private void start() {
		sequences = new ArrayList<>();
		load();
	}

	// ************************************************
	// load dataset into memory
	// ************************************************
	private void load() {
		try {
			Map<String, DNASequence> temp = FastaReaderHelper.readFastaDNASequence(new File(file));
			temp.forEach((k, v) -> sequences.add(v.getSequenceAsString().toLowerCase()));
		} catch (IOException e) {
			System.err.println("Dataset nao encontrado.");
			e.printStackTrace();
		}
	}

	// ************************************************
	// get subsequence from sequence dataset
	// ************************************************
	public String getSubsequence(int sequenceNumber, int initialPosition) {
		return sequences.get(sequenceNumber).substring(initialPosition, initialPosition + w)
				.toLowerCase();
	}

	// ************************************************
	// get subsequences from sequence dataset
	// ************************************************
	public List<String> getSubsequences(int sequenceNumber) {
		ArrayList<String> subSequences = new ArrayList<>();

		for (int i = 0; i < getSequenceSize(sequenceNumber) - w + 1; i++) {
			subSequences.add(getSubsequence(sequenceNumber, i).toLowerCase());
		}

		return subSequences;
	}

	// ************************************************
	// get sequence from the sequence number
	// ************************************************
	public String getSequence(int sequenceNumber) {
		return sequences.get(sequenceNumber).toLowerCase();
	}

	// ************************************************
	// set sequence from the sequence number
	// ************************************************
	public void setSequence(int sequenceNumber, String seq) {
		seq = seq.toLowerCase();
		sequences.set(sequenceNumber, seq);
	}

	// ************************************************
	// get dataset length
	// ************************************************
	public int getSize() {
		return sequences.size();
	}

	// ************************************************
	// get sequence length from the sequence number
	// ************************************************
	public int getSequenceSize(int sequenceNumber) {
		return sequences.get(sequenceNumber).length();
	}

	// ************************************************
	// get all dataset sequences
	// ************************************************
	public List<String> getSequences() {
		return sequences;
	}

	// ************************************************
	// get sequences size
	// ************************************************
	public int getSequenceSize() {
		return sequences.get(0).length();
	}

	// ************************************************
	// get msa
	// ************************************************
	public List<String> getMsa(Map<Integer, List<Integer>> positions, int length) {
		List<String> msa = new ArrayList<>();

		positions.forEach((k, v) -> {
			int seqNumber = k - 1;
			v.forEach(pos -> {
				msa.add(getSubsequence(seqNumber, pos));
			});
		});

		return msa;
	}

	// ************************************************
	// get msa
	// ************************************************
	public List<String> getMsa(List<Integer> positions, int length) {
		ArrayList<String> msa = new ArrayList<>();

		for (int i = 0; i < positions.size(); i++) {
			try {
				DNASequence temp = new DNASequence(getSubsequence(i, positions.get(i)));
				buildMsa(msa, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return msa;
	}

	// ************************************************
	// get msa
	// ************************************************
	public List<String> getMsa(Object[] positions, int length) {
		ArrayList<String> msa = new ArrayList<>();

		for (int i = 0; i < positions.length; i++) {
			try {
				DNASequence temp = new DNASequence(getSubsequence(i, ((Integer) positions[i])));
				buildMsa(msa, temp);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return msa;
	}

	public int getValidPositions() {
		return getSequenceSize() - w + 1;
	}

	// ************************************************
	// build msa
	// ************************************************
	private void buildMsa(ArrayList<String> msa, DNASequence temp) {
		msa.add(temp.getSequenceAsString());
		if (dimmer)
			msa.add(temp.getReverseComplement().getSequenceAsString());
	}

	// ************************************************
	// get motif length
	// ************************************************
	public int getW() {
		return w;
	}

}
