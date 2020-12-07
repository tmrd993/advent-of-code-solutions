package aoc20;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import myutils20.StaticUtils;

public class Day7 {

    private List<String> rawData;
    private final String targetBag = "shiny gold";

    public Day7(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	Queue<String> targetQueue = new LinkedList<>();
	targetQueue.add(targetBag);
	Set<String> uniqueBags = new HashSet<>();

	while (!targetQueue.isEmpty()) {
	    String currentTarget = targetQueue.poll();
	    for (String bagData : rawData) {
		String bagContent = bagData.substring(bagData.indexOf("contain") + "contain".length() + 1);
		if (bagContent.contains(currentTarget)) {
		    String nextTarget = bagData.substring(0, bagData.indexOf("bags") - 1);
		    targetQueue.add(nextTarget);
		    uniqueBags.add(nextTarget);
		}
	    }
	}
	return uniqueBags.size();
    }

    public int run2() {
	Set<String> noContentBags = rawData.stream().filter(s -> s.contains("no other bags"))
		.map(s -> s.substring(0, s.indexOf("bags") - 1)).collect(Collectors.toSet());
	
	return bagCount(targetBag, 1, noContentBags) - 1;
    }

    private int bagCount(String target, int multiplier, Set<String> noContentBags) {
	if(noContentBags.contains(target)) {
	    return multiplier;
	}
	
	String bagData = rawData.stream().filter(s -> s.substring(0, s.indexOf("bags") - 1).equals(target)).findFirst().get();
	String[] contents = bagData.substring(bagData.indexOf("contain") + "contain".length() + 1).split(", ");
	
	int count = 0;
	for(String content : contents) {
	    int amount = Integer.parseInt(Character.toString(content.charAt(0)));
	    String nextTarget = content.substring(2, content.indexOf("bag") - 1);
	    count += bagCount(nextTarget, multiplier * amount, noContentBags);
	}
	count += multiplier;
	
	return count;
    }

    public static void main(String[] args) {
	Day7 test = new Day7(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 7\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
