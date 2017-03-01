package heuristics;

import interfaces.Heuristics;
import interfaces.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import sources.Solution;
import util.Dataset;

public class Vnd implements Heuristics {

	private int r;
	private Score ic;
	private Dataset dataset;
	private int w;

	public Vnd(Dataset dataset, Score ic, int r, int w) {
		this.dataset = dataset;
		this.ic = ic;
		this.r = r;
		this.w = w;
	}

	@Override
	public Solution calculates(Solution s) {
		int parada = 100;

		while (parada > 0) {
			int k = 1;
			while (k <= r) {
				Solution s1 = busca_local(gera_vizinho(s, k), r);
				double scoreS1 = ic.calculates(dataset.getMsa(s1.getPositions(), dataset.getW()));
				double scoreS = ic.calculates(dataset.getMsa(s.getPositions(), dataset.getW()));

				if (scoreS1 > scoreS) {
					s = s1;
					k = 1;
				} else {
					k = k + 1;
				}
			}
			parada--;
		}

		return s;
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
