package aoc21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day6 {
	
	private final String input = "1,2,1,3,2,1,1,5,1,4,1,2,1,4,3,3,5,1,1,3,5,3,4,5,5,4,3,1,1,4,3,1,5,2,5,2,4,1,1,1,1,1,1,1,4,1,4,4,4,1,4,4,1,4,2,1,1,1,1,3,5,4,3,3,5,4,1,3,1,1,2,1,1,1,4,1,2,5,2,3,1,1,1,2,1,5,1,1,1,4,4,4,1,5,1,2,3,2,2,2,1,1,4,3,1,4,4,2,1,1,5,1,1,1,3,1,2,1,1,1,1,4,5,5,2,3,4,2,1,1,1,2,1,1,5,5,3,5,4,3,1,3,1,1,5,1,1,4,2,1,3,1,1,4,3,1,5,1,1,3,4,2,2,1,1,2,1,1,2,1,3,2,3,1,4,5,1,1,4,3,3,1,1,2,2,1,5,2,1,3,4,5,4,5,5,4,3,1,5,1,1,1,4,4,3,2,5,2,1,4,3,5,1,3,5,1,3,3,1,1,1,2,5,3,1,1,3,1,1,1,2,1,5,1,5,1,3,1,1,5,4,3,3,2,2,1,1,3,4,1,1,1,1,4,1,3,1,5,1,1,3,1,1,1,1,2,2,4,4,4,1,2,5,5,2,2,4,1,1,4,2,1,1,5,1,5,3,5,4,5,3,1,1,1,2,3,1,2,1,1";
	
	public long run(int days) {
		
		Map<Integer, Long> fishTable = getTempFishTable();
		List<Integer> fish = input.chars().filter(c -> (char) c != ',').mapToObj(n -> Integer.parseInt(Character.toString((char) n))).collect(Collectors.toList());
		
		for(int num : fish) {
			fishTable.put(num, fishTable.get(num) + 1);
		}
		
		for(int i = 0; i < days; i++) {
			Map<Integer, Long> tempFishTable = getTempFishTable();
			for(Map.Entry<Integer, Long> e : fishTable.entrySet()) {
				if(e.getKey() == 0 && e.getValue() > 0) {
					tempFishTable.put(8, e.getValue());
					tempFishTable.put(6, e.getValue());
				} else if (e.getKey() > 0 && e.getValue() > 0) {
					tempFishTable.put(e.getKey() - 1, tempFishTable.get(e.getKey() - 1) + e.getValue());
				}
			}
			
			fishTable = tempFishTable;
			
		}
		
		return fishTable.entrySet().stream().map(e -> e.getValue()).mapToLong(e -> e).sum();
	}
	
	private Map<Integer, Long> getTempFishTable() {
		Map<Integer, Long> tempFishTable = new HashMap<>();
		for(int i = 0; i <= 8; i++) {
			tempFishTable.put(i, 0L);
		}
		return tempFishTable;
	}
	
	public static void main(String[] args) {
		Day6 test = new Day6();
		System.out.println(test.run(80));
		System.out.println(test.run(256));
	}

}
