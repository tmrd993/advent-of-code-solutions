package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day21 {

    private List<Long> initialProgram;

    public Day21(File inputFile) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(inputFile);
    }

    // solved using a 4 variable truth table
    // jump if ~(A + B + C) + D
    public int run1() {

	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));

	computer.run();
	computer.setInputValues('N', 'O', 'T', ' ', 'A', ' ', 'T', 10);
	computer.setInputValues('N', 'O', 'T', ' ', 'B', ' ', 'J', 10);
	computer.setInputValues('O', 'R', ' ', 'T', ' ', 'J', 10);
	computer.setInputValues('N', 'O', 'T', ' ', 'C', ' ', 'T', 10);
	computer.setInputValues('O', 'R', ' ', 'T', ' ', 'J', 10);
	computer.setInputValues('A', 'N', 'D', ' ', 'D', ' ', 'J', 10);
	computer.setInputValues('W', 'A', 'L', 'K', 10);
	computer.run();

	Queue<Long> output = computer.outputValues();
	int result = 0;
	while (!output.isEmpty()) {
	    result = output.poll().intValue();
	}

	return result;
    }

    // pretty much the same as above, we only need to make sure that the next jump
    // is also possible, eg, by checking E and H, this might not work for ALL
    // inputs, if it doesn't make sure to also take a look at F and I
    public int run2() {

	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));

	computer.run();
	computer.setInputValues('N', 'O', 'T', ' ', 'A', ' ', 'T', 10);
	computer.setInputValues('N', 'O', 'T', ' ', 'B', ' ', 'J', 10);
	computer.setInputValues('O', 'R', ' ', 'T', ' ', 'J', 10);
	computer.setInputValues('N', 'O', 'T', ' ', 'C', ' ', 'T', 10);
	computer.setInputValues('O', 'R', ' ', 'T', ' ', 'J', 10);
	computer.setInputValues('A', 'N', 'D', ' ', 'D', ' ', 'J', 10);

	computer.setInputValues('N', 'O', 'T', ' ', 'J', ' ', 'T', 10);
	computer.setInputValues('O', 'R', ' ', 'E', ' ', 'T', 10);
	computer.setInputValues('O', 'R', ' ', 'H', ' ', 'T', 10);
	computer.setInputValues('A', 'N', 'D', ' ', 'T', ' ', 'J', 10);
	computer.setInputValues('R', 'U', 'N', 10);
	computer.run();

	Queue<Long> output = computer.outputValues();
	int result = 0;
	while (!output.isEmpty()) {
	    result = output.poll().intValue();
	}

	return result;
    }

    public static void main(String[] args) {
	Day21 test = new Day21(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 21\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
