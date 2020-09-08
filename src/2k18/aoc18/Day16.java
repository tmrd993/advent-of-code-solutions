package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day16 {

    private List<int[]> registersBefore;
    private List<int[]> instructions;
    private List<int[]> registersAfter;
    // private Map<int[], List<String>> possibleOpsForInstructions;

    public Day16(File inputFile) throws IOException {
	registersBefore = new ArrayList<int[]>();
	instructions = new ArrayList<int[]>();
	registersAfter = new ArrayList<int[]>();
	// possibleOpsForInstructions = new HashMap<int[], List<String>>();

	fillLists(inputFile, registersBefore, instructions, registersAfter);
    }

    // @params regs array of 4 numbers. the registers.
    // @params instr array of 4 numbers. the instructions (op code, A, B, C).
    public void addr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] + regs[instr[2]];
    }

    public void addi(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] + instr[2];
    }

    public void mulr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] * regs[instr[2]];
    }

    public void muli(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] * instr[2];
    }

    // bitwise AND
    public void banr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] & regs[instr[2]];
    }

    public void bani(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] & instr[2];
    }

    // bitwise OR
    public void borr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] | regs[instr[2]];
    }

    public void bori(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] | instr[2];
    }

    // set value
    public void setr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]];
    }

    public void seti(int[] regs, int[] instr) {
	regs[instr[3]] = instr[1];
    }

    public void gtir(int[] regs, int[] instr) {
	regs[instr[3]] = instr[1] > regs[instr[2]] ? 1 : 0;
    }

    public void gtri(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] > instr[2] ? 1 : 0;
    }

    public void gtrr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] > regs[instr[2]] ? 1 : 0;
    }

    public void eqir(int[] regs, int[] instr) {
	regs[instr[3]] = instr[1] == regs[instr[2]] ? 1 : 0;
    }

    public void eqri(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] == instr[2] ? 1 : 0;
    }

    public void eqrr(int[] regs, int[] instr) {
	regs[instr[3]] = regs[instr[1]] == regs[instr[2]] ? 1 : 0;
    }

    // fills the lists from the given input file
    private void fillLists(File input, List<int[]> registersBefore, List<int[]> instructions,
	    List<int[]> registersAfter) throws IOException {
	Scanner sc = new Scanner(input);

	String currentLine = "";
	while (sc.hasNext()) {

	    if (((currentLine = sc.nextLine()).length() == 0))
		continue;

	    if (currentLine.charAt(0) == 'B') {
		int[] registerBefore = new int[4];
		int registerIndex = 0;

		String[] numsAsStringArray = currentLine
			.substring(currentLine.indexOf('[') + 1, currentLine.indexOf(']')).trim().split("\\s*,\\s*");
		;

		for (String st : numsAsStringArray) {
		    registerBefore[registerIndex++] = Integer.parseInt(st);
		}
		registersBefore.add(registerBefore);
	    } else if (Character.isDigit(currentLine.charAt(0))) {
		int[] instruction = new int[4];
		int instructionIndex = 0;

		String[] numsAsStringArray = currentLine.split(" ");

		for (String st : numsAsStringArray) {
		    instruction[instructionIndex++] = Integer.parseInt(st);
		}

		instructions.add(instruction);
	    } else if (currentLine.charAt(0) == 'A') {
		int[] registerAfter = new int[4];
		int registerAfterIndex = 0;

		String[] numsAsStringArray = currentLine
			.substring(currentLine.indexOf('[') + 1, currentLine.indexOf(']')).trim().split("\\s*,\\s*");
		;
		for (String st : numsAsStringArray) {
		    registerAfter[registerAfterIndex++] = Integer.parseInt(st);
		}

		registersAfter.add(registerAfter);
	    }
	}
	sc.close();
    }

    // runs a test for a single instruction and saves the possible operations in
    // a map for later evaluation
    private void runTest(int[] registerBefore, int[] instruction, int[] registerAfter,
	    Map<int[], List<String>> possibleOpsForInstructions) {

	int[] tmpRegister = registerBefore.clone();
	List<String> possibleOps = new LinkedList<String>();
	addi(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("addi");

	tmpRegister = registerBefore.clone();
	addr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("addr");

	tmpRegister = registerBefore.clone();
	bani(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("bani");

	tmpRegister = registerBefore.clone();
	banr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("banr");

	tmpRegister = registerBefore.clone();
	bori(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("bori");

	tmpRegister = registerBefore.clone();
	borr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("borr");

	tmpRegister = registerBefore.clone();
	eqir(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("eqir");

	tmpRegister = registerBefore.clone();
	eqri(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("eqri");

	tmpRegister = registerBefore.clone();
	eqrr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("eqrr");

	tmpRegister = registerBefore.clone();
	gtir(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("gtir");

	tmpRegister = registerBefore.clone();
	gtri(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("gtri");

	tmpRegister = registerBefore.clone();
	gtrr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("gtrr");

	tmpRegister = registerBefore.clone();
	muli(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("muli");

	tmpRegister = registerBefore.clone();
	mulr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("mulr");

	tmpRegister = registerBefore.clone();
	seti(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("seti");

	tmpRegister = registerBefore.clone();
	setr(tmpRegister, instruction);
	if (Arrays.equals(tmpRegister, registerAfter))
	    possibleOps.add("setr");

	possibleOpsForInstructions.putIfAbsent(instruction, possibleOps);
    }

    // part 1
    public int run1() throws IOException {
	int count = 0;

	Map<int[], List<String>> possibleOpsForInstructions = getPossibleOperationsMap();

	for (Map.Entry<int[], List<String>> entry : possibleOpsForInstructions.entrySet()) {
	    if (entry.getValue().size() >= 3)
		count++;
	}
	return count;
    }

    // part 2
    public int run2() throws IOException {
	Map<Integer, String> opcodes = getOpcodes();

	int[] mainRegisters = { 0, 0, 0, 0 };

	List<int[]> instructions = getInstructions(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 16\\InputFileSectionTwo.txt"));

	for (int[] instruction : instructions) {
	    int opcode = instruction[0];
	    if (opcodes.get(opcode).equals("addi"))
		addi(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("addr"))
		addr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("mulr"))
		mulr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("muli"))
		muli(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("banr"))
		banr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("bani"))
		bani(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("borr"))
		borr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("bori"))
		bori(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("setr"))
		setr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("seti"))
		seti(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("gtir"))
		gtir(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("gtri"))
		gtri(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("gtrr"))
		gtrr(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("eqir"))
		eqir(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("eqri"))
		eqri(mainRegisters, instruction);
	    else if (opcodes.get(opcode).equals("eqrr"))
		eqrr(mainRegisters, instruction);
	}
	return mainRegisters[0];
    }

    private List<int[]> getInstructions(File inputFile) throws IOException {
	List<int[]> instructions = new ArrayList<int[]>();

	Scanner sc = new Scanner(inputFile);

	while (sc.hasNext()) {
	    String currentLine = sc.nextLine();
	    int[] instruction = new int[4];
	    int instructionIndex = 0;

	    String[] numsAsStringArray = currentLine.split(" ");

	    for (String st : numsAsStringArray) {
		instruction[instructionIndex++] = Integer.parseInt(st);
	    }

	    instructions.add(instruction);
	}

	sc.close();

	return instructions;
    }

    // returns a mapping of opcodes (integer 0 - 15) and the corresponding
    // operation (addi, addr, mulr...)
    private Map<Integer, String> getOpcodes() {
	Map<int[], List<String>> possibleOpsForInstr = getPossibleOperationsMap();

	Map<Integer, String> opcodes = new HashMap<Integer, String>();
	int totalNumOfOpcodes = 16;

	while (opcodes.size() != totalNumOfOpcodes) {
	    int[] targetInstruction = null;
	    String targetOperation = "";

	    // search for an instruction that only has a single corresponding
	    // operation
	    for (Map.Entry<int[], List<String>> entry : possibleOpsForInstr.entrySet()) {
		if (entry.getValue().size() == 1 && !opcodes.containsKey(entry.getKey()[0])) {
		    targetInstruction = entry.getKey();
		    targetOperation = entry.getValue().get(0);
		    break;
		}
	    }

	    // map the operation to an opcode
	    if (targetInstruction != null)
		opcodes.putIfAbsent(targetInstruction[0], targetOperation);

	    // delete the operation from every list as it's already mapped to an
	    // opcode
	    for (Map.Entry<int[], List<String>> entry : possibleOpsForInstr.entrySet()) {
		if (entry.getValue().contains(targetOperation)) {
		    entry.getValue().remove(targetOperation);
		}
	    }
	}

	return opcodes;
    }

    // returns a mapping of instructions and possible operations
    private Map<int[], List<String>> getPossibleOperationsMap() {
	Map<int[], List<String>> possibleOpsForInstructions = new HashMap<int[], List<String>>();
	int numOfSamples = registersBefore.size();
	for (int i = 0; i < numOfSamples; i++) {
	    runTest(registersBefore.get(i), instructions.get(i), registersAfter.get(i), possibleOpsForInstructions);
	}
	return possibleOpsForInstructions;
    }

    public static void main(String[] args) throws IOException {

	Day16 test = new Day16(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 16\\InputFileSectionOne.txt"));

	int resultPart2 = test.run2();
	// int resultPart1 = test.run1();

	System.out.println(resultPart2);
    }
}