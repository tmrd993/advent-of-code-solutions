package intcodeutils;

import java.util.List;

import myutils19.IntCodeComputer;

public class OutputOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private final int outputSkipCount = 2;
    private final int index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final IntCodeComputer computer;
    private final int id = 4;
    
    public OutputOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	this.computer = computer;
	program = computer.program();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	int targetPos = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	int outputVal = program.get(targetPos);
	computer.saveOutputValue(outputVal);
	//System.out.println("Opcode 4 detected, output: " + outputVal);
    }

    @Override
    public int moveInstructionPointer() {
	return index + outputSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
