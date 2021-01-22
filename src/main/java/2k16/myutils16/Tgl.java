package myutils16;

import java.util.List;

public class Tgl implements Instruction {
    
    private int[] registers;
    private List<String> instructions;
    private final String paramA;
    private final int index;

    public Tgl(int index, String paramA, List<String> instructions, int[] registers) {
	this.instructions = instructions;
	this.index = index;
	this.paramA = paramA;
	this.registers = registers;
    }
    
    @Override
    public int execute() {
	int cmp = paramA.matches("-?\\d+") ? index + Integer.parseInt(paramA) : index + registers[indexOf(paramA.charAt(0))];
	if(cmp >= 0 && cmp < instructions.size()) {
	    String instr = instructions.get(cmp);
	    String rplc = "";
	    if(instr.contains("inc")) {
		rplc = "dec";
	    } else if(instr.contains("tgl") || instr.contains("dec")) {
		rplc = "inc";
	    } else if(instr.contains("jnz")) {
		rplc = "cpy";
	    } else if(instr.contains("cpy")) {
		rplc = "jnz";
	    }
	    instructions.set(cmp, rplc + instr.substring(3));
	}
	return 0;
    }

    @Override
    public int index() {
	return index + 1;
    }

}
