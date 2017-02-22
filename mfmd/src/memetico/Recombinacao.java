package memetico;

import java.util.ArrayList;

import sources.Solution;

public class Recombinacao {
	// ************************************************************
	// atributos
	// ************************************************************
	private Solution p1;
	private Solution p2;
	private Solution c1;
	private Solution c2;
	private int corte;

	// ************************************************************
	// construtor publico
	// ************************************************************
	public Recombinacao(Solution p1, Solution p2, int corte) {
		this.p1 = p1;
		this.p2 = p2;
		this.corte = corte;
		c1 = new Solution(new ArrayList<>(), 0d);
		c2 = new Solution(new ArrayList<>(), 0d);
	}

	// ************************************************************
	// aplica recombinacao
	// ************************************************************
	public void start() {
		// cabeca
		for (int i = 0; i < corte; i++) {
			c1.getPositions().add(p1.getPositions().get(i));
			c2.getPositions().add(p2.getPositions().get(i));
		}

		// cauda
		for (int i = corte; i < p2.getPositions().size(); i++) {
			c1.getPositions().add(p2.getPositions().get(i));
			c2.getPositions().add(p1.getPositions().get(i));
		}

	}

	// ************************************************************
	// retorna filho_1
	// ************************************************************
	public Solution getC1() {
		return c1;
	}

	// ************************************************************
	// retorna filho_2
	// ************************************************************
	public Solution getC2() {
		return c2;
	}

}
