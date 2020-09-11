package intcodeutils;

import java.util.List;

import myutils19.IntCodeComputer;

public class JumpIfTrueOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private int jumpIfTrueSkipCount = 3;
    private final int index;
    private final ParameterMode c;
    private final ParameterMode b;
    private final ParameterModeParser parser;
    private final int id = 5;
    
    public JumpIfTrueOpCodeCommand(IntCodeComputer computer, ParameterMode b, ParameterMode c) {
	program = computer.program();
	index = computer.instructionPointer();
	this.c = c;
	this.b = b;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	int posVal1 = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	int posVal2 = parser.getTargetIndex(b, index + 2, program.get(index + 2));
	int val1 = program.get(posVal1);
	if(val1 != 0) {
	    jumpIfTrueSkipCount = program.get(posVal2) - index;
	}
    }

    @Override
    public int moveInstructionPointer() {
	return index + jumpIfTrueSkipCount;
    }

    @Override
    public int opCodeId() {
	return id;
    }
}
