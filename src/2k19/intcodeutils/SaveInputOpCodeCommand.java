package intcodeutils;

import java.util.List;
import java.util.Scanner;

import myutils19.IntCodeComputer;

public class SaveInputOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private final int saveInputSkipCount = 2;
    private final int index;
    private final ParameterMode c;
    private final ParameterModeParser parser;
    private final int id = 3;
    
    public SaveInputOpCodeCommand(IntCodeComputer computer, ParameterMode c) {
	program = computer.program();
	index = computer.instructionPointer();
	this.c = c;
	parser = ParameterModeParser.getInstance();
    }

    @Override
    public void execute() {
	int targetPos = parser.getTargetIndex(c, index + 1, program.get(index + 1));
	Scanner sc = new Scanner(System.in);
	System.out.println("Opcode 3 detected, please enter the input value: ");
	int userInput = sc.nextInt();
	program.set(targetPos, userInput);
	sc.close();
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
