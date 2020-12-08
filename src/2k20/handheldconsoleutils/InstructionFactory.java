package handheldconsoleutils;

import myutils20.HandheldGameConsole;

public class InstructionFactory {
    
    public static Instruction getInstruction(String instruction, HandheldGameConsole console, int value) {
	if(instruction.equals("acc")) {
	    return new Acc(console, value);
	}
	else if(instruction.equals("jmp")) {
	    return new Jmp(value);
	}
	else if(instruction.equals("nop")) {
	    return new Nop();
	}
	
	throw new IllegalArgumentException(instruction + " is not a valid instruction");
    }

}
