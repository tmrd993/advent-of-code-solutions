package intcodeutils;

import myutils19.IntCodeComputer;

public class JumpIfTrueOpCodeCommand implements OpCodeCommand {
    private IntCodeMemory memory;
    private long jumpIfTrueSkipCount = 3;
    private final long index;
    private final ParameterMode c;
    private final ParameterMode b;
    private final IntCodeComputer computer;
    private final ParameterModeParser parser;
    private final int id = 5;
    
    public JumpIfTrueOpCodeCommand(IntCodeComputer computer, ParameterMode b, ParameterMode c) {
	memory = computer.memory();
	this.computer = computer;
	index = computer.instructionPointer();
	this.c = c;
	this.b = b;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	long posVal1 = parser.getTargetIndex(c, index + 1, memory.get(index + 1), memory.get(index + 1) + computer.relativeBase());
	long posVal2 = parser.getTargetIndex(b, index + 2, memory.get(index + 2), memory.get(index + 2) + computer.relativeBase());
	
	long val1 = memory.get(posVal1);

	if(val1 != 0) {
	    jumpIfTrueSkipCount = memory.get(posVal2) - index;
	}
    }

    @Override
    public long moveInstructionPointer() {
	return index + jumpIfTrueSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }
}
