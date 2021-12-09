package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {
	
	private List<List<String>> patterns;
	private List<List<String>> outputs;
	
	
	public Day8(File file) {
		initLists(file);
	}
	
	private int run1() {
		int count = 0;
		
		for(List<String> output : outputs) {
			for(String digit : output) {
				if(digit.length() == 2 || digit.length() == 4 || digit.length() == 3 || digit.length() == 7) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	private int run2() {
		
		int sum = 0;
		
		for(int i = 0; i < patterns.size(); i++) {
			List<String> pattern = patterns.get(i);
			Map<Integer, String> numberMapping = new HashMap<>();
			
			fillBaseNumbers(numberMapping, pattern);
			
			
			put(9, numberMapping, pattern);
			put(0, numberMapping, pattern);
			put(6, numberMapping, pattern);
			put(3, numberMapping, pattern);
			put(5, numberMapping, pattern);
			put(2, numberMapping, pattern);
			
			List<String> output = outputs.get(i);
			
			StringBuilder sb = new StringBuilder();
			for(String o : output) {
				Set<Character> oSet = o.chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
				for(Map.Entry<Integer, String> e : numberMapping.entrySet()) {
					Set<Character> nSet = e.getValue().chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
					if(nSet.equals(oSet)) {
						sb.append(e.getKey());
						break;
					}
				}
				
			}
			sum += Integer.parseInt(sb.toString());
			
		}
		
		return sum;
	}
	
	private void put(int num, Map<Integer, String> numberMapping, List<String> pattern) {
		
		if(num == 9) {
			Set<Character> fourSet = numberMapping.get(4).chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
			for(String p : pattern) {
				Set<Character> pSet = p.chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
				if(pSet.containsAll(fourSet) && pSet.size() == 6) {
					numberMapping.put(9, p);
					return;
				}
			}
		}
		
		if(num == 0) {
			Set<Character> sevenSet = numberMapping.get(7).chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
			for(String p : pattern) {
				Set<Character> pSet = p.chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
				if(pSet.containsAll(sevenSet) && pSet.size() == 6 && !pSet.equals(numberMapping.get(9).chars().mapToObj(n -> (char) n).collect(Collectors.toSet()))) {
					numberMapping.put(0, p);
					return;
				}
			}
		}
		
		
		if(num == 6) {
			String six = pattern.stream().filter(p -> p.length() == 6 && !numberMapping.containsValue(p)).findAny().get();
			numberMapping.put(6, six);
		}
		
		
		
		
		if(num == 3) {
			Set<Character> sevenSet = numberMapping.get(7).chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
			for(String p : pattern) {
				Set<Character> pSet = p.chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
				if(pSet.containsAll(sevenSet) && pSet.size() == 5 ) {
					numberMapping.put(3, p);
					return;
				}
			}
		}
		

		
		if(num == 5) {
			Set<Character> sixSet = numberMapping.get(6).chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
			for(String p : pattern) {
				Set<Character> pSet = p.chars().mapToObj(n -> (char) n).collect(Collectors.toSet());
				if(sixSet.containsAll(pSet) && pSet.size() == 5) {
					numberMapping.put(5, p);
					return;
				}
			}
		}
		

	
		
		if(num == 2) {
			String two = pattern.stream().filter(p -> p.length() == 5 && !numberMapping.containsValue(p)).findAny().get();
			numberMapping.put(2, two);
		}
		
		
	}
	
	private void fillBaseNumbers(Map<Integer, String> numberMapping, List<String> pattern) {
		pattern.stream().forEach(p -> {
			if(p.length() == 2) {
				numberMapping.put(1, p);
			} else if(p.length() == 4) {
				numberMapping.put(4, p);
			} else if(p.length() == 3) {
				numberMapping.put(7, p);
			} else if(p.length() == 7) {
				numberMapping.put(8, p);
			}
		});;
	}
	
	private void initLists(File inputFile) {
		patterns = new ArrayList<>();
		outputs = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = "";
			while ((line = br.readLine()) != null) {
				String patternStr = line.substring(0, line.indexOf('|') - 1);
				String outputStr = line.substring(line.indexOf('|') + 2);
				
				patterns.add(Arrays.stream(patternStr.split(" ")).collect(Collectors.toList()));
				outputs.add(Arrays.stream(outputStr.split(" ")).collect(Collectors.toList()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		Day8 test = new Day8(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day8\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}



}
