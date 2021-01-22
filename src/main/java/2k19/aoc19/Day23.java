package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day23 {

    private List<Long> initialProgram;
    private final int computerCount = 50;
    private final int natRegister = 255;
    
    public Day23(File inputFile) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(inputFile);
    }
    
    public long run1() {
	IntCodeComputer[] computers = getComputers();
	int targetIndex = 255;
	long targetVal = 0;
	
	boolean foundTarget = false;
	while(!foundTarget) {
	    
	    for(int i = 0; i < computerCount; i++) {
		IntCodeComputer current = computers[i];
		current.run();
		
		if(current.isStandby()) {
		    current.setInputValues(-1);
		    current.run();
		}
		
		Queue<Long> output = current.outputValues();
		while(!output.isEmpty()) {
		    long register = output.poll();
		    long x = output.poll();
		    long y = output.poll();
		    
		    if(register == targetIndex) {
			foundTarget = true;
			targetVal = y;
			break;
		    }
		    
		    if(register < computerCount) {
			computers[(int) register].setInputValues(x, y);
		    }
		}
		
		if(foundTarget) {
		    break;
		}
	    }
	}
	
	return targetVal;
    }
    
    private long run2() {
	IntCodeComputer[] computers = getComputers();

	long natX = -1;
	long natY = -1;
	
	boolean foundTarget = false;
	long regZeroY = -1;
	
	while(!foundTarget) {
	    
	    for(int i = 0; i < computerCount; i++) {
		IntCodeComputer current = computers[i];
		current.run();
		
		if(current.isStandby()) {
		    current.setInputValues(-1);
		    current.run();
		}
		
		Queue<Long> output = current.outputValues();
		while(!output.isEmpty()) {
		    long register = output.poll();
		    long x = output.poll();
		    long y = output.poll();
		    
		    if(register == natRegister) {
			natX = x;
			natY = y;
		    } else {
			computers[(int) register].setInputValues(x, y);
		    }
		}
	    }
	    
	    if(computersAreIdle(computers)) {
		if(natY == regZeroY) {
		    foundTarget = true;
		}
		computers[0].setInputValues(natX, natY);
		regZeroY = natY;
	    }	    
	}

	return regZeroY;
    }
    
    private boolean computersAreIdle(IntCodeComputer[] computers) {
	for(IntCodeComputer computer : computers) {
	    if(!computer.inputValues().isEmpty()) {
		return false;
	    }
	}
	
	return true;
    }
    
    private IntCodeComputer[] getComputers() {
	IntCodeComputer[] computers = new IntCodeComputer[computerCount];
	for(int i = 0; i < computerCount; i++) {
	    IntCodeComputer comp = new IntCodeComputer(new ArrayList<>(initialProgram));
	    comp.setInputValues(i);
	    computers[i] = comp;
	}
	
	return computers;
    }
    
    public static void main(String[] args) {
	Day23 test = new Day23(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 23\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
