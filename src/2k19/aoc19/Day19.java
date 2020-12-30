package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.IncomparablePair;
import myutils19.IntCodeComputer;
import myutils19.Point2d;
import myutils19.StaticUtils;

public class Day19 {

    private List<Long> initialProgram;
    int limitsPt1 = 50;
    int limitsPt2 = 100;

    public Day19(File inputFile) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(inputFile);
    }

    public int run1() {
	int count = 0;
	for (int i = 0; i < limitsPt1; i++) {
	    for (int j = 0; j < limitsPt1; j++) {
		IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
		computer.setInputValues(j, i);
		computer.run();

		if (computer.outputValues().poll() == 1) {
		    count++;
		}
	    }
	}

	return count;
    }

    public int run2() {

	List<IncomparablePair<Point2d, Point2d>> grid = grid();

	int gridLen = grid.size();
	for (int i = 0; i < grid.size(); i++) {
	    IncomparablePair<Point2d, Point2d> row = grid.get(i);
	    Point2d leftPointer = row.k();
	    Point2d rightPointer = row.v();
	    if (i < gridLen - (limitsPt2 - 1)) {
		for (int j = leftPointer.x(); j <= rightPointer.x() - (limitsPt2 - 1); j++) {
		    IncomparablePair<Point2d, Point2d> rowDown = grid.get(i + (limitsPt2 - 1));
		    // if the current x-coordinate is a valid point 100 rows down, we are done
		    if (j >= rowDown.k().x() && j <= rowDown.v().x()) {
			return (j * 10000) + leftPointer.y();
		    }
		}
	    }
	}

	return -1;
    }

    // returns a list of pairs consisting of the leftmost point and the rightmost point of an active row
    private List<IncomparablePair<Point2d, Point2d>> grid() {
	List<IncomparablePair<Point2d, Point2d>> grid = new ArrayList<>();

	int row = 10;
	int col = 0;

	Point2d leftPointer = leftPointer(row, col);
	Point2d rightPointer = rightPointer(leftPointer.y(), leftPointer.x());

	// 3000 rows should be enough to find a 100x100 rectangle inside
	for (int i = row + 1; i < 3000; i++) {

	    // row == i, go down exactly one row and get the new pointers, this should
	    // create exactly 4 computers in the worst case, per row

	    leftPointer = leftPointer(i, leftPointer.x());
	    rightPointer = rightPointer(i, rightPointer.x());

	    grid.add(new IncomparablePair<>(leftPointer, rightPointer));

	}
	return grid;
    }

    // move the pointer to the left until its in range
    private Point2d leftPointer(int row, int col) {
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.setInputValues(col, row);
	computer.run();

	// get left pointer onto the first hit from the left
	while (computer.outputValues().poll() != 1) {
	    col++;
	    computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	    computer.setInputValues(col, row);
	    computer.run();
	}

	return new Point2d(col, row);
    }

    // move the pointer to the right until its out of range
    private Point2d rightPointer(int row, int col) {
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.setInputValues(col, row);
	computer.run();

	// get right pointer onto last hit
	while (computer.outputValues().poll() == 1) {
	    col++;
	    computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	    computer.setInputValues(col, row);
	    computer.run();
	}

	return new Point2d(col - 1, row);
    }

    public static void main(String[] args) {
	Day19 test = new Day19(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 19\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
