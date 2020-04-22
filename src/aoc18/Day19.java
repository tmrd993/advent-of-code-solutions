package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// part 1 of this challenge is self-explanatory. part 2 was very tricky.
// I actually don't know if my solution for part 2 works for every input out
// there
// To solve part 2, I simply printed the registers before and after every
// instruction and
// tried to figure out what the program is actually trying to accomplish
// it becomes apparent that the program is summing up the factors of a
// number
// that gets calculated at the beginning stage and stays constant at
// register 2. The sum of the factors of this number gets saved at register 0
// the target number is fully calculated when register 0 turns from 1 to 0.
// That's where it simply becomes a matter
// of summing up the factors and returning that sum which is what ends up in
// register 0 after the program finishes
public class Day19 {
    // the register the instruction pointer is binded to
    private int ipBinding;
    // written opcodes of the instruction
    private List<String> instructionOpcodes;
    // instructions
    private List<int[]> instructions;
    // the main registers
    private int[] registers;

    public Day19(File inputFile) throws IOException {
	instructions = new ArrayList<int[]>();
	instructionOpcodes = new ArrayList<String>();
	parseInput(inputFile);
	registers = new int[] { 0, 0, 0, 0, 0, 0 };
    }

    // part 1
    public int run() {

	int instructionPointer = 0;
	int numOfInstructions = instructions.size();

	while (instructionPointer < numOfInstructions) {
	    String instructionOpcode = instructionOpcodes.get(instructionPointer);
	    int[] instruction = instructions.get(instructionPointer);
	    registers[ipBinding] = instructionPointer;
	    executeInstruction(instructionOpcode, instruction, registers);
	    instructionPointer = registers[ipBinding];
	    instructionPointer++;
	}
	return registers[0];
    }

    // part 2
    public long run2() {
	registers = new int[] { 1, 0, 0, 0, 0, 0 };
	int instructionPointer = 0;

	// run until the target number is fully calculated at register 2
	while (registers[0] == 1) {
	    String instructionOpcode = instructionOpcodes.get(instructionPointer);
	    int[] instruction = instructions.get(instructionPointer);

	    registers[ipBinding] = instructionPointer;

	    executeInstruction(instructionOpcode, instruction, registers);

	    instructionPointer = registers[ipBinding];
	    instructionPointer++;
	}

	return sumOfFactors(registers[2]);
    }

    private void executeInstruction(String opcode, int[] instruction, int[] registers) {
	if (opcode.equals("addi"))
	    Day16.addi(registers, instruction);
	else if (opcode.equals("addr"))
	    Day16.addr(registers, instruction);
	else if (opcode.equals("mulr"))
	    Day16.mulr(registers, instruction);
	else if (opcode.equals("muli"))
	    Day16.muli(registers, instruction);
	else if (opcode.equals("banr"))
	    Day16.banr(registers, instruction);
	else if (opcode.equals("bani"))
	    Day16.bani(registers, instruction);
	else if (opcode.equals("borr"))
	    Day16.borr(registers, instruction);
	else if (opcode.equals("bori"))
	    Day16.bori(registers, instruction);
	else if (opcode.equals("setr"))
	    Day16.setr(registers, instruction);
	else if (opcode.equals("seti"))
	    Day16.seti(registers, instruction);
	else if (opcode.equals("gtir"))
	    Day16.gtir(registers, instruction);
	else if (opcode.equals("gtri"))
	    Day16.gtri(registers, instruction);
	else if (opcode.equals("gtrr"))
	    Day16.gtrr(registers, instruction);
	else if (opcode.equals("eqir"))
	    Day16.eqir(registers, instruction);
	else if (opcode.equals("eqri"))
	    Day16.eqri(registers, instruction);
	else if (opcode.equals("eqrr"))
	    Day16.eqrr(registers, instruction);

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

    // helper method for part 2
    // returns the sum of the factors of the given integer
    public static long sumOfFactors(int num) {
	long sumOfFactors = 0;
	int i = 1;
	while (i <= num) {
	    if (num % i == 0) {
		sumOfFactors += num / i;
	    }
	    i++;
	}
	return sumOfFactors;
    }

    public static void main(String[] args) throws IOException {
	Day19 test = new Day19(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 19\\InputFile1.txt"));

	// int result1 = test.run();
	long res2 = test.run2();

	System.out.println(res2);
    }
}
