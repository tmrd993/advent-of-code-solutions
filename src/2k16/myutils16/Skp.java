package myutils16;

// skips the current instruction
public class Skp implements Instruction {

    private final int index;
    
    public Skp(int index) {
	this.index = index;
    }
    
    @Override
    public int execute() {
	return 0;
    }

    @Override
    public int index() {
	return index + 1;
    }

}
