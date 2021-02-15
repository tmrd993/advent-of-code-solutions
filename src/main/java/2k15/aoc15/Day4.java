package aoc15;

import myutils15.StaticUtils;

public class Day4 {

    private final String input = "iwrupvqb";
    
    public int run(boolean part2) {
	int index = 0;
	while(!startsWithNZero(StaticUtils.md5AsByteArray(input + index), part2 ? 6 : 5)) {
	    index++;
	}
	
	return index;
    }
    
    private boolean startsWithNZero(byte[] hash, int n) {
	int cmp = 0;
	int cmpIndex = 0;
	for(int i = 0; i < n - (n / 2); i++) {
	    // move high nibble to current index
	    cmp |= ((hash[i] >> 4) & 0x0F) << cmpIndex++;
	    if(cmpIndex < n)
		// move low nibble to current index
		cmp |= (hash[i] & 0x0F) << cmpIndex++;
	}
	// first 5 high/low nibbles have to be 0
	return cmp == 0;
    }
    
    public static void main(String[] args) {
	Day4 test = new Day4();
	System.out.println(test.run(true));
    }

}
