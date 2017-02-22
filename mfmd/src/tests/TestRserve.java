package tests;

import java.util.Arrays;

import org.rosuda.REngine.Rserve.RConnection;

import util.Functions;

public class TestRserve {

	public static void main(String[] args) {

		RConnection connection;

		try {
			connection = new RConnection();
			int seed[] = { (int) (Math.random() * 1000000d) };
			connection.assign("seed", seed);
			connection.eval("set.seed(seed)");
			connection.assign("spar", new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
			double d[] = connection.eval("sample(spar,2)").asDoubles();
			Arrays.stream(d).forEach(System.out::println);
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(Functions.sample(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 2, 0));

		System.out.println(Functions.numberGeneratedBetween(0, 10, 2));
	}

}
