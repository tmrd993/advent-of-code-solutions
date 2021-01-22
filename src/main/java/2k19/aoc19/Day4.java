package aoc19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Day4 {
    private final int rangeLower = 234208;
    private final int rangeUpper = 765869;
    
    public long run1() {
	return IntStream
		.range(rangeLower, rangeUpper + 1)
		.filter(num -> isIncreasing(num) && hasAtLeastDoubleChar(num))
		.count(); 
    }
    
    public long run2() {
	return IntStream
		.range(rangeLower, rangeUpper + 1)
		.filter(num -> isIncreasing(num) && hasExactlyDoubleChar(num))
		.count(); 
    }
    
    private boolean isIncreasing(int num) {
	String numStr = String.valueOf(num);
	byte[] chars = numStr.getBytes();
	Arrays.sort(chars);
	return numStr.equals(new String(chars));
    }
    
    private boolean hasAtLeastDoubleChar(int num) {
	String numStr = String.valueOf(num);
	for(int i = 0; i < numStr.length() - 1; i++) {
	    if(numStr.charAt(i) == numStr.charAt(i + 1))
		return true;
	}
	return false;
    }
    
    private boolean hasExactlyDoubleChar(int num) {
	Map<Character, Integer> frequency = new HashMap<>();
	String numStr = String.valueOf(num);
	for(int i = 0; i < numStr.length(); i++) {
	    frequency.putIfAbsent(numStr.charAt(i), 0);
	    frequency.put(numStr.charAt(i), frequency.get(numStr.charAt(i)) + 1);
	}
	return frequency.entrySet()
		.stream()
		.filter(e -> e.getValue() == 2)
		.count() > 0;
    }
    
    public static void main(String[] args) {
	Day4 test = new Day4();
	System.out.println(test.run2());
    }

}
