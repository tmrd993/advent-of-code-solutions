package intcodeutils;

import java.util.List;

import myutils19.IntCodeComputer;

public class AdditionOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private final int additionSkipCount = 4;
    private final int index;
    private final ParameterMode a;
    private final ParameterMode b;
    private final ParameterMode c;
    private final int id = 1;
    private final ParameterModeParser parser;
    
    public AdditionOpCodeCommand(IntCodeComputer computer, ParameterMode a, ParameterMode b, ParameterMode c) {
	program = computer.program();
	index = computer.instructionPointer();
	this.a = a;
	this.b = b;
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	//c
	int pos1 = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	//b
	int pos2 = parser.getTargetIndex(b, index + 2, program.get(index  + 2));
	//a
	int targetPos = parser.getTargetIndex(a, index + 3, program.get(index + 3));
	
	int valAtPos1 = program.get(pos1);
	int valAtPos2 = program.get(pos2);
	program.set(targetPos, valAtPos1 + valAtPos2);
    }

    @Override
    public int moveInstructionPointer() {
	return index + additionSkipCount;
    }
    
    @Override
    public int opCodeId() {
	return id;
    }

}
