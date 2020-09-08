package aoc17;

import java.util.HashSet;
import java.util.Set;

import myutils2k17.Point2d;

public class Day14 {

    private final String INPUT;

    public Day14(String input) {
	this.INPUT = input;
    }

    // part 1
    public int usedSquares() {
	char[][] grid = diskGrid();
	int count = 0;
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid.length; j++) {
		if (grid[i][j] == '1')
		    count++;
	    }
	}
	return count;
    }


    // part 2
    public int regions() {
	char[][] grid = diskGrid();
	int count = 0;
	Set<Point2d> marked = new HashSet<>();
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid.length; j++) {
		Point2d currentPoint = new Point2d(j, i);
		if (!marked.contains(currentPoint) && grid[i][j] == '1') {
		    floodFill(currentPoint, marked, grid);
		    count++;
		}
	    }
	}

	return count;
    }

    private void floodFill(Point2d currentPoint, Set<Point2d> marked, char[][] grid) {
	if (marked.contains(currentPoint))
	    return;

	marked.add(currentPoint);

	Point2d up = new Point2d(currentPoint.x(), currentPoint.y() - 1);
	Point2d down = new Point2d(currentPoint.x(), currentPoint.y() + 1);
	Point2d left = new Point2d(currentPoint.x() - 1, currentPoint.y());
	Point2d right = new Point2d(currentPoint.x() + 1, currentPoint.y());

	if (up.y() >= 0 && !marked.contains(up) && grid[up.y()][up.x()] == '1')
	    floodFill(up, marked, grid);
	if (down.y() < grid.length && !marked.contains(down) && grid[down.y()][down.x()] == '1')
	    floodFill(down, marked, grid);
	if (left.x() >= 0 && !marked.contains(left) && grid[left.y()][left.x()] == '1')
	    floodFill(left, marked, grid);
	if (right.x() < grid.length && !marked.contains(right) && grid[right.y()][right.x()] == '1')
	    floodFill(right, marked, grid);
    }

    private char[][] diskGrid() {
	int maxRows = 128;
	char[][] grid = new char[maxRows][];

	for (int i = 0; i < maxRows; i++) {
	    int column = 0;

	    String toHash = INPUT + "-" + i;

	    Day10 knotHashCalculator = new Day10(toHash);
	    String hashedInputHex = knotHashCalculator.knotHash();

	    String hashedInputBin = knotHashToBinString(hashedInputHex);

	    grid[i] = new char[maxRows];
	    for (int j = 0; j < hashedInputBin.length(); j++) {
		grid[i][column++] = hashedInputBin.charAt(j);
	    }
	}

	return grid;
    }

    private String knotHashToBinString(String knotHash) {
	StringBuilder knotHashBinary = new StringBuilder();
	for (int i = 0; i < knotHash.length(); i++) {
	    int hexValue = Integer.parseInt(Character.toString(knotHash.charAt(i)), 16);

	    String binValue = Integer.toBinaryString(hexValue);
	    if (binValue.length() == 1)
		binValue = "000" + binValue;
	    else if (binValue.length() == 2)
		binValue = "00" + binValue;
	    else if (binValue.length() == 3)
		binValue = "0" + binValue;

	    knotHashBinary.append(binValue);
	}

	return knotHashBinary.toString();
    }

    public static void main(String[] args) {
	String input = "ffayrhll";
	// String exampleInput = "flqrgnkx";
	Day14 test = new Day14(input);
	System.out.println(test.regions());
    }
}
