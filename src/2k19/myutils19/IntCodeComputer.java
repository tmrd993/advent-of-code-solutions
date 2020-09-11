package myutils19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import intcodeutils.OpCodeCommand;
import intcodeutils.OpCodeCommandFactory;

public class IntCodeComputer {
    private List<Integer> program;
    private boolean halt;
    private boolean standby;
    private int instructionPointer;
    private Queue<Integer> inputValues;
    private Optional<Integer> outputValue = Optional.empty();
    private List<Integer> outputValues;
    
    public IntCodeComputer(List<Integer> program) {
	this.program = program;
	halt = false;
	instructionPointer = 0;
	inputValues = new LinkedList<>();
	outputValues = new ArrayList<>();
    }
    
    public IntCodeComputer(List<Integer> program, int noun, int verb) {
	this.program = program;
	halt = false;
	instructionPointer = 0;
	this.replace(1, noun);
	this.replace(2, verb);
    }
    
    // complete shutdown
    public void setHalt() {
	halt = true;
    }
    
    public boolean isShutdown() {
	return halt;
    }
    
    // pause till next input is ready
    public void setStandby() {
	standby = true;
    }
    
    public boolean isStandby() {
	return standby;
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
    
    public void setInputValues(int... input) {
	Arrays.stream(input).forEach(n -> inputValues.add(n));
    }
    
    public Queue<Integer> inputValues() {
	return inputValues;
    }
    
    public void saveOutputValue(int val) {
	outputValue = Optional.of(val);
	outputValues.add(val);
    }
    
    public Optional<Integer> mostRecentOutputValue() {
	return outputValue;
    }
    
    public void run() {
	standby = false;
	while(!halt && !standby) {
	    int instruction = program.get(instructionPointer);
	    OpCodeCommand command = OpCodeCommandFactory.getCommand(instruction, this, instructionPointer);
	    command.execute();
	    instructionPointer = command.moveInstructionPointer();
	}
    }

}
