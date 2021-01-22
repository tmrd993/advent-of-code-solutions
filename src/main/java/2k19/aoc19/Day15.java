package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashMap;

import myutils19.IncomparablePair;
import myutils19.IntCodeComputer;
import myutils19.StaticUtils;
import myutils19.Point2d;

public class Day15 {

    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;

    private List<Long> initialProgram;

    public Day15(File input) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(input);
    }

    public int run1() {
	Queue<IncomparablePair<Point2d, List<Integer>>> positionQueue = new LinkedList<>();
	Point2d startingPoint = new Point2d(0, 0);
	List<Integer> startingMoves = new ArrayList<>();
	IncomparablePair<Point2d, List<Integer>> startingPair = new IncomparablePair<>(startingPoint, startingMoves);
	Set<Point2d> visited = new HashSet<>();
	visited.add(startingPoint);
	positionQueue.add(startingPair);

	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));

	while (!positionQueue.isEmpty()) {
	    IncomparablePair<Point2d, List<Integer>> posAndMoves = positionQueue.poll();
	    Point2d currentPos = posAndMoves.k();
	    List<Integer> movesFromOrigin = posAndMoves.v();

	    moveToCurrentPos(computer, movesFromOrigin);

	    Point2d north = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d south = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d west = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d east = new Point2d(currentPos.x() + 1, currentPos.y());

	    int northCode = move(UP, computer);
	    if (northCode == 1)
		move(DOWN, computer);
	    int southCode = move(DOWN, computer);
	    if (southCode == 1)
		move(UP, computer);
	    int westCode = move(LEFT, computer);
	    if (westCode == 1)
		move(RIGHT, computer);
	    int eastCode = move(RIGHT, computer);
	    if (eastCode == 1)
		move(LEFT, computer);

	    if (northCode == 2 || southCode == 2 || westCode == 2 || eastCode == 2) {
		return movesFromOrigin.size() + 1;
	    }

	    visited.add(currentPos);

	    if (!visited.contains(north) && northCode == 1) {
		List<Integer> movesNorth = new ArrayList<>(movesFromOrigin);
		movesNorth.add(UP);
		positionQueue.add(new IncomparablePair<>(north, movesNorth));
	    }
	    if (!visited.contains(south) && southCode == 1) {
		List<Integer> movesSouth = new ArrayList<>(movesFromOrigin);
		movesSouth.add(DOWN);
		positionQueue.add(new IncomparablePair<>(south, movesSouth));
	    }
	    if (!visited.contains(west) && westCode == 1) {
		List<Integer> movesWest = new ArrayList<>(movesFromOrigin);
		movesWest.add(LEFT);
		positionQueue.add(new IncomparablePair<>(west, movesWest));
	    }
	    if (!visited.contains(east) && eastCode == 1) {
		List<Integer> movesEast = new ArrayList<>(movesFromOrigin);
		movesEast.add(RIGHT);
		positionQueue.add(new IncomparablePair<>(east, movesEast));
	    }
	    moveToOrigin(computer, movesFromOrigin);

	}

	return -1;
    }

    public int run2() {
	int count = 0;
	Map<Point2d, AreaType> areaMapping = areaMapping();
	Point2d oxygenPos = areaMapping.entrySet().stream().filter(s -> s.getValue() == AreaType.OXYGEN).findAny().get()
		.getKey();
	Set<Point2d> oxygenPoints = new HashSet<>();
	oxygenPoints.add(oxygenPos);
	Set<Point2d> activeOxygenPoints = new HashSet<>(oxygenPoints);
	Set<Point2d> wallPoints = areaMapping.entrySet().stream().filter(s -> s.getValue() == AreaType.WALL)
		.map(s -> s.getKey()).collect(Collectors.toSet());
	Set<Point2d> openArea = areaMapping.entrySet().stream().filter(s -> s.getValue() != AreaType.WALL)
		.map(s -> s.getKey()).collect(Collectors.toSet());

	while (!activeOxygenPoints.isEmpty()) {
	    Set<Point2d> toRemoveSet = new HashSet<>();
	    Set<Point2d> toAddSet = new HashSet<>();
	    for (Point2d oxygenPoint : activeOxygenPoints) {
		toRemoveSet.add(oxygenPoint);

		Point2d north = new Point2d(oxygenPoint.x(), oxygenPoint.y() - 1);
		Point2d south = new Point2d(oxygenPoint.x(), oxygenPoint.y() + 1);
		Point2d west = new Point2d(oxygenPoint.x() - 1, oxygenPoint.y());
		Point2d east = new Point2d(oxygenPoint.x() + 1, oxygenPoint.y());

		if (openArea.contains(north) && !oxygenPoints.contains(north) && !wallPoints.contains(north)) {
		    oxygenPoints.add(north);
		    if (isSpreadableOxygen(north, oxygenPoints) && !activeOxygenPoints.contains(north)) {
			toAddSet.add(north);
		    }
		}

		if (openArea.contains(south) && !oxygenPoints.contains(south) && !wallPoints.contains(south)) {
		    oxygenPoints.add(south);
		    if (isSpreadableOxygen(south, oxygenPoints) && !activeOxygenPoints.contains(south)) {
			toAddSet.add(south);
		    }
		}

		if (openArea.contains(east) && !oxygenPoints.contains(east) && !wallPoints.contains(east)) {
		    oxygenPoints.add(east);
		    if (isSpreadableOxygen(east, oxygenPoints) && !activeOxygenPoints.contains(east)) {
			toAddSet.add(east);
		    }
		}

		if (openArea.contains(west) && !oxygenPoints.contains(west) && !wallPoints.contains(west)) {
		    oxygenPoints.add(west);
		    if (isSpreadableOxygen(west, oxygenPoints) && !activeOxygenPoints.contains(west)) {
			toAddSet.add(west);
		    }
		}
	    }
	    activeOxygenPoints.removeAll(toRemoveSet);
	    activeOxygenPoints.addAll(toAddSet);
	    count++;
	}

	return count - 1;
    }

    @SuppressWarnings("unused")
    private void printGrid(Map<Point2d, AreaType> areaMapping, Set<Point2d> oxygenPoints) {
	if(areaMapping.size() < 20)
	    return;
	int xMax = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().x()).max().getAsInt();
	int xMin = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().x()).min().getAsInt();
	int yMax = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().y()).max().getAsInt();
	int yMin = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().y()).min().getAsInt();

	for (int i = yMin; i <= yMax; i++) {
	    for (int j = xMin; j <= xMax; j++) {
		Point2d point = new Point2d(j, i);
		if (oxygenPoints.contains(point)) {
		    System.out.print("O");
		} else if (areaMapping.containsKey(point)) {
		    if (areaMapping.get(point) == AreaType.OPEN) {
			System.out.print(".");
		    } else if (areaMapping.get(point) == AreaType.WALL) {
			System.out.print("#");
		    }
		}
	    }
	    System.out.println();
	}
    }

    private boolean isSpreadableOxygen(Point2d oxygenPoint, Set<Point2d> oxygenPoints) {
	Point2d north = new Point2d(oxygenPoint.x(), oxygenPoint.y() - 1);
	Point2d south = new Point2d(oxygenPoint.x(), oxygenPoint.y() + 1);
	Point2d west = new Point2d(oxygenPoint.x() - 1, oxygenPoint.y());
	Point2d east = new Point2d(oxygenPoint.x() + 1, oxygenPoint.y());

	return !oxygenPoints.contains(north) || !oxygenPoints.contains(south) || !oxygenPoints.contains(east)
		|| !oxygenPoints.contains(west);
    }

    private Map<Point2d, AreaType> areaMapping() {
	Map<Point2d, AreaType> areaMapping = new HashMap<>();

	Queue<IncomparablePair<Point2d, List<Integer>>> positionQueue = new LinkedList<>();
	Point2d startingPoint = new Point2d(0, 0);
	List<Integer> startingMoves = new ArrayList<>();
	IncomparablePair<Point2d, List<Integer>> startingPair = new IncomparablePair<>(startingPoint, startingMoves);
	Set<Point2d> visited = new HashSet<>();
	visited.add(startingPoint);
	positionQueue.add(startingPair);

	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));

	while (!positionQueue.isEmpty()) {
	    IncomparablePair<Point2d, List<Integer>> posAndMoves = positionQueue.poll();
	    Point2d currentPos = posAndMoves.k();
	    List<Integer> movesFromOrigin = posAndMoves.v();

	    moveToCurrentPos(computer, movesFromOrigin);

	    Point2d north = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d south = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d west = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d east = new Point2d(currentPos.x() + 1, currentPos.y());

	    int northCode = move(UP, computer);
	    if (northCode != 2) {
		if (northCode == 1) {
		    move(DOWN, computer);
		    areaMapping.putIfAbsent(north, AreaType.OPEN);
		} else {
		    areaMapping.putIfAbsent(north, AreaType.WALL);
		}
	    }

	    int southCode = move(DOWN, computer);
	    if (southCode != 2) {
		if (southCode == 1) {
		    move(UP, computer);
		    areaMapping.putIfAbsent(south, AreaType.OPEN);
		} else {
		    areaMapping.putIfAbsent(south, AreaType.WALL);
		}
	    }

	    int westCode = move(LEFT, computer);
	    if (westCode != 2) {
		if (westCode == 1) {
		    move(RIGHT, computer);
		    areaMapping.putIfAbsent(west, AreaType.OPEN);
		} else {
		    areaMapping.putIfAbsent(west, AreaType.WALL);
		}
	    }
	    int eastCode = move(RIGHT, computer);
	    if (eastCode != 2) {
		if (eastCode == 1) {
		    move(LEFT, computer);
		    areaMapping.putIfAbsent(east, AreaType.OPEN);
		} else {
		    areaMapping.putIfAbsent(east, AreaType.WALL);
		}
	    }

	    if (northCode == 2 || southCode == 2 || westCode == 2 || eastCode == 2) {
		Point2d oxygenArea = getOxygenArea(northCode, southCode, westCode, eastCode, currentPos);
		areaMapping.putIfAbsent(oxygenArea, AreaType.OXYGEN);
	    }

	    visited.add(currentPos);

	    if (!visited.contains(north) && northCode != 0) {
		List<Integer> movesNorth = new ArrayList<>(movesFromOrigin);
		movesNorth.add(UP);
		positionQueue.add(new IncomparablePair<>(north, movesNorth));
	    }
	    if (!visited.contains(south) && southCode != 0) {
		List<Integer> movesSouth = new ArrayList<>(movesFromOrigin);
		movesSouth.add(DOWN);
		positionQueue.add(new IncomparablePair<>(south, movesSouth));
	    }
	    if (!visited.contains(west) && westCode != 0) {
		List<Integer> movesWest = new ArrayList<>(movesFromOrigin);
		movesWest.add(LEFT);
		positionQueue.add(new IncomparablePair<>(west, movesWest));
	    }
	    if (!visited.contains(east) && eastCode != 0) {
		List<Integer> movesEast = new ArrayList<>(movesFromOrigin);
		movesEast.add(RIGHT);
		positionQueue.add(new IncomparablePair<>(east, movesEast));
	    }
	    moveToOrigin(computer, movesFromOrigin);

	}
	
	int xMax = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().x()).max().getAsInt();
	int xMin = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().x()).min().getAsInt();
	int yMax = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().y()).max().getAsInt();
	int yMin = areaMapping.entrySet().stream().mapToInt(s -> s.getKey().y()).min().getAsInt();
	
	for(int i = yMin; i <= yMax; i++) {
	    for(int j = xMin; j <= xMax; j++) {
		areaMapping.putIfAbsent(new Point2d(j, i), AreaType.WALL);
	    }
	}
	return areaMapping;
    }

    private Point2d getOxygenArea(int northCode, int southCode, int westCode, int eastCode, Point2d currentPos) {
	if (northCode == 2)
	    return new Point2d(currentPos.x(), currentPos.y() - 1);
	if (southCode == 2)
	    return new Point2d(currentPos.x(), currentPos.y() + 1);
	if (westCode == 2)
	    return new Point2d(currentPos.x() - 1, currentPos.y());
	if (eastCode == 2)
	    return new Point2d(currentPos.x() + 1, currentPos.y());

	throw new IllegalArgumentException("No oxygen area detected");
    }

    private int backwardsMovement(int dir) {
	switch (dir) {
	case UP:
	case DOWN:
	    return dir == UP ? DOWN : UP;
	case LEFT:
	case RIGHT:
	    return dir == LEFT ? RIGHT : LEFT;
	default:
	    throw new IllegalArgumentException(dir + " not mapped to any direction");
	}
    }

    private int move(int dir, IntCodeComputer computer) {
	if (dir < 1 || dir > 4) {
	    throw new IllegalArgumentException(dir + "is not mapped to any direction");
	}

	computer.setInputValues(dir);
	computer.run();
	return computer.outputValues().poll().intValue();
    }

    private void moveToCurrentPos(IntCodeComputer computer, List<Integer> moves) {
	for (Integer dir : moves) {
	    move(dir, computer);
	}
    }

    private void moveToOrigin(IntCodeComputer computer, List<Integer> moves) {
	for (int i = moves.size() - 1; i >= 0; i--) {
	    move(backwardsMovement(moves.get(i)), computer);
	}
    }

    public static void main(String[] args) {
	Day15 test = new Day15(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 15\\InputFile.txt"));
	System.out.println(test.run2());
    }

    private enum AreaType {
	WALL, OPEN, OXYGEN
    };
}
