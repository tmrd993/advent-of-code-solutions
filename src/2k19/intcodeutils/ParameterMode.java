package intcodeutils;

import java.util.Map;
import java.util.HashMap;

public enum ParameterMode {
    POSITION(0, "Position"),
    IMMEDIATE(1, "Immediate");
    
    private static final Map<Integer, ParameterMode> parameterModes = new HashMap<>(values().length, 1);
    
    static {
	for(ParameterMode pm : values())
	    parameterModes.put(pm.val, pm);
    }
    
    private final int val;
    private final String name;
    
    ParameterMode(int val, String name) {
	this.val = val;
	this.name = name;
    }
    
    @Override
    public String toString() {
	return name;
    }
    
    public int getValue() {
	return val;
    }
    
    public static ParameterMode of(int val) {
	ParameterMode pm = parameterModes.get(val);
	if(pm == null) {
	    throw new IllegalArgumentException("Value " + val + " is not mapped to any parameter mode");
	}
	return pm;
    }

}
