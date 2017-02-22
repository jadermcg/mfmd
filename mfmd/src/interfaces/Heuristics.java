package interfaces;

import java.util.Random;

import sources.Solution;

/**
 * interface thats represents the heuristic for a some solution
 * 
 * @author jadermcg
 *
 */
public interface Heuristics {

	// ************************************************************************
	// calculates the best solution ny a specific heuristic
	// ************************************************************************
	public Solution calculates(Solution actual);

	// ************************************************************************
	// generating neighborhood
	// ************************************************************************
	default Solution neighborhood(Solution best, int validPositions) {

		Random r = new Random();
		Solution temp = best.clone();

		int howManyTimes = r.nextInt(temp.getPositions().size());

		while (howManyTimes > 0) {
			int whatPositionChange = r.nextInt(temp.getPositions().size());
			int whatValuePut = r.nextInt(validPositions);
			temp.getPositions().set(whatPositionChange, whatValuePut);
			howManyTimes--;
		}

		return temp;
	}
}
