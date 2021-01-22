package aoc20;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myutils20.Point2d;
import myutils20.StaticUtils;

public class Day3 {

    private Map<Point2d, Character> initialGrid;
    private final char tree = '#';

    public Day3(File inputFile) {
	initialGrid = new HashMap<>();
	setupGrid(inputFile);
    }

    public int run1() {
	return getTreeCount(3, 1);
    }

    public long run2() {
	return (long) getTreeCount(1, 1) * getTreeCount(3, 1) * getTreeCount(5, 1) * getTreeCount(7, 1) * getTreeCount(1, 2);
    }

    private int getTreeCount(int dx, int dy) {
	int treeCount = 0;
	int gridHeight = initialGrid.entrySet().stream().mapToInt(e -> e.getKey().y()).max().getAsInt() + 1;
	int gridWidth = initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1;

	Point2d pos = new Point2d(0, 0);

	while (pos.y() < gridHeight) {
	    if (initialGrid.get(pos) == tree) {
		treeCount++;
	    }

	    pos = new Point2d(((pos.x() + dx) % gridWidth), pos.y() + dy);
	}

	return treeCount;
    }

    private void setupGrid(File inputFile) {
	List<String> gridRows = StaticUtils.inputFileToStringList(inputFile);

	for (int i = 0; i < gridRows.size(); i++) {
	    for (int j = 0; j < gridRows.get(i).length(); j++) {
		initialGrid.put(new Point2d(j, i), gridRows.get(i).charAt(j));
	    }
	}
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	int gridHeight = initialGrid.entrySet().stream().mapToInt(e -> e.getKey().y()).max().getAsInt() + 1;
	int gridWidth = initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1;

	for (int i = 0; i < gridHeight; i++) {
	    for (int j = 0; j < gridWidth; j++) {
		System.out.print(initialGrid.get(new Point2d(j, i)));
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) {
	Day3 test = new Day3(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 3\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
