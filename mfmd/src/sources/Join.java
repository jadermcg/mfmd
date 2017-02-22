package sources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Join {

	// *************************************************************************
	// add patternDiscoveryPositions found in the pattern discovery into new
	// positions found in pattern matching
	// *************************************************************************
	public static Map<Integer, List<Integer>> patternDiscoveryPositions_patternMatchingPositions(
			List<Integer> patternDiscoveryPositions,
			Map<Integer, List<Integer>> patternMatchingPositions) {

		Map<Integer, List<Integer>> newPositions = new LinkedHashMap<>();

		int i = 0;
		for (int key : patternMatchingPositions.keySet()) {
			newPositions.put(i, patternMatchingPositions.get(i));
			if (!newPositions.get(key).contains(patternDiscoveryPositions.get(i))) {
				newPositions.get(i).add(patternDiscoveryPositions.get(i));
			}
			i++;
		}

		return newPositions;
	}

}
