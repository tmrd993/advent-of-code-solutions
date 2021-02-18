package aoc15;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import myutils15.StaticUtils;

public class Day17 {

    private List<Integer> inputNumbers;

    public Day17(File input) {
	inputNumbers = StaticUtils.fileToStringList(input).stream().map(s -> Integer.parseInt(s))
		.collect(Collectors.toList());
    }
    
    public int run1() {
	Counter counter = new Counter();
	count(0, 0, counter, inputNumbers, 0, new ArrayList<>());
	return counter.count();
    }
    
    public int run2() {
	List<Integer> filledContainers = new ArrayList<>();
	count(0, 0, new Counter(), inputNumbers, 0, filledContainers);
	int min = Collections.min(filledContainers);
	return (int) filledContainers.stream().filter(n -> n == min).count();
    }
    
    public void count(int index, int sum, Counter counter, List<Integer> numbers, int numOfContainers, List<Integer> filledContainers) {
	if(sum == 150) {
	    filledContainers.add(numOfContainers);
	    counter.increment();
	    return;
	}
	
	if(index >= numbers.size()) {
	    return;
	}
	
	count(index + 1, sum + numbers.get(index), counter, numbers, numOfContainers + 1, filledContainers);
	count(index + 1, sum, counter, numbers, numOfContainers, filledContainers);
    }

    public static void main(String[] args) {
	Day17 test = new Day17(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 17\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }
    
    private static class Counter {
	private int count;
	
	public Counter() {
	    count = 0;
	}
	
	public void increment() {
	    count++;
	}
	
	public int count() {
	    return count;
	}
    }

}
