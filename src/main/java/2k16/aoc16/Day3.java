package aoc16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import myutils16.MathUtils;
import myutils16.Triple;

public class Day3 {

    private List<Triple<Integer, Integer, Integer>> sideLengths;

    public Day3(File input) {
	sideLengths = getSideLengths(input);
	
    }
    
    public int run1() {
	int count = 0;
	for(Triple<Integer, Integer, Integer> triple : sideLengths) {
	    if(MathUtils.isValidTriangle(triple.getLeft(), triple.getMiddle(), triple.getRight()))
		count++;
	}
	return count;
    }
    
    public int run2() {
	int count = 0;
	for(int i = 0; i < sideLengths.size() - 2; i += 3) {
	    Triple<Integer, Integer, Integer> first = sideLengths.get(i);
	    Triple<Integer, Integer, Integer> second = sideLengths.get(i + 1);
	    Triple<Integer, Integer, Integer> third = sideLengths.get(i + 2);
	    
	    if(MathUtils.isValidTriangle(first.getLeft(), second.getLeft(), third.getLeft()))
		count++;
	    if(MathUtils.isValidTriangle(first.getMiddle(), second.getMiddle(), third.getMiddle()))
		count++;
	    if(MathUtils.isValidTriangle(first.getRight(), second.getRight(), third.getRight()))
		count++;
	}
	return count;
    }

    private List<Triple<Integer, Integer, Integer>> getSideLengths(File input) {
	List<Triple<Integer, Integer, Integer>> sideLengths = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		String[] nums = line.trim().split("\\s+");
		sideLengths.add(new Triple<Integer, Integer, Integer>(Integer.parseInt(nums[0]),
			Integer.parseInt(nums[1]), Integer.parseInt(nums[2])));
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return sideLengths;
    }

    public static void main(String[] args) {
	Day3 test = new Day3(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 3\\InputFile1.txt"));
	System.out.println(test.run2() + "   " + test.sideLengths.size());

    }

}
