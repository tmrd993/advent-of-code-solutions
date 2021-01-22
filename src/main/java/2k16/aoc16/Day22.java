package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import myutils16.GridNode;
import myutils16.Pair;
import myutils16.Point2d;
import myutils16.StaticUtils;

public class Day22 {

    private List<String> rawData;

    public Day22(File input) {
	rawData = StaticUtils.inputToList(input);
    }

    public int run1() {
	return getValidPairs().size() - 1;
    }

    public int run2() {
	Map<Point2d, Pair<Integer, Integer>> nodeMapping = nodeMapping();
	// node where y == 0 && x == max
	Point2d startPos = nodeMapping.entrySet().stream().filter(e -> e.getKey().y() == 0)
		.max(Comparator.comparing(e -> e.getKey().x())).map(e -> e.getKey()).get();
	Point2d targetPos = new Point2d(0, 0);
	Point2d emptySpotPos = nodeMapping.entrySet().stream().filter(e -> e.getValue().k() == 0).findAny().get()
		.getKey();

	Queue<State> queue = new LinkedList<>();
	queue.add(new State(startPos, emptySpotPos, 0));

	Set<State> visited = new HashSet<>();

	// run a simple BFS while moving only the payload and the empty node around
	while (!queue.isEmpty()) {
	    State currentState = queue.poll();
	    Point2d currentPos = currentState.payloadPos;
	    Point2d currentEmptySpotPos = currentState.emptySpotPos;
	    int steps = currentState.stepsTaken;

	    // done
	    if (currentPos.equals(targetPos)) {
		return steps;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    Point2d nextEmptySpot;
	    Point2d nextPayloadPos;
	    int nextStepCount;
	    if (nodeMapping.containsKey(up) && !visited.contains(new State(up, currentPos, 0))) {
		nextEmptySpot = currentPos;
		nextPayloadPos = up;
		if (up.equals(currentEmptySpotPos)) {
		    nextStepCount = steps + 1;
		} else {
		    // move the empty spot to the next position
		    nextStepCount = bfs(currentEmptySpotPos, up, nodeMapping, currentPos);
		    if (nextStepCount > 0) {
			nextStepCount += steps + 1;
		    }
		}
		// can move to next spot
		if (nextStepCount > 0) {
		    queue.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		    visited.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		}
	    }
	    
	    if (nodeMapping.containsKey(down) && !visited.contains(new State(down, currentPos, 0))) {
		nextEmptySpot = currentPos;
		nextPayloadPos = down;
		if (down.equals(currentEmptySpotPos)) {
		    nextStepCount = steps + 1;
		} else {
		    // move the empty spot to the next position
		    nextStepCount = bfs(currentEmptySpotPos, down, nodeMapping, currentPos);
		    if (nextStepCount > 0) {
			nextStepCount += steps + 1;
		    }
		}
		// can move to next spot
		if (nextStepCount > 0) {
		    queue.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		    visited.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		}
	    }
	    
	    if (nodeMapping.containsKey(left) && !visited.contains(new State(left, currentPos, 0))) {
		nextEmptySpot = currentPos;
		nextPayloadPos = left;
		if (left.equals(currentEmptySpotPos)) {
		    nextStepCount = steps + 1;
		} else {
		    // move the empty spot to the next position
		    nextStepCount = bfs(currentEmptySpotPos, left, nodeMapping, currentPos);
		    if (nextStepCount > 0) {
			nextStepCount += steps + 1;
		    }
		}
		// can move to next spot
		if (nextStepCount > 0) {
		    queue.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		    visited.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		}
	    }
	    
	    if (nodeMapping.containsKey(right) && !visited.contains(new State(right, currentPos, 0))) {
		nextEmptySpot = currentPos;
		nextPayloadPos = right;
		if (right.equals(currentEmptySpotPos)) {
		    nextStepCount = steps + 1;
		} else {
		    // move the empty spot to the next position
		    nextStepCount = bfs(currentEmptySpotPos, right, nodeMapping, currentPos);
		    if (nextStepCount > 0) {
			nextStepCount += steps + 1;
		    }
		}
		// can move to next spot
		if (nextStepCount > 0) {
		    queue.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		    visited.add(new State(nextPayloadPos, nextEmptySpot, nextStepCount));
		}
	    }
	}

	return -1;
    }

    // returns a list of nodes that have at least 1 pair (also contains the only
    // node that holds no data)
    public List<GridNode> getValidPairs() {
	List<GridNode> nodes = getNodes();
	List<GridNode> validPairs = new ArrayList<>();

	GridNode zeroTile = null;
	for (int i = 0; i < nodes.size(); i++) {
	    for (int j = 0; j < nodes.size(); j++) {
		if (i != j && nodes.get(i).getUsed() > 0 && nodes.get(i).getUsed() <= nodes.get(j).getAvail()) {
		    validPairs.add(nodes.get(i));
		    zeroTile = nodes.get(j);
		}
	    }
	}

	validPairs.add(zeroTile);
	return validPairs;
    }

    private List<GridNode> getNodes() {
	List<GridNode> nodes = new ArrayList<>();

	for (String line : rawData) {
	    String[] nodeData = Arrays.stream(line.split("\s")).filter(s -> !s.isBlank()).map(s -> s.trim())
		    .toArray(String[]::new);
	    String gridPath = nodeData[0];
	    int x = Integer.parseInt(
		    gridPath.substring(gridPath.indexOf('-') + 2, gridPath.indexOf('-', gridPath.indexOf('-') + 1)));
	    int y = Integer.parseInt(gridPath.substring(gridPath.indexOf('-', gridPath.indexOf('-') + 1) + 2));
	    int size = Integer.parseInt(nodeData[1].substring(0, nodeData[1].length() - 1));
	    int used = Integer.parseInt(nodeData[2].substring(0, nodeData[2].length() - 1));
	    int avail = Integer.parseInt(nodeData[3].substring(0, nodeData[3].length() - 1));
	    int usedPerc = Integer.parseInt(nodeData[4].substring(0, nodeData[4].length() - 1));

	    nodes.add(new GridNode(new Point2d(x, y), size, used, avail, usedPerc));
	}

	return nodes;
    }

    // moves startPos (the empty node) to the targetPos (next position of payload) while skipping the payload position
    // returns the minimum amount of steps to move the empty node to the desired destination
    private int bfs(Point2d startPos, Point2d targetPos, Map<Point2d, Pair<Integer, Integer>> nodeMapping, Point2d payloadPos) {
	Queue<Point2d> queue = new LinkedList<>();
	Queue<Integer> stepQueue = new LinkedList<>();
	stepQueue.add(0);
	queue.add(startPos);
	Set<Point2d> visited = new HashSet<>();

	while (!queue.isEmpty()) {
	    Point2d current = queue.poll();
	    int currentSteps = stepQueue.poll();

	    if (current.equals(targetPos)) {
		return currentSteps;
	    }

	    Point2d up = new Point2d(current.x(), current.y() - 1);
	    Point2d down = new Point2d(current.x(), current.y() + 1);
	    Point2d left = new Point2d(current.x() - 1, current.y());
	    Point2d right = new Point2d(current.x() + 1, current.y());

	    if (nodeMapping.containsKey(up) && !visited.contains(up) && !up.equals(payloadPos)) {
		queue.add(up);
		stepQueue.add(currentSteps + 1);
		visited.add(up);
	    }

	    if (nodeMapping.containsKey(down) && !visited.contains(down) && !down.equals(payloadPos)) {
		queue.add(down);
		stepQueue.add(currentSteps + 1);
		visited.add(down);
	    }

	    if (nodeMapping.containsKey(left) && !visited.contains(left) && !left.equals(payloadPos)) {
		queue.add(left);
		stepQueue.add(currentSteps + 1);
		visited.add(left);
	    }

	    if (nodeMapping.containsKey(right) && !visited.contains(right) && !right.equals(payloadPos)) {
		queue.add(right);
		stepQueue.add(currentSteps + 1);
		visited.add(right);
	    }

	}

	return -1;
    }

    // map positions of the nodes to their size/avail parameters
    private Map<Point2d, Pair<Integer, Integer>> nodeMapping() {
	Map<Point2d, Pair<Integer, Integer>> nodeMap = new HashMap<>();
	List<GridNode> validNodes = getValidPairs();
	for (GridNode node : validNodes) {
	    nodeMap.put(node.getPos(), new Pair<>(node.getUsed(), node.getSize()));
	}

	return nodeMap;
    }

    public static void main(String[] args) {
	Day22 test = new Day22(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 22\\InputFile.txt"));
	System.out.println(test.run2());

    }

    private static class State {
	private Point2d payloadPos;
	private Point2d emptySpotPos;
	int stepsTaken;

	public State(Point2d payloadPos, Point2d emptySpotPos, int stepsTaken) {
	    this.payloadPos = payloadPos;
	    this.emptySpotPos = emptySpotPos;
	    this.stepsTaken = stepsTaken;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || !(o instanceof State)) {
		return false;
	    }

	    State tmp = (State) o;

	    return tmp.emptySpotPos.equals(this.emptySpotPos) && tmp.payloadPos.equals(this.payloadPos);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(emptySpotPos, payloadPos);
	}

    }

}
