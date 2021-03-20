package aoc15;

public class Day25 {
    private final int targetRow = 2978;
    private final int targetColumn = 3083; 
    
    private final int multiplicator = 252533;
    private final int modulus = 33554393;
    
    private final int firstCode = 20151125;
    
    public long run1() {
	int targetNum = getNumAtTargetPos();
	
	long previousCode = firstCode;
	long nextCode = 0;
	for(int i = 2; i <= targetNum; i++) {
	    nextCode = (previousCode * multiplicator) % modulus;
	    previousCode = nextCode;
	}
	
	return nextCode;
    }
    
    public int getNumAtTargetPos() {
	int colOffset = 2;
	int targetNum = 1;
	for(int i = 1; i <= targetColumn - 1; i++) {
	    targetNum += (colOffset++);
	}
	
	int rowOffset = targetColumn;
	for(int i = 1; i <= targetRow - 1; i++) {
	    targetNum += (rowOffset++);
	}
	
	return targetNum;
    }
    
    public static void main(String[] args) {
	Day25 test = new Day25();
	System.out.println(test.run1());
    }

}
