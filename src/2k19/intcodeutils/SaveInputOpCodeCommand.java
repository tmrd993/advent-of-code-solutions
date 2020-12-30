package intcodeutils;

import java.util.Queue;

import myutils19.IntCodeComputer;

public class SaveInputOpCodeCommand implements OpCodeCommand {
    private IntCodeComputer computer;
    private final int saveInputSkipCount = 2;
    private long index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final IntCodeMemory memory;
    private final int id = 3;

    public SaveInputOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	this.computer = computer;
	memory = computer.memory();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	long targetPos = parser.getTargetIndex(c, index + 1, memory.get(index + 1), memory.get(index + 1) + computer.relativeBase());
	Queue<Long> inputValues = computer.inputValues();

	// reset the instruction pointer if no input available
	if(inputValues.isEmpty()) {
	    index -= saveInputSkipCount;
	    computer.setStandby();
	}
	else {
	    long inputValue = inputValues.poll();
	    memory.set(targetPos, inputValue);
	}
	
    }

    @Override
    public long moveInstructionPointer() {
	return index + saveInputSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
