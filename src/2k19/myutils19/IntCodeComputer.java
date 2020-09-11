package myutils19;

import java.util.List;

import intcodeutils.OpCodeCommand;
import intcodeutils.OpCodeCommandFactory;

public class IntCodeComputer {
    private List<Integer> program;
    private boolean halt;
    private int instructionPointer;
    
    public IntCodeComputer(List<Integer> program) {
	this.program = program;
	halt = false;
	instructionPointer = 0;
    }
    
    public IntCodeComputer(List<Integer> program, int noun, int verb) {
	this.program = program;
	halt = false;
	instructionPointer = 0;
	this.replace(1, noun);
	this.replace(2, verb);
    }
    
    public void setHalt() {
	halt = true;
    }
    
    public List<Integer> program() {
	return program;
    }
    
    public void replace(int index, int value) {
	program.set(index, value);
    }
    
    public int instructionPointer() {
	return instructionPointer;
    }
    
    public void run() {
	while(!halt) {
	    int instruction = program.get(instructionPointer);
	    OpCodeCommand command = OpCodeCommandFactory.getCommand(instruction, this, instructionPointer);
	    command.execute();
	    instructionPointer = command.moveInstructionPointer();
	}
    }

}
