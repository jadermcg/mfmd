package tests;

import heuristics.Grasp;
import interfaces.Score;

import java.util.Arrays;
import java.util.List;

import scores.InformationContentScore;
import sources.Background;
import sources.EstimaPosicoesIniciais;
import sources.Profile;
import sources.Solution;
import util.Dataset;
import util.Functions;

public class TestSemi {

	public static void main(String[] args) {

		int w = 5;
		boolean dimmer = true;

		Dataset dataset = new Dataset(System.getProperty("user.home")
				+ "/datasets/final/real/dataset_crp/dataset.fa", dimmer, w);

		Background bg = new Background(dataset, 1, 1);

		Score score = new InformationContentScore(bg);

		EstimaPosicoesIniciais epi = new EstimaPosicoesIniciais(dataset, bg, 5);
		epi.start();
		System.out.println("posicoes estimadas -> " + epi.getPosicoes() + "\n");

		Grasp grasp = new Grasp(dataset, w, epi.getPosicoes(), score, false);
		grasp.start();

		Solution best = grasp.getBestSolution();

		List<Integer> positions = best.getPositions();

		Profile p = new Profile(dataset.getMsa(positions, w), bg);

		int pfm[][] = p.getPfm();
		double pwm[][] = p.getPwm();

		double T[][] = new double[pfm.length][pwm[0].length];

		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < T[0].length; j++) {
				T[i][j] = pfm[i][j] * pwm[i][j];
			}
		}

		T = Functions.transversa(T);
		double colunas[] = new double[T.length];

		for (int i = 0; i < T.length; i++) {
			colunas[i] = Arrays.stream(T[i]).sum();
		}

		double pvalor = 1;

		for (int i = 0; i < colunas.length; i++) {
			pvalor *= Functions.tStudentPvalue(colunas[i], 3);
		}

		System.out.println(pvalor);

	}
}
