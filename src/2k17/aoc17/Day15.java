package aoc17;

import java.util.LinkedList;
import java.util.Queue;

public class Day15 {
    private final int FACTOR_A = 16807;
    private final int FACTOR_B = 48271;
    private final int DIVISOR = 2147483647;
    private final int START_VALUE_A;
    private final int START_VALUE_B;
    private final int MOD_A = 4;
    private final int MOD_B = 8;

    public Day15(int startGeneratorA, int startGeneratorB) {
	this.START_VALUE_A = startGeneratorA;
	this.START_VALUE_B = startGeneratorB;
    }

    // part 1
    public int getPairCount() {
	int last16bitMask = (1 << 16) - 1;
	int count = 0;

	long previousValueA = START_VALUE_A;
	long previousValueB = START_VALUE_B;
	for (int i = 0; i < 40000000; i++) {
	    long currentValueA = (previousValueA * FACTOR_A) % DIVISOR;
	    long currentValueB = (previousValueB * FACTOR_B) % DIVISOR;

	    if ((currentValueA & last16bitMask) == (currentValueB & last16bitMask)) {
		count++;
	    }

	    previousValueA = currentValueA;
	    previousValueB = currentValueB;
	}

	return count;
    }

    // part 2
    public int getPairCount2() {
	int last16bitMask = (1 << 16) - 1;
	int count = 0;

	Queue<Long> validValuesA = new LinkedList<>();
	Queue<Long> validValuesB = new LinkedList<>();

	long previousValueA = START_VALUE_A;
	long previousValueB = START_VALUE_B;
	int pairCount = 0;
	while(pairCount < 5000000) {
	    long currentValueA = (previousValueA * FACTOR_A) % DIVISOR;
	    long currentValueB = (previousValueB * FACTOR_B) % DIVISOR;

	    //System.out.println(currentValueA + "    " + currentValueB);

	    if(currentValueA % MOD_A == 0) {
		validValuesA.offer(currentValueA);
		//System.out.println(currentValueA);
	    }

	    if(currentValueB % MOD_B == 0) {
		validValuesB.offer(currentValueB);
		//System.out.println(currentValueB);
	    }

	    while(!validValuesA.isEmpty() && !validValuesB.isEmpty()) {
		pairCount++;
		long valA = validValuesA.poll();
		long valB = validValuesB.poll();
		//System.out.println(valA + "    " + valB);


		if((valA & last16bitMask) == (valB & last16bitMask))
		    count++;
	    }

	    previousValueA = currentValueA;
	    previousValueB = currentValueB;

	}
	return count;
    }

    public static void main(String[] args) {

	int startGenA = 722;
	int startGenB = 354;


	 //sample values
	 //int startGenA = 65;
	 //int startGenB = 8921;


	Day15 test = new Day15(startGenA, startGenB);
	System.out.println(test.getPairCount2());

    }

}
