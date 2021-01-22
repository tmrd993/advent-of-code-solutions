package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import myutils19.StaticUtils;

public class Day16 {

    private List<Integer> initialNumbers;
    private List<Integer> basePattern = List.of(0, 1, 0, -1);
    private final int phaseCount = 100;

    public Day16(File input) {
	initialNumbers = StaticUtils.digitFileToList(input);
    }

    public String run1() {
	List<Integer> inputNums = new ArrayList<>(initialNumbers);
	int patternLen = basePattern.size();

	for (int i = 0; i < phaseCount; i++) {
	    List<Integer> output = new ArrayList<>();

	    for (int j = 0; j < inputNums.size(); j++) {
		int patternIndex = 0;
		int repeatAmount = j + 1;
		int sum = 0;
		for (int k = 0; k < inputNums.size(); k++) {
		    if(k == 0) {
			repeatAmount--;
		    }
		    
		    if (repeatAmount == 0) {
			patternIndex = (patternIndex + 1) % patternLen;
			repeatAmount = j + 1;
		    }
		    sum += inputNums.get(k) * basePattern.get(patternIndex);
		    repeatAmount--;
		}
		output.add(Math.abs(sum % 10));
	    }
	    inputNums = output;
	}

	return inputNums.stream().limit(8).map(n -> n + "").collect(Collectors.joining());
    }
    
    private String run2() {
	List<Integer> input = repeatedList(new ArrayList<>(initialNumbers));
	int offset = Integer.parseInt(input.stream().limit(7).map(n -> n + "").collect(Collectors.joining()));
	for(int i = 0; i < phaseCount; i++) {
	    calculateOutput(input, offset);
	}
	return input.subList(offset, offset + 8).stream().map(n -> n + "").collect(Collectors.joining());
    }
    
    private void calculateOutput(List<Integer> input, int offset) {
	int lastIndex = input.size() - 1;
	int sum = 0;
	for(int i = lastIndex; i >= offset; i--) {
	    int result = (input.get(i) + sum) % 10;
	    sum = input.get(i) + sum;
	    input.set(i, result);
	}
    }
   
    
    // returns a new list that is the old list repeated times 10.000
    private List<Integer> repeatedList(List<Integer> nums) {
	List<Integer> output = new ArrayList<>();
	for(int i = 0; i < 10000; i++) {
	    output.addAll(nums);
	}
	return output;
    }

    public static void main(String[] args) {
	Day16 test = new Day16(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 16\\InputFile.txt"));
	System.out.println(test.run2());
    }
}
