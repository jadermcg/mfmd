package tests;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Map;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

public class AjustaDatasetJaspar {

	public static void main(String[] args) throws Exception {

		String file = System.getProperty("user.home") + "/datasets/final/chip/MA0014.2/dataset.fa";

		Map<String, DNASequence> data = FastaReaderHelper.readFastaDNASequence(Paths.get(file)
				.toFile());
		
		PrintStream ps = new PrintStream(file);
		
		int seqNumber = 1;
		for (String k : data.keySet()) {
			String seq = data.get(k).getSequenceAsString();
			ps.print(">seq" + seqNumber++ + "\n");
			int j = 0;
			for (int i = 0; i < seq.length(); i++) {
				if (j != 60) {
					ps.print(seq.charAt(i));
					j++;
				}
				else {
					ps.print("\n" + seq.charAt(i));
					j = 0;
				}
			}
			 ps.println();
		}
		
		ps.close();
	}
}
