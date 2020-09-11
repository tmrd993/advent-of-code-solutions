package intcodeutils;

import java.util.List;

import myutils19.IntCodeComputer;

public class LessThanOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private int lessThanSkipCount = 4;
    private final int index;
    private final ParameterMode c;
    private final ParameterMode b;
    private final ParameterMode a;
    private final ParameterModeParser parser;
    private final int id = 7;

    public LessThanOpCodeCommand(IntCodeComputer computer, ParameterMode a, ParameterMode b, ParameterMode c) {
	program = computer.program();
	index = computer.instructionPointer();
	this.a = a;
	this.b = b;
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	int pos1 = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	int pos2 = parser.getTargetIndex(b, index + 2, program.get(index + 2));
	int targetPos = parser.getTargetIndex(a, index + 3, program.get(index + 3));

	int val1 = program.get(pos1);
	int val2 = program.get(pos2);

	if (val1 < val2) {
	    program.set(targetPos, 1);
	} else {
	    program.set(targetPos, 0);
	}

    }

    @Override
    public int moveInstructionPointer() {
	return index + lessThanSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }

}
