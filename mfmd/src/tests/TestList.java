package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sources.Solution;

public class TestList {

	public static void main(String[] args) {

		List<Solution> solutions = new ArrayList<Solution>();

		Solution s1 = new Solution(Arrays.asList(1, 1, 2), 10d);
		Solution s2 = new Solution(Arrays.asList(10, 10, 20), 100d);
		Solution s3 = new Solution(Arrays.asList(100, 100, 200), 1000d);

		solutions.add(s1);
		solutions.add(s2);
		solutions.add(s3);

		Solution s = solutions.get(0);

		solutions.clear();

		System.out.println(s.getPositions());

	}

}
