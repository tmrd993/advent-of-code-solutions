package aoc17;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import myutils2k17.Point2d;

import java.util.List;

public class Day3 {

    private Map<Integer, Point2d> grid;
    private final int INPUT;

    private enum Direction {
	UP, DOWN, LEFT, RIGHT
    };

    public Day3(int puzzleInput) {
	INPUT = puzzleInput;
	grid = new HashMap<Integer, Point2d>();
	fillGrid();
    }
    // part 1
    public int getDistanceToStartingPoint() {
	return grid.get(1).distanceL1(grid.get(INPUT));
    }

    // part 2
    public int getFirstHit() {
	Map<Point2d, Integer> calculatedSquares = new HashMap<Point2d, Integer>();
	calculatedSquares.put(grid.get(1), 1);
	calculatedSquares.put(grid.get(2), 1);

	int currentMaxValue = 1;
	int currentIndex = 3;

	while(currentMaxValue < INPUT) {
	    Point2d currentPoint = grid.get(currentIndex);
	    int squareValue = getSquareValue(currentPoint, calculatedSquares);
	    calculatedSquares.put(currentPoint, squareValue);
	    currentIndex++;
	    currentMaxValue = squareValue;
	}

	return currentMaxValue;
    }

    private int getSquareValue(Point2d currentPoint, Map<Point2d, Integer> calculatedSquares) {
	Point2d n = new Point2d(currentPoint.x(), currentPoint.y() - 1);
	Point2d w = new Point2d(currentPoint.x() - 1, currentPoint.y());
	Point2d s = new Point2d(currentPoint.x(), currentPoint.y() + 1);
	Point2d e = new Point2d(currentPoint.x() + 1, currentPoint.y());
	Point2d nw = new Point2d(currentPoint.x() - 1, currentPoint.y() - 1);
	Point2d ne = new Point2d(currentPoint.x() + 1, currentPoint.y() - 1);
	Point2d sw = new Point2d(currentPoint.x() - 1, currentPoint.y() + 1);
	Point2d se = new Point2d(currentPoint.x() + 1, currentPoint.y() + 1);
	
	List<Point2d> cardinalDirections = Arrays.asList(n, w, s, e, nw, ne, sw, se);

	int value = 0;
	
	for(Point2d neighbour : cardinalDirections) {
	    value += calculatedSquares.getOrDefault(neighbour, 0);
	}

	return value;
    }

    // creates the spiraling grid by doing a walk through every point
    private void fillGrid() {
	Point2d currentPos = new Point2d(0, 0);
	int currentValue = 1;
	Direction direction = Direction.RIGHT;
	grid.put(currentValue++, currentPos);

	// number of neighboring points to generate
	int moveFor = 1;
	int stepCounter = 0;

	while (currentValue < INPUT) {

	    for (int i = 0; i < moveFor; i++) {
		currentPos = nextPoint(direction, currentPos);
		grid.put(currentValue++, currentPos);
	    }
	    stepCounter++;
	    direction = nextDirection(direction);

	    // increment number of neighboring points to generate after every
	    // 2nd generation
	    if (stepCounter % 2 == 0) {
		moveFor++;
	    }

	}
    }

    private Direction nextDirection(Direction dir) {
	Direction nextDir = dir;
	switch (dir) {
	case UP:
	    nextDir = Direction.LEFT;
	    break;
	case DOWN:
	    nextDir = Direction.RIGHT;
	    break;
	case LEFT:
	    nextDir = Direction.DOWN;
	    break;
	case RIGHT:
	    nextDir = Direction.UP;
	    break;
	}
	return nextDir;
    }

    private Point2d nextPoint(Direction dir, Point2d currentPoint) {
	Point2d nextPoint = currentPoint;

	switch (dir) {
	case UP:
	    nextPoint = new Point2d(currentPoint.x(), currentPoint.y() - 1);
	    break;
	case DOWN:
	    nextPoint = new Point2d(currentPoint.x(), currentPoint.y() + 1);
	    break;
	case LEFT:
	    nextPoint = new Point2d(currentPoint.x() - 1, currentPoint.y());
	    break;
	case RIGHT:
	    nextPoint = new Point2d(currentPoint.x() + 1, currentPoint.y());
	    break;
	}
	return nextPoint;

    }

    public static void main(String[] args) {
	int input = 347991;
	Day3 test = new Day3(input);

	System.out.println(test.getDistanceToStartingPoint() + "\n" + test.getFirstHit());
    }

}
