package intcodeutils;

import myutils19.IntCodeComputer;

public class AdjustRelativeBaseOpCodeCommand implements OpCodeCommand {
    private IntCodeMemory memory;
    private final int adjustRelativeSkipCount = 2;
    private final long index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final IntCodeComputer computer;
    private final int id = 9;
    
    public AdjustRelativeBaseOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	this.computer = computer;
	//program = computer.program();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
	memory = computer.memory();
    }

    @Override
    public void execute() {
	long targetPos = parser.getTargetIndex(c, index + 1, memory.get(index + 1), memory.get(index + 1) + computer.relativeBase());
	computer.setRelativeBase(computer.relativeBase() + memory.get(targetPos));
    }

    @Override
    public long moveInstructionPointer() {
	return index + adjustRelativeSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
