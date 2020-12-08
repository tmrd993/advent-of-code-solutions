package handheldconsoleutils;

import myutils20.HandheldGameConsole;

public class Acc implements Instruction {
    
    private HandheldGameConsole console;
    private int dAcc;
    
    public Acc(HandheldGameConsole console, int dAcc) {
	this.console = console;
	this.dAcc = dAcc;
    }

    @Override
    public int run() {
	console.increaseAcc(dAcc);
	return 1;
    }

}
