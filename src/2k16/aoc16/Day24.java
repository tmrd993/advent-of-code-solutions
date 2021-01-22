package aoc16;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import myutils16.Pair;
import myutils16.Point2d;
import myutils16.StaticUtils;

public class Day24 {

    private List<String> rawData;

    public Day24(File input) {
	rawData = StaticUtils.inputToList(input);
    }

    public int run(boolean part2) {
	Map<Point2d, Character> areaMapping = areaMapping();
	Set<Point2d> keyPositions = areaMapping.entrySet().stream().filter(e -> Character.isDigit(e.getValue()))
		.map(e -> e.getKey()).collect(Collectors.toSet());
	Point2d startPos = areaMapping.entrySet().stream().filter(e -> e.getValue() == '0').map(e -> e.getKey())
		.findAny().get();
	Map<Character, Point2d> keyPosMapping = areaMapping.entrySet().stream().filter(e -> e.getValue() != '.')
		.collect(Collectors.toMap(Entry::getValue, Entry::getKey));

	int keyLen = keyPositions.size();

	// keystate is a bitfield that represents collected keys. index n: key n.
	int fullKeyState = Integer.parseInt(Stream.iterate("1", i -> i).limit(keyLen).collect(Collectors.joining()), 2);
	// actual keystate is twice as long because it also contains information about
	// the currently occupied key
	int initialKeyState = (0 | (1 << (keyLen))) + 1;

	Map<Point2d, Map<Character, Integer>> distanceMapping = distanceMapping(areaMapping, keyPositions);
	Map<State, Integer> stateStepMapping = new HashMap<>();

	Queue<State> queue = new LinkedList<>();
	State initialState = new State(initialKeyState, startPos);
	queue.add(initialState);
	Queue<Character> keyQueue = new LinkedList<>();
	keyQueue.add('0');

	stateStepMapping.put(initialState, 0);

	int lowestStepCount = Integer.MAX_VALUE;
	while (!queue.isEmpty()) {
	    State currentState = queue.poll();
	    Point2d currentPos = currentState.pos;
	    int currentCollectedKeys = currentState.collectedKeys;
	    int currentSteps = stateStepMapping.get(currentState);
	    char currentKey = keyQueue.poll();

	    if ((currentCollectedKeys & fullKeyState) == fullKeyState) {
		if (part2) {
		    if (lowestStepCount > currentSteps + distanceMapping.get(currentPos).get('0')) {
			lowestStepCount = currentSteps + distanceMapping.get(currentPos).get('0');
		    }
		} else {
		    if (lowestStepCount > currentSteps) {
			lowestStepCount = currentSteps;
		    }
		}
	    }

	    Set<Pair<Character, Integer>> reachableKeys = distanceMapping.get(currentPos).entrySet().stream()
		    .filter(e -> (((currentCollectedKeys >> (e.getKey() - 48)) & 1) == 0))
		    .map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toSet());

	    for (Pair<Character, Integer> reachableKey : reachableKeys) {
		// clear position bit
		int nextKeyState = currentCollectedKeys & ~(1 << (currentKey - 48 + keyLen));
		// set next position bit
		nextKeyState |= (1 << (reachableKey.k() - 48 + keyLen));
		// set key bit
		nextKeyState |= (1 << (reachableKey.k() - 48));

		State nextState = new State(nextKeyState, keyPosMapping.get(reachableKey.k()));
		if (stateStepMapping.containsKey(nextState)
			&& stateStepMapping.get(nextState) > reachableKey.v() + currentSteps) {
		    stateStepMapping.put(nextState, reachableKey.v() + currentSteps);
		} else if (!stateStepMapping.containsKey(nextState)) {
		    stateStepMapping.put(nextState, reachableKey.v() + currentSteps);
		    queue.add(nextState);
		    keyQueue.add(reachableKey.k());
		}
	    }

	}

	return lowestStepCount;
    }

    private Map<Point2d, Map<Character, Integer>> distanceMapping(Map<Point2d, Character> areaMapping,
	    Set<Point2d> keyPositions) {
	Map<Point2d, Map<Character, Integer>> distanceMapping = new HashMap<>();
	keyPositions.stream().forEach(k -> distanceMapping.put(k, bfs(k, areaMapping, keyPositions)));

	return distanceMapping;

    }

    private Map<Character, Integer> bfs(Point2d start, Map<Point2d, Character> areaMapping, Set<Point2d> keyPoints) {
	Map<Character, Integer> reachablePosMapping = new HashMap<>();

	Queue<Point2d> queue = new LinkedList<>();
	Queue<Integer> stepQueue = new LinkedList<>();
	Set<Point2d> visited = new HashSet<>();
	queue.add(start);
	stepQueue.add(0);

	while (!queue.isEmpty()) {

	    Point2d currentPos = queue.poll();
	    int steps = stepQueue.poll();

	    if (Character.isDigit(areaMapping.get(currentPos)) && !currentPos.equals(start)) {
		reachablePosMapping.putIfAbsent(areaMapping.get(currentPos), steps);
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    if (areaMapping.containsKey(up) && !visited.contains(up)) {
		queue.add(up);
		stepQueue.add(steps + 1);
		visited.add(up);
	    }

	    if (areaMapping.containsKey(down) && !visited.contains(down)) {
		queue.add(down);
		stepQueue.add(steps + 1);
		visited.add(down);
	    }

	    if (areaMapping.containsKey(left) && !visited.contains(left)) {
		queue.add(left);
		stepQueue.add(steps + 1);
		visited.add(left);
	    }

	    if (areaMapping.containsKey(right) && !visited.contains(right)) {
		queue.add(right);
		stepQueue.add(steps + 1);
		visited.add(right);
	    }

	}

	return reachablePosMapping;
    }

    private Map<Point2d, Character> areaMapping() {
	Map<Point2d, Character> areaMap = new HashMap<>();
	for (int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    for (int j = 0; j < line.length(); j++) {
		if (line.charAt(j) != '#') {
		    areaMap.put(new Point2d(j, i), line.charAt(j));
		}
	    }
	}

	return areaMap;
    }

    public static void main(String[] args) {
	Day24 test = new Day24(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 24\\InputFile1.txt"));
	System.out.println(test.run(true));
    }

    private static class State {
	private Point2d pos;
	// collected numbers (keys), as a bitfield that is twice as long as the number
	// of keys. first half of the bitfield == collected keys, second half of the
	// bitfield == currently occupied key position
	private int collectedKeys;

	public State(int collectedKeys, Point2d pos) {
	    this.collectedKeys = collectedKeys;
	    this.pos = pos;
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

	    return this.collectedKeys == tmp.collectedKeys;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(collectedKeys);
	}
    }

}
