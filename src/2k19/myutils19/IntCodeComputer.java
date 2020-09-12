package myutils19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import intcodeutils.IntCodeMemory;
import intcodeutils.OpCodeCommand;
import intcodeutils.OpCodeCommandFactory;

public class IntCodeComputer {
    private IntCodeMemory memory;
    private boolean halt;
    private boolean standby;
    private long instructionPointer;
    private Queue<Long> inputValues;
    private Optional<Long> outputValue = Optional.empty();
    private List<Long> outputValues;
    private long relativeBase;
    
    public IntCodeComputer(List<Integer> program) {
	memory = new IntCodeMemory(program.stream().map(n -> (long) n).collect(Collectors.toList()));
	halt = false;
	instructionPointer = 0;
	relativeBase = 0;
	inputValues = new LinkedList<>();
	outputValues = new ArrayList<>();
    }
    
    public IntCodeComputer(List<Integer> program, int noun, int verb) {
	halt = false;
	instructionPointer = 0;
	memory = new IntCodeMemory(program.stream().map(n -> (long) n).collect(Collectors.toList()));
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
    
    public IntCodeMemory memory() {
	return memory;
    }
    
    public void replace(int index, int value) {
	memory.set(index, value);
    }
    
    public long instructionPointer() {
	return instructionPointer;
    }
    
    public void setInputValues(long... input) {
	Arrays.stream(input).forEach(n -> inputValues.add(n));
    }
    
    public Queue<Long> inputValues() {
	return inputValues;
    }
    
    public void saveOutputValue(long val) {
	outputValue = Optional.of(val);
	outputValues.add(val);
    }
    
    public Optional<Long> mostRecentOutputValue() {
	return outputValue;
    }
    
    public long relativeBase() {
	return relativeBase;
    }
    
    public void setRelativeBase(long base) {
	relativeBase = base;
    }
    
    public void run() {
	standby = false;
	while(!halt && !standby) {
	    long instruction = memory.get(instructionPointer);
	    OpCodeCommand command = OpCodeCommandFactory.getCommand(instruction, this);
	    command.execute();
	    instructionPointer = command.moveInstructionPointer();
	}
    }

}
