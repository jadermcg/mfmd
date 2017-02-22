package tests;

import java.util.List;

import sources.Background;
import sources.EstimaPosicoesIniciais;
import util.Dataset;
import execution.Preprocessing;

public class TestInitialPositionsScore {
	public static void main(String[] args) throws Exception {

		//
		// parametros
		// ///////////////////////////////////////////////////////////////////////////////
		boolean dimmer = true;
		int w = 8;
		String datasetName = "dataset_pdr3";
		String datasetFile = System.getProperty("user.home") + "/datasets/final/real/"
				+ datasetName + "/dataset.fa";
		Dataset dataset = new Dataset(datasetFile, dimmer, w);
		Background bg = new Background(dataset, 1, 1);
		Preprocessing preprocessing = new Preprocessing(dataset, bg);
		dataset = preprocessing.getDataset();
		int nPosicoes = 5;

		EstimaPosicoesIniciais empi = new EstimaPosicoesIniciais(dataset, bg, nPosicoes);
		empi.start();

		List<Integer> melhoresPosicoes = empi.getPosicoes();

		System.out.println(melhoresPosicoes);

	}
}
