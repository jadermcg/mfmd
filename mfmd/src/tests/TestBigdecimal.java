package tests;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestBigdecimal {

	public static void main(String[] args) {

		BigDecimal b = new BigDecimal(100.55258);
		System.out.println(b.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));

	}

}
