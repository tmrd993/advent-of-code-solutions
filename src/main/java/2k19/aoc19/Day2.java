package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day2 {
    private List<Long> opCodeProgram;
    private final int targetPart2 = 19690720;

    public Day2(File input) {
	opCodeProgram = StaticUtils.commaSeperatedLongFileToList(input);
    }

    public long run1() {
	List<Long> program = new ArrayList<>(opCodeProgram);
	IntCodeComputer computer = new IntCodeComputer(program);
	computer.replace(1, 12);
	computer.replace(2, 2);
	computer.run();
	return computer.memory().get(0);
    }
    
    public int run2() {
	for(int i = 0; i <= 99; i++) {
	    for(int j = 0; j <= 99; j++) {
		List<Long> program = new ArrayList<>(opCodeProgram);
		IntCodeComputer computer = new IntCodeComputer(program, i, j);
		computer.run();
		if(computer.memory().get(0) == targetPart2) {
		    return (100 * i) + j;
		}
	    }
	}
	return -1;
    }

    public static void main(String[] args) {
	Day2 test = new Day2(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 2\\InputFile.txt"));
	System.out.println(test.run2());

    }

}
