package aoc15;

import java.io.File;
import java.util.List;
import java.util.Set;

import myutils15.StaticUtils;

public class Day5 {

    private List<String> rawData;
    private Set<String> illegalStrings = Set.of("ab", "cd", "pq", "xy");
    private Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');

    public Day5(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	return (int) rawData.stream().filter(s -> isNicePt1(s)).count();
    }

    public int run2() {
	return (int) rawData.stream().filter(s -> isNicePt2(s)).count();
    }

    private boolean isNicePt1(String str) {
	int vowelCount = 0;
	boolean hasDoubleLetter = false;
	boolean hasIllegalStr = false;
	for (int i = 0; i < str.length(); i++) {
	    if (vowels.contains(str.charAt(i))) {
		vowelCount++;
	    }

	    if (i < str.length() - 1 && str.charAt(i) == str.charAt(i + 1)) {
		hasDoubleLetter = true;
	    }

	    if (i < str.length() - 1 && illegalStrings.contains(str.charAt(i) + "" + str.charAt(i + 1))) {
		hasIllegalStr = true;
	    }
	}

	return vowelCount >= 3 && hasDoubleLetter && !hasIllegalStr;
    }

    private boolean isNicePt2(String str) {
	boolean hasPair = false;
	boolean hasRepeatBetween = false;

	for (int i = 0; i < str.length() - 1; i++) {
	    String pair = str.charAt(i) + "" + str.charAt(i + 1);
	    if (str.indexOf(pair, str.indexOf(pair) + 2) >= 0) {
		hasPair = true;
	    }

	    if (i < str.length() - 2 && (str.charAt(i) != str.charAt(i + 1) && str.charAt(i) == str.charAt(i + 2))) {
		hasRepeatBetween = true;
	    }
	}

	return hasPair && hasRepeatBetween;
    }

    public static void main(String[] args) {
	Day5 test = new Day5(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 5\\InputFile.txt"));
	System.out.println(test.run2());

    }

}
