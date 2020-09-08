package aoc17;

import java.util.List;
import java.util.Scanner;

import myutils17.Point2d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Day11 {

    private List<String> path;
    private int furthestFromStartingPoint;

    public Day11(File input) {
	path = getPath(input);
	furthestFromStartingPoint = 0;
    }

    private Point2d getTargetPosition() {
	int x = 0;
	int y = 0;
	int distanceFromStart = 0;
	for (String direction : path) {
	    if (direction.equals("n")) {
		y--;
	    } else if (direction.equals("s")) {
		y++;
	    } else if (direction.equals("nw")) {
		x--;
	    } else if (direction.equals("ne")) {
		x++;
		y--;
	    } else if (direction.equals("sw")) {
		x--;
		y++;
	    } else if (direction.equals("se")) {
		x++;
	    }

	    distanceFromStart = shortestPathLength(new Point2d(x, y));
	    if (distanceFromStart > furthestFromStartingPoint) {
		furthestFromStartingPoint = distanceFromStart;
	    }
	}

	return new Point2d(x, y);
    }

    // part 2
    public int getLongestDistance() {
	if (furthestFromStartingPoint == 0)
	    shortestPathLength();
	return furthestFromStartingPoint;
    }

    private int shortestPathLength(Point2d target) {

	int dx = target.x();
	int dy = target.y();
	int dist = dy - dx;

	return Math.max(Math.max(dx, dy), dist);
    }

    // part 1
    public int shortestPathLength() {
	Point2d target = getTargetPosition();
	return shortestPathLength(target);
    }

    private List<String> getPath(File input) {
	List<String> path = new ArrayList<>();
	try {
	    Scanner sc = new Scanner(input);

	    String line;
	    while (sc.hasNextLine()) {
		line = sc.nextLine();
		String[] paths = line.split(",");
		for (String direction : paths) {
		    path.add(direction);
		}
	    }

	    sc.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return path;
    }

    // MOVING ON A HEX GRID USING 2D COORDINATES
    // N: y - 1
    // S: y + 1
    // NE: y - 1, x + 1
    // SE: x + 1
    // SW: y + 1, x - 1
    // NW: x - 1

    public static void main(String[] args) {
	Day11 test = new Day11(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 11\\InputFile1.txt"));
	System.out.println(test.shortestPathLength() + "\n" + test.getLongestDistance());
    }

}
