package aoc16;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import myutils16.Instruction;
import myutils16.InstructionFactory;
import myutils16.StaticUtils;

public class Day12 {

    private List<String> instructions;
    
    public Day12(File input) {
	instructions = StaticUtils.inputToList(input);
    }
    
    public int run(boolean part2) {
	int[] registers = IntStream.of(0, 0, 0, 0).toArray();
	int index = 0;
	
	if(part2) {
	    registers[2] = 1;
	}
	
	while(index < instructions.size()) {
	    Instruction instruction = InstructionFactory.getInstruction(instructions.get(index), registers, index, instructions);
	    instruction.execute();
	    index = instruction.index();
	}
	
	return registers[0];
    }
    
    public static void main(String[] args) {
	Day12 test = new Day12(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 12\\InputFile.txt"));
	System.out.println(test.run(true));
    }

}
