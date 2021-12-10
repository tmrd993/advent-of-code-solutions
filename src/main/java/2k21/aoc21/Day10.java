package aoc21;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import utils.StaticUtils;

public class Day10 {
	
	private List<String> rawData;
	private Map<Character, Character> openClosed;
	Set<Character> openSet = Set.of('(', '{', '[', '<');
	Set<Character> closedSet = Set.of(')', '}', ']', '>');
	
	public Day10(File file) {
		rawData = StaticUtils.inputFileToStringList(file);
		openClosed = Map.of('(', ')',
				'{', '}',
				'[', ']',
				'<', '>');
	}
	
	private int run1() {
		
		Map<Character, Integer> scoreTable = getScoreTable();

		
		int score = 0;
		for(String line : rawData) {
			
			
			Stack<Character> stack = new Stack<>();
			for(int i = 0; i < line.length(); i++) {
				char currentChar = line.charAt(i);
				if(openSet.contains(currentChar)) {
					stack.push(currentChar);
				} else {
					char open = stack.pop();
					if(openClosed.get(open) != currentChar) {
						score += scoreTable.get(currentChar);
						continue;
					}
				}
			}
			
			
		}
		
		return score;
	}
	
	private long run2() {
		
		List<String> lines = getIncompleteLines();
		Map<Character, Integer> scoreTable = Map.of(')', 1,
				']', 2,
				'}', 3,
				'>', 4);
		
		List<String> tailList = new ArrayList<>();
		for(String line : lines) {
			Stack<Character> stack = new Stack<>();
			
			for(int i = 0; i < line.length(); i++) {
				char currentChar = line.charAt(i);
				if(openSet.contains(currentChar)) {
					stack.push(currentChar);
				} else {
					stack.pop();
				}
			}
			
			StringBuilder sb = new StringBuilder();
			
			while(!stack.isEmpty()) {
				sb.append(openClosed.get(stack.pop()));
			}
			
			tailList.add(sb.toString());
		}
		
		List<Long> scores = tailList.stream().map(s -> {
			long score = 0;
			for(int i = 0; i < s.length(); i++) {
				score = (score * 5) + scoreTable.get(s.charAt(i));
			}
			return score;
		}).collect(Collectors.toList());
		
		scores.sort(Comparator.naturalOrder());
		
		return scores.get(scores.size() / 2);
	}
	
	private List<String> getIncompleteLines() {
		List<String> lines = new ArrayList<>();
		
		for(String line : rawData) {
			Stack<Character> stack = new Stack<>();
			boolean isCorrupted = false;
			for(int i = 0; i < line.length(); i++) {
				char currentChar = line.charAt(i);
				if(openSet.contains(currentChar)) {
					stack.push(currentChar);
				} else {
					char open = stack.pop();
					if(openClosed.get(open) != currentChar) {
						isCorrupted = true;
						continue;
					}
				}
			}
			if(!isCorrupted) {
				lines.add(line);
			}
		}
		
		
		return lines;
	}
	
	private Map<Character, Integer> getScoreTable() {
		return Map.of(')', 3,
				']', 57,
				'}', 1197,
				'>', 25137);
	}

	public static void main(String[] args) {
		Day10 test = new Day10(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day10\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}



}
