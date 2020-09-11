package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day5 {

    private List<Integer> opCodeProgram;

    public Day5(File input) {
	opCodeProgram = StaticUtils.commaSeperatedIntegerFileToList(input);
    }

    // provide val 1 for solution part 1, val 5 for solution part 2
    public int run() {
	List<Integer> defensiveCopyProgram = new ArrayList<>(opCodeProgram);
	IntCodeComputer computer = new IntCodeComputer(defensiveCopyProgram);
	computer.run();
	return 0;
    }

    public static void main(String[] args) {
	Day5 test = new Day5(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 5\\InputFile.txt"));
	System.out.println(test.run());
    }

}
