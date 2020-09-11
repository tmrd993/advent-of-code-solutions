package intcodeutils;

import java.util.List;
import java.util.Queue;

import myutils19.IntCodeComputer;

public class SaveInputOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private final int saveInputSkipCount = 2;
    private int index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final IntCodeComputer computer;
    private final int id = 3;

    public SaveInputOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	this.computer = computer;
	program = computer.program();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	int targetPos = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	Queue<Integer> inputValues = computer.inputValues();

	// reset the instruction pointer if no input available
	if(inputValues.isEmpty()) {
	    index -= saveInputSkipCount;
	    computer.setStandby();
	    //System.out.println(computer + "   paused");
	}
	else {
	    int inputValue = inputValues.poll();
	    program.set(targetPos, inputValue);
	}
	
    }

    @Override
    public int moveInstructionPointer() {
	return index + saveInputSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
