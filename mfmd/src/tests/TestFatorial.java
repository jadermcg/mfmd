package tests;

import java.math.BigDecimal;

import util.Functions;

public class TestFatorial {

	public static void main(String[] args) {

		System.out.println(Functions.fatorial(22));

		BigDecimal d[] = new BigDecimal[3];
		d[0] = new BigDecimal(5);
		d[1] = new BigDecimal(5);
		d[2] = new BigDecimal(5);

		System.out.println(Functions.multiplicatorio(d));

	}

}
