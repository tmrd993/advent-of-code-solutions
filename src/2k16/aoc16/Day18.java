package aoc16;

import java.util.Set;

public class Day18 {

    private final String input = "^.^^^..^^...^.^..^^^^^.....^...^^^..^^^^.^^.^^^^^^^^.^^.^^^^...^^...^^^^.^.^..^^..^..^.^^.^.^.......";
    private final int rowsPt1 = 40;
    private final int rowsPt2 = 400000;
    private Set<String> trapSet = Set.of("^..", "..^", "^^.", ".^^");

    public int run(boolean part2) {
	String row = input;
	int rows = part2 ? rowsPt2 : rowsPt1;
	
	int count = (int) row.chars().filter(n -> (char) n == '.').count();
	for (int i = 0; i < rows - 1; i++) {
	    row = nextRow(row);
	    count += (int) row.chars().filter(n -> (char) n == '.').count();
	}

	return count;
    }

    public String nextRow(String prevRow) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < prevRow.length(); i++) {
	    if (i == 0) {
		if (trapSet.contains(new StringBuilder().append('.').append(prevRow.charAt(i))
			.append(prevRow.charAt(i + 1)).toString())) {
		    sb.append('^');
		} else {
		    sb.append('.');
		}
	    } else if (i == prevRow.length() - 1) {
		if (trapSet.contains(new StringBuilder().append(prevRow.charAt(i - 1)).append(prevRow.charAt(i))
			.append('.').toString())) {
		    sb.append('^');
		} else {
		    sb.append('.');
		}
	    } else if (trapSet.contains(new StringBuilder().append(prevRow.charAt(i - 1)).append(prevRow.charAt(i))
		    .append(prevRow.charAt(i + 1)).toString())) {
		sb.append('^');
	    } else {
		sb.append('.');
	    }
	}
	return sb.toString();
    }

    public static void main(String[] args) {
	Day18 test = new Day18();
	System.out.println(test.run(true));
    }

}
