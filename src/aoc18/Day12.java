package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Day12 {
    private String initialState;
    List<String> spreadNotes;

    public Day12(String initialState, List<String> spreadNotes) {
	this.initialState = initialState;
	this.spreadNotes = spreadNotes;
    }

    // part 1
    public int run(int gens) {
	String currentState = initialState;
	// position of plant number 0
	int zeroPosition = 0;

	for (int i = 0; i < gens; i++) {
	    StringBuilder tempState = new StringBuilder();

	    // go through left bounds
	    boolean leftAppendFlag = false;
	    for (String note : spreadNotes) {
		if (note.substring(0, 5).equals("...." + currentState.charAt(0))) {
		    tempState.append(note.charAt(note.length() - 1));
		    zeroPosition++;
		    leftAppendFlag = true;
		    break;
		}
	    }

	    for (String note : spreadNotes) {
		if (note.substring(0, 5).equals("..." + currentState.charAt(0) + currentState.charAt(1))) {
		    tempState.append(note.charAt(note.length() - 1));
		    zeroPosition++;
		    leftAppendFlag = false;
		    break;
		}
	    }

	    if (leftAppendFlag) {
		tempState.append(".");
		zeroPosition++;
	    }

	    // go through current state
	    for (int j = 0; j < currentState.length(); j++) {
		boolean appendFlag = false;
		String compare = getComparison(currentState, j);

		for (String note : spreadNotes) {
		    if (note.substring(0, 5).equals(compare)) {
			appendFlag = true;
			tempState.append(note.charAt(note.length() - 1));
		    }
		}

		if (!appendFlag) {
		    tempState.append(currentState.charAt(j));
		}
	    }

	    // go through right bounds
	    boolean rightAppendFlag = false;
	    for (String note : spreadNotes) {
		if (note.substring(0, 5).equals("" + currentState.charAt(currentState.length() - 2)
			+ currentState.charAt(currentState.length() - 1) + "...")) {
		    tempState.append(note.charAt(note.length() - 1));
		    rightAppendFlag = true;
		    break;
		}
	    }

	    for (String note : spreadNotes) {
		if (note.substring(0, 5).equals(currentState.charAt(currentState.length() - 1) + "....")) {
		    if (!rightAppendFlag) {
			tempState.append(".");
		    }
		    tempState.append(note.charAt(note.length() - 1));
		    break;
		}
	    }
	    // System.out.println(tempState.toString());
	    currentState = tempState.toString();
	}
	// System.out.println(currentState + "\n" + zeroPosition);
	return potSum(currentState, zeroPosition);
    }

    // part 2
    // assumes that the increase in number of pots reaches an equilibrium before
    // generation 5000
    public long run2() {
	int previous = 0;
	int convergingGen = 0;
	int convergingIncrease = 0;

	int convergenceCounter = 0;

	// breaks out of the loop after an equilibrium is reached
	// equilibrium is reached after the increase doesn't change 5 times in a
	// row
	for (int i = 0; i < 5000; i++) {
	    int current = this.run(i);

	    int currentIncrease = current - previous;
	    if (currentIncrease == convergingIncrease) {
		convergenceCounter++;
	    } else {
		convergenceCounter = 0;
	    }

	    if (convergenceCounter == 5) {
		convergingGen = i - 5;
		break;
	    }
	    convergingIncrease = currentIncrease;
	    previous = current;
	}

	// total number of pots at cutoff point (before every increase is the
	// same value)
	int convergingTotal = this.run(convergingGen);

	// total number of pots after cutoff point until generation
	// 50.000.000.000
	long result = (50000000000L - convergingGen) * convergingIncrease;

	return result + convergingTotal;
    }

    // returns the sum of numbers (indicies) of all pots that contain a plant
    private int potSum(String state, int zeroPosition) {
	int sum = 0;
	int posIndex = 0;
	for (int i = zeroPosition; i < state.length(); i++) {
	    if (state.charAt(i) == '#')
		sum += posIndex;
	    posIndex++;
	}

	int negIndex = -1;
	for (int i = zeroPosition - 1; i >= 0; i--) {
	    if (state.charAt(i) == '#')
		sum += negIndex;
	    negIndex--;
	}
	return sum;
    }

    // returns the current string (current plant + 2 left and 2 right) that gets
    // compared to all notes
    private String getComparison(String currentState, int j) {
	String compare = "";
	if (j == 0) {
	    compare = ".." + currentState.charAt(j) + currentState.charAt(j + 1) + currentState.charAt(j + 2);
	}

	else if (j == 1) {
	    compare = "." + currentState.charAt(j - 1) + currentState.charAt(j) + currentState.charAt(j + 1)
		    + currentState.charAt(j + 2);
	}

	else if (j == currentState.length() - 2) {
	    compare = "" + currentState.charAt(j - 2) + currentState.charAt(j - 1) + currentState.charAt(j)
		    + currentState.charAt(j + 1) + ".";
	}

	else if (j == currentState.length() - 1) {
	    compare = "" + currentState.charAt(j - 2) + currentState.charAt(j - 1) + currentState.charAt(j) + "..";
	}

	else {
	    compare = currentState.substring(j - 2, j + 3);
	}
	return compare;
    }

    public static void main(String[] args) throws IOException {
	String zeroState = "##.####..####...#.####..##.#..##..#####.##.#..#...#.###.###....####.###...##..#...##.#.#...##.##..";
	// String sampleState = "#..#.#..##......###...###";

	List<String> notes = Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 12\\InputFile.txt"));

	Day12 test = new Day12(zeroState, notes);

	// part 2 result
	long res = test.run2();
	System.out.println(res);
    }
}