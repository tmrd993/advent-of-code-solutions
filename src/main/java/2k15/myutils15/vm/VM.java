package myutils15.vm;

import java.util.HashMap;
import java.util.Map;

// virutal machine for day 23
public class VM {

    private int instructionPointer;
    private Map<Character, Integer> registers;

    public VM() {
	registers = new HashMap<>();
	initRegisters();
    }

    public int getInstructionPointer() {
	return instructionPointer;
    }

    public void setInstructionPointer(int instructionPointer) {
	this.instructionPointer = instructionPointer;
    }

    public int getRegister(char reg) {
	if (!registers.containsKey(reg)) {
	    throw new IllegalArgumentException(reg + " is not a valid register");
	}

	return registers.get(reg);
    }

    public void setRegister(char reg, int val) {
	if (!registers.containsKey(reg)) {
	    throw new IllegalArgumentException(reg + " is not a valid register");
	}

	registers.put(reg, val);
    }

    private void initRegisters() {
	registers.put('a', 0);
	registers.put('b', 0);
    }

}
