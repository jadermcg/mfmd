package heuristics;

import interfaces.Heuristics;
import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import sources.Solution;
import util.Dataset;

public class ILS implements Heuristics {

	private int r;
	private Score ic;
	private Dataset dataset;
	private int w;

	public ILS(Dataset dataset, Score ic, int r, int w) {
		this.dataset = dataset;
		this.ic = ic;
		this.r = r;
		this.w = w;
	}

	@Override
	public Solution calculates(Solution s) {
		Solution corrente = s.clone();
		Solution melhor = corrente.clone();
		int parada = 1000;
		int k = 10;
		while (parada > 0) {
			int j = 1;
			// busca
			while (j == 1) {
				corrente = busca_local(corrente, r);
				double scoreCorrente = ic.calculates(dataset.getMsa(corrente.getPositions(), w));
				double scoreMelhor = ic.calculates(dataset.getMsa(melhor.getPositions(), w));

				if (scoreCorrente > scoreMelhor) {
					melhor = corrente;
					j = 1;
				} else {
					j = 0;
				}
			}

			// perturbacao
			corrente = gera_vizinho(corrente, k);

			parada--;
		}

		return melhor;
	}

	// gera vizinho com distancia k
	private Solution gera_vizinho(Solution s, int k) {
		Solution v = s.clone();
		while (k > 0) {
			int posicao = new Random().nextInt(dataset.getSize());
			int valor = new Random().nextInt(dataset.getValidPositions());
			v.getPositions().set(posicao, valor);
			k--;
		}

		return v;
	}

	// realiza busca local
	private Solution busca_local(Solution s, int r) {
		List<Solution> solutions = new ArrayList<>();
		while (r > 0) {
			int p1 = new Random().nextInt(dataset.getSize());
			int p2 = new Random().nextInt(dataset.getSize());
			Solution v = s.clone();
			int temp = v.getPositions().get(p1);
			v.getPositions().set(p1, v.getPositions().get(p2));
			v.getPositions().set(p2, temp);

			double score = ic.calculates(dataset.getMsa(v.getPositions(), w));
			v.setScore(score);

			solutions.add(v);
			r--;
		}

		return solutions.stream().max(Comparator.comparing(Solution::getScore)).get();
	}

}
