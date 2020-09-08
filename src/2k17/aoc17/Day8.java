package aoc17;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

import myutils2k17.Command;
import myutils2k17.DecreaseCommand;
import myutils2k17.IncreaseCommand;

import java.util.LinkedList;

public class Day8 {

    private List<String> rawInstructions;
    private Map<String, Integer> registers;
    private int maxValueInRegisters;

    public Day8(File input) {
	rawInstructions = getInstructions(input);
	registers = getRegisters();
	maxValueInRegisters = Integer.MIN_VALUE;
    }


    // part 1
    public int run() {
	Queue<Command> commands = getCommandQueue();

	for(Command command : commands) {
	    int registerValue = command.execute();
	    if(registerValue > maxValueInRegisters)
		maxValueInRegisters = registerValue;
	}

	return registers.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
    }

    private Queue<Command> getCommandQueue()
    {
	Queue<Command> commands = new LinkedList<Command>();
	for(String instruction : rawInstructions) {
	    String reg1 = "";
	    String reg2 = "";
	    int index1 = 0;
	    while (instruction.charAt(index1) != ' ') {
		reg1 += instruction.charAt(index1++);
	    }
	    int index2 = instruction.indexOf("if") + 3;
	    while(instruction.charAt(index2) != ' ') {
		reg2 += instruction.charAt(index2++);
	    }

	    String value1 = "";
	    int indexOfValue1 = instruction.indexOf("if") - 2;
	    while(instruction.charAt(indexOfValue1) != ' ') {
		value1 = instruction.charAt(indexOfValue1--) + value1;
	    }
	    int value1num = Integer.parseInt(value1);

	    String value2 = "";
	    int indexOfValue2 = instruction.length() - 1;
	    while(instruction.charAt(indexOfValue2) != ' ') {
		value2 = instruction.charAt(indexOfValue2--) + value2;
	    }
	    int value2num = Integer.parseInt(value2);

	    String condition = "";
	    int conditionIndex = indexOfValue2 - 1;
	    while(instruction.charAt(conditionIndex) != ' ') {
		condition = instruction.charAt(conditionIndex--) + condition;
	    }

	    //System.out.println(reg1 + " " + reg2 + " " +value1num + " " + value2num + " " + condition);

	    if(instruction.contains("dec")) {
		commands.offer(new DecreaseCommand(reg1, reg2, value1num, value2num, registers, condition));
	    }
	    else
		commands.offer(new IncreaseCommand(reg1, reg2, value1num, value2num, registers, condition));
	}

	return commands;
    }

    private Map<String, Integer> getRegisters() {
	registers = new HashMap<String, Integer>();

	for (String instruction : rawInstructions) {
	    String reg1 = "";
	    String reg2 = "";
	    int index1 = 0;
	    while (instruction.charAt(index1) != ' ') {
		reg1 += instruction.charAt(index1++);
	    }
	    int index2 = instruction.indexOf("if") + 1;
	    while(instruction.charAt(index2) != ' ') {
		reg2 += instruction.charAt(index2++);
	    }
	    registers.putIfAbsent(reg1, 0);
	    registers.putIfAbsent(reg2, 0);
	}

	return registers;
    }

    private List<String> getInstructions(File input) {
	List<String> instructions = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));

	    String line = "";
	    while ((line = br.readLine()) != null) {
		instructions.add(line);
	    }
	    br.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return instructions;
    }

    public static void main(String[] args) {
	Day8 test = new Day8(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 8\\InputFile1.txt"));
	System.out.println(test.run() + "   " + test.maxValueInRegisters);

    }

}
