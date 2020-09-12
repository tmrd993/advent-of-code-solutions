package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day9 {
    
    private List<Integer> initialProgram;
    
    public Day9(File input) {
	initialProgram = StaticUtils.commaSeperatedIntegerFileToList(input);
    }
    
    public long run() {
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	// provide input value 1 for part 1, input value 2 for part 2
	computer.setInputValues(2);
	computer.run();
	return computer.mostRecentOutputValue().get();
    }
    
    public static void main(String[] args) {
	Day9 test = new Day9(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 9\\InputFile.txt"));
	System.out.println(test.run());
    }

}
