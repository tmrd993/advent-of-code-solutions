package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import myutils20.Point2d;
import myutils20.StaticUtils;

public class Day11 {

    private List<String> rawData;
    private final char empty = 'L';
    private final char occupied = '#';
    private final char floor = '.';

    public Day11(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	char[][] grid = initialGrid();
	char[][] nextStateGrid = new char[grid.length][grid[0].length];

	while (nextGridState(grid, nextStateGrid)) {
	    grid = nextStateGrid;
	    nextStateGrid = new char[grid.length][grid[0].length];
	}

	return (int) Stream.of(nextStateGrid).flatMapToInt(c -> new String(c).chars()).filter(c -> (char) c == occupied)
		.count();
    }

    public int run2() {
	char[][] grid = initialGrid();
	char[][] nextStateGrid = new char[grid.length][grid[0].length];
	Map<Point2d, List<Point2d>> adjMap = adjacencyMap(grid);

	while (nextGridStatePt2(grid, nextStateGrid, adjMap)) {
	    grid = nextStateGrid;
	    nextStateGrid = new char[grid.length][grid[0].length];
	}

	return (int) Stream.of(nextStateGrid).flatMapToInt(c -> new String(c).chars()).filter(c -> (char) c == occupied)
		.count();
    }

    private boolean nextGridStatePt2(char[][] grid, char[][] nextGrid, Map<Point2d, List<Point2d>> adjMap) {
	boolean hasChanged = false;

	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {

		char nextSeatState = nextSeatStatePt2(grid, j, i, adjMap);
		if (nextSeatState != grid[i][j]) {
		    hasChanged = true;
		}
		nextGrid[i][j] = nextSeatState;
	    }
	}

	return hasChanged;
    }

    private char nextSeatStatePt2(char[][] grid, int x, int y, Map<Point2d, List<Point2d>> adjMap) {
	char currentState = grid[y][x];
	if (currentState == floor) {
	    return currentState;
	}
	List<Point2d> adjSeatPoints = adjMap.get(new Point2d(x, y));
	int occupiedSeats = (int) adjSeatPoints.stream().map(p -> grid[p.y()][p.x()]).filter(c -> (char) c == occupied)
		.count();

	if (currentState == empty && occupiedSeats == 0) {
	    currentState = occupied;
	} else if (currentState == occupied && occupiedSeats >= 5) {
	    currentState = empty;
	}

	return currentState;
    }

    private Map<Point2d, List<Point2d>> adjacencyMap(char[][] grid) {
	Map<Point2d, List<Point2d>> adjMap = new HashMap<>();

	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		if (grid[i][j] == empty || grid[i][j] == occupied) {
		    Point2d seatPoint = new Point2d(j, i);
		    List<Point2d> closestSeats = closestSeats(grid, j, i);
		    adjMap.put(seatPoint, closestSeats);
		}
	    }
	}

	return adjMap;
    }

    private List<Point2d> closestSeats(char[][] grid, int j, int i) {
	List<Point2d> closestSeats = new ArrayList<>();
	Point2d north = closestSeat(grid, j, i, 0, -1);
	Point2d south = closestSeat(grid, j, i, 0, 1);
	Point2d west = closestSeat(grid, j, i, -1, 0);
	Point2d east = closestSeat(grid, j, i, 1, 0);
	Point2d northWest = closestSeat(grid, j, i, -1, -1);
	Point2d northEast = closestSeat(grid, j, i, 1, -1);
	Point2d southWest = closestSeat(grid, j, i, -1, 1);
	Point2d southEast = closestSeat(grid, j, i, 1, 1);

	if (north != null)
	    closestSeats.add(north);
	if (south != null)
	    closestSeats.add(south);
	if (west != null)
	    closestSeats.add(west);
	if (east != null)
	    closestSeats.add(east);
	if (northWest != null)
	    closestSeats.add(northWest);
	if (northEast != null)
	    closestSeats.add(northEast);
	if (southWest != null)
	    closestSeats.add(southWest);
	if (southEast != null)
	    closestSeats.add(southEast);

	return closestSeats;
    }

    private Point2d closestSeat(char[][] grid, int x, int y, int dx, int dy) {
	// search vertically
	if (dx == 0) {
	    for (int i = y; (dy > 0 ? (i < grid.length) : (i >= 0)); i += dy) {
		if (i != y && (grid[i][x] == occupied || grid[i][x] == empty)) {
		    return new Point2d(x, i);
		}
	    }
	//search horizontally
	} else if (dy == 0) {
	    for (int i = x; (dx > 0 ? (i < grid[0].length) : (i >= 0)); i += dx) {
		if (i != x && (grid[y][i] == occupied || grid[y][i] == empty)) {
		    return new Point2d(i, y);
		}
	    }
	// search diagonally    
	} else {
	    while(x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
		x += dx;
		y += dy;
		
		if(x < 0 || x >= grid[0].length || y < 0 || y >= grid.length)
		    break;
		
		if(grid[y][x] == occupied || grid[y][x] == empty) {
		    return new Point2d(x, y);
		}
	    }
	}

	// no seats found
	return null;
    }

    // changes the state of the second grid, returns true if the state changed
    private boolean nextGridState(char[][] grid, char[][] nextState) {
	boolean hasChanged = false;

	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		char nextSeatState = nextSeatState(grid, j, i);
		if (nextSeatState != grid[i][j]) {
		    hasChanged = true;
		}
		nextState[i][j] = nextSeatState;
	    }
	}

	return hasChanged;
    }

    private char nextSeatState(char[][] grid, int x, int y) {
	char currentState = grid[y][x];

	List<Character> adjacentStates = adjacentStates(grid, x, y);

	int occupiedSeats = (int) adjacentStates.stream().filter(c -> c == occupied).count();
	if (currentState == empty && occupiedSeats == 0) {
	    currentState = occupied;
	} else if (currentState == occupied && occupiedSeats >= 4) {
	    currentState = empty;
	}

	return currentState;
    }

    private List<Character> adjacentStates(char[][] grid, int x, int y) {
	List<Character> adjStates = new ArrayList<>();

	char north = y > 0 ? grid[y - 1][x] : floor;
	char south = y < grid.length - 1 ? grid[y + 1][x] : floor;
	char west = x > 0 ? grid[y][x - 1] : floor;
	char east = x < grid[y].length - 1 ? grid[y][x + 1] : floor;
	char northWest = (y > 0 && x > 0) ? grid[y - 1][x - 1] : floor;
	char northEast = (y > 0 && x < grid[y].length - 1) ? grid[y - 1][x + 1] : floor;
	char southWest = (y < grid.length - 1 && x > 0) ? grid[y + 1][x - 1] : floor;
	char southEast = (y < grid.length - 1 && x < grid[y].length - 1) ? grid[y + 1][x + 1] : floor;

	adjStates.add(north);
	adjStates.add(south);
	adjStates.add(west);
	adjStates.add(east);
	adjStates.add(northWest);
	adjStates.add(northEast);
	adjStates.add(southWest);
	adjStates.add(southEast);

	return adjStates;
    }

    private char[][] initialGrid() {
	char[][] grid = new char[rawData.size()][rawData.get(0).length()];
	for (int i = 0; i < rawData.size(); i++) {
	    String row = rawData.get(i);
	    for (int j = 0; j < row.length(); j++) {
		grid[i][j] = row.charAt(j);
	    }
	}

	return grid;
    }

    @SuppressWarnings("unused")
    private void printGrid(char[][] grid) {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		System.out.print(grid[i][j]);
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) {
	Day11 test = new Day11(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 11\\InputFile1.txt"));
	System.out.println(test.run2());

    }

}
