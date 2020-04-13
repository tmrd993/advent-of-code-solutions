package aoc18;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day6 {

    public static List<Point2D.Double> getCoordinatesFromFile(File file) throws IOException {
	List<Point2D.Double> coordinates = new ArrayList<Point2D.Double>();
	Scanner sc = new Scanner(file);
	while (sc.hasNext()) {
	    String coordinate = sc.nextLine();
	    double x = java.lang.Double.parseDouble(coordinate.substring(0, coordinate.indexOf(",")));
	    double y = java.lang.Double
		    .parseDouble(coordinate.substring(coordinate.indexOf(",") + 1, coordinate.length()));
	    coordinates.add(new Point2D.Double(x, y));
	}
	sc.close();
	return coordinates;
    }

    // part 1 solution
    public static int calculateSafeArea(List<Point2D.Double> coordinates) {
	// set that contains coordinates that are unsafe (infinitely large
	// areas)
	Set<Point2D.Double> dangerZones = new HashSet<Point2D.Double>();

	// find the upper and lower bound of the coordinates
	int maxX = Integer.MIN_VALUE;
	int maxY = Integer.MIN_VALUE;
	for (int i = 0; i < coordinates.size(); i++) {
	    if (coordinates.get(i).x > maxX)
		maxX = (int) coordinates.get(i).x;
	    if (coordinates.get(i).y > maxY)
		maxY = (int) coordinates.get(i).y;
	}

	// calculate the areas of every coordinate and put unsafe coordinates
	// into danger set
	Map<Point2D.Double, Integer> areasOfMainCoordinates = new HashMap<Point2D.Double, Integer>();
	for (int i = 0; i <= maxY + 1; i++) {
	    for (int j = 0; j <= maxX + 1; j++) {
		Map<Point2D.Double, Integer> manhattanValuesForMainCoordinates = new HashMap<Point2D.Double, Integer>();
		for (int k = 0; k < coordinates.size(); k++) {
		    Point2D.Double currentPoint = coordinates.get(k);
		    int currentManhattan = getManhattanDistance(currentPoint, new Point2D.Double(j, i));
		    manhattanValuesForMainCoordinates.put(currentPoint, currentManhattan);
		}

		// look for the lowest value
		int minValue = Integer.MAX_VALUE;
		Point2D.Double minPoint = null;
		for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinates.entrySet()) {
		    if (e.getValue() < minValue) {
			minPoint = e.getKey();
			minValue = e.getValue();
		    }
		}

		// look for a second minimum value
		for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinates.entrySet()) {
		    if (!e.getKey().equals(minPoint) && e.getValue() == minValue)
			minPoint = null;
		}

		if (minPoint != null && (i == 0 || i == maxY || j == 0 || j == maxX)) {
		    dangerZones.add(minPoint);
		}

		else if (minPoint != null) {
		    if (areasOfMainCoordinates.containsKey(minPoint)) {
			areasOfMainCoordinates.put(minPoint, areasOfMainCoordinates.get(minPoint) + 1);
		    } else {
			areasOfMainCoordinates.put(minPoint, 1);
		    }
		}
	    }
	}

	// find the largest safe area
	int maxSafeArea = Integer.MIN_VALUE;
	for (Map.Entry<Point2D.Double, Integer> e : areasOfMainCoordinates.entrySet()) {
	    // if coordinate is inside dangerZone, it's infinitely large
	    if (!dangerZones.contains(e.getKey())) {
		if (e.getValue() > maxSafeArea) {
		    maxSafeArea = e.getValue();
		}
	    }
	}
	return maxSafeArea;
    }

    // part 2 solution
    public static int getLargestSafeArea(List<Point2D.Double> coordinates) {
	// find the upper and lower bound of the coordinates
	int maxX = Integer.MIN_VALUE;
	int maxY = Integer.MIN_VALUE;
	for (int i = 0; i < coordinates.size(); i++) {
	    if (coordinates.get(i).x > maxX)
		maxX = (int) coordinates.get(i).x;
	    if (coordinates.get(i).y > maxY)
		maxY = (int) coordinates.get(i).y;
	}

	Set<Point2D.Double> safeArea = new HashSet<Point2D.Double>();

	for (int i = 0; i <= maxY + 1; i++) {
	    for (int j = 0; j <= maxX + 1; j++) {
		Point2D.Double possibleSafeCoordinate = new Point2D.Double(j, i);
		int sum = 0;
		for (Point2D.Double p : coordinates) {
		    sum += getManhattanDistance(p, possibleSafeCoordinate);
		}
		// set to sum < 32 for solution of test case
		if (sum < 10000)
		    safeArea.add(possibleSafeCoordinate);
	    }
	}
	return safeArea.size();
    }

    // alternative way of getting danger zones, too complicated and inefficient
    public static Set<Point2D.Double> getDangerZones(List<Point2D.Double> coordinates) {
	int maxX = Integer.MIN_VALUE;
	int maxY = Integer.MIN_VALUE;
	for (int i = 0; i < coordinates.size(); i++) {
	    if (coordinates.get(i).x > maxX)
		maxX = (int) coordinates.get(i).x;
	    if (coordinates.get(i).y > maxY)
		maxY = (int) coordinates.get(i).y;
	}

	// stores the coordinates that stretch infinitely so we can ignore them
	// later on
	Set<Point2D.Double> dangerZones = new HashSet<Point2D.Double>();

	// compute danger coordinates for lower bound, upper bound
	for (int i = -1; i <= maxX + 2; i++) {
	    // System.out.println("x:" + i + " y:" + -1 + " " + "x: " + i + "
	    // y:" + (maxY + 2));

	    Map<Point2D.Double, Integer> manhattanValuesForMainCoordinatesLower = new HashMap<Point2D.Double, Integer>();
	    Map<Point2D.Double, Integer> manhattanValuesForMainCoordinatesUpper = new HashMap<Point2D.Double, Integer>();
	    for (int j = 0; j < coordinates.size(); j++) {
		Point2D.Double currentPoint = coordinates.get(j);
		int currentManhattanLower = getManhattanDistance(currentPoint, new Point2D.Double(i, -1));
		int currentManhattanUpper = getManhattanDistance(currentPoint, new Point2D.Double(i, maxY + 2));
		manhattanValuesForMainCoordinatesLower.put(currentPoint, currentManhattanLower);
		manhattanValuesForMainCoordinatesUpper.put(currentPoint, currentManhattanUpper);
	    }
	    int minValue = Integer.MAX_VALUE;
	    Point2D.Double minPoint = null;
	    for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinatesLower.entrySet()) {
		if (e.getValue() == minValue) {
		    minPoint = null;
		    break;
		} else if (e.getValue() < minValue) {
		    minValue = e.getValue();
		    minPoint = e.getKey();
		}
	    }
	    if (minPoint != null)
		dangerZones.add(minPoint);

	    minValue = Integer.MAX_VALUE;
	    minPoint = null;
	    for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinatesUpper.entrySet()) {
		if (e.getValue() == minValue) {
		    minPoint = null;
		    break;
		} else if (e.getValue() < minValue) {
		    minValue = e.getValue();
		    minPoint = e.getKey();
		}
	    }
	    if (minPoint != null)
		dangerZones.add(minPoint);
	}

	// compute left and right bound danger zones
	// left bound, right bound
	for (int i = -1; i <= maxY + 2; i++) {
	    // System.out.println("x: " + -1 + " y: " + i + " " + "x: " + (maxX
	    // + 2) + " y: " + i);
	    Map<Point2D.Double, Integer> manhattanValuesForMainCoordinatesLower = new HashMap<Point2D.Double, Integer>();
	    Map<Point2D.Double, Integer> manhattanValuesForMainCoordinatesUpper = new HashMap<Point2D.Double, Integer>();
	    for (int j = 0; j < coordinates.size(); j++) {
		Point2D.Double currentPoint = coordinates.get(j);
		int currentManhattanLower = getManhattanDistance(currentPoint, new Point2D.Double(-1, i));
		int currentManhattanUpper = getManhattanDistance(currentPoint, new Point2D.Double(maxX + 2, i));
		manhattanValuesForMainCoordinatesLower.put(currentPoint, currentManhattanLower);
		manhattanValuesForMainCoordinatesUpper.put(currentPoint, currentManhattanUpper);
	    }
	    int minValue = Integer.MAX_VALUE;
	    Point2D.Double minPoint = null;
	    for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinatesLower.entrySet()) {
		if (e.getValue() == minValue) {
		    minPoint = null;
		    break;
		} else if (e.getValue() < minValue) {
		    minValue = e.getValue();
		    minPoint = e.getKey();
		}
	    }
	    if (minPoint != null)
		dangerZones.add(minPoint);

	    minValue = Integer.MAX_VALUE;
	    minPoint = null;
	    for (Map.Entry<Point2D.Double, Integer> e : manhattanValuesForMainCoordinatesUpper.entrySet()) {
		if (e.getValue() == minValue) {
		    minPoint = null;
		    break;
		} else if (e.getValue() < minValue) {
		    minValue = e.getValue();
		    minPoint = e.getKey();
		}
	    }
	    if (minPoint != null)
		dangerZones.add(minPoint);
	}

	return dangerZones;
    }

    public static int getManhattanDistance(int x1, int y1, int x2, int y2) {
	return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static int getManhattanDistance(Point2D.Double p1, Point2D.Double p2) {
	return Math.abs((int) p2.x - (int) p1.x) + Math.abs((int) p2.y - (int) p1.y);
    }

    public static void main(String[] args) throws IOException {
	List<Point2D.Double> coordinates = getCoordinatesFromFile(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 6\\InputFile.txt"));
	System.out.println(calculateSafeArea(coordinates));
	System.out.println(getLargestSafeArea(coordinates));
    }
}