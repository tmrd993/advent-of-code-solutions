package aoc20;

public class Day25 {
    
    private final int doorKey = 1004920;
    private final int cardKey = 10441485;
    private final int subjectNum = 7;
    private final int divNum = 20201227;
    
    public long run1() {
	
	int loopSizeDoor = findLoopSize(doorKey);
	
	long val = 1;
	for(int i = 0; i < loopSizeDoor; i++) {
	    val *= cardKey;
	    val %= divNum;
	}
	
	return val;
    }
    
    public int findLoopSize(int key) {
	int i = 1;
	int transformedNum = 1;
	
	int val = 1;
	while(key != transformedNum) {
	    
	    val *= subjectNum;
	    val %= divNum;
	    
	    transformedNum = val;
	    i++;
	}
	
	return i - 1;
    }
    

    public static void main(String[] args) {
	Day25 test = new Day25();
	System.out.println(test.run1());

    }
}
