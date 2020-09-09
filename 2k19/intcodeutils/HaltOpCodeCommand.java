package intcodeutils;

import myutils19.IntCodeComputer;

public class HaltOpCodeCommand implements OpCodeCommand {
    private IntCodeComputer computer;
    private final int haltSkipCount = 0;
    //private final int id = 99;
    
    public HaltOpCodeCommand(IntCodeComputer computer) {
	this.computer = computer;
    }

    @Override
    public void execute() {
	computer.setHalt();
    }

    @Override
    public int skipCount() {
	return haltSkipCount;
    }

}
