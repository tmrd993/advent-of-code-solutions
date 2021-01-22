package aoc16;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import myutils16.Instruction;
import myutils16.InstructionFactory;
import myutils16.StaticUtils;

public class Day25 {

    private List<String> instructions;

    public Day25(File input) {
	instructions = StaticUtils.inputToList(input);
    }

    public int run() {
	int a = 1;
	while(!isValid(a)) {
	    a++;
	}
	
	return a;
    }

    private boolean isValid(int a) {
	int[] registers = IntStream.of(0, 0, 0, 0).toArray();
	registers[0] = a;

	int index = 0;
	int outNum = 0;
	int outIncr = 0;
	while (index < instructions.size() && index >= 0) {
	    Instruction instruction = InstructionFactory.getInstruction(instructions.get(index), registers, index,
		    instructions);
	    int out = instruction.execute();

	    if(instructions.get(index).contains("out")) {
		outNum = outNum | (out << outIncr++);
	    }
	    
	    if(outIncr == 8) {
		if(0b10101010 == outNum) {
		    return true;
		} else {
		    return false;
		}
	    }

	    index = instruction.index();

	}
	return false;
    }

    public static void main(String[] args) {
	Day25 test = new Day25(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 25\\InputFile.txt"));

	System.out.println(test.run());
    }

}
