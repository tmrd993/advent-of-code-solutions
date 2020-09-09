package intcodeutils;

import myutils19.IntCodeComputer;

public class OpCodeCommandFactory {
    
    private OpCodeCommandFactory() {};
    
    public static OpCodeCommand getCommand(int opCode, IntCodeComputer computer, int index) {
	switch(opCode) {
	case 1:
	    return new AdditionOpCodeCommand(computer.program(), index);
	case 2:
	    return new MultiplyOpCodeCommand(computer.program(), index);
	case 99:
	    return new HaltOpCodeCommand(computer);
	default:
	    throw new IllegalArgumentException("Supplied op-code " + opCode + " is not mapped to any action");
	}
    }
}
