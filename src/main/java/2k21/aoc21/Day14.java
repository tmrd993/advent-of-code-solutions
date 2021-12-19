package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import utils.Pair;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Day14 {
	
	private String start;
	private Map<String, String> polyMap;
	
	public Day14(File file) {
		initData(file);
	}
	
	private long run1() {
		int steps = 10;
		String poly = start;
		for(int i = 0; i < steps; i++) {
			StringBuilder next = new StringBuilder();
			for(int j = 0; j < poly.length(); j++) {
				if(j == poly.length() - 1) {
					next.append(poly.charAt(poly.length() - 1));
					continue;
				}
				
				String sub = "";
				String pair = poly.charAt(j) + "" + poly.charAt(j + 1);
				if(polyMap.containsKey(pair)) {
					sub = pair.charAt(0) + polyMap.get(pair);
				} else {
					sub = Character.toString(poly.charAt(j));
				}
				next.append(sub);
			}
			poly = next.toString();
		}
		
		Map<Character, Long> frequency = poly.chars().mapToObj(c -> (char) c).collect(groupingBy(Function.identity(), counting()));
		long max = frequency.entrySet().stream().mapToLong(f -> f.getValue()).max().getAsLong();
		long min = frequency.entrySet().stream().mapToLong(f -> f.getValue()).min().getAsLong();
		
		return max - min;
	}
	
	private long run2() {
		int steps = 40;
		String poly = start;
		Map<String, Pair<String, String>> pairMapping = new HashMap<>();
		polyMap.entrySet().stream().forEach(e -> {
			String key = e.getKey();
			String pairA = Character.toString(key.charAt(0)) + e.getValue();
			String pairB = e.getValue() + Character.toString(key.charAt(1));
			pairMapping.put(key, new Pair<>(pairA, pairB));
		});
		
		Map<Character, Long> freq = new HashMap<>();
		Map<String, Long> pairs = new HashMap<>();
		
		for(int i = 0; i < poly.length() - 1; i++) {
			String pair = poly.charAt(i) + "" + poly.charAt(i + 1);
			pairs.putIfAbsent(pair, 0L);
			pairs.put(pair, pairs.get(pair) + 1);
			
			freq.putIfAbsent(poly.charAt(i), 0L);
			freq.put(poly.charAt(i), freq.get(poly.charAt(i)) + 1);
		}
		
		freq.putIfAbsent(poly.charAt(poly.length() - 1), 0L);
		freq.put(poly.charAt(poly.length() - 1), freq.get(poly.charAt(poly.length() - 1)) + 1);
		
		for(int i = 0; i < steps; i++) {
			
			Map<String, Long> pairsTmp = new HashMap<>();
			
			for(Map.Entry<String, Long> e : pairs.entrySet()) {
				long amount = e.getValue();
				Pair<String, String> nextPairs = pairMapping.get(e.getKey());
				
				pairsTmp.putIfAbsent(nextPairs.k(), 0L);
				pairsTmp.putIfAbsent(nextPairs.v(), 0L);
				
				pairsTmp.put(nextPairs.k(), amount + pairsTmp.get(nextPairs.k()));
				pairsTmp.put(nextPairs.v(), amount + pairsTmp.get(nextPairs.v()));
				
				char addedChar = nextPairs.k().charAt(1);
				freq.putIfAbsent(addedChar, 0L);
				freq.put(addedChar, freq.get(addedChar) + amount);
			}
			
			pairs = pairsTmp;
		
		}
		
		long max = freq.entrySet().stream().mapToLong(f -> f.getValue()).max().getAsLong();
		long min = freq.entrySet().stream().mapToLong(f -> f.getValue()).min().getAsLong();
		
		return max - min;
		
		
		
	}
	
	
	
	
	private void initData(File file) {
		polyMap = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			start = br.readLine();
			br.readLine();
			String line = "";
			while((line = br.readLine()) != null) {
				String left = line.substring(0, line.indexOf('-') - 1);
				polyMap.put(left, line.substring(line.indexOf('>') + 2));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Day14 test = new Day14(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day14\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}



}
