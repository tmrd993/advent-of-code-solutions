package intcodeutils;

public class ParameterModeParser {
    
    private static ParameterModeParser parser = null;
    
    private ParameterModeParser() {}
    
    public static ParameterModeParser getInstance() {
	if(parser == null) {
	    parser = new ParameterModeParser();
	}
	return parser;
    }
    
    public long getTargetIndex(ParameterMode pm, long currentIndex, long valueAtCurrentIndex, long valueAtRelativeBase) {
	switch(pm) {
	case POSITION:
	    return valueAtCurrentIndex;
	case IMMEDIATE:
	    return currentIndex;
	case RELATIVE:
	    return valueAtRelativeBase;
	}
	throw new IllegalArgumentException("The supplied parameter mode " + pm.toString() + " is unsupported");
    }

}
