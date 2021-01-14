package myutils16;

public class Cpy implements Instruction {
    
    private final int index;
    private final int[] registers;
    private final String paramA;
    private final String paramB;
    
    public Cpy(int[] registers, int index, String paramA, String paramB) {
	this.index = index;
	this.registers = registers;
	this.paramA = paramA;
	this.paramB = paramB;
    }

    @Override
    public void execute() {
	if(paramA.matches("-?\\d+")) {
	    registers[indexOf(paramB.charAt(0))] = Integer.parseInt(paramA);
	} else {
	    registers[indexOf(paramB.charAt(0))] = registers[indexOf(paramA.charAt(0))];
	}
    }

    @Override
    public int index() {
	return index + 1;
    }

}
