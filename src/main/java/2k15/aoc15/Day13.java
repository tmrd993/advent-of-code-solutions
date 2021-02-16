package aoc15;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import myutils15.StaticUtils;

public class Day13 {
    
    private List<String> rawData;
    private final Pattern NUMBER = Pattern.compile("\\d+");
    
    public Day13(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }
    
    public int run1() {
	String people = rawData.stream().map(s -> Character.toString(s.charAt(0))).distinct().collect(Collectors.joining());
	Map<String, Integer> happinessTable = happinessTable();
	List<String> seatingPermutations = StaticUtils.permutations(people);
	return findMaxSeating(happinessTable, seatingPermutations);
    }
    
    public int run2() {
	String people = rawData.stream().map(s -> Character.toString(s.charAt(0))).distinct().collect(Collectors.joining()) + "X";
	Map<String, Integer> happinessTable = happinessTable();
	Map<String, Integer> tmpHappinessTable = new HashMap<>();
	happinessTable.entrySet().stream().forEach(e -> {
	    tmpHappinessTable.putIfAbsent("X" + e.getKey().charAt(0), 0);
	    tmpHappinessTable.putIfAbsent(e.getKey().charAt(0) + "X", 0);
	    tmpHappinessTable.putIfAbsent("X" + e.getKey().charAt(1), 0);
	    tmpHappinessTable.putIfAbsent(e.getKey().charAt(1) + "X", 0);
	});
	happinessTable.putAll(tmpHappinessTable);
	List<String> seatingPermutations = StaticUtils.permutations(people);
	
	return findMaxSeating(happinessTable, seatingPermutations);
    }
    
    private int findMaxSeating(Map<String, Integer> happinessTable, List<String> seatingPermutations) {
	int max = 0;
	for(String currentSeating : seatingPermutations) {
	    int sum = 0;
	    for(int i = 0; i < currentSeating.length(); i++) {
		if(i == currentSeating.length() - 1) {
		    sum += happinessTable.get(currentSeating.charAt(0) + "" + currentSeating.charAt(i));
		} else {
		    sum += happinessTable.get(currentSeating.substring(i, i + 2));
		}
	    }
	    
	    if(max < sum) {
		max = sum;
		
	    }
	}
	
	return max;
    }
    
    private Map<String, Integer> happinessTable() {
	Map<String, Integer> happinessTable = new HashMap<>();
	for(int i = 0; i < rawData.size(); i++) {
	    String currentRelationship = rawData.get(i);
	    char currentPersonId = currentRelationship.charAt(0);
	    char targetPersonId = currentRelationship.charAt(currentRelationship.indexOf("to") + 3);
	    for(int j = i + 1; j < rawData.size(); j++) {
		String cmpRelationship = rawData.get(j);
		char cmpPersonId = cmpRelationship.charAt(0);
		char cmpTargetId = cmpRelationship.charAt(cmpRelationship.indexOf("to") + 3);
		if(cmpPersonId == targetPersonId && currentPersonId == cmpTargetId) {
		    Matcher matcherA = NUMBER.matcher(currentRelationship);
		    Matcher matcherB = NUMBER.matcher(cmpRelationship);
		    matcherA.find();
		    matcherB.find();
		    int numA = Integer.parseInt(matcherA.group());
		    numA = currentRelationship.contains("gain") ? numA : -numA;
		    int numB = Integer.parseInt(matcherB.group());
		    numB = cmpRelationship.contains("gain") ? numB : -numB;
		    happinessTable.putIfAbsent(currentPersonId + "" + targetPersonId, numA + numB);
		    happinessTable.putIfAbsent(targetPersonId + "" + currentPersonId, numA + numB);
		}
	    }
	}
	return happinessTable;
    }

    public static void main(String[] args) {
	Day13 test = new Day13(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 13\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
