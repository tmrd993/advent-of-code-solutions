package aoc18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.awt.Point;

//both parts are kinda slow with the giant input (~ 1 minute on my slow
// notebook), might need to optimize it later
// the slow performance is caused by the breadh first search for every single room
public class Day20 {

    private Set<Point> rooms;
    private Map<Point, Character> doors;
    private List<Character> navigationList;

    public Day20(File inputFile) throws IOException {
	rooms = new HashSet<Point>();
	// add the (relative) starting point
	rooms.add(new Point(0, 0));
	doors = new HashMap<Point, Character>();
	navigationList = new ArrayList<Character>();
	parseInput(inputFile);
	// remove the regex begin (^) and end ($) identifiers
	navigationList.remove(0);
	navigationList.remove(navigationList.size() - 1);
    }

    // part 1
    // returns the shortest path from the starting location to the room where
    // the most doors are past through on the way
    public int run() {
	int maxDoorsPassed = 0;
	Point startingLocation = new Point(0, 0);

	buildFacility();

	for (Point p : rooms) {
	    if (!p.equals(startingLocation)) {
		int doorsPassed = bfs(startingLocation, p);
		if (doorsPassed > maxDoorsPassed) {
		    maxDoorsPassed = doorsPassed;
		}
	    }
	}
	return maxDoorsPassed;
    }

    // part 2
    // returns the number of shortest paths from the starting location that pass
    // through at least 1000 doors
    public int run2() {
	int numOfTargets = 0;
	Point startingLocation = new Point(0, 0);
	buildFacility();

	for (Point p : rooms) {
	    if (!p.equals(startingLocation)) {
		int doorsPassed = bfs(startingLocation, p);
		if (doorsPassed >= 1000) {
		    numOfTargets++;
		}
	    }
	}
	return numOfTargets;
    }

    // returns the number of doors getting passed on the shortest path from
    // start to target
    private int bfs(Point start, Point target) {
	// queue for the BFS
	Queue<Node> nodes = new LinkedList<Node>();
	nodes.add(new Node(start, null, 0));
	Set<Point> examinedPoints = new HashSet<Point>();
	// stores the target (if possible)
	Node nodeWithShortestPath = null;

	while (!nodes.isEmpty()) {
	    Node current = nodes.poll();
	    examinedPoints.add(current.position);

	    Point up = new Point(current.position.x, current.position.y - 1);
	    Point down = new Point(current.position.x, current.position.y + 1);
	    Point left = new Point(current.position.x - 1, current.position.y);
	    Point right = new Point(current.position.x + 1, current.position.y);

	    if ((doors.containsKey(up) || rooms.contains(up)) && !examinedPoints.contains(up)) {
		if (up.equals(target)) {
		    nodeWithShortestPath = new Node(up, current, current.distanceFromRoot + 1);
		    break;
		}
		nodes.offer(new Node(up, current, current.distanceFromRoot + 1));
	    }
	    if ((doors.containsKey(down) || rooms.contains(down)) && !examinedPoints.contains(down)) {
		if (down.equals(target)) {
		    nodeWithShortestPath = new Node(down, current, current.distanceFromRoot + 1);
		    break;
		}
		nodes.offer(new Node(down, current, current.distanceFromRoot + 1));
	    }
	    if ((doors.containsKey(left) || rooms.contains(left)) && !examinedPoints.contains(left)) {
		if (left.equals(target)) {
		    nodeWithShortestPath = new Node(left, current, current.distanceFromRoot + 1);
		    break;
		}
		nodes.offer(new Node(left, current, current.distanceFromRoot + 1));
	    }
	    if ((doors.containsKey(right) || rooms.contains(right)) && !examinedPoints.contains(right)) {
		if (right.equals(target)) {
		    nodeWithShortestPath = new Node(right, current, current.distanceFromRoot + 1);
		    break;
		}
		nodes.offer(new Node(right, current, current.distanceFromRoot + 1));
	    }
	}

	return nodeWithShortestPath == null ? -1 : nodeWithShortestPath.distanceFromRoot / 2;
    }

    // builds the facility by going through the navigation file, storing the
    // relative positions of the rooms and the doors in the relevant data
    // structures
    public void buildFacility() {
	Stack<Point> intersections = new Stack<Point>();
	Point current = new Point(0, 0);
	for (int i = 0; i < navigationList.size(); i++) {
	    if (navigationList.get(i) == 'N' || navigationList.get(i) == 'S' || navigationList.get(i) == 'W'
		    || navigationList.get(i) == 'E') {
		current = move(current, navigationList.get(i));
	    } else if (navigationList.get(i) == '|') {
		if (navigationList.get(i + 1) == ')') {
		    intersections.pop();
		} else {
		    current = intersections.pop();
		}
	    } else if (navigationList.get(i) == '(') {
		int numOfDirections = getIntersectionCount(i + 1);
		for (int j = 0; j < numOfDirections; j++) {
		    intersections.push(current);
		}
	    }
	}
    }

    // helper function to map to parse the input
    // returns the number of directions the current position splits into
    private int getIntersectionCount(int currentIndex) {
	int numOfDirections = 0;

	while (currentIndex < navigationList.size()) {
	    if (navigationList.get(currentIndex) == '|') {
		numOfDirections++;
	    } else if (navigationList.get(currentIndex) == '(') {
		int neededIntersections = 1;
		for (int i = currentIndex + 1; i < navigationList.size(); i++) {
		    if (navigationList.get(i) == '(')
			neededIntersections++;
		    else if (navigationList.get(i) == ')')
			neededIntersections--;

		    if (neededIntersections == 0) {
			currentIndex = i;
			break;
		    }
		}
	    } else if (navigationList.get(currentIndex) == ')') {
		break;
	    }
	    currentIndex++;
	}
	return numOfDirections;
    }

    // moves the current position based on the given direction (N, S, W, E)
    // saves the corresponding door and room into the set/hashmap
    private Point move(Point currentPoint, char cardinalDirection) {
	Point resultingPoint = currentPoint;
	switch (cardinalDirection) {
	case 'N':
	    doors.put(new Point(currentPoint.x, currentPoint.y - 1), '-');
	    rooms.add(new Point(currentPoint.x, currentPoint.y - 2));
	    resultingPoint = new Point(currentPoint.x, currentPoint.y - 2);
	    break;
	case 'W':
	    doors.put(new Point(currentPoint.x - 1, currentPoint.y), '|');
	    rooms.add(new Point(currentPoint.x - 2, currentPoint.y));
	    resultingPoint = new Point(currentPoint.x - 2, currentPoint.y);
	    break;
	case 'E':
	    doors.put(new Point(currentPoint.x + 1, currentPoint.y), '|');
	    rooms.add(new Point(currentPoint.x + 2, currentPoint.y));
	    resultingPoint = new Point(currentPoint.x + 2, currentPoint.y);
	    break;
	case 'S':
	    doors.put(new Point(currentPoint.x, currentPoint.y + 1), '-');
	    rooms.add(new Point(currentPoint.x, currentPoint.y + 2));
	    resultingPoint = new Point(currentPoint.x, currentPoint.y + 2);
	    break;
	}
	return resultingPoint;
    }

    private void parseInput(File inputFile) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(inputFile));

	int currentCharacter = 0;
	while ((currentCharacter = br.read()) != -1) {
	    navigationList.add((char) currentCharacter);
	}
	br.close();
    }

    // [0] == lowest y-value, [1] == highest y-value
    private int[] getVerticalLimits() {
	int min = Integer.MAX_VALUE;
	int max = Integer.MIN_VALUE;
	for (Point p : rooms) {
	    if (p.y > max)
		max = p.y;
	    if (p.y < min)
		min = p.y;
	}
	return new int[] { min, max };
    }

    private int[] getHorizontalLimits() {
	int min = Integer.MAX_VALUE;
	int max = Integer.MIN_VALUE;
	for (Point p : rooms) {
	    if (p.x > max)
		max = p.x;
	    if (p.x < min)
		min = p.x;
	}
	return new int[] { min, max };
    }

    // visual aid. prints the entire grid with doors, rooms and walls
    @SuppressWarnings("unused")
    private void printFacility() {
	int[] verticalLimits = getVerticalLimits();
	int[] horizontalLimits = getHorizontalLimits();

	for (int i = verticalLimits[0] - 1; i < verticalLimits[1] + 2; i++) {
	    for (int j = horizontalLimits[0] - 1; j < horizontalLimits[1] + 2; j++) {
		if (doors.containsKey(new Point(j, i)))
		    System.out.print(doors.get(new Point(j, i)));
		else if (rooms.contains(new Point(j, i)))
		    System.out.print('.');
		else
		    System.out.print('#');
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) throws IOException {

	File input = new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 20\\InputFile1.txt");

	Day20 test = new Day20(input);

	// int res1 = test.run();

	int res2 = test.run2();
	System.out.println(res2);

    }

    // helper data structure for the BFS
    public static class Node {
	Node parent;
	Point position;
	int distanceFromRoot;

	public Node(Point position, Node parent, int distanceFromRoot) {
	    this.position = position;
	    this.parent = parent;
	    this.distanceFromRoot = distanceFromRoot;
	}
    }
}
