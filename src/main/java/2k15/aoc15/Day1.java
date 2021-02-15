package aoc15;

import java.io.File;
import java.util.List;

import myutils15.StaticUtils;

public class Day1 {

    private List<String> rawData;
    
    public Day1(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }
    
    public int run1() {
	return (int) (rawData.get(0).chars().filter(c -> c == '(').count() - rawData.get(0).chars().filter(c -> c == ')').count()); 
    }
    
    public int run2() {
	int floor = 0;
	String input = rawData.get(0);
	for(int i = 0; i < input.length(); i++) {
	    if(input.charAt(i) == '(') {
		floor++;
	    } else {
		floor--;
	    }
	    
	    if(floor == -1) {
		return i + 1;
	    }
	}
	
	return -1;
    }
    
    public static void main(String[] args) {
	Day1 test = new Day1(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 1\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
