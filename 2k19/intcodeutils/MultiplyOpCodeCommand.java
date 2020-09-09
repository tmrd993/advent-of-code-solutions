package intcodeutils;

import java.util.List;

public class MultiplyOpCodeCommand implements OpCodeCommand {
    private List<Integer> program;
    private final int multiplySkipCount = 4;
    private final int index;
    //private final int id = 2;
    
    public MultiplyOpCodeCommand(List<Integer> program, int index) {
	this.program = program;
	this.index = index;
    }

    @Override
    public void execute() {
	int pos1 = program.get(index + 1);
	int pos2 = program.get(index  + 2);
	int targetPos = program.get(index + 3);
	int valAtPos1 = program.get(pos1);
	int valAtPos2 = program.get(pos2);
	program.set(targetPos, valAtPos1 * valAtPos2);
    }

    @Override
    public int skipCount() {
	return multiplySkipCount;
    }

}
