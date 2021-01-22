package myutils17;

import java.util.Map;

public class IncreaseCommand implements Command {

    private final String registerA;
    private final String registerB;
    private final int incrBy;
    private final int cmpVal;
    private Map<String, Integer> registers;
    private final String condition;

    public IncreaseCommand(String registerA, String registerB, int incrBy, int cmpVal, Map<String, Integer> registers,
	    String condition) {
	this.registerA = registerA;
	this.registerB = registerB;
	this.incrBy = incrBy;
	this.registers = registers;
	this.condition = condition;
	this.cmpVal = cmpVal;
    }

    @Override
    public int execute() {
	if (condition.equals("==")) {
	    if(registers.get(registerB) == cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }
	} else if (condition.equals("<")) {
	    if(registers.get(registerB) < cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }

	} else if (condition.equals(">")) {
	    if(registers.get(registerB) > cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }

	} else if (condition.equals("<=")) {
	    if(registers.get(registerB) <= cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }

	} else if (condition.equals(">=")) {
	    if(registers.get(registerB) >= cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }

	} else if (condition.equals("!=")) {
	    if(registers.get(registerB) != cmpVal) {
		registers.put(registerA, registers.get(registerA) + incrBy);
	    }
	}

	return registers.get(registerA);

    }

}
