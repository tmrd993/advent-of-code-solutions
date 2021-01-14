package myutils16;

public class InstructionFactory {
    
    public static Instruction getInstruction(String instructionStr, int[] registers, int index) {
	String[] instructionParams = instructionStr.split(" ");
	String instructionName = instructionParams[0];
	
	switch(instructionName) {
	case "cpy":
	    return new Cpy(registers, index, instructionParams[1], instructionParams[2]);
	case "inc":
	    return new Inc(registers, index, instructionParams[1]);
	case "dec":
	    return new Dec(registers, index, instructionParams[1]);
	case "jnz":
	    return new Jnz(registers, index, instructionParams[1], instructionParams[2]);
	default:
	    throw new IllegalArgumentException(instructionName + " is not mapped to an instruction");
	}
    }

}
