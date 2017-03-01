package heuristics;

import interfaces.Heuristics;
import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import sources.Solution;
import util.Dataset;

public class Vns implements Heuristics {

	private int r;
	private Score ic;
	private Dataset dataset;
	private int w;

	public Vns(Dataset dataset, Score ic, int r, int w) {
		this.dataset = dataset;
		this.ic = ic;
		this.r = r;
		this.w = w;
	}

	@Override
	public Solution calculates(Solution s) {
		Solution sEstrela = s.clone();
		int parada = 1000;

		while (parada > 0) {
			int k = 1;
			while (k <= r) {
				// gera vizinho
				Solution sLinha = gera_vizinho(sEstrela, k);
				// busca local
				Solution sDuasLinhas = busca_local(sLinha, r);
				// calcula score
				double scoreSduasLinhas = ic.calculates(dataset.getMsa(sDuasLinhas.getPositions(),
						w));
				double scoreSestrela = ic.calculates(dataset.getMsa(sEstrela.getPositions(), w));
				// testa
				if (scoreSduasLinhas > scoreSestrela) {
					sEstrela = sDuasLinhas.clone();
					k = 1;
				} else {
					k++;
				}
			}
			parada--;
		}

		return sEstrela;
	}

	// gera vizinho com distancia k
	private Solution gera_vizinho(Solution sEstrela, int k) {
		Solution v = sEstrela.clone();
		while (k > 0) {
			int posicao = new Random().nextInt(dataset.getSize());
			int valor = new Random().nextInt(dataset.getValidPositions());
			v.getPositions().set(posicao, valor);
			k--;
		}

		return v;
	}

	// realiza busca local
	private Solution busca_local(Solution sLinha, int r) {
		List<Solution> solutions = new ArrayList<>();
		while (r > 0) {
			int p1 = new Random().nextInt(dataset.getSize());
			int p2 = new Random().nextInt(dataset.getSize());
			Solution v = sLinha.clone();
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
