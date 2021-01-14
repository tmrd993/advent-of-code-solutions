package aoc16;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import myutils16.Point2d;

public class Day13 {

    private final int inputNumber = 1352;
    private final Point2d targetPart1 = new Point2d(31, 39);

    public int run1() {

	Point2d startPoint = new Point2d(1, 1);
	Set<Point2d> visited = new HashSet<>();

	Queue<Node> queue = new LinkedList<>();
	queue.add(new Node(0, startPoint));

	while (!queue.isEmpty()) {
	    Node currentNode = queue.poll();
	    int currentSteps = currentNode.steps;
	    Point2d currentPos = currentNode.pos;

	    if (currentPos.equals(targetPart1)) {
		return currentSteps;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    if (up.y() >= 0 && !visited.contains(up) && isOpenSpace(up)) {
		queue.add(new Node(currentSteps + 1, up));
		visited.add(up);
	    }

	    if (!visited.contains(down) && isOpenSpace(down)) {
		queue.add(new Node(currentSteps + 1, down));
		visited.add(down);
	    }

	    if (left.x() >= 0 && !visited.contains(left) && isOpenSpace(left)) {
		queue.add(new Node(currentSteps + 1, left));
		visited.add(left);
	    }

	    if (!visited.contains(right) && isOpenSpace(right)) {
		queue.add(new Node(currentSteps + 1, right));
		visited.add(right);
	    }
	}

	// no path to target found
	return -1;
    }

    public int run2() {
	Point2d startPoint = new Point2d(1, 1);
	Set<Point2d> visited = new HashSet<>();

	Queue<Node> queue = new LinkedList<>();
	queue.add(new Node(0, startPoint));

	while (!queue.isEmpty()) {
	    Node currentNode = queue.poll();
	    int currentSteps = currentNode.steps;
	    Point2d currentPos = currentNode.pos;

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    if (up.y() >= 0 && !visited.contains(up) && isOpenSpace(up) && currentSteps < 50) {
		queue.add(new Node(currentSteps + 1, up));
		visited.add(up);
	    }

	    if (!visited.contains(down) && isOpenSpace(down) && currentSteps < 50) {
		queue.add(new Node(currentSteps + 1, down));
		visited.add(down);
	    }

	    if (left.x() >= 0 && !visited.contains(left) && isOpenSpace(left) && currentSteps < 50) {
		queue.add(new Node(currentSteps + 1, left));
		visited.add(left);
	    }

	    if (!visited.contains(right) && isOpenSpace(right) && currentSteps < 50) {
		queue.add(new Node(currentSteps + 1, right));
		visited.add(right);
	    }
	}

	return visited.size();
    }

    public boolean isOpenSpace(Point2d pos) {
	return Integer.bitCount(((pos.x() * pos.x()) + (3 * pos.x()) + (2 * pos.x() * pos.y()) + (pos.y())
		+ (pos.y() * pos.y()) + inputNumber)) % 2 == 0;
    }

    public static void main(String[] args) {
	Day13 test = new Day13();
	System.out.println(test.run2());
    }

    private static class Node {
	private final int steps;
	private final Point2d pos;

	public Node(int steps, Point2d pos) {
	    this.pos = pos;
	    this.steps = steps;
	}
    }

}
