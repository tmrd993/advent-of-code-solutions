package aoc17;

import java.util.HashSet;
import java.util.Set;

/**
 * NOTE: This is not a universal solution. I don't know if other input only
 * differs in the stepcount. If they do, it's a universal solution. Otherwise, it
 * isnt.
 * 
 * @author Timucin Merdin
 *
 */
public class Day25 {

    private final int STEP_COUNT;

    public Day25(int stepCount) {
	this.STEP_COUNT = stepCount;
    }

    public int getChecksum() {
	char state = 'A';
	// all the tape positions containing a 1
	Set<Integer> tapeValuesOne = new HashSet<>();
	int steps = 0;

	int cursor = 0;
	while (steps < STEP_COUNT) {
	    if (state == 'A') {
		if (tapeValuesOne.contains(cursor)) {
		    tapeValuesOne.remove(cursor);
		    cursor++;
		    state = 'F';
		} else {
		    tapeValuesOne.add(cursor);
		    cursor++;
		    state = 'B';
		}
	    }

	    else if (state == 'B') {
		if (tapeValuesOne.contains(cursor)) {
		    cursor--;
		    state = 'C';
		} else {
		    cursor--;
		    state = 'B';
		}
	    }

	    else if (state == 'C') {
		if (tapeValuesOne.contains(cursor)) {
		    tapeValuesOne.remove(cursor);
		    cursor++;
		    state = 'C';
		} else {
		    tapeValuesOne.add(cursor);
		    cursor--;
		    state = 'D';
		}
	    }

	    else if (state == 'D') {
		if (tapeValuesOne.contains(cursor)) {
		    cursor++;
		    state = 'A';
		} else {
		    tapeValuesOne.add(cursor);
		    cursor--;
		    state = 'E';
		}
	    }

	    else if (state == 'E') {
		if (tapeValuesOne.contains(cursor)) {
		    tapeValuesOne.remove(cursor);
		    cursor--;
		    state = 'D';
		} else {
		    tapeValuesOne.add(cursor);
		    cursor--;
		    state = 'F';
		}
	    }

	    else if (state == 'F') {
		if (tapeValuesOne.contains(cursor)) {
		    tapeValuesOne.remove(cursor);
		    cursor--;
		    state = 'E';
		} else {
		    tapeValuesOne.add(cursor);
		    cursor++;
		    state = 'A';
		}
	    }
	    steps++;
	}
	return tapeValuesOne.size();
    }

    public static void main(String[] args) {
	Day25 test = new Day25(12425180);
	System.out.println(test.getChecksum());
	
    }

}
