package sources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Wam {

	// *****************************************************
	// atributos
	// *****************************************************
	private List<String> msa;
	private int n;
	private List<Map<String, Double>> probabilidades;
	private List<Map<String, Integer>> frequencias;
	private List<Integer> somas;
	private int pseudocontadores;
	private int ordem;
	private List<List<String>> symbols;

	// *****************************************************
	// construtor
	// *****************************************************
	public Wam(List<String> msa, List<List<String>> symbols, int pseudocontadores, int ordem) {
		this.msa = msa;
		this.symbols = symbols;
		this.pseudocontadores = pseudocontadores;
		this.ordem = ordem;
		n = msa.get(0).length();
		probabilidades = new ArrayList<>();
		frequencias = new ArrayList<>();
		somas = new ArrayList<>();
		inicializar();
	}

	// *****************************************************
	// metodo principal
	// *****************************************************
	private void inicializar() {
		permutacao();
		calcFrequencias();
		pseudocontadores();
		somas();
		probabilidades();
	}

	private void permutacao() {
		int o = 1;
		for (int i = 0; i < n; i++) {
			Map<String, Integer> temp = new LinkedHashMap<>();
			if (o < ordem) {
				List<String> tempSymbols = symbols.get(o - 1);
				temp = new LinkedHashMap<>();
				for (String s : tempSymbols) {
					temp.put(s, 0);
				}
				o++;
			}

			else {
				List<String> tempSymbols = symbols.get(o - 1);
				temp = new LinkedHashMap<>();
				for (String s : tempSymbols) {
					temp.put(s, 0);
				}
			}

			frequencias.add(temp);
		}
	}

	// ******************************************************
	// calcula frequencias
	// ******************************************************
	private void calcFrequencias() {
		// primeiras vl colunas
		int i = 0, j = 1;
		while (j < ordem) {
			for (String seq : msa) {
				String sub = seq.substring(i, j);
				frequencias.get(j - 1).replace(sub, frequencias.get(j - 1).get(sub) + 1);
			}
			j++;
		}

		// restante das colunas
		for (String seq : msa) {
			for (i = 0, j = ordem; i < seq.length() - ordem + 1; i++, j++) {
				String sub = seq.substring(i, j);
				frequencias.get(j - 1).replace(sub, frequencias.get(j - 1).get(sub) + 1);
			}
		}

	}

	// ******************************************************
	// pseudocontadores
	// ******************************************************
	private void pseudocontadores() {
		frequencias.forEach(v -> {
			v.forEach((k, s) -> {
				v.replace(k, s + pseudocontadores);
			});
		});
	}

	// ******************************************************
	// somas
	// ******************************************************
	private void somas() {
		int i = 0;
		int soma = 0;
		for (Map<String, Integer> v : frequencias) {
			for (String key : v.keySet()) {
				if (i == 0 || i % 4 != 0) {
					soma += v.get(key);
				} else {
					somas.add(soma);
					soma = 0;
					soma += v.get(key);
				}
				i++;
			}
		}
		somas.add(soma);
	}

	// ******************************************************
	// probabilidades
	// ******************************************************
	private void probabilidades() {
		int i = 0;
		int j = 1;
		for (Map<String, Integer> freqs : frequencias) {
			Map<String, Double> P = new LinkedHashMap<>();
			for (String key : freqs.keySet()) {
				P.put(key, (double) freqs.get(key) / somas.get(i));
				if (j % 4 == 0)
					i++;
				j++;
			}
			probabilidades.add(P);
		}
	}

	// ******************************************************************
	// calcula a probabilidade da sequencia dado o modelo
	// ******************************************************************
	public double probSeqDadoModelo(String seq) {
		double score = 1d;
		List<String> subs = new ArrayList<>();

		// primeiras colunas
		int i = 0;
		int j = 1;
		while (j <= ordem) {
			String sub = seq.substring(i, j);
			subs.add(sub);
			j++;
		}

		// demais colunas
		for (i = 1, j = ordem + 1; i <= seq.length() - ordem; i++, j++) {
			String sub = seq.substring(i, j);
			subs.add(sub);
		}

		// calcula probabilidade
		i = 0;
		for (String sub : subs) {
			score = score * probabilidades.get(i).get(sub);
			i++;
		}

		return score;
	}

	/**
	 * getters and setters
	 * 
	 * @return
	 */

	public List<Map<String, Double>> getProbabilidades() {
		return probabilidades;
	}

	public List<Map<String, Integer>> getFrequencias() {
		return frequencias;
	}

}
