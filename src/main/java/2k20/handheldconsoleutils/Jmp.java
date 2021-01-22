package handheldconsoleutils;

public class Jmp implements Instruction {
    
    private int jmpCount;
    
    public Jmp(int jmpCount) {
	this.jmpCount = jmpCount;
    }

    @Override
    public int run() {
	return jmpCount;
    }

}
