package aoc19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import myutils19.IncomparablePair;
import myutils19.Point2d;
import myutils19.StaticUtils;

public class Day18 {

    private static final char entrance = '@';
    private static final char wall = '#';

    private Map<Point2d, Character> areaMap;

    public Day18(File input) {
	areaMap = getAreaMap(input);
    }

    public int run1() {
	Map<Character, Map<Character, IncomparablePair<Integer, Integer>>> distanceMapping = distanceMapping(areaMap);

	long firstState = 0;
	Map<Long, Integer> stateStepMapping = new HashMap<>();
	int alphabetLen = distanceMapping.size() - 1;

	firstState = 0L | (1L << (alphabetLen * 2));
	stateStepMapping.put(firstState, 0);
	long fullKeyState = Long.parseLong(StaticUtils.repeat("1", distanceMapping.size() - 1), 2);

	Queue<Long> stateQueue = new LinkedList<>();
	Queue<Character> keyQueue = new LinkedList<>();
	keyQueue.add('@');
	stateQueue.add(firstState);

	int lowestStepCount = Integer.MAX_VALUE;
	while (!stateQueue.isEmpty()) {
	    long state = stateQueue.poll();
	    char currentKey = keyQueue.poll();

	    // found a state with all keys collected
	    if ((state & fullKeyState) == fullKeyState) {
		if (stateStepMapping.get(state) < lowestStepCount) {
		    lowestStepCount = stateStepMapping.get(state);
		}
	    }

	    Map<Character, IncomparablePair<Integer, Integer>> distancesToOtherKeys = distanceMapping.get(currentKey);
	    Set<Character> reachableKeys = distancesToOtherKeys.entrySet().stream().filter(
		    e -> ((state >> (e.getKey() - 97)) & 1) == 0 && ((state & e.getValue().v()) == e.getValue().v()))
		    .map(c -> c.getKey()).collect(Collectors.toSet());

	    for (Character reachableKey : reachableKeys) {

		int currentSteps = stateStepMapping.get(state) + distancesToOtherKeys.get(reachableKey).k();
		// set the key bit
		long currentState = (1L << (reachableKey - 97)) | state;
		// clear current positon bit first (bit number 26 + 26 is the initial position
		// at '@')
		currentState &= ~(1L << getBitPos(currentKey, alphabetLen) + alphabetLen);
		// set new position bit
		currentState |= (1L << getBitPos(reachableKey, alphabetLen) + alphabetLen);
		// check if we already got to this key, update the step count if this one is
		// better
		if (stateStepMapping.containsKey(currentState) && stateStepMapping.get(currentState) > currentSteps) {
		    stateStepMapping.put(currentState, currentSteps);
		    // otherwise, just add the current state to the mapping
		} else if (!stateStepMapping.containsKey(currentState)) {
		    stateStepMapping.put(currentState, currentSteps);
		    stateQueue.add(currentState);
		    keyQueue.add(reachableKey);
		}
	    }
	}

	return lowestStepCount;
    }

    // works pretty much the same as part 1, only this time states are defined
    // differently
    // IMPORTANT: I changed the input file manually for part 2. If you want to use
    // this method, you should do the same. Input of part 1 doesn't work with this
    // one without changing it manually.
    public int run2() {

	List<Map<Point2d, Character>> areaMappings = getAreaMaps(areaMap);
	List<Map<Character, Map<Character, IncomparablePair<Integer, Integer>>>> distanceMappings = areaMappings
		.stream().map(e -> distanceMapping(e)).collect(Collectors.toList());

	// all robots are at their initial positions, no keys have been collected, steps
	// are at 0
	State initial = new State('@', '@', '@', '@', 0, 0);

	long fullKeyState = Long
		.parseLong(StaticUtils.repeat("1", distanceMappings.stream().mapToInt(e -> e.size() - 1).sum()), 2);

	Queue<State> queue = new LinkedList<>();
	queue.add(initial);

	Map<State, Integer> stateStepMapping = new HashMap<>();
	stateStepMapping.put(initial, 0);

	int lowestSteps = Integer.MAX_VALUE;
	while (!queue.isEmpty()) {
	    State currentState = queue.poll();

	    if (currentState.keys == fullKeyState && stateStepMapping.get(currentState) < lowestSteps) {
		lowestSteps = stateStepMapping.get(currentState);
	    }

	    // go through every grid once and check the current state for advancements
	    for (int i = 0; i < 4; i++) {
		Map<Character, Map<Character, IncomparablePair<Integer, Integer>>> distanceMapping = distanceMappings
			.get(i);
		char currentKey = currentState.getRobot(i);

		Map<Character, IncomparablePair<Integer, Integer>> distancesToOtherKeys = distanceMapping
			.get(currentKey);
		if (distancesToOtherKeys.isEmpty()) {
		    continue;
		}

		Set<Character> reachableKeys = distancesToOtherKeys.entrySet().stream()
			.filter(e -> ((currentState.keys >> (e.getKey() - 97)) & 1) == 0
				&& ((currentState.keys & e.getValue().v()) == e.getValue().v()))
			.map(c -> c.getKey()).collect(Collectors.toSet());

		for (Character reachableKey : reachableKeys) {
		    long nextKeyState = currentState.keys | (1 << reachableKey - 97);
		    int nextSteps = stateStepMapping.get(currentState) + distancesToOtherKeys.get(reachableKey).k();
		    State nextState = new State(currentState.robot1, currentState.robot2, currentState.robot3,
			    currentState.robot4, nextKeyState, nextSteps);
		    nextState.setRobotPos(i, reachableKey);

		    if (stateStepMapping.containsKey(nextState) && stateStepMapping.get(nextState) > nextSteps) {
			stateStepMapping.put(nextState, nextSteps);
		    } else if (!stateStepMapping.containsKey(nextState)) {
			stateStepMapping.put(nextState, nextSteps);
			queue.add(nextState);
		    }

		}

	    }

	}

	return lowestSteps;
    }

    // returns a mapping of characters and the required steps + keys (as a bitfield)
    // to reach the character. startPos is the position of a key.
    private Map<Character, IncomparablePair<Integer, Integer>> bfs(Point2d startPos, Map<Point2d, Character> areaMap) {
	Set<Point2d> visited = new HashSet<>();

	Node start = new Node(startPos, 0, 0);

	Queue<Node> queue = new LinkedList<>();
	queue.add(start);

	Map<Character, IncomparablePair<Integer, Integer>> targetMapping = new HashMap<>();

	while (!queue.isEmpty()) {
	    Node currentNode = queue.poll();
	    Point2d currentPos = currentNode.pos();
	    int requiredKeys = currentNode.keys();

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    visited.add(currentPos);

	    char areaUp = areaMap.get(up);
	    char areaDown = areaMap.get(down);
	    char areaLeft = areaMap.get(left);
	    char areaRight = areaMap.get(right);

	    // if (currentPos.equals(target)) {
	    // return currentNode;
	    // }

	    if (areaMap.containsKey(up) && !visited.contains(up) && areaUp != wall) {
		int keys = requiredKeys;

		if (Character.isLowerCase(areaUp)) {
		    targetMapping.putIfAbsent(areaUp, new IncomparablePair<>(currentNode.steps() + 1, keys));
		}

		else if (Character.isUpperCase(areaUp)) {
		    keys = (1 << (Character.toLowerCase(areaUp) - 'a')) | keys;
		}

		Node nodeUp = new Node(up, currentNode.steps() + 1, keys);

		queue.add(nodeUp);
	    }

	    if (areaMap.containsKey(down) && !visited.contains(down) && areaDown != wall) {
		int keys = requiredKeys;

		if (Character.isLowerCase(areaDown)) {
		    targetMapping.putIfAbsent(areaDown, new IncomparablePair<>(currentNode.steps() + 1, keys));
		}

		else if (Character.isUpperCase(areaDown)) {
		    keys = (1 << (Character.toLowerCase(areaDown) - 'a')) | keys;
		}

		Node nodeDown = new Node(down, currentNode.steps() + 1, keys);
		queue.add(nodeDown);
	    }

	    if (areaMap.containsKey(left) && !visited.contains(left) && areaLeft != wall) {
		int keys = requiredKeys;

		if (Character.isLowerCase(areaLeft)) {
		    targetMapping.putIfAbsent(areaLeft, new IncomparablePair<>(currentNode.steps() + 1, keys));
		}

		else if (Character.isUpperCase(areaLeft)) {
		    keys = (1 << (Character.toLowerCase(areaLeft)) - 'a') | keys;
		}

		Node nodeLeft = new Node(left, currentNode.steps() + 1, keys);
		queue.add(nodeLeft);
	    }

	    if (areaMap.containsKey(right) && !visited.contains(right) && areaRight != wall) {
		int keys = requiredKeys;

		if (Character.isLowerCase(areaRight)) {
		    targetMapping.putIfAbsent(areaRight, new IncomparablePair<>(currentNode.steps() + 1, keys));
		}

		else if (Character.isUpperCase(areaRight)) {
		    keys = (1 << (Character.toLowerCase(areaRight) - 'a')) | keys;
		}

		Node nodeRight = new Node(right, currentNode.steps() + 1, keys);
		queue.add(nodeRight);
	    }

	}

	return targetMapping;
    }

    // returns a list of 4 areamappings for part 2
    private List<Map<Point2d, Character>> getAreaMaps(Map<Point2d, Character> fullMap) {
	List<Map<Point2d, Character>> areaMappings = new ArrayList<>();

	int xMax = fullMap.entrySet().stream().map(e -> e.getKey().x()).max(Comparator.naturalOrder()).get();
	int dx = xMax / 2;
	int yMax = fullMap.entrySet().stream().map(e -> e.getKey().y()).max(Comparator.naturalOrder()).get();
	int dy = yMax / 2;

	// get upper left area
	Map<Point2d, Character> upperLeft = new HashMap<>();
	for (int i = 0; i <= dy; i++) {
	    for (int j = 0; j <= dx; j++) {
		upperLeft.put(new Point2d(j, i), fullMap.get(new Point2d(j, i)));
	    }
	}
	areaMappings.add(upperLeft);

	// get upper right area
	Map<Point2d, Character> upperRight = new HashMap<>();
	for (int i = 0; i <= dy; i++) {
	    for (int j = dx; j <= xMax; j++) {
		upperRight.put(new Point2d(j, i), fullMap.get(new Point2d(j, i)));
	    }
	}
	areaMappings.add(upperRight);

	// get lower left area
	Map<Point2d, Character> lowerLeft = new HashMap<>();
	for (int i = dy; i <= yMax; i++) {
	    for (int j = 0; j <= dx; j++) {
		lowerLeft.put(new Point2d(j, i), fullMap.get(new Point2d(j, i)));
	    }
	}
	areaMappings.add(lowerLeft);

	// get lower right area
	Map<Point2d, Character> lowerRight = new HashMap<>();
	for (int i = dy; i <= yMax; i++) {
	    for (int j = dx; j <= xMax; j++) {
		lowerRight.put(new Point2d(j, i), fullMap.get(new Point2d(j, i)));
	    }
	}
	areaMappings.add(lowerRight);

	return areaMappings;
    }

    // parses the input into a map of points and characters
    private Map<Point2d, Character> getAreaMap(File input) {
	Map<Point2d, Character> areaMap = new HashMap<>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String row = "";

	    int rowCount = 0;
	    int colCount = 0;
	    while ((row = br.readLine()) != null) {
		for (int i = 0; i < row.length(); i++) {
		    Point2d currentPoint = new Point2d(colCount++, rowCount);
		    areaMap.put(currentPoint, row.charAt(i));
		}
		colCount = 0;
		rowCount++;
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return areaMap;
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	int xMax = areaMap.entrySet().stream().mapToInt(s -> s.getKey().x()).max().getAsInt();
	int yMax = areaMap.entrySet().stream().mapToInt(s -> s.getKey().y()).max().getAsInt();

	for (int i = 0; i <= yMax; i++) {
	    for (int j = 0; j <= xMax; j++) {
		System.out.print(areaMap.get(new Point2d(j, i)));
	    }
	    System.out.println();
	}
    }

    // returns a map of all keys mapped to a map that maps all reachable keys to a
    // pair that consists of key: steps to reach that key, value: keys required to
    // reach that key (as a bitfield). Example: @ = {a = {1 , 010}, b = {5,
    // 110}}.... means that from position "@", keys a and b are reachable with 1 and
    // 5 steps.
    private Map<Character, Map<Character, IncomparablePair<Integer, Integer>>> distanceMapping(
	    Map<Point2d, Character> areaMap) {
	Map<Character, Map<Character, IncomparablePair<Integer, Integer>>> distanceMapping = new HashMap<>();

	List<Point2d> keyPositions = areaMap.entrySet().stream().filter(e -> Character.isLowerCase(e.getValue()))
		.map(e -> e.getKey()).collect(Collectors.toList());

	Point2d startingPos = areaMap.entrySet().stream().filter(e -> e.getValue() == entrance).map(e -> e.getKey())
		.findFirst().get();

	distanceMapping.put(areaMap.get(startingPos), bfs(startingPos, areaMap));

	keyPositions.stream().forEach(k -> distanceMapping.put(areaMap.get(k), bfs(k, areaMap)));

	return distanceMapping;
    }

    private int getBitPos(char key, int alphabetLen) {
	if (key == '@') {
	    return alphabetLen;
	} else {
	    return key - 97;
	}
    }

    public static void main(String[] args) {
	Day18 test = new Day18(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 18\\InputFilePt2.txt"));

	System.out.println(test.run2());
    }

    public static class Node {
	private Point2d pos;
	private int keys;
	private int steps;

	public Node(Point2d pos, int steps, int keys) {
	    this.pos = pos;
	    this.steps = steps;
	    this.keys = keys;
	}

	public int steps() {
	    return steps;
	}

	public Point2d pos() {
	    return pos;
	}

	public int keys() {
	    return keys;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || !(o instanceof Node)) {
		return false;
	    }

	    Node tmp = (Node) o;
	    return this.keys == tmp.keys && this.pos.equals(tmp.pos);

	}

	@Override
	public int hashCode() {
	    return Objects.hash(keys, pos);
	}
    }

    public static class State {
	public char robot1;
	public char robot2;
	public char robot3;
	public char robot4;

	public long keys;
	public int steps;

	public State(char r1, char r2, char r3, char r4, long keys, int steps) {
	    this.robot1 = r1;
	    this.robot2 = r2;
	    this.robot3 = r3;
	    this.robot4 = r4;
	    this.keys = keys;
	    this.steps = steps;
	}

	public char getRobot(int index) {
	    switch (index) {
	    case 0:
		return robot1;
	    case 1:
		return robot2;
	    case 2:
		return robot3;
	    case 3:
		return robot4;
	    default:
		throw new IllegalArgumentException();
	    }
	}

	public void setRobotPos(int index, char pos) {
	    switch (index) {
	    case 0:
		robot1 = pos;
		break;
	    case 1:
		robot2 = pos;
		break;
	    case 2:
		robot3 = pos;
		break;
	    case 3:
		robot4 = pos;
		break;
	    default:
		throw new IllegalArgumentException();
	    }
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

	    return this.keys == tmp.keys && this.robot1 == tmp.robot1 && this.robot2 == tmp.robot2
		    && this.robot3 == tmp.robot3 && this.robot4 == tmp.robot4;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(keys, robot1, robot2, robot3, robot4);
	}
    }

}
