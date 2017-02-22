package sources;

import java.util.ArrayList;
import java.util.List;

public class Solution {

	// ****************************************************
	// atributes
	// ****************************************************
	private List<Integer> positions;
	private Double score;

	// ****************************************************
	// public constructor
	// ****************************************************
	public Solution(List<Integer> positions, Double scores) {
		this.positions = positions;
		score = scores;
	}

	// ****************************************************
	// private constructor
	// ****************************************************
	private Solution() {

	}

	// ****************************************************
	// return positions
	// ****************************************************
	public List<Integer> getPositions() {
		return positions;
	}

	// ****************************************************
	// return scores
	// ****************************************************
	public Double getScore() {
		return score;
	}

	// ****************************************************
	// set score
	// ****************************************************
	public void setScore(double score) {
		this.score = score;
	}

	// ****************************************************
	// clone solution
	// ****************************************************
	public Solution clone() {
		Solution clone = new Solution();
		List<Integer> pos = new ArrayList<>();
		positions.forEach(v -> pos.add(v));
		clone.positions = pos;
		clone.score = score;
		return clone;
	}
}
