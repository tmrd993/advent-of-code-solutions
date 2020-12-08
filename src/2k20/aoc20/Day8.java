package aoc20;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import myutils20.HandheldGameConsole;
import myutils20.StaticUtils;

public class Day8 {
    
    private List<String> instructions;
    
    public Day8(File inputFile) {
	instructions = StaticUtils.inputFileToStringList(inputFile);
    }
    
    public int run1() {
	HandheldGameConsole console = new HandheldGameConsole();
	console = runWithInstructions(instructions);
	return console.acc();
    }
    
    public int run2() {
	int currentIndex = 0;
	String nextChange = "";
	
	HandheldGameConsole console = new HandheldGameConsole();
	while(console.ip() != instructions.size()) {
	    for(int i = currentIndex; i < instructions.size(); i++) {
		String instr = instructions.get(i);
		if(instr.contains("jmp")) {
		    nextChange = "nop " + instr.substring(instr.indexOf(' ') + 1);
		    currentIndex = i;
		    break;
		}
		else if(instr.contains("nop")) {
		    nextChange = "jmp " + instr.substring(instr.indexOf(' ') + 1);
		    currentIndex = i;
		    break;
		}
	    }
	    
	    String oldValue = instructions.get(currentIndex);
	    instructions.set(currentIndex, nextChange);
	    
	    console = runWithInstructions(instructions);
	  
	    instructions.set(currentIndex, oldValue);
	    currentIndex++;
	}
	
	return console.acc();
    }
    
    private HandheldGameConsole runWithInstructions(List<String> instructions) {
	HandheldGameConsole console = new HandheldGameConsole();
	Set<Integer> visitedIndex = new HashSet<>();
	
	while(!visitedIndex.contains(console.ip()) && console.ip() < instructions.size()) {
	    visitedIndex.add(console.ip());
	    String instr = instructions.get(console.ip());
	    console.run(instr);
	}
	
	return console;
    }

    public static void main(String[] args) {
	Day8 test = new Day8(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 8\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
