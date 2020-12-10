package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import myutils20.StaticUtils;

public class Day10 {
    
    private List<Integer> rawData;
    
    public Day10(File inputFile) {
	rawData = StaticUtils.inputFileToIntList(inputFile);
    }
    
    public int run1() {
	List<Integer> chain = new ArrayList<>(rawData);
	Collections.sort(chain);
	chain.add(0, 0);
	chain.add(chain.get(chain.size() - 1) + 3);
	int oneJolt = 0;
	int threeJolt = 0;
	for(int i = 0; i < chain.size() - 1; i++) {
	    if(chain.get(i) == (chain.get(i + 1) - 1)) {
		oneJolt++;
	    }
	    else if(chain.get(i) == (chain.get(i + 1) - 3)) {
		threeJolt++;
	    }
	}
	
	return oneJolt * threeJolt;
    }
    
    public long run2() {
	List<Integer> numbers = new ArrayList<>(rawData);
	numbers.add(0, 0);
	Collections.sort(numbers);
	numbers.add(numbers.get(numbers.size() - 1) + 3);
	// memoization array
	long[] edges = LongStream.iterate(-1, i -> i).limit(numbers.get(numbers.size() - 1)).toArray();
	
	return count(numbers, 0, edges);
    }
    
    private long count(List<Integer> numbers, int index, long[] edges) {
	int num = numbers.get(index);
	if(index == numbers.size() - 2) {
	    return 1;
	}
	if(edges[num] != -1) {
	    return edges[num];
	}
	
	long count = 0;
	
	if(index < numbers.size() - 2 && (numbers.get(index + 1) - numbers.get(index) < 4)) {
	    count += count(numbers, index + 1, edges);
	}
	
	if(index < numbers.size() - 3 && (numbers.get(index + 2) - numbers.get(index) < 4)) {
	    count += count(numbers, index + 2, edges);
	}
	
	if(index < numbers.size() - 4 && (numbers.get(index + 3) - numbers.get(index) < 4)) {
	    count += count(numbers, index + 3, edges); 
	}
	
	edges[num] = count;
	
	return count;
    }

    public static void main(String[] args) {
	Day10 test = new Day10(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 10\\InputFile1.txt"));
	System.out.println(test.run2());
	
    }

}
