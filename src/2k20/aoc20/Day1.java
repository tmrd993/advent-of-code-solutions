package aoc20;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import myutils20.StaticUtils;

public class Day1 {
    
    private List<Integer> numbers;
    
    public Day1(File inputFile) {
	numbers = StaticUtils.inputFileToIntList(inputFile);
    }
    
    public int run1() {
	int targetSum = 2020;
	
	Set<Integer> nums = new HashSet<>();
	for(int i = 0; i < numbers.size(); i++) {
	    nums.add(numbers.get(i));
	}
	
	for(int i = 0; i < numbers.size(); i++) {
	    int targetNum = targetSum - numbers.get(i);
	    if(nums.contains(targetNum)) {
		return targetNum * numbers.get(i);
	    }
	}
	
	return -1;
    }
    
    public long run2() {
	int targetSum = 2020;
	
	Set<Integer> nums = new HashSet<>();
	for(int i = 0; i < numbers.size(); i++) {
	    nums.add(numbers.get(i));
	}
	
	for(int i = 0; i < numbers.size(); i++) {
	    for(int j = i + 1; j < numbers.size(); j++) {
		int targetNum = targetSum - numbers.get(i) - numbers.get(j);
		if(nums.contains(targetNum)) {
		    return targetNum * numbers.get(i) * numbers.get(j);
		}
	    }
	}
	
	return -1;
    }

    public static void main(String[] args) {
	Day1 test = new Day1(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 1\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
