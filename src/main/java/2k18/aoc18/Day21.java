package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day21 {
    // the register the instruction pointer is binded to
    private int ipBinding;
    // written opcodes of the instruction
    private List<String> instructionOpcodes;
    // instructions
    private List<int[]> instructions;
    // the main registers
    private int[] registers;

    private Map<Integer, Long> haltNumbers;

    public Day21(File inputFile) throws IOException {
	instructions = new ArrayList<int[]>();
	instructionOpcodes = new ArrayList<String>();
	parseInput(inputFile);
	registers = new int[] { 0, 0, 0, 0, 0, 0 };
	// map of numbers at register 0 that cause the program to halt at some
	// point and the number of instruction it took till it stopped
	haltNumbers = new HashMap<Integer, Long>();
    }

    // part 1 and 2
    // true argument == solution for part 1
    // false argument == solution for part 2
    public int run(boolean part1) {
	long count = 0;
	int instructionPointer = 0;
	int numOfInstructions = instructions.size();

	while (instructionPointer < numOfInstructions) {
	    String instructionOpcode = instructionOpcodes.get(instructionPointer);

	    int[] instruction = instructions.get(instructionPointer);

	    // the only instruction that uses register 0
	    if (instruction[1] == 4 && instruction[2] == 0 && instruction[3] == 5) {
		if (part1) {
		    return registers[4];
		}
		// the program loops infinitely. at sometime, it's going to
		// start repeating the target value at register 4. this is when
		// we stop.
		if (haltNumbers.containsKey(registers[4])) {
		    return haltNumbers.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
		}
		haltNumbers.put(registers[4], count);
	    }

	    registers[ipBinding] = instructionPointer;
	    Day19.executeInstruction(instructionOpcode, instruction, registers);
	    instructionPointer = registers[ipBinding];
	    instructionPointer++;
	    count++;
	}
	return -1;
    }

    private void parseInput(File inputFile) throws IOException {
	Scanner sc = new Scanner(inputFile);

	String currentLine = "";
	while (sc.hasNext()) {
	    currentLine = sc.nextLine();
	    if (currentLine.startsWith("#")) {
		ipBinding = Integer.parseInt(Character.toString(currentLine.charAt(currentLine.length() - 1)));
	    } else {
		instructionOpcodes.add(currentLine.substring(0, 4));
		String[] instructionsAsString = currentLine.substring(5, currentLine.length()).split(" ");
		int[] currentInstructions = new int[4];
		// the real opcode has no bearing on the operation in this case
		// since we already know what every instruction does, so we set
		// it to -1
		currentInstructions[0] = -1;
		currentInstructions[1] = Integer.parseInt(instructionsAsString[0]);
		currentInstructions[2] = Integer.parseInt(instructionsAsString[1]);
		currentInstructions[3] = Integer.parseInt(instructionsAsString[2]);

		instructions.add(currentInstructions);
	    }
	}
	sc.close();
    }

    public static void main(String[] args) throws IOException {

	Day21 test = new Day21(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 21\\InputFile1.txt"));
	// test.setRegisterZero(13591713);

	// test.run();

	int res2 = test.run(false);
	System.out.println(res2);
    }
}
