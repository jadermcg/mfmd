package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.rosuda.REngine.Rserve.RConnection;

import sources.Background;

/**
 * Classe com funcoes matemáticas uteis
 * 
 * @author jadermcg
 *
 */
public class Functions {

	public static String NORMAL = "NORMAL";
	public static String T_STUDENTE = "T_STUDENTE";
	public static String CHI_SQUARE = "CHI_SQUARE";

	// *************************************************
	// calculate specified base logarithm
	// *************************************************
	public static Double logarithm(double n, double base) {
		return Math.log(n) / Math.log(base);
	}

	// *************************************************
	// calculate sum of double array values
	// *************************************************
	public static Double sum(List<Double> values) {
		Double sum = 0d;
		for (Double d : values) {
			sum += d;
		}
		return sum;
	}

	// *************************************************
	// calculate mean of double array values
	// *************************************************
	public static Double mean(List<Double> values) {
		return new Mean().evaluate(values.stream().mapToDouble(Double::doubleValue).toArray());
	}

	// *************************************************
	// calculate mean of double map values
	// *************************************************
	public static Double mean(Map<Integer, List<Double>> values) {
		List<Double> allValues = new ArrayList<>();
		for (int k : values.keySet()) {
			List<Double> temp = values.get(k);
			allValues.addAll(temp);
		}

		return new Mean().evaluate(allValues.stream().mapToDouble(Double::doubleValue).toArray());

	}

	// **************************************************************
	// calculate standard deviation of double array values
	// **************************************************************
	public static Double sd(List<Double> values) {
		return new StandardDeviation().evaluate(values.stream().mapToDouble(Double::doubleValue)
				.toArray());
	}

	// **************************************************************
	// calculate standard deviation of double map values
	// **************************************************************
	public static Double sd(Map<Integer, List<Double>> values) {

		List<Double> allValues = new ArrayList<>();
		for (int k : values.keySet()) {
			List<Double> temp = values.get(k);
			allValues.addAll(temp);
		}

		return new StandardDeviation().evaluate(allValues.stream().mapToDouble(Double::doubleValue)
				.toArray());
	}

	// ******************************************************************
	// calculate normal distribution p-values of double array values
	// ******************************************************************
	public static ArrayList<Double> normalPvalue(List<Double> values) {
		ArrayList<Double> pvalues = new ArrayList<>();
		double mean = mean(values);
		double sd = sd(values);
		NormalDistribution nd = new NormalDistribution(mean, sd);
		values.forEach(v -> pvalues.add(1 - nd.cumulativeProbability(v)));
		return pvalues;
	}

	// ******************************************************************
	// calculate normal distribution p-values of single double values
	// ******************************************************************
	public static double normalPvalue(double value, double mean, double sd) {
		NormalDistribution nd = new NormalDistribution(mean, sd);
		return 1 - nd.cumulativeProbability(value);
	}

	// ********************************************************************
	// calculate t-student distribution p-values of double array values
	// ********************************************************************
	public static ArrayList<Double> tStudentPvalue(List<Double> values) {
		ArrayList<Double> pvalues = new ArrayList<>();
		TDistribution nd = new TDistribution(values.size() - 1);
		values.forEach(v -> pvalues.add(1 - nd.cumulativeProbability(v)));
		return pvalues;
	}

	// **********************************************************************
	// calculate t-student distribution p-values of single double value
	// **********************************************************************
	public static double tStudentPvalue(double value, double degreeOfFreedom) {
		TDistribution nd = new TDistribution(degreeOfFreedom);
		return 1 - nd.cumulativeProbability(value);
	}

	// ******************************************************************
	// calculate chi-square distribution p-values of double array values
	// ******************************************************************
	public static ArrayList<Double> chiSquarePvalue(List<Double> values) {
		ArrayList<Double> pvalues = new ArrayList<>();
		ChiSquaredDistribution nd = new ChiSquaredDistribution(values.size() - 1);
		values.forEach(v -> pvalues.add(1 - nd.cumulativeProbability(v)));
		return pvalues;
	}

	// ******************************************************************
	// calculate chi-square distribution p-values of single double values
	// ******************************************************************
	public static double chiSquarePvalue(double value, int degreeOfFreedom) {
		ChiSquaredDistribution nd = new ChiSquaredDistribution(degreeOfFreedom);
		return 1 - nd.cumulativeProbability(value);
	}

	// *************************************************
	// permute symbols
	// *************************************************
	public static ArrayList<String> permutation(String alphabet[], int size) {
		ArrayList<String> perm = new ArrayList<>();

		int sizeOfAlphabet = alphabet.length;
		int numberOfPermutations = (int) Math.pow(sizeOfAlphabet, size);
		String mPerm[][] = new String[numberOfPermutations][size];

		// permutation
		for (int j = 0; j < mPerm[0].length; j++) {
			int numberOfPermutationInEachSymbol = (int) Math.pow(sizeOfAlphabet, j);
			int t = numberOfPermutationInEachSymbol;
			int idx = 0;
			for (int i = 0; i < mPerm.length; i++) {
				if (t > 0) {
					mPerm[i][j] = alphabet[idx];
					t--;
				}

				if (t == 0) {
					t = numberOfPermutationInEachSymbol;
					idx++;
				}

				if (idx == sizeOfAlphabet)
					idx = 0;
			}
		}

		// from matrix to arraysList
		for (int i = 0; i < mPerm.length; i++) {
			String temp = "";
			for (int j = mPerm[0].length - 1; j >= 0; j--) {
				temp = temp.concat(mPerm[i][j]);
			}
			perm.add(temp);
		}

		return perm;
	}

	// *************************************************
	// normalize array - MIN-MAX
	// *************************************************
	public static List<Double> minMaxNormalize(List<Double> array) {
		ArrayList<Double> normArray = new ArrayList<>();
		double min = array.stream().min(Comparator.comparing(Double::doubleValue)).get();
		double max = array.stream().max(Comparator.comparing(Double::doubleValue)).get();

		array.forEach(v -> {
			Double temp = (max - v) / (max - min);
			normArray.add(temp);
		});

		return normArray;
	}

	// *************************************************
	// normalize array - default
	// *************************************************
	public static List<Double> normalize(List<Double> array) {
		ArrayList<Double> normArray = new ArrayList<>();

		final double sum = array.stream().mapToDouble(Double::doubleValue).sum();

		array.forEach(v -> normArray.add(v / sum));

		return normArray;
	}

	// *************************************************
	// select a position based a probabilistic ruler
	// *************************************************
	public static int rulerProbabilisticSelect(List<Double> array) {

		int position = 0;

		// ruler
		ArrayList<Double> ruler = new ArrayList<>();
		for (int j = 0, k = 1; k <= array.size(); k++) {
			List<Double> temp = array.subList(j, k);
			Double r = 0d;
			for (Double t : temp) {
				r += t;
			}
			ruler.add(r);
		}

		// select o position basead a probability ruler
		Double random = Math.random();

		for (Double r : ruler) {
			if (random < r) {
				position = ruler.indexOf(r);
				break;
			}
		}

		return position;
	}

	// *************************************************
	// return duplicated values into a list
	// *************************************************
	public static Set<Double> getDuplicatedValues(List<Double> list) {
		Set<Double> duplicated = list.stream().filter(v -> Collections.frequency(list, v) > 1)
				.collect(Collectors.toSet());
		return duplicated;
	}

	// *************************************************
	// return duplicated index into a list
	// *************************************************
	public static List<Integer> getDuplicatedIndexes(List<Double> list) {
		ArrayList<Integer> idxDuplicated = new ArrayList<>();
		ArrayList<Double> numbers = new ArrayList<>();
		list.forEach(v -> numbers.add(v));
		Set<Double> duplicated = numbers.stream()
				.filter(v -> Collections.frequency(numbers, v) > 1).collect(Collectors.toSet());
		duplicated.forEach(d -> {
			int idx = 0;
			while ((idx = numbers.indexOf(d)) != -1) {
				idxDuplicated.add(idx);
				numbers.set(idx, Math.random());
			}
		});

		return idxDuplicated;
	}

	// *************************************************
	// round number
	// *************************************************
	public static Double round(double number, double factor) {
		int r = (int) Math.round(number * factor);
		return r / factor;
	}

	// *************************************************
	// generate nucleotide from background probability
	// *************************************************
	public static String generateNucleotideFromBgProbability(Background bg) {
		double probs[] = bg.getProbabilities().values().stream().mapToDouble(Double::doubleValue)
				.toArray();
		String seq = "";
		double r = Math.random();
		if (r <= probs[0])
			seq = seq.concat("a");
		else if (r > probs[0] && r <= probs[0] + probs[1])
			seq = seq.concat("c");
		else if (r > probs[0] + probs[1] && r <= probs[0] + probs[1] + probs[2])
			seq = seq.concat("g");
		else if (r > probs[0] + probs[1] + probs[2]) {
			seq = seq.concat("t");
		}
		return seq;
	}

	// *************************************************
	// generate list of number between min and max
	// *************************************************
	public static List<Integer> numberGeneratedBetween(int min, int max, int amount) {
		List<Integer> numbers = null;

		RConnection conn = null;

		try {
			conn = new RConnection();
			int seed[] = { (int) (Math.random() * 1000000d) };
			conn.assign("seed", seed);
			conn.eval("set.seed(seed)");
			conn.assign("min", new int[] { min });
			conn.assign("max", new int[] { max });
			conn.assign("amount", new int[] { amount });
			int temp[] = conn.eval("sample(min:max,amount)").asIntegers();
			numbers = Arrays.stream(temp).boxed().collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return numbers;
	}

	// *************************************************
	// generate sample
	// *************************************************
	public static List<Integer> sample(List<Integer> list, int amount, int replacement) {
		List<Integer> numbers = null;
		RConnection conn = null;
		int temp[] = null;
		try {
			conn = new RConnection();
			int seed[] = { (int) (Math.random() * 1000000d) };
			conn.assign("seed", seed);
			conn.eval("set.seed(seed)");
			conn.assign("list", list.stream().mapToInt(Integer::intValue).toArray());
			conn.assign("amount", new int[] { amount });
			conn.assign("replacement", new int[] { replacement });
			temp = conn.eval("sample(x=list, size=amount, replace=replacement)").asIntegers();
			numbers = Arrays.stream(temp).boxed().collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return numbers;
	}

	// *************************************************
	// devolve a matriz transversa
	// *************************************************
	public static double[][] transversa(double[][] pfm) {
		double T[][] = new double[pfm[0].length][pfm.length];

		for (int i = 0; i < pfm.length; i++) {
			for (int j = 0; j < pfm[i].length; j++) {
				T[j][i] = pfm[i][j];
			}
		}

		return T;
	}

	// *************************************************
	// calcula fatorial de um numero
	// *************************************************
	public static BigDecimal fatorial(int n) {
		BigDecimal fat = new BigDecimal(1);
		for (int i = n; i > 1; i--) {
			fat = fat.multiply(new BigDecimal(i));
		}
		return fat;
	}

	// *************************************************
	// calcula fatorial de um numero
	// *************************************************
	public static BigDecimal multiplicatorio(BigDecimal[] d) {
		BigDecimal m = new BigDecimal(1);

		for (int i = 0; i < d.length; i++) {
			m = m.multiply(d[i]);
		}

		return m;
	}

}
