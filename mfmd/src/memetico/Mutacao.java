package memetico;

import interfaces.Score;

import java.util.List;

import sources.Solution;
import util.Dataset;
import util.Functions;

public class Mutacao {

	// *******************************************************
	// atributos
	// *******************************************************
	private int nr_mutation;
	private Dataset dataset;
	private Score score;
	private int n_pop;
	private List<Solution> Q;

	// *******************************************************
	// construtor publico
	// *******************************************************
	public Mutacao(List<Solution> Q, int nr_mutation, int n_pop, Dataset dataset, Score score) {
		this.Q = Q;
		this.nr_mutation = nr_mutation;
		this.n_pop = n_pop;
		this.dataset = dataset;
		this.score = score;
	}

	// *******************************************************
	// metodo principal
	// *******************************************************
	public void start() {
		for (int i = 0; i < nr_mutation; i++) {
			// quantas posicoes para mutacionar
			int quantasPosicoes = Functions.numberGeneratedBetween(0, dataset.getSize() - 1, 1)
					.get(0);

			for (int j = 0; j < quantasPosicoes; j++) {
				// qual individuo mutacionar
				int qualIndividuo = Functions.numberGeneratedBetween(0, n_pop - 1, 1).get(0);

				// qual posicao mutacionar
				int qualPosicao = Functions.numberGeneratedBetween(0, dataset.getSize() - 1, 1)
						.get(0);

				// qual valor colocar
				int qualValor = Functions.numberGeneratedBetween(0,
						dataset.getValidPositions() - 1, 1).get(0);

				// seleciona motif da populacao intermediaria
				Solution motif = Q.get(qualIndividuo).clone();

				// calcula score antes da mutacao
				double scoreAntes = score.calculates(dataset.getMsa(motif.getPositions(),
						dataset.getW()));

				// efetua mutacao
				motif.getPositions().set(qualPosicao, qualValor);

				// calcula score apos mutacao
				double scoreDepois = score.calculates(dataset.getMsa(motif.getPositions(),
						dataset.getW()));

				// se scoreDepois > scoreAntes faz conclui mutacao
				if (scoreDepois > scoreAntes)
					Q.get(qualIndividuo).getPositions().set(qualPosicao, qualValor);
			}
		}
	}
}
