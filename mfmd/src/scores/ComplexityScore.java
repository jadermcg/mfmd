package scores;

import interfaces.Score;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import sources.Background;
import sources.Profile;
import util.Functions;

public class ComplexityScore implements Score {

	private Background bg;

	public ComplexityScore(Background bg) {
		this.bg = bg;
	}

	@Override
	public Double calculates(List<String> msa) {
		double complexity = 0;
		// calcula tamanho do w
		int w = msa.get(0).length();

		// calcula fatorial de w
		BigDecimal fat_w = Functions.fatorial(w);

		// cria profile
		Profile p = new Profile(msa, bg);

		// extrai pfm
		int pfm[][] = p.getPfm();

		for (int j = 0; j < pfm[0].length; j++) {
			BigDecimal fat[] = new BigDecimal[4];
			for (int i = 0; i < pfm.length; i++) {
				fat[i] = Functions.fatorial(pfm[i][j]);
			}

			BigDecimal multiplicatorio = Functions.multiplicatorio(fat);
			double result = fat_w.divide(multiplicatorio, 2, RoundingMode.HALF_UP).doubleValue();
			complexity += Functions.logarithm(result, 4);
		}

		return complexity;
	}

}
