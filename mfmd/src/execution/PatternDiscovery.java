package execution;

import heuristics.Grasp;
import heuristics.Vnd;
import interfaces.Heuristics;
import interfaces.Score;

import java.util.List;
import java.util.stream.Collectors;

import memetico.Memetico;
import sources.Background;
import sources.EstimaPosicoesIniciais;
import sources.Solution;
import util.Dataset;

public class PatternDiscovery {

	// ********************************************************
	// atributos
	// ********************************************************
	private Dataset dataset;
	private Score score;
	private Background bg;
	private List<Integer> initialPositions;
	private int nPosicoes;
	private List<Solution> P;
	private Solution best;
	private boolean greedy;

	// ********************************************************
	// construtor publico
	// ********************************************************
	public PatternDiscovery(boolean greedy, Dataset dataset, Background bg, Score score,
			int nPosicoes) {
		this.greedy = greedy;
		this.dataset = dataset;
		this.bg = bg;
		this.score = score;
		this.nPosicoes = nPosicoes;
	}

	// ********************************************************
	// metodo principal
	// ********************************************************
	public void start() {
		estimaPosicoesIniciais();
		constroiArvores();
		rodaMemetico();
	}

	// ********************************************************
	// estima melhores posicoes iniciais
	// ********************************************************
	private void estimaPosicoesIniciais() {
		System.out.println("Estimando posicoes iniciais...");
		EstimaPosicoesIniciais epi = new EstimaPosicoesIniciais(dataset, bg, nPosicoes);
		epi.start();
		initialPositions = epi.getPosicoes();
		System.out.println("\nMelhores posicoes iniciais");
		System.out.println(epi.getPosicoes() + "\n");
	}

	// ********************************************************
	// constroi arvores com melhores posicoes
	// ********************************************************
	private void constroiArvores() {
		System.out.println("construindo arvores...");
		// usa posicoes iniciais estimadas como sementes para construir as
		// arvores
		Grasp grasp = new Grasp(dataset, dataset.getW(), initialPositions, score, greedy);
		grasp.start();
		best = grasp.getBestSolution();
		P = grasp.getSolutions().stream().limit(50).collect(Collectors.toList());
		System.out.println("melhor arvore -> " + best.getPositions().get(0) + "\n");
	}

	// ********************************************************
	// roda algoritmo memetico
	// ********************************************************
	private void rodaMemetico() {
		System.out.println("roda memetico...");
		int n_pop = P.size();
		int nGeneration = 100;
		int nr_recombination = n_pop * 8;
		int nr_mutation = (int) (nr_recombination * 0.1);
		// Heuristics h = new HillClimbing(score, dataset, dataset.getW(),
		// dataset.getValidPositions(), 100);
		Heuristics h = new Vnd(dataset, score, 5, dataset.getW());
		Memetico memetico = new Memetico(P, n_pop, nGeneration, nr_recombination, nr_mutation,
				dataset, score, h);
		memetico.start();
		best = memetico.getBestSolution();
		System.out.println("memetico aplicado.\n");
	}

	/**
	 * getters and setters
	 */

	public Solution getBest() {
		return best;
	}

}
