package scores;

import interfaces.Score;

import java.util.List;

import sources.Background;

public class TotalScore implements Score {

	private Background bg;
	private double v1;
	private double v2;

	public TotalScore(Background bg, double v1, double v2) {
		this.bg = bg;
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Double calculates(List<String> msa) {
		Score ic = new InformationContentScore(bg);
		Score cs = new ComplexityScore(bg);

		return v1 * ic.calculates(msa) + v2 * cs.calculates(msa);
	}

}
