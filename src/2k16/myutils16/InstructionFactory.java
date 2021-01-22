package myutils16;

import java.util.List;

public class InstructionFactory {

    public static Instruction getInstruction(String instructionStr, int[] registers, int index,
	    List<String> instructions) {
	String[] instructionParams = instructionStr.split(" ");
	String instructionName = instructionParams[0];

	switch (instructionName) {
	case "cpy":
	    return instructionParams.length == 3 ? new Cpy(registers, index, instructionParams[1], instructionParams[2])
		    : new Skp(index);
	case "inc":
	    return instructionParams.length == 2 ? new Inc(registers, index, instructionParams[1]) : new Skp(index);
	case "dec":
	    return instructionParams.length == 2 ? new Dec(registers, index, instructionParams[1]) : new Skp(index);
	case "jnz":
	    return instructionParams.length == 3 ? new Jnz(registers, index, instructionParams[1], instructionParams[2]) : new Skp(index);
	case "tgl":
	    return instructionParams.length == 2 ? new Tgl(index, instructionParams[1], instructions, registers) : new Skp(index);
	case "out":
	    return new Out(registers, index, instructionParams[1]);
	default:
	    throw new IllegalArgumentException(instructionName + " is not mapped to an instruction");
	}
    }

}
