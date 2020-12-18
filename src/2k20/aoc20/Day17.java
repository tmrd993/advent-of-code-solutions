package aoc20;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myutils20.StaticUtils;
import myutils20.Point3d;
import myutils20.Point4d;

public class Day17 {

    private List<String> rawData;
    private final char active = '#';
    private final int cycleCount = 6;

    public Day17(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	Map<Point3d, Set<Point3d>> neighborMap = new HashMap<>();
	Set<Point3d> activePoints = initialActivePoints();

	// go through 6 cycles
	for (int i = 0; i < cycleCount; i++) {
	    // create new list for next cycle (all change simultaneously)
	    Set<Point3d> nextCycleActives = new HashSet<>();
	    // go through every active point in current cycle
	    for (Point3d activePoint : activePoints) {
		if (!neighborMap.containsKey(activePoint)) {
		    neighborMap.put(activePoint, neighbors(activePoint));
		}

		Set<Point3d> neighbors = neighborMap.get(activePoint);

		// check neighbors of inactive neighbors
		for (Point3d neighbor : neighbors) {
		    if (!activePoints.contains(neighbor)) {
			checkNeighbors(activePoints, nextCycleActives, neighbor, neighborMap, false);
		    }
		}
		// check neighbors of current point
		checkNeighbors(activePoints, nextCycleActives, activePoint, neighborMap, true);
	    }

	    activePoints = nextCycleActives;
	}

	return activePoints.size();
    }

    public int run2() {
	Map<Point4d, Set<Point4d>> neighborMap = new HashMap<>();
	Set<Point4d> activePoints = initialActivePointsP2();

	// go through 6 cycles
	for (int i = 0; i < cycleCount; i++) {
	    // create new list for next cycle (all change simultaneously)
	    Set<Point4d> nextCycleActives = new HashSet<>();
	    // go through every active point in current cycle
	    for (Point4d activePoint : activePoints) {
		if (!neighborMap.containsKey(activePoint)) {
		    neighborMap.put(activePoint, neighbors(activePoint));
		}

		Set<Point4d> neighbors = neighborMap.get(activePoint);

		// check neighbors of inactive neighbors
		for (Point4d neighbor : neighbors) {
		    if (!activePoints.contains(neighbor)) {
			checkNeighbors(activePoints, nextCycleActives, neighbor, neighborMap, false);
		    }
		}
		// check neighbors of current point
		checkNeighbors(activePoints, nextCycleActives, activePoint, neighborMap, true);
	    }

	    activePoints = nextCycleActives;
	}

	return activePoints.size();

    }

    private void checkNeighbors(Set<Point3d> activePoints, Set<Point3d> nextActivePoints, Point3d p,
	    Map<Point3d, Set<Point3d>> neighborMap, boolean isActive) {
	if (!neighborMap.containsKey(p)) {
	    neighborMap.put(p, neighbors(p));
	}

	Set<Point3d> neighbors = neighborMap.get(p);

	int activeCount = (int) neighbors.stream().filter(n -> activePoints.contains(n)).count();
	if (isActive && (activeCount == 2 || activeCount == 3)) {
	    nextActivePoints.add(p);
	} else if (!isActive && activeCount == 3) {
	    nextActivePoints.add(p);
	}
    }

    private void checkNeighbors(Set<Point4d> activePoints, Set<Point4d> nextActivePoints, Point4d p,
	    Map<Point4d, Set<Point4d>> neighborMap, boolean isActive) {
	if (!neighborMap.containsKey(p)) {
	    neighborMap.put(p, neighbors(p));
	}

	Set<Point4d> neighbors = neighborMap.get(p);

	int activeCount = (int) neighbors.stream().filter(n -> activePoints.contains(n)).count();
	if (isActive && (activeCount == 2 || activeCount == 3)) {
	    nextActivePoints.add(p);
	} else if (!isActive && activeCount == 3) {
	    nextActivePoints.add(p);
	}
    }

    private Set<Point4d> initialActivePointsP2() {
	Set<Point4d> points = new HashSet<>();

	for (int i = 0; i < rawData.size(); i++) {
	    String row = rawData.get(i);
	    for (int j = 0; j < row.length(); j++) {
		if (row.charAt(j) == active) {
		    points.add(new Point4d(j, i, 0, 0));
		}
	    }
	}

	return points;
    }

    private Set<Point3d> initialActivePoints() {
	Set<Point3d> points = new HashSet<>();

	for (int i = 0; i < rawData.size(); i++) {
	    String row = rawData.get(i);
	    for (int j = 0; j < row.length(); j++) {
		if (row.charAt(j) == active) {
		    points.add(new Point3d(j, i, 0));
		}
	    }
	}

	return points;
    }

    private Set<Point3d> neighbors(Point3d p) {
	Set<Point3d> neighbors = new HashSet<>();
	for (int i = p.x() - 1; i <= p.x() + 1; i++) {
	    for (int j = p.y() - 1; j <= p.y() + 1; j++) {
		for (int k = p.z() - 1; k <= p.z() + 1; k++) {
		    Point3d pn = new Point3d(i, j, k);
		    if (!pn.equals(p)) {
			neighbors.add(new Point3d(i, j, k));
		    }
		}
	    }
	}

	return neighbors;
    }

    private Set<Point4d> neighbors(Point4d p) {
	Set<Point4d> neighbors = new HashSet<>();
	for (int i = p.x() - 1; i <= p.x() + 1; i++) {
	    for (int j = p.y() - 1; j <= p.y() + 1; j++) {
		for (int k = p.z() - 1; k <= p.z() + 1; k++) {
		    for (int l = p.w() - 1; l <= p.w() + 1; l++) {
			Point4d pn = new Point4d(i, j, k, l);
			if (!pn.equals(p)) {
			    neighbors.add(pn);
			}
		    }
		}
	    }
	}
	return neighbors;
    }

    public static void main(String[] args) {
	Day17 test = new Day17(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 17\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
