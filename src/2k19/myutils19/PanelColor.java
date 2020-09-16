package myutils19;

import java.util.HashMap;
import java.util.Map;

public enum PanelColor {
    BLACK(0, "Black"), 
    WHITE(1, "White");

    private final int colorCode;
    private final String name;
    
    private static final Map<Integer, PanelColor> panelColors = new HashMap<>(values().length, 1);

    static {
	for(PanelColor pc : values())
	    panelColors.put(pc.colorCode, pc);
    }

    PanelColor(int colorCode, String name) {
	this.colorCode = colorCode;
	this.name = name;
    }
    
    @Override
    public String toString() {
	return name;
    }
    
    public int colorCode() {
	return colorCode;
    }
    
    public static PanelColor of(int colorCode) {
	PanelColor pc = panelColors.get(colorCode);
	if(pc == null) {
	    throw new IllegalArgumentException("Color code " + colorCode + " is not mapped to any Color");
	}
	return pc;
    }
    
    
    

}
