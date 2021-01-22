package aoc19;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import myutils19.StaticUtils;

public class Day1 {
    
    private List<Integer> fuelMasses;
    private final double divisionFactor = 3.0;
    private final int subtractionFactor = 2;
    
    public Day1(File inputFile) {
	fuelMasses = StaticUtils.fileToStringList(inputFile)
		.stream()
		.map(n -> Integer.parseInt(n))
		.collect(Collectors.toList());
    }
    
    public int run1() {
	return fuelMasses
		.stream()
		.map(mass -> Math.floor(mass / divisionFactor) - subtractionFactor)
		.reduce(0.0, (a, b) -> a + b)
		.intValue();
    }
    
    public int run2() {
	int totalRequirement = 0;
	for(int mass : fuelMasses) {
	    while(mass > 0) {
		mass = ((int) Math.floor(mass / divisionFactor)) - subtractionFactor;
		if(mass > 0)
		    totalRequirement += mass;
	    }
	}
	return totalRequirement;
    }

    public static void main(String[] args) {
	Day1 test = new Day1(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 1\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
