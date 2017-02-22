package heuristics;

import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sources.Solution;
import sources.TreeSolution;
import util.Dataset;

public class Grasp {

	// *************************************************************************
	// atributes
	// **************************************************************************
	private Dataset dataset;
	private int w;
	private Score score;
	private List<List<Integer>> paths;
	private List<Solution> solutions;
	private boolean greedy;
	private List<Integer> positions;

	// *************************************************************************
	// constructores
	// **************************************************************************
	public Grasp(Dataset dataset, int w, List<Integer> initialPositions, Score score, boolean greedy) {
		this.dataset = dataset;
		this.w = w;
		this.positions = initialPositions;
		this.score = score;
		this.greedy = greedy;
		paths = new ArrayList<>();
		solutions = new ArrayList<>();
	}

	public Grasp(Dataset dataset, int w, int validPositions, Score score, boolean greedy) {
		this.dataset = dataset;
		this.w = w;
		this.score = score;
		this.greedy = greedy;
		paths = new ArrayList<>();
		solutions = new ArrayList<>();
		positions = new ArrayList<>();
		// add posicoes validas no array posicoes
		for (int i = 0; i < validPositions; i++) {
			positions.add(i);
		}
	}

	// *************************************************************************
	// start method
	// **************************************************************************
	public void start() {
		getPaths();
		buildSolutions();
	}

	// *************************************************************************
	// construct tree and get all paths
	// **************************************************************************
	private void getPaths() {
		for (int i : positions) {
			TreeSolution treeSolution = new TreeSolution(i, w, dataset, score, greedy);
			Map<Integer, List<Integer>> p = treeSolution.getPaths();
			for (int key : p.keySet()) {
				paths.add(p.get(key));
			}
			System.out.println("contruindo arvore " + i);
		}
		System.out.println();
	}

	// *************************************************************************
	// construct solutions with paths
	// **************************************************************************
	private void buildSolutions() {
		for (List<Integer> v : paths) {
			List<String> msa = dataset.getMsa(v, w);
			double s = score.calculates(msa);
			solutions.add(new Solution(v, s));
		}

	}

	// *************************************************************************
	// return best solution
	// **************************************************************************
	public Solution getBestSolution() {
		return solutions.stream().max((o1, o2) -> o1.getScore().compareTo(o2.getScore())).get();
	}

	// *************************************************************************
	// return all sorted solutions
	// **************************************************************************
	public List<Solution> getSolutions() {
		return solutions.stream().sorted(Comparator.comparing(Solution::getScore).reversed())
				.collect(Collectors.toList());
	}
}
