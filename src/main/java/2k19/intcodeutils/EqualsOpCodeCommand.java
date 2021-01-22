package intcodeutils;

import myutils19.IntCodeComputer;

public class EqualsOpCodeCommand implements OpCodeCommand {
    private IntCodeMemory memory;
    private int equalsSkipCount = 4;
    private final long index;
    private final ParameterMode c;
    private final ParameterMode b;
    private final ParameterMode a;
    private final IntCodeComputer computer;
    private final ParameterModeParser parser;
    private final int id = 8;

    public EqualsOpCodeCommand(IntCodeComputer computer, ParameterMode a, ParameterMode b, ParameterMode c) {
	memory = computer.memory();
	this.computer = computer;
	index = computer.instructionPointer();
	this.a = a;
	this.b = b;
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	// c
	long pos1 = parser.getTargetIndex(c, index + 1, memory.get(index + 1),
		memory.get(index + 1) + computer.relativeBase());
	// b
	long pos2 = parser.getTargetIndex(b, index + 2, memory.get(index + 2),
		memory.get(index + 2) + computer.relativeBase());
	// a
	long targetPos = parser.getTargetIndex(a, index + 3, memory.get(index + 3),
		memory.get(index + 3) + computer.relativeBase());

	long val1 = memory.get(pos1);
	long val2 = memory.get(pos2);
	

	if (val1 == val2) {
	    memory.set(targetPos, 1);
	} else {
	    memory.set(targetPos, 0);
	}
    }

    @Override
    public long moveInstructionPointer() {
	return index + equalsSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }
}
