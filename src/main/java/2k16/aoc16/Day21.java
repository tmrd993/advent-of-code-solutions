package aoc16;

import java.io.File;
import java.util.List;
import java.util.Map;

import myutils16.StaticUtils;

public class Day21 {

    private final String scrambleInputPt1 = "abcdefgh";
    private final String scrambleInputPt2 = "fbgdceah";
    private List<String> rawData;
    // key: index the char has been moved to, val: number of steps char needs to be
    // moved to the left to get the previous state
    private Map<Integer, Integer> moveBasedOnIndexTable = Map.of(1, 1,
	    3, 2,
	    5, 3,
	    7, 4,
	    2, 6,
	    4, 7,
	    0, 9,
	    6, 0);

    public Day21(File input) {
	rawData = StaticUtils.inputToList(input);
    }

    public String run1() {
	char[] input = scrambleInputPt1.toCharArray();
	for (String line : rawData) {
	    if (line.contains("reverse")) {
		int i = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int j = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		reverse(input, i, j);
	    } else if (line.contains("swap position")) {
		int i = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int j = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		swap(input, i, j);
	    } else if (line.contains("swap letter")) {
		int i = indexOf(input, line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1));
		int j = indexOf(input, line.charAt(line.length() - 1));
		swap(input, i, j);
	    } else if (line.contains("rotate left")) {
		int i = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		input = rotateLeftRight(input, i, -1);
	    } else if (line.contains("rotate right")) {
		int i = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		input = rotateLeftRight(input, i, 1);
	    } else if (line.contains("rotate based")) {
		int i = indexOf(input, line.charAt(line.length() - 1));
		input = rotateBasedOnLetter(input, i, 1);
	    } else if (line.contains("move")) {
		int i = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int j = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		moveXToY(input, i, j);
	    }
	}

	return new String(input);
    }

    public String run2() {
	char[] input = scrambleInputPt2.toCharArray();

	for (int i = rawData.size() - 1; i >= 0; i--) {
	    String line = rawData.get(i);
	    if (line.contains("reverse")) {
		int x = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int y = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		reverse(input, x, y);
	    } else if (line.contains("swap position")) {
		int x = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int y = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		swap(input, x, y);
	    } else if (line.contains("swap letter")) {
		int x = indexOf(input, line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1));
		int y = indexOf(input, line.charAt(line.length() - 1));
		swap(input, x, y);
	    } else if (line.contains("rotate left")) {
		int x = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		input = rotateLeftRight(input, x, 1);
	    } else if (line.contains("rotate right")) {
		int x = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		input = rotateLeftRight(input, x, -1);
	    } else if (line.contains("rotate based")) {
		int x = indexOf(input, line.charAt(line.length() - 1));
		input = rotateBasedOnLetter(input, x, -1);
	    } else if (line.contains("move")) {
		int x = Integer.parseInt(Character.toString(line.charAt(line.indexOf(' ', line.indexOf(' ') + 1) + 1)));
		int y = Integer.parseInt(Character.toString(line.charAt(line.length() - 1)));
		moveXToY(input, y, x);
	    }
	}

	return new String(input);
    }

    public static void main(String[] args) {
	Day21 test = new Day21(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 21\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println();
	System.out.println(test.run2());
	System.out.println();

    }

    private void swap(char[] arr, int i, int j) {
	char tmp = arr[i];
	arr[i] = arr[j];
	arr[j] = tmp;
    }

    private void reverse(char[] arr, int l, int h) {
	int index = 0;
	for (int i = l; i < ((h - l) / 2) + l + 1; i++) {
	    swap(arr, i, (h - l) + l - index++);
	}
    }

    // direction: -1 = left, 1 = right
    private char[] rotateLeftRight(char[] arr, int steps, int direction) {
	char[] tmp = new char[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    tmp[Math.floorMod(i + (direction * steps), arr.length)] = arr[i];
	}
	return tmp;
    }

    // direction: -1 = left, 1 = right
    private char[] rotateBasedOnLetter(char[] arr, int index, int direction) {
	int offset = direction == 1 ? (index >= 4 ? 1 : 0) : 0;
	index = direction == 1 ? index : moveBasedOnIndexTable.get(index);
	int steps = direction == 1 ? index + offset + 1 : index + offset;
	return rotateLeftRight(arr, steps, direction);
    }

    private void moveXToY(char[] arr, int x, int y) {
	if (x < y) {
	    int index = x;
	    while (index != y) {
		swap(arr, index, index + 1);
		index++;
	    }
	} else {
	    int index = x;
	    while (index != y) {
		swap(arr, index, index - 1);
		index--;
	    }
	}
    }

    private int indexOf(char[] arr, char t) {
	for (int i = 0; i < arr.length; i++) {
	    if (arr[i] == t) {
		return i;
	    }
	}
	return -1;
    }

}
