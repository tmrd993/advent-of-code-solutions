package aoc16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import myutils16.Point2d;

public class Day1 {

    private List<String> directionComs;

    public Day1(File input) {
	directionComs = directionCommands(input);
    }

    private List<String> directionCommands(File input) {
	List<String> coms = new ArrayList<>();
	try (Scanner sc = new Scanner(input)) {
	    sc.useDelimiter(", ");
	    while (sc.hasNext()) {
		coms.add(sc.next());
	    }
	    sc.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return coms;
    }

    public int runPart1() {
	// unit vectors for directions (up, down, left, right)
	Point2d[] unitVectors = { new Point2d(1, 0), new Point2d(0, -1), new Point2d(-1, 0), new Point2d(0, 1) };
	// facing north
	int direction = 3;
	Point2d currentPos = new Point2d(0, 0);
	for (String com : directionComs) {
	    char dir = com.charAt(0);
	    int stepCount = Integer.parseInt(com.substring(1));
	    if (dir == 'L')
		direction = Math.floorMod(direction - 1, unitVectors.length);
	    else
		direction = Math.floorMod(direction + 1, unitVectors.length);

	    for (int i = 0; i < stepCount; i++) {
		currentPos = currentPos.add(unitVectors[direction]);
	    }
	}
	return Math.abs(currentPos.x()) + Math.abs(currentPos.y());
    }

    public int runPart2() {
	// unit vectors for directions (up, down, left, right)
	Point2d[] unitVectors = { new Point2d(1, 0), new Point2d(0, -1), new Point2d(-1, 0), new Point2d(0, 1) };
	// facing north
	int direction = 3;
	Set<Point2d> visited = new HashSet<>();
	Point2d currentPos = new Point2d(0, 0);
	for (String com : directionComs) {
	    char dir = com.charAt(0);
	    int stepCount = Integer.parseInt(com.substring(1));
	    if (dir == 'L')
		direction = Math.floorMod(direction - 1, unitVectors.length);
	    else
		direction = Math.floorMod(direction + 1, unitVectors.length);

	    for (int i = 0; i < stepCount; i++) {
		currentPos = currentPos.add(unitVectors[direction]);
		if(visited.contains(currentPos)) {
		    return Math.abs(currentPos.x()) + Math.abs(currentPos.y());
		}
		visited.add(currentPos);
	    }
	}
	return -1;
    }

    public static void main(String[] args) {
	Day1 test = new Day1(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 1\\InputFile1.txt"));
	System.out.println(test.runPart2());
    }
}
