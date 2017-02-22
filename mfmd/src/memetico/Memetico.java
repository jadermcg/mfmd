package memetico;

import interfaces.Heuristics;
import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import sources.Solution;
import util.Dataset;
import util.Functions;

/**
 * Observacao: para utilizar mutacao descomentar codigo no metodo start();
 * 
 * @author jadermcg
 *
 */

@SuppressWarnings("unused")
public class Memetico {

	// *******************************************************
	// atributos
	// *******************************************************
	private List<Solution> P;
	private List<Solution> Q;
	private List<Solution> R;
	private int n_pop;
	private int nGeneration;
	private int nr_recombination;
	private int nr_mutation;
	private Dataset dataset;
	private Score score;
	private Heuristics h;

	// *******************************************************
	// construtor publico
	// *******************************************************
	public Memetico(List<Solution> P, int n_pop, int nGeneration, int nr_recombination,
			int nr_mutation, Dataset dataset, Score score, Heuristics h) {
		this.P = P;
		this.n_pop = n_pop;
		this.nGeneration = nGeneration;
		this.nr_recombination = nr_recombination;
		this.nr_mutation = nr_mutation;
		this.dataset = dataset;
		this.score = score;
		this.h = h;
		Q = new ArrayList<>();
		R = new ArrayList<>();
	}

	// *******************************************************
	// metodo principal
	// *******************************************************
	public void start() {
		int k = 0;

		while (k < nGeneration) {
			recombination();
			// mutation();
			joinPopulations();
			selection();

			// proximo geracao
			System.out.print("!");
			k++;
		}
		System.out.println();
	}

	// *******************************************************
	// faz a recombinacao binaria
	// *******************************************************
	private void recombination() {
		// solucoes que participarao da recombinacao
		List<Integer> spar = Functions.numberGeneratedBetween(0, n_pop - 1, n_pop / 2);

		// enquanto a populacao Q for menor que numero de recombinacao
		while (Q.size() < nr_recombination) {
			// solucoes selecionadas para recombinacao na iteracao
			List<Integer> pos = Functions.sample(spar, 2, 0);

			// pai_1 e pai_2
			Solution p1 = P.get(pos.get(0));
			Solution p2 = P.get(pos.get(1));

			// pega score de pai_1 e pai_2
			double scoreP1 = p1.getScore();
			double scoreP2 = p2.getScore();

			// melhor pai
			double saux = 0d;
			if (scoreP1 > scoreP2)
				saux = scoreP1;
			else
				saux = scoreP2;

			// executa recombinacao
			int corte = Functions.numberGeneratedBetween(1, p1.getPositions().size() - 1, 1).get(0);
			Recombinacao recomb = new Recombinacao(p1, p2, corte);
			recomb.start();

			// filhos
			Solution c1 = recomb.getC1();
			Solution c2 = recomb.getC2();

			// pega score filho_1 e e filho_2
			double score_c1 = score.calculates(dataset.getMsa(c1.getPositions(), dataset.getW()));
			c1.setScore(score_c1);
			double score_c2 = score.calculates(dataset.getMsa(c2.getPositions(), dataset.getW()));
			c2.setScore(score_c2);

			// testa score dos filhos
			if (saux > score_c1) {
				c1.setScore(score_c1);
				c1 = h.calculates(c1);
			}

			if (saux > score_c2) {
				c2.setScore(score_c2);
				c2 = h.calculates(c2);
			}

			// adiciona filhos na populacao intermediaria
			Q.add(c1);
			Q.add(c2);
		}

	}

	// *******************************************************
	// mutacao multi-gene
	// *******************************************************
	private void mutation() {
		Mutacao mut = new Mutacao(Q, nr_mutation, n_pop, dataset, score);
		mut.start();
	}

	// *******************************************************
	// junta populacao P e Q na R
	// *******************************************************
	private void joinPopulations() {
		R.clear();
		P.forEach(v -> R.add(v.clone()));
		Q.forEach(v -> R.add(v.clone()));
		Q.clear();
	}

	// *******************************************************
	// coloca as |P| melhores solucoes de R em P
	// *******************************************************
	private void selection() {
		P = R.stream().sorted(Comparator.comparing(Solution::getScore).reversed()).limit(n_pop)
				.collect(Collectors.toList());
	}

	// *******************************************************
	// retorna a melhor solucao encontrada
	// *******************************************************
	public Solution getBestSolution() {
		return P.stream().max(Comparator.comparing(Solution::getScore)).get();
	}

	// *******************************************************
	// retorna a populacao ordenada
	// *******************************************************
	public List<Solution> getP() {
		return P.stream().sorted(Comparator.comparing(Solution::getScore).reversed())
				.collect(Collectors.toList());
	}

}
