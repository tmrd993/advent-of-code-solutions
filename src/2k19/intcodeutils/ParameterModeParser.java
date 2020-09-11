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
    
    public int getTargetIndex(ParameterMode pm, int currentIndex, int valueAtCurrentIndex) {
	switch(pm) {
	case POSITION:
	    return valueAtCurrentIndex;
	case IMMEDIATE:
	    return currentIndex;
	}
	throw new IllegalArgumentException("The supplied parameter mode " + pm.toString() + " is unsupported");
    }

}
