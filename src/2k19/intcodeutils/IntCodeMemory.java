package intcodeutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntCodeMemory {
    private List<Long> programMemory;
    private Map<Long, Long> outsideMemoryRange;
    
    public IntCodeMemory(List<Long> programMemory) {
	this.programMemory = programMemory;
	outsideMemoryRange = new HashMap<>();
    }
    
    public long get(long index) {
	if(index > mainMemorySize() - 1) {
	    outsideMemoryRange.putIfAbsent(index, 0L);
	    return outsideMemoryRange.get(index);
	}
	int mainIndex = (int) index;
	return programMemory.get(mainIndex);
    }
    
    public void set(long index, long value) {
	if(index > mainMemorySize() - 1) {
	    outsideMemoryRange.putIfAbsent(index, 0L);
	    outsideMemoryRange.put(index, value);
	}
	else {
	    int mainIndex = (int) index;
	    programMemory.set(mainIndex, value);
	}
    }
    
    public int mainMemorySize() {
	return programMemory.size();
    }
    
    public int totalSize() {
	return programMemory.size() + outsideMemoryRange.size();
    }

}
