package sources;

import interfaces.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import scores.InformationContentScore;
import util.Dataset;

public class EstimaPosicoesIniciais {

	// **********************************************************
	// atributos
	// **********************************************************
	private Dataset dataset;
	private Background bg;
	private List<Profile> profiles;
	private int validPositions;
	private int N;
	private int w;
	private List<Integer> posicoes;
	private int nPosicoes;

	// **********************************************************
	// construtor publico
	// **********************************************************
	public EstimaPosicoesIniciais(Dataset dataset, Background bg, int nPosicoes) {
		this.dataset = dataset;
		this.bg = bg;
		this.nPosicoes = nPosicoes;
		validPositions = this.dataset.getValidPositions();
		N = this.dataset.getSize();
		w = this.dataset.getW();
		profiles = new ArrayList<>();
		posicoes = new ArrayList<>();
	}

	// **********************************************************
	// metodo principal
	// **********************************************************
	public void start() {
		geraProfiles();
		reestimaProfiles();
		achaMelhoresPosicoesIniciais();
	}

	// **********************************************************
	// gera profiles da primeira sequencia
	// **********************************************************
	private void geraProfiles() {
		for (int i = 0; i < validPositions; i++) {
			String sub = dataset.getSubsequence(0, i);
			Profile p = new Profile(Arrays.asList(sub), bg);
			profiles.add(p);
		}
	}

	// **********************************************************
	// gera profiles da primeira sequencia
	// **********************************************************
	private void reestimaProfiles() {
		// numero de vezes que a reestimacao e executada
		int n = 5;

		while (n > 0) {
			// para cada profile
			for (int i = 0; i < profiles.size(); i++) {
				Profile p = profiles.get(i);
				List<Integer> posicoes = new ArrayList<>();
				// para cada sequencia
				for (int j = 0; j < N; j++) {
					Map<Integer, Double> scores = new LinkedHashMap<>();
					// para cada posicao valida
					for (int z = 0; z < validPositions; z++) {
						String sub = dataset.getSubsequence(j, z);
						double score = Math.log(p.probSequenceGivenModel(sub)
								/ bg.probSequenceGivenBackground(sub));
						scores.put(z, score);
					}
					// ordena score e extrai melhor posicao
					posicoes.add(scores.entrySet().stream()
							.sorted(Map.Entry.<Integer, Double> comparingByValue().reversed())
							.map(v -> {
								return v.getKey();
							}).collect(Collectors.toList()).get(0));
				}
				// reestima profile
				p = new Profile(dataset.getMsa(posicoes, w), bg);
				profiles.set(i, p);
			}
			System.out.println("reestimando..." + n);
			n--;
		}
	}

	// **********************************************************
	// encontra melhor profile
	// **********************************************************
	private void achaMelhoresPosicoesIniciais() {
		Map<Integer, Double> profileScores = new LinkedHashMap<>();
		Score ic = new InformationContentScore(bg);
		for (int i = 0; i < profiles.size(); i++) {
			double sco = ic.calculates(profiles.get(i).getMsa());
			profileScores.put(i, sco);
		}

		posicoes = profileScores.entrySet().stream()
				.sorted(Map.Entry.<Integer, Double> comparingByValue().reversed()).map(v -> {
					return v.getKey();
				}).limit(nPosicoes).collect(Collectors.toList());

	}

	/**
	 * getters and setters
	 */

	public List<Integer> getPosicoes() {
		return posicoes;
	}
}
