package tests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestMap {

	public static void main(String[] args) {

		Map<Integer, Double> map = new LinkedHashMap<>();
		map.put(0, 150d);
		map.put(1, 1d);
		map.put(2, 100d);
		map.put(3, 900d);
		map.put(4, 10d);

		List<Integer> lista = new ArrayList<>();

		lista.add(map.entrySet().stream()
				.sorted(Map.Entry.<Integer, Double> comparingByValue().reversed()).map(v -> {
					return v.getKey();
				}).collect(Collectors.toList()).get(0));

		System.out.println(lista);

	}

}
