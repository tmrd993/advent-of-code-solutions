package aoc15;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import myutils15.Point2d;
import myutils15.StaticUtils;

public class Day18 {

    private List<String> rawData;
    private final int LIMIT = 100;
    private final char ON = '#';
    private final char OFF = '.';

    public Day18(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	Map<Point2d, Character> grid = initialGrid();

	for (int i = 0; i < LIMIT; i++) {

	    Map<Point2d, Character> tmpGrid = new HashMap<>();

	    for (Entry<Point2d, Character> entry : grid.entrySet()) {
		char currentChar = entry.getValue();
		Point2d currentPos = entry.getKey();
		char nextLightState = getNextLightState(currentPos, currentChar, grid);
		tmpGrid.put(currentPos, nextLightState);
	    }

	    grid = tmpGrid;
	}

	return onLightCount(grid);
    }

    public int run2() {
	Map<Point2d, Character> grid = initialGrid();

	for (int i = 0; i < LIMIT; i++) {
	    Map<Point2d, Character> tmpGrid = new HashMap<>();
	    for (Entry<Point2d, Character> entry : grid.entrySet()) {
		char currentChar = entry.getValue();
		Point2d currentPos = entry.getKey();
		char nextLightState = getNextLightState(currentPos, currentChar, grid);
		tmpGrid.put(currentPos, nextLightState);
	    }

	    grid = tmpGrid;
	    grid.put(new Point2d(0, 0), ON);
	    grid.put(new Point2d(0, 99), ON);
	    grid.put(new Point2d(99, 99), ON);
	    grid.put(new Point2d(99, 0), ON);
	}

	return onLightCount(grid);
    }

    private int onLightCount(Map<Point2d, Character> grid) {
	int count = 0;
	for (Entry<Point2d, Character> entry : grid.entrySet()) {
	    if (entry.getValue() == ON) {
		count++;
	    }
	}
	return count;
    }

    private char getNextLightState(Point2d pos, char currentChar, Map<Point2d, Character> grid) {
	Point2d n = new Point2d(pos.x(), pos.y() - 1);
	Point2d s = new Point2d(pos.x(), pos.y() + 1);
	Point2d w = new Point2d(pos.x() - 1, pos.y());
	Point2d e = new Point2d(pos.x() + 1, pos.y());
	Point2d nw = new Point2d(pos.x() - 1, pos.y() - 1);
	Point2d ne = new Point2d(pos.x() + 1, pos.y() - 1);
	Point2d sw = new Point2d(pos.x() - 1, pos.y() + 1);
	Point2d se = new Point2d(pos.x() + 1, pos.y() + 1);
	List<Point2d> neighbors = List.of(n, s, w, e, nw, ne, sw, se);

	int numOfOnNeighbors = (int) neighbors.stream().map(p -> grid.getOrDefault(p, OFF)).filter(c -> c == ON)
		.count();
	if (currentChar == ON && (numOfOnNeighbors != 2 && numOfOnNeighbors != 3)) {
	    currentChar = OFF;
	} else if (currentChar == OFF && numOfOnNeighbors == 3) {
	    currentChar = ON;
	}

	return currentChar;
    }

    public Map<Point2d, Character> initialGrid() {
	Map<Point2d, Character> grid = new HashMap<>();
	for (int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    for (int j = 0; j < line.length(); j++) {
		grid.put(new Point2d(j, i), line.charAt(j));
	    }
	}

	return grid;
    }

    public static void main(String[] args) {
	Day18 test = new Day18(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 18\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
