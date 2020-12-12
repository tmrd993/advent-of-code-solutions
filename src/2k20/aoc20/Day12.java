package aoc20;

import java.io.File;
import java.util.List;

import myutils20.Point2d;
import myutils20.Ship;
import myutils20.StaticUtils;

public class Day12 {

    private List<String> rawData;

    public Day12(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	Ship ship = new Ship();
	for (String com : rawData) {
	    char dirCom = com.charAt(0);
	    int units = Integer.parseInt(com.substring(1));

	    if (dirCom == 'L' || dirCom == 'R') {
		ship.turn(dirCom, units);
	    } else if (dirCom == 'F') {
		ship.moveForward(ship.dir(), units);
	    } else {
		ship.move(dirCom, units);
	    }
	}

	return Math.abs(ship.pos().x()) + Math.abs(ship.pos().y());
    }

    public int run2() {
	Ship ship = new Ship();
	Point2d waypointPos = new Point2d(10, 1);
	for (String com : rawData) {
	    char comDir = com.charAt(0);
	    int units = Integer.parseInt(com.substring(1));

	    if (comDir == 'L' || comDir == 'R') {
		units = comDir == 'R' ? (-1 * units) : units;
		double angle = Math.toRadians(units);
		int waypointX = (int) (waypointPos.x() * Math.cos(angle)) - (int) (waypointPos.y() * Math.sin(angle));
		int waypointY = (int) (waypointPos.x() * Math.sin(angle)) + (int) (waypointPos.y() * Math.cos(angle));
		waypointPos = new Point2d(waypointX, waypointY);
	    } else if (comDir == 'F') {
		ship.moveTowards(waypointPos, units);
	    } else {
		waypointPos = moveWaypoint(comDir, units, waypointPos);
	    }
	}

	return Math.abs(ship.pos().x()) + Math.abs(ship.pos().y());
    }
    
    private Point2d moveWaypoint(char dir, int units, Point2d waypoint) {
	switch(dir) {
	case 'N':
	    return new Point2d(waypoint.x(), waypoint.y() + units);
	case 'E':
	    return new Point2d(waypoint.x() + units, waypoint.y());
	case 'S':
	    return new Point2d(waypoint.x(), waypoint.y() - units);
	case 'W':
	    return new Point2d(waypoint.x() - units, waypoint.y());
	}
	throw new IllegalArgumentException("No direction mapped to, " + dir);
    }

    public static void main(String[] args) {
	Day12 test = new Day12(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 12\\InputFile1.txt"));
	System.out.println(test.run2());

    }

}
