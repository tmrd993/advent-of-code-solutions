package myutils16;

public class Inc implements Instruction {
    private final int index;
    private final int[] registers;
    private final String paramA;
    
    public Inc(int[] registers, int index, String paramA) {
	this.registers = registers;
	this.index = index;
	this.paramA = paramA;
    }

    @Override
    public int execute() {
	registers[indexOf(paramA.charAt(0))]++;
	return 0;
    }

    @Override
    public int index() {
	return index + 1;
    }

}
