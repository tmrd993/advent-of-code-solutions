package aoc19;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import myutils19.StaticUtils;
import myutils19.Pair;
import myutils19.Point2d;

public class Day10 {

    private char[][] asteroidMap;
    private Set<Point2d> asteroidPositions;
    private final char asteroid = '#';
    private final int height;
    private final int width;

    public Day10(File input) {
	asteroidMap = getAsteroidMap(input);
	asteroidPositions = getAsteroidPositions();
	height = asteroidMap.length;
	width = asteroidMap[0].length;
    }

    public Pair<Point2d, Integer> run1() {
	int maxVisibleAsteroids = 0;
	Point2d bestPosition = null;
	for (Point2d asteroidPos : asteroidPositions) {
	    Set<Point2d> visiblePositions = new HashSet<>();
	    Set<Point2d> invisiblePositions = new HashSet<>();
	    // eliminate values on the same y-axis
	    processHorizontalAxis(asteroidPos, visiblePositions, invisiblePositions);
	    // eliminate values on the same x-axis
	    processVerticalAxis(asteroidPos, visiblePositions, invisiblePositions);

	    processRestOfMap(asteroidPos, visiblePositions, invisiblePositions);

	    if (visiblePositions.size() > maxVisibleAsteroids) {
		maxVisibleAsteroids = visiblePositions.size();
		bestPosition = asteroidPos;
	    }
	}
	// System.out.println("Best Position is: " + bestPosition + " with " +
	// maxVisibleAsteroids + " asteroids visible");
	return new Pair<Point2d, Integer>(bestPosition, maxVisibleAsteroids);
    }

    public int run2() {
	System.out.println("Calculating optimal cannon position...");
	// this takes a few seconds to calculate...
	Point2d canonPosition = run1().k();
	System.out.println("Done.");
	System.out.println("Optimal position: " + canonPosition);

	Set<Point2d> asteroidPositions = getAsteroidPositions();
	int dx = -canonPosition.x();
	int dy = -canonPosition.y();

	// shift the coordiante system to turn the cannon position into the origin
	asteroidPositions = shiftOriginOfCoordinateSystem(asteroidPositions, dx, dy);

	// map asteroid positions to angles, 0° = north, 90° = east, 180° = south....
	List<Pair<Point2d, Double>> pointsAndAngles = new ArrayList<>();
	Point2d origin = new Point2d(0, 0);
	for (Point2d point : asteroidPositions) {
	    if (!point.equals(origin)) {
		pointsAndAngles
			.add(new Pair<Point2d, Double>(point, StaticUtils.calcRotationAngleInDegrees(origin, point)));
	    }
	}

	// sort by position
	pointsAndAngles.sort(new Comparator<Pair<Point2d, Double>>() {
	    @Override
	    public int compare(Pair<Point2d, Double> o1, Pair<Point2d, Double> o2) {
		Point2d origin = new Point2d(0, 0);
		int distanceFromOriginP1 = o1.k().distanceL1(origin);
		int distanceFromOriginP2 = o2.k().distanceL1(origin);
		if (distanceFromOriginP1 > distanceFromOriginP2) {
		    return 1;
		}
		if (distanceFromOriginP1 < distanceFromOriginP2) {
		    return -1;
		}
		return 0;
	    }
	});
	// sort by angle
	pointsAndAngles.sort(Comparator.comparing(Pair::v));

	Pair<Point2d, Double> targetAsteroid = null;

	int index = 0;
	int count = 1;
	while (!pointsAndAngles.isEmpty()) {
	    Pair<Point2d, Double> asteroidAnglePair = pointsAndAngles.get(index);
	    if (onlyDuplicateValuesLeft(pointsAndAngles) || pointsAndAngles.size() == 1) {
		while (!pointsAndAngles.isEmpty()) {
		    System.out.println("Destroyed asteroid number" + (count++) + " ["
			    + (pointsAndAngles.get(0).k().x() + (-dx)) + ", " + (pointsAndAngles.get(0).k().y() + (-dy))
			    + "] , at " + pointsAndAngles.get(0).v() + " Degrees.");
		    if (count == 200 + 1) {
			targetAsteroid = pointsAndAngles.get(0);
		    }
		    pointsAndAngles.remove(0);
		}
	    } else {
		System.out.println(
			"Destroyed asteroid number " + (count++) + " [" + (pointsAndAngles.get(index).k().x() + (-dx))
				+ ", " + (pointsAndAngles.get(index).k().y() + (-dy)) + "] , at "
				+ pointsAndAngles.get(index).v() + " Degrees.");
		if (count == 200 + 1) {
		    targetAsteroid = pointsAndAngles.get(index);
		}
		pointsAndAngles.remove(index);
		index = index % pointsAndAngles.size();
		while (asteroidAnglePair.v().equals(pointsAndAngles.get(index).v())) {
		    index = (index + 1) % pointsAndAngles.size();
		    if (onlyDuplicateValuesLeft(pointsAndAngles))
			break;
		}
	    }
	}
	int result = ((targetAsteroid.k().x() + (-dx)) * 100) + (targetAsteroid.k().y() + (-dy));
	return targetAsteroid != null ? result : -1;
    }

    private boolean onlyDuplicateValuesLeft(List<Pair<Point2d, Double>> pointsWithAngles) {
	for (int i = 0; i < pointsWithAngles.size() - 1; i++) {
	    if (!pointsWithAngles.get(i).v().equals(pointsWithAngles.get(i + 1).v()))
		return false;
	}
	return true;
    }

    private Set<Point2d> shiftOriginOfCoordinateSystem(Set<Point2d> points, int dx, int dy) {
	Set<Point2d> shiftedPoints = new HashSet<>();
	for (Point2d point : points) {
	    shiftedPoints.add(new Point2d(point.x() + dx, point.y() + dy));
	}
	return shiftedPoints;
    }

    private void processRestOfMap(Point2d point, Set<Point2d> visiblePoints, Set<Point2d> invisiblePoints) {
	for (Point2d cmpPoint : asteroidPositions) {
	    if (point != cmpPoint) {
		double alphaAngle = Math.atan2(point.y() - cmpPoint.y(), point.x() - cmpPoint.x());
		Set<Point2d> pointsOnSameLine = new HashSet<>();
		pointsOnSameLine.add(cmpPoint);
		for (Point2d cmpPoint2 : asteroidPositions) {
		    double angle = Math.atan2(point.y() - cmpPoint2.y(), point.x() - cmpPoint2.x());
		    if (alphaAngle == angle) {
			pointsOnSameLine.add(cmpPoint2);
		    }
		}
		Point2d closestPoint = null;
		int distanceFromPoint = height * width;
		for (Point2d pointOnSameLine : pointsOnSameLine) {
		    int distanceL1 = point.distanceL1(pointOnSameLine);
		    if (distanceL1 < distanceFromPoint && point != pointOnSameLine) {
			closestPoint = pointOnSameLine;
			distanceFromPoint = distanceL1;
		    }
		}
		visiblePoints.add(closestPoint);
		pointsOnSameLine.remove(closestPoint);
		invisiblePoints.addAll(pointsOnSameLine);
	    }
	}
    }

    private void processVerticalAxis(Point2d point, Set<Point2d> visiblePositions, Set<Point2d> invisiblePositions) {
	// move up
	boolean foundUpper = false;
	for (int i = point.y() - 1; i >= 0; i--) {
	    char currentSpace = asteroidMap[i][point.x()];
	    if (!foundUpper && currentSpace == asteroid) {
		foundUpper = true;
		visiblePositions.add(new Point2d(point.x(), i));
	    } else if (currentSpace == asteroid) {
		invisiblePositions.add(new Point2d(point.x(), i));
	    }
	}
	// move down
	boolean foundLower = false;
	for (int i = point.y() + 1; i < height; i++) {
	    char currentSpace = asteroidMap[i][point.x()];
	    if (!foundLower && currentSpace == asteroid) {
		foundLower = true;
		visiblePositions.add(new Point2d(point.x(), i));
	    } else if (currentSpace == asteroid) {
		invisiblePositions.add(new Point2d(point.x(), i));
	    }
	}
    }

    private void processHorizontalAxis(Point2d point, Set<Point2d> visiblePositions, Set<Point2d> invisiblePositions) {
	// move left
	boolean foundLeft = false;
	for (int i = point.x() - 1; i >= 0; i--) {
	    char currentSpace = asteroidMap[point.y()][i];
	    if (!foundLeft && currentSpace == asteroid) {
		foundLeft = true;
		visiblePositions.add(new Point2d(i, point.y()));
	    } else if (currentSpace == asteroid) {
		invisiblePositions.add(new Point2d(i, point.y()));
	    }
	}
	// move right
	boolean foundRight = false;
	for (int i = point.x() + 1; i < width; i++) {
	    char currentSpace = asteroidMap[point.y()][i];
	    if (!foundRight && currentSpace == asteroid) {
		foundRight = true;
		visiblePositions.add(new Point2d(i, point.y()));
	    } else if (currentSpace == asteroid) {
		invisiblePositions.add(new Point2d(i, point.y()));
	    }
	}
    }

    private Set<Point2d> getAsteroidPositions() {
	Set<Point2d> asteroidPositions = new HashSet<>();
	for (int i = 0; i < asteroidMap.length; i++) {
	    for (int j = 0; j < asteroidMap[i].length; j++) {
		char currentSpace = asteroidMap[i][j];
		if (currentSpace == asteroid) {
		    asteroidPositions.add(new Point2d(j, i));
		}
	    }
	}
	return asteroidPositions;
    }

    private char[][] getAsteroidMap(File input) {
	List<String> asteroidList = StaticUtils.fileToStringList(input);
	int height = asteroidList.size();
	int width = asteroidList.get(0).length();

	char[][] asteroidMap = new char[height][width];
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		asteroidMap[i][j] = asteroidList.get(i).charAt(j);
	    }
	}
	return asteroidMap;
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	for (int i = 0; i < asteroidMap.length; i++) {
	    for (int j = 0; j < asteroidMap[i].length; j++) {
		System.out.print(asteroidMap[i][j]);
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) {
	Day10 test = new Day10(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 10\\InputFile.txt"));
	System.out.println(test.run2());
    }
}
