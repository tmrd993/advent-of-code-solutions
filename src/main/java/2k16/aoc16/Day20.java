package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import myutils16.Pair;
import myutils16.StaticUtils;

public class Day20 {

    private List<Pair<Long, Long>> ranges;

    public Day20(File input) {
	List<String> rawData = StaticUtils.inputToList(input);
	ranges = getRanges(rawData);
    }

    public long run1() {
	Collections.sort(ranges, Comparator.comparing(n -> n.k()));
	boolean targetFound = false;

	long high = ranges.get(0).v();
	while (!targetFound) {
	    long max = high;
	    Optional<Pair<Long, Long>> updatedMax = ranges.stream().filter(n -> n.k() <= max)
		    .max(Comparator.comparing(num -> num.v()));

	    if (updatedMax.isPresent() && updatedMax.get().v() != high) {
		high = updatedMax.get().v();
	    } else {
		targetFound = true;
	    }

	}

	return high + 1;
    }

    public long run2() {
	Collections.sort(ranges, Comparator.comparing(n -> n.k()));

	long count = 0;
	long high = ranges.get(0).v();

	boolean targetFound = false;
	while (!targetFound) {
	    long max = high;
	    Optional<Pair<Long, Long>> updatedMax = ranges.stream().filter(n -> n.k() <= max)
		    .max(Comparator.comparing(num -> num.v()));

	    if (updatedMax.isPresent() && updatedMax.get().v() != high) {
		high = updatedMax.get().v();
	    } else {

		Optional<Pair<Long, Long>> nextLow = ranges.stream().filter(n -> n.k() > max)
			.min(Comparator.comparing(n -> n.k()));
		
		if(nextLow.isPresent()) {
		    count += nextLow.get().k() - high - 1;
		    high = nextLow.get().v();
		} else {
		    targetFound = true;
		}
	    }
	}

	return count;
    }

    private List<Pair<Long, Long>> getRanges(List<String> rawData) {
	List<Pair<Long, Long>> ranges = new ArrayList<>();
	for (String line : rawData) {
	    long low = Long.parseLong(line.substring(0, line.indexOf('-')));
	    long high = Long.parseLong(line.substring(line.indexOf('-') + 1));
	    ranges.add(new Pair<>(low, high));
	}
	return ranges;
    }

    public static void main(String[] args) {
	Day20 test = new Day20(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 20\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
