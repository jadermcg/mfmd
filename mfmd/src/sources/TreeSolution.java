package sources;

import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.Dataset;
import util.Functions;

@SuppressWarnings("unchecked")
public class TreeSolution {

	// ******************************************
	// atributes
	// ******************************************
	private Node root;
	private int w;
	private Dataset dataset;
	private Score score;
	private Map<Integer, List<Integer>> paths;
	private boolean greedy;

	// ******************************************
	// public constructor
	// ******************************************
	public TreeSolution(int initialPosition, int w, Dataset dataset, Score score, boolean greedy) {
		this.w = w;
		this.dataset = dataset;
		this.score = score;
		this.greedy = greedy;
		root = new Node(initialPosition);
		paths = new LinkedHashMap<>();
		start();
	}

	// ******************************************
	// start class atributes
	// ******************************************
	private void start() {
		treeGeneration();
	}

	// ******************************************
	// generate tree based on score
	// ******************************************
	private void treeGeneration() {
		int n = dataset.getSize();
		for (int i = 1; i < n; i++) {
			// subsequences
			List<String> subSequences = dataset.getSubsequences(i);

			// pathway tree
			Enumeration<Node> e = root.preorderEnumeration();

			while (e.hasMoreElements()) {
				Node node = e.nextElement();
				if (node.isLeaf()) {
					// create positions object
					Object positions[] = node.getUserObjectPath();
					// create a msa based a positions
					List<String> msa = dataset.getMsa(positions, w);
					// calculates score all windows
					List<Double> scores = new ArrayList<>();
					for (String sub : subSequences) {
						msa.add(sub);
						double c = Functions.round(score.calculates(msa), 100d);
						scores.add(c);
						msa.remove(sub);
					}

					// find best value into list of temporary scores
					double bestValue = scores.stream()
							.max(Comparator.comparing(Double::doubleValue)).get();

					if (greedy) {
						// next position in this tree -> add only best position
						// into
						// tree
						int bestIdx = scores.indexOf(bestValue);
						node.add(new Node(bestIdx));
					}

					else {
						// next position in this tree -> add all tied positions
						// into
						// tree
						for (int j = 0; j < scores.size(); j++) {
							if (scores.get(j) == bestValue)
								node.add(new Node(j));
						}
					}
				}
			}
		}
	}

	// ******************************************
	// returns root element
	// ******************************************
	public Node getTree() {
		return root;
	}

	// ******************************************
	// returns root element
	// ******************************************
	public Map<Integer, List<Integer>> getPaths() {
		Enumeration<Node> e = root.preorderEnumeration();
		int i = 0;
		while (e.hasMoreElements()) {
			Node node = e.nextElement();

			if (node.isLeaf()) {
				Object temp[] = node.getUserObjectPath();
				List<Integer> path = new ArrayList<>();
				for (int j = 0; j < temp.length; j++) {
					path.add((int) temp[j]);
				}
				paths.put(i++, path);
			}
		}

		return paths;
	}

}
