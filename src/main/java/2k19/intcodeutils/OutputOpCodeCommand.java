package intcodeutils;

import myutils19.IntCodeComputer;

public class OutputOpCodeCommand implements OpCodeCommand {
    private IntCodeMemory memory;
    private final int outputSkipCount = 2;
    private final long index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final IntCodeComputer computer;
    private final int id = 4;
    
    public OutputOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	this.computer = computer;
	memory = computer.memory();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	long targetPos = parser.getTargetIndex(c, index + 1, memory.get(index + 1), memory.get(index + 1) + computer.relativeBase());
	long outputVal = memory.get(targetPos);
	computer.saveOutputValue(outputVal);
    }

    @Override
    public long moveInstructionPointer() {
	return index + outputSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
