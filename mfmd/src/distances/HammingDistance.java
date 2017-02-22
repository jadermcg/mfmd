package distances;

import interfaces.Distance;

/**
 * Implementacao do calculo ad distancia de hamming entre dois Strings
 * 
 * @author jadermcg
 *
 */
public class HammingDistance implements Distance {

	@Override
	public Double calculates(String seq1, String seq2) {
		Double distance = 0d;
		for (int i = 0; i < seq1.length(); i++) {
			if (seq1.charAt(i) != seq2.charAt(i))
				distance++;
		}

		return distance;
	}

}
