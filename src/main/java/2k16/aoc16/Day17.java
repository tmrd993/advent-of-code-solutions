package aoc16;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

import myutils16.Point2d;
import myutils16.StaticUtils;

public class Day17 {

    private final Point2d target = new Point2d(3, 3);
    private final String input = "njfxhljp";
    private final Set<Character> openDoorChars = Set.of('b', 'c', 'd', 'e', 'f');

    public String run1() {
	Point2d startingPos = new Point2d(0, 0);
	Queue<Node> queue = new LinkedList<>();
	queue.add(new Node(startingPos, "", 0));
	Map<String, Set<Point2d>> visitedSetMapping = new HashMap<>();

	while (!queue.isEmpty()) {
	    Node currentNode = queue.poll();
	    Point2d currentPos = currentNode.pos;
	    String currentPath = currentNode.path;

	    visitedSetMapping.putIfAbsent(currentPath, new HashSet<>());
	    Set<Point2d> visited = visitedSetMapping.get(currentPath);
	    visited.add(currentPos);

	    if (currentPos.equals(target)) {
		return currentPath;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    // 0:up, 1:down, 2:left, 3:right
	    String doorChars = StaticUtils.md5(input + currentPath).substring(0, 4);

	    if (openDoorChars.contains(doorChars.charAt(0)) && !visited.contains(up) && up.y() >= 0) {
		queue.add(new Node(up, currentPath + "U", 0));
	    }

	    if (openDoorChars.contains(doorChars.charAt(1)) && !visited.contains(down) && down.y() < 4) {
		queue.add(new Node(down, currentPath + "D", 0));
	    }

	    if (openDoorChars.contains(doorChars.charAt(2)) && !visited.contains(left) && left.x() >= 0) {
		queue.add(new Node(left, currentPath + "L", 0));
	    }

	    if (openDoorChars.contains(doorChars.charAt(3)) && !visited.contains(right) && right.x() < 4) {
		queue.add(new Node(right, currentPath + "R", 0));
	    }
	}

	// no path found
	return "";
    }

    public int run2() {
	Point2d startingPos = new Point2d(0, 0);
	Queue<Node> queue = new LinkedList<>();
	queue.add(new Node(startingPos, "", 0));
	Map<String, Set<Point2d>> visitedSetMapping = new HashMap<>();

	int maxSteps = 0;
	while (!queue.isEmpty()) {
	    Node currentNode = queue.poll();
	    Point2d currentPos = currentNode.pos;
	    String currentPath = currentNode.path;
	    int currentSteps = currentNode.steps;

	    visitedSetMapping.putIfAbsent(currentPath, new HashSet<>());
	    Set<Point2d> visited = visitedSetMapping.get(currentPath);
	    visited.add(currentPos);

	    if (currentPos.equals(target)) {
		if (maxSteps < currentSteps) {
		    maxSteps = currentSteps;
		}
		continue;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    // 0:up, 1:down, 2:left, 3:right
	    String doorChars = StaticUtils.md5(input + currentPath).substring(0, 4);

	    if (openDoorChars.contains(doorChars.charAt(0)) && !visited.contains(up) && up.y() >= 0) {
		queue.add(new Node(up, currentPath + "U", currentSteps + 1));
	    }

	    if (openDoorChars.contains(doorChars.charAt(1)) && !visited.contains(down) && down.y() < 4) {
		queue.add(new Node(down, currentPath + "D", currentSteps + 1));
	    }

	    if (openDoorChars.contains(doorChars.charAt(2)) && !visited.contains(left) && left.x() >= 0) {
		queue.add(new Node(left, currentPath + "L", currentSteps + 1));
	    }

	    if (openDoorChars.contains(doorChars.charAt(3)) && !visited.contains(right) && right.x() < 4) {
		queue.add(new Node(right, currentPath + "R", currentSteps + 1));
	    }
	}

	return maxSteps;
    }

    public static void main(String[] args) {
	Day17 test = new Day17();
	System.out.println(test.run2());
    }

    private static class Node {
	public Point2d pos;
	public String path;
	public int steps;

	public Node(Point2d pos, String path, int steps) {
	    this.pos = pos;
	    this.path = path;
	    this.steps = steps;
	}
    }

}
