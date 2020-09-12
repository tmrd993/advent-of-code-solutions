package intcodeutils;

import myutils19.IntCodeComputer;

public class MultiplyOpCodeCommand implements OpCodeCommand {
    private IntCodeMemory memory;
    private final int multiplySkipCount = 4;
    private final long index;
    private final ParameterMode a;
    private final ParameterMode b;
    private final ParameterMode c;
    private final IntCodeComputer computer;
    private final int id = 2;
    private final ParameterModeParser parser;

    public MultiplyOpCodeCommand(IntCodeComputer computer, ParameterMode a, ParameterMode b, ParameterMode c) {
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

	long valAtPos1 = memory.get(pos1);
	long valAtPos2 = memory.get(pos2);
	memory.set(targetPos, valAtPos1 * valAtPos2);
    }

    @Override
    public long moveInstructionPointer() {
	return index + multiplySkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
