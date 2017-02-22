package util;

/**
 * Utilitary class to print objects
 * 
 * @author jadermcg
 *
 */
public class Prints {

	// ***********************************************
	// Method to print matrix in console
	// ***********************************************
	public static void matrix(Double matrix[][]) {

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%.4f\t", matrix[i][j]);
			}
			System.out.println();
		}
	}

	// ***********************************************
	// Method to print matrix in console
	// ***********************************************
	public static void matrix(int matrix[][]) {

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
	}

}
