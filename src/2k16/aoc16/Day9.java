package aoc16;

import java.io.File;
import java.util.stream.Collectors;

import myutils16.StaticUtils;

public class Day9 {

    private String input;

    public Day9(File inputFile) {
	input = StaticUtils.inputToList(inputFile).stream().collect(Collectors.joining());
    }

    public int run1() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < input.length(); i++) {
	    // start of marker
	    if (input.charAt(i) == '(') {
		int indexOfX = input.indexOf('x', i);
		int indexClosingParen = input.indexOf(')', i);
		int subStrLen = Integer.parseInt(input.substring(i + 1, indexOfX));
		int repAmount = Integer.parseInt(input.substring(indexOfX + 1, indexClosingParen));
		String toRepeat = input.substring(indexClosingParen + 1, indexClosingParen + subStrLen + 1);
		sb.append(repeatStr(toRepeat, repAmount));
		i += subStrLen + (indexClosingParen - i);
	    } else {
		sb.append(input.charAt(i));
	    }
	}
	return sb.length();
    }

    public long run2() {
	return expand(input, 1);
    }

    private long expand(String input, int multiplyBy) {
	if (!input.contains("(") && !input.contains(")")) {
	    return multiplyBy * input.length();
	}

	long counter = 0;
	for (int i = 0; i < input.length(); i++) {
	    if (input.charAt(i) == '(') {
		int indexOfX = input.indexOf('x', i);
		int indexClosingParen = input.indexOf(')', i);
		int subStrLen = Integer.parseInt(input.substring(i + 1, indexOfX));
		int repAmount = Integer.parseInt(input.substring(indexOfX + 1, indexClosingParen));
		counter += expand(input.substring(indexClosingParen + 1, subStrLen + indexClosingParen + 1),
			repAmount * multiplyBy);
		i += subStrLen + (indexClosingParen - i);
	    } else {
		counter++;
	    }

	}

	return counter;

    }

    private String repeatStr(String str, int amount) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < amount; i++) {
	    sb.append(str);
	}
	return sb.toString();
    }

    public static void main(String[] args) {
	Day9 test = new Day9(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 9\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
