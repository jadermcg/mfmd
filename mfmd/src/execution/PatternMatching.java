package execution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rosuda.REngine.Rserve.RConnection;

import sources.Background;
import sources.Profile;
import util.Dataset;
import util.Functions;

/**
 * observacao: para utilizar algoritmo de Johnson descomentar codigo no metodo
 * start();
 * 
 * @author jadermcg
 *
 */

@SuppressWarnings("unused")
public class PatternMatching {

	// *************************************************************************
	// attributes
	// *************************************************************************
	private Dataset dataset;
	private double significanceLevel;
	private Background bg;
	private List<String> msa;
	private Profile profile;
	private Map<Integer, List<Double>> scores;
	private double mean;
	private double sd;
	private Map<Integer, List<Double>> pvalues;
	private Map<Integer, List<Integer>> newPositions;

	// *************************************************************************
	// constructor
	// *************************************************************************
	public PatternMatching(Dataset dataset, List<String> msa, Background bg,
			double significanceLevel) {
		this.dataset = dataset;
		this.msa = msa;
		this.bg = bg;
		this.significanceLevel = significanceLevel;
		scores = new LinkedHashMap<>();
		pvalues = new LinkedHashMap<>();
		newPositions = new LinkedHashMap<>();
		start();
	}

	// *************************************************************************
	// start method
	// *************************************************************************
	private void start() {
		buildProfile();
		calcScores();
		// JohnsonPowerTransformation();
		mean();
		sd();
		calcPvalues();
		findNewPositions();
	}

	// *************************************************************************
	// build profile from msa
	// *************************************************************************
	private void buildProfile() {
		profile = new Profile(msa, bg);
	}

	// *************************************************************************
	// calculates scores of all valid window
	// *************************************************************************
	private void calcScores() {
		scores = new LinkedHashMap<>();
		for (int i = 0; i < dataset.getSize(); i++) {
			List<String> subs = dataset.getSubsequences(i);
			List<Double> temp = new ArrayList<Double>();
			double score = 0d;
			for (int j = 0; j < subs.size(); j++) {
				String seq = subs.get(j);
				double probSeqModel = profile.probSequenceGivenModel(seq);
				double probSeqBg = bg.probSequenceGivenBackground(seq);
				score = Functions.logarithm(probSeqModel / probSeqBg, 4d);
				temp.add(score);
			}
			scores.put(i, temp);
		}
	}

	// *************************************************************************
	// calculates mean from scores
	// *************************************************************************
	private void mean() {
		mean = Functions.mean(scores);
	}

	// *************************************************************************
	// calculates standard deviation from scores
	// *************************************************************************
	private void sd() {
		sd = Functions.sd(scores);
	}

	// *************************************************************************
	// apply power johnson power transformation
	// *************************************************************************
	private void JohnsonPowerTransformation() {
		// copia lista para vetor (compativel com R)
		double scoresTemp[] = new double[dataset.getValidPositions() * dataset.getSize()];
		int j = 0;
		for (int k : scores.keySet()) {
			List<Double> v = scores.get(k);
			for (int i = 0; i < v.size(); i++, j++) {
				scoresTemp[j] = v.get(i);
			}
		}

		RConnection conn = null;

		try {
			conn = new RConnection();
			conn.eval("library(Johnson)");
			conn.assign("scores", scoresTemp);
			scoresTemp = conn.eval("RE.Johnson(scores)$transformed").asDoubles();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		j = 0;
		for (int k : scores.keySet()) {
			List<Double> v = scores.get(k);
			for (int i = 0; i < v.size(); i++, j++) {
				v.set(i, scoresTemp[j]);
			}
		}

	}

	// *************************************************************************
	// calculates pvalues of all valid window
	// *************************************************************************
	private void calcPvalues() {
		pvalues = new LinkedHashMap<>();
		scores.forEach((k, v) -> {
			List<Double> temp = new ArrayList<>();
			v.forEach(sco -> {
				double pvalue = Functions.normalPvalue(sco, mean, sd);
				temp.add(pvalue);
			});
			pvalues.put(k, temp);
		});
	}

	// *************************************************************************
	// find new positions based p-values
	// *************************************************************************
	private void findNewPositions() {
		newPositions = new LinkedHashMap<>();
		for (int k : pvalues.keySet()) {
			List<Double> pv = pvalues.get(k);
			List<Integer> temp = new ArrayList<>();
			int pos = 0;
			for (Double p : pv) {
				if (p < significanceLevel) {
					temp.add(pos);
				}
				pos++;
			}
			newPositions.put(k, temp);
		}
	}

	// *************************************************************************
	// return new positions
	// *************************************************************************
	public Map<Integer, List<Integer>> getNewPositions() {
		return newPositions;
	}

	// *************************************************************************
	// return scores
	// **************************************************************************
	public Map<Integer, List<Double>> getScores() {
		return scores;
	}

}
