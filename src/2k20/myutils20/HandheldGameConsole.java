package myutils20;

import handheldconsoleutils.Instruction;
import handheldconsoleutils.InstructionFactory;

public class HandheldGameConsole {
    private int acc;
    private int ip;
    
    public HandheldGameConsole() {
	acc = 0;
	ip = 0;
    }
    
    public int acc() {
	return acc;
    }
    
    public void increaseAcc(int dAcc) {
	acc += dAcc;
    }
    
    public int ip() {
	return ip;
    }
    
    public void run(String instruction) {
	String com = instruction.substring(0, instruction.indexOf(' '));
	int value = Integer.parseInt(instruction.substring(instruction.indexOf(' ') + 1));
	
	Instruction instr = InstructionFactory.getInstruction(com, this, value);
	ip += instr.run();
    }

}
