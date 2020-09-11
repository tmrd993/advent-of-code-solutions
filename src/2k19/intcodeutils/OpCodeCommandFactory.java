package intcodeutils;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class OpCodeCommandFactory {
    
    private OpCodeCommandFactory() {};
    
    public static OpCodeCommand getCommand(int instruction, IntCodeComputer computer, int index) {
	String instrStr = StaticUtils.padWithZeroToLengthFive(String.valueOf(instruction));
	int opCode = Integer.parseInt(instrStr.substring(3, 5));
	ParameterMode a = ParameterMode.of(Character.getNumericValue(instrStr.charAt(0)));
	ParameterMode b = ParameterMode.of(Character.getNumericValue(instrStr.charAt(1)));
	ParameterMode c = ParameterMode.of(Character.getNumericValue(instrStr.charAt(2)));
	
	switch(opCode) {
	case 1:
	    return new AdditionOpCodeCommand(computer, a, b, c);
	case 2:
	    return new MultiplyOpCodeCommand(computer, a, b, c);
	case 3:
	    return new SaveInputOpCodeCommand(computer, c);
	case 4:
	    return new OutputOpCodeCommand(computer, c);
	case 5:
	    return new JumpIfTrueOpCodeCommand(computer, b, c);
	case 6:
	    return new JumpIfFalseOpCodeCommand(computer, b, c);
	case 7:
	    return new LessThanOpCodeCommand(computer, a, b, c);
	case 8:
	    return new EqualsOpCodeCommand(computer, a, b, c);
	case 99:
	    return new HaltOpCodeCommand(computer);
	default:
	    throw new IllegalArgumentException("Supplied op-code " + opCode + " is not mapped to any action");
	}
    }
    
    
}
