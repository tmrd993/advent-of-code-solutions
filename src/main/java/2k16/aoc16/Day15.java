package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils16.Pair;
import myutils16.StaticUtils;

public class Day15 {

    private List<String> rawData;

    public Day15(File input) {
	rawData = StaticUtils.inputToList(input);
    }

    public int run(boolean part2) {
	List<Pair<Integer, Integer>> positionList = parseInput();
	
	if(part2) {
	    positionList.add(new Pair<>(11, 0));
	}

	int index = 0;
	while ((positionList.get(0).v() + index) % positionList.get(0).k() != 0) {
	    index++;
	}
	
	int offset = 1;
	int skipCount = positionList.get(0).k();
	for (int i = 0; i < positionList.size() - 1; i++) {
	    Pair<Integer, Integer> cmpVal = positionList.get(i + 1);
	    int modVal = cmpVal.k();
	    int startPos = cmpVal.v();

	    while ((startPos + index + offset) % modVal != 0) {
		index += skipCount;
	    }

	    // should be skipCount = lcm(modVal1, modVal2) but lcm of prime numbers reduces
	    // to modVal1 * modVal2
	    skipCount *= modVal;
	    offset++;
	}

	return index - 1;
    }

    private List<Pair<Integer, Integer>> parseInput() {
	List<Pair<Integer, Integer>> positionList = new ArrayList<>();

	for (String line : rawData) {
	    int modVal = Integer.parseInt(line.substring(line.indexOf("has") + 4, line.indexOf("pos") - 1));
	    int startPos = Integer.parseInt(line
		    .substring(line.indexOf("pos", line.indexOf(';')) + "position".length() + 1, line.length() - 1));
	    positionList.add(new Pair<>(modVal, startPos));
	}

	return positionList;
    }

    public static void main(String[] args) {
	Day15 test = new Day15(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 15\\InputFile.txt"));
	System.out.println(test.run(true));
    }

}
