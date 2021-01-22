package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import myutils20.StaticUtils;

public class Day9 {

    private List<Long> numbers;
    private final int preambleLen = 25;

    public Day9(File inputFile) {
	numbers = StaticUtils.inputFileToStringList(inputFile).stream().map(s -> Long.parseLong(s))
		.collect(Collectors.toList());
    }

    public long run1() {
	List<Set<Long>> allSums = computeSums(numbers);
	
	for(int i = preambleLen; i < numbers.size(); i++) {
	    if(!allSums.get(i - preambleLen).contains(numbers.get(i))) {
		return numbers.get(i);
	    }
	}

	return -1;
    }
    
    public long run2() {
	long targetNumber = run1();
	
	for(int i = 0; i < numbers.size(); i++) {
	    long sum = 0;
	    List<Long> nums = new ArrayList<>();
	    for(int j = i; j < numbers.size(); j++) {
		sum += numbers.get(j);
		nums.add(numbers.get(j));
		if(sum > targetNumber) {
		    break;
		}
		else if(sum == targetNumber) {
		    return nums.stream().max(Comparator.naturalOrder()).get() + nums.stream().min(Comparator.naturalOrder()).get();
		}
	    }
	}
	
	return -1;
    }

    private List<Set<Long>> computeSums(List<Long> numbers) {
	List<Set<Long>> allSums = new ArrayList<>();
	
	for(int i = 0; i < numbers.size() - preambleLen + 1; i++) {
	    Set<Long> subSums = new HashSet<>();
	    for(int j = i; j < i + preambleLen; j++) {
		for(int k = j + 1; k < i + preambleLen; k++) {
		    subSums.add(numbers.get(j) + numbers.get(k));
		}
	    }
	    allSums.add(subSums);
	}
	
	return allSums;
    }

    public static void main(String[] args) {
	Day9 test = new Day9(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 9\\InputFile1.txt"));
	//test.computeSums(test.numbers).stream().forEach(s -> System.out.println(s.size() + "  " + s));
	System.out.println(test.run2());
    }

}
