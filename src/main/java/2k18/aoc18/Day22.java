package aoc18;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class Day22 {

    private final char ROCKY = '.';
    private final char WET = '=';
    private final char NARROW = '|';

    private HashMap<Point, Character> caveMapping;
    private HashMap<Point, Integer> geologicalIndicies;
    private HashMap<Point, Integer> erosionLevels;
    private Point targetPosition;
    private int caveDepth;

    public Day22(int targetX, int targetY, int depth) {
	targetPosition = new Point(targetX, targetY);
	caveDepth = depth;
	caveMapping = new HashMap<Point, Character>();
	geologicalIndicies = new HashMap<Point, Integer>();
	erosionLevels = new HashMap<Point, Integer>();
    }

    // part 1
    public int run() {
	createCaveMappings();
	int riskLevel = 0;
	for (Map.Entry<Point, Character> entry : caveMapping.entrySet()) {
	    if (entry.getValue() == WET)
		riskLevel += 1;
	    else if (entry.getValue() == NARROW)
		riskLevel += 2;
	}

	return riskLevel;
    }

    // part 2
    public int run2() {
	createCaveMappings();
	return getMinsToTarget();
    }

    // finds the optimal path with the shortest time spent to the target using
    // A*
    // returns the time needed to find the target
    private int getMinsToTarget() {

	// initialize a priority queue with a comparator with ascending score
	// based ordering
	PriorityQueue<Node> openList = new PriorityQueue<Node>(10, new Comparator<Node>() {
	    @Override
	    public int compare(Node arg0, Node arg1) {
		return arg0.score() - arg1.score();
	    }
	});

	Set<Node> closedList = new HashSet<Node>();

	// add the starting position to the openlist
	openList.add(new Node(new Point(0, 0), 0, 0, 0, null, Node.Equipment.TORCH));
	Node targetNode = null;

	boolean searchFinished = false;
	while (!searchFinished) {
	    Node current = openList.poll();
	    Point currentPosition = current.getPosition();

	    if (currentPosition.equals(targetPosition) && current.equipment() == Node.Equipment.TORCH) {
		targetNode = current;
		searchFinished = true;
		continue;
	    }

	    closedList.add(current);

	    // add current position with switched gear to list
	    Set<Node.Equipment> usableCurrent = getUsableEquipment(currentPosition);
	    Node.Equipment switchedGear = usableCurrent.stream().filter(n -> n != current.equipment()).findFirst()
		    .get();
	    Node switchedNode = new Node(currentPosition, current.getTime() + 7, current.hScore, current.gScore + 6,
		    current.parent, switchedGear);
	    if (!closedList.contains(switchedNode)) {
		if (openList.contains(switchedNode))
		    updateOpenScore(openList, switchedNode, current, 7);
		else
		    putNewOpenNode(openList, switchedNode, current, 7);
	    }

	    Point right = new Point(currentPosition.x + 1, currentPosition.y);
	    Point down = new Point(currentPosition.x, currentPosition.y + 1);
	    Point left = new Point(currentPosition.x - 1, currentPosition.y);
	    Point up = new Point(currentPosition.x, currentPosition.y - 1);

	    // creates the area type in all 4 cardinal directions
	    createCardinalMappings(right, down, left, up);
	    // usable equipment in possible next positions
	    Set<Node.Equipment> usableRight = getUsableEquipment(right);
	    Set<Node.Equipment> usableDown = getUsableEquipment(down);
	    Set<Node.Equipment> usableLeft = null;
	    Set<Node.Equipment> usableUp = null;
	    if (left.x >= 0)
		usableLeft = getUsableEquipment(left);
	    if (up.y >= 0)
		usableUp = getUsableEquipment(up);

	    // add node to right to list
	    usableRight.retainAll(usableCurrent);
	    Node.Equipment optimalGearRight = usableRight.iterator().next();
	    if (current.equipment() == optimalGearRight || usableRight.contains(current.equipment())) {
		Node dummy = new Node(right, current.getTime() + 1, 0, 0, current, current.equipment());
		if (!closedList.contains(dummy)) {
		    if (openList.contains(dummy)) {
			updateOpenScore(openList, dummy, current, 1);
		    } else {
			putNewOpenNode(openList, dummy, current, 1);
		    }
		}
	    }

	    // add node below to list
	    usableDown.retainAll(usableCurrent);
	    Node.Equipment optimalGearDown = usableDown.iterator().next();
	    if (current.equipment() == optimalGearDown || usableDown.contains(current.equipment())) {
		Node dummy = new Node(down, 0, 0, 0, current, current.equipment());
		if (!closedList.contains(dummy)) {
		    if (openList.contains(dummy)) {
			updateOpenScore(openList, dummy, current, 1);
		    } else {
			putNewOpenNode(openList, dummy, current, 1);
		    }
		}
	    }

	    // add node to the left to list
	    if (left.x >= 0) {
		usableLeft.retainAll(usableCurrent);
		Node.Equipment optimalGearLeft = usableLeft.iterator().next();
		if (current.equipment() == optimalGearLeft || usableLeft.contains(current.equipment())) {
		    Node dummy = new Node(left, 0, 0, 0, current, current.equipment());
		    if (!closedList.contains(dummy)) {
			if (openList.contains(dummy)) {
			    updateOpenScore(openList, dummy, current, 1);
			} else {
			    putNewOpenNode(openList, dummy, current, 1);
			}
		    }
		}
	    }

	    // add node above to list
	    if (up.y >= 0) {
		usableUp.retainAll(usableCurrent);
		Node.Equipment optimalGearUp = usableUp.iterator().next();
		if (current.equipment() == optimalGearUp || usableUp.contains(current.equipment())) {
		    Node dummy = new Node(up, 0, 0, 0, current, current.equipment());
		    if (!closedList.contains(dummy)) {
			if (openList.contains(dummy)) {
			    updateOpenScore(openList, dummy, current, 1);
			} else {
			    putNewOpenNode(openList, dummy, current, 1);
			}
		    }
		}
	    }

	}
	return targetNode.getTime();
    }

    // calculates geo index, erosion level and area type for all 4 cardinal
    // directions
    private void createCardinalMappings(Point e, Point s, Point w, Point n) {
	if (!caveMapping.containsKey(e))
	    createMapping(e);
	if (!caveMapping.containsKey(s))
	    createMapping(s);
	if (w.x >= 0 && !caveMapping.containsKey(w))
	    createMapping(w);
	if (n.y >= 0 && !caveMapping.containsKey(n))
	    createMapping(n);
    }

    // adds a new node to the openlist
    private void putNewOpenNode(PriorityQueue<Node> openList, Node dummy, Node current, int timeAmount) {
	int gScore = current.gScore + timeAmount;
	int hScore = getManhattanDistance(dummy.position, targetPosition);
	int elapsedTime = current.getTime() + timeAmount;

	openList.add(new Node(dummy.getPosition(), elapsedTime, hScore, gScore, current, dummy.equipment()));
    }

    // updates a node that is already present in the openlist
    private void updateOpenScore(PriorityQueue<Node> openList, Node nextNode, Node current, int timeAmount) {
	Iterator<Node> queueIterator = openList.iterator();

	// find the target node in the openlist
	while (queueIterator.hasNext()) {
	    Node currentNode;
	    if ((currentNode = queueIterator.next()).equals(nextNode)) {
		nextNode = currentNode;
		break;
	    }
	}

	int newGScore = current.gScore + timeAmount;
	int newTime = current.getTime() + timeAmount;

	//if this route is better, update the node
	if (newGScore >= nextNode.gScore) {
	    return;
	} else {
	    openList.remove(nextNode);
	    openList.add(
		    new Node(nextNode.position, newTime, nextNode.hScore, newGScore, current, nextNode.equipment()));
	}
    }

    // returns a set of gear that is usable on the current position
    private Set<Node.Equipment> getUsableEquipment(Point currentPosition) {
	char currentAreaType = caveMapping.get(currentPosition);
	Set<Node.Equipment> usableEquipment = new HashSet<Node.Equipment>();
	if (currentAreaType == ROCKY) {
	    usableEquipment.add(Node.Equipment.CLIMBING_GEAR);
	    usableEquipment.add(Node.Equipment.TORCH);
	} else if (currentAreaType == WET) {
	    usableEquipment.add(Node.Equipment.CLIMBING_GEAR);
	    usableEquipment.add(Node.Equipment.NOTHING);
	} else if (currentAreaType == NARROW) {
	    usableEquipment.add(Node.Equipment.TORCH);
	    usableEquipment.add(Node.Equipment.NOTHING);
	}
	return usableEquipment;
    }

    private char getGeoType(Point currentPoint) {
	int erosionLevel = erosionLevels.get(currentPoint);

	if (erosionLevel % 3 == 0)
	    return ROCKY;
	else if (erosionLevel % 3 == 1)
	    return WET;
	else
	    return NARROW;
    }

    // calculates geoindex, erosionlevels and cave type
    // and maps specific Points to them
    private void createMapping(Point current) {

	if (current.x == 0 || current.y == 0) {
	    int geoIndex = 0;
	    int erosionLevel = 0;
	    if (current.x == 0) {
		geoIndex = current.y * 48271;
	    } else if (current.y == 0) {
		geoIndex = current.x * 16807;
	    }
	    erosionLevel = (geoIndex + caveDepth) % 20183;
	    geologicalIndicies.putIfAbsent(current, geoIndex);
	    erosionLevels.putIfAbsent(current, erosionLevel);
	    char geoType = getGeoType(current);
	    caveMapping.putIfAbsent(current, geoType);
	} else {
	    Point p1 = new Point(current.x - 1, current.y);
	    Point p2 = new Point(current.x, current.y - 1);
	    if (!erosionLevels.containsKey(p1))
		createMapping(p1);
	    if (!erosionLevels.containsKey(p2))
		createMapping(p2);

	    int geoIndex = erosionLevels.get(p1) * erosionLevels.get(p2);
	    int erosionLevel = (geoIndex + caveDepth) % 20183;
	    geologicalIndicies.putIfAbsent(current, geoIndex);
	    erosionLevels.putIfAbsent(current, erosionLevel);
	    char geoType = getGeoType(current);
	    caveMapping.putIfAbsent(current, geoType);
	}
    }

    // calculates geologic index, erosion level and area type from x = 0 to
    // x = targetX, and y = 0 to y = targetY and saves them in the relevant data
    // structures
    private void createCaveMappings() {
	// create target and starting position mappings
	geologicalIndicies.put(new Point(0, 0), 0);
	geologicalIndicies.put(targetPosition, 0);
	erosionLevels.put(new Point(0, 0), ((geologicalIndicies.get(new Point(0, 0)) + caveDepth) % 20183));
	erosionLevels.put(targetPosition, ((geologicalIndicies.get(targetPosition) + caveDepth) % 20183));
	caveMapping.put(new Point(0, 0), getGeoType(new Point(0, 0)));
	caveMapping.put(targetPosition, getGeoType(targetPosition));

	for (int i = 0; i <= targetPosition.y; i++) {
	    for (int j = 0; j <= targetPosition.x; j++) {
		Point current = new Point(j, i);
		createMapping(current);
	    }
	}
    }

    @SuppressWarnings("unused")
    private void printCave() {
	for (int i = 0; i <= targetPosition.y; i++) {
	    for (int j = 0; j <= targetPosition.x; j++) {
		Point current = new Point(j, i);
		if (i == 0 && j == 0)
		    System.out.print("M");
		else if (current.equals(targetPosition))
		    System.out.print("T");
		else
		    System.out.print(caveMapping.get(current));
	    }
	    System.out.println();
	}
    }

    public static int getManhattanDistance(Point a, Point b) {
	return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static void main(String[] args) {
	// input values
	int targetX = 11;
	int targetY = 718;
	int depth = 11739;

	Day22 test = new Day22(targetX, targetY, depth);
	// int res1 = test.run();
	int res2 = test.run2();
	System.out.println(res2);
    }

    public static class Node {
	private enum Equipment {
	    TORCH, CLIMBING_GEAR, NOTHING
	};

	private Point position;
	private int elapsedTimeMinutes;
	private int fScore;
	private int hScore;
	private int gScore;
	private Node parent;
	private Equipment currentGear;

	public Node(Point pos, int currentTime, int hScore, int gScore, Node parent, Equipment gear) {
	    position = pos;
	    elapsedTimeMinutes = currentTime;
	    this.hScore = hScore;
	    this.gScore = gScore;
	    this.fScore = hScore + gScore;
	    this.parent = parent;
	    currentGear = gear;
	}

	public Point getPosition() {
	    return position;
	}

	public int getTime() {
	    return elapsedTimeMinutes;
	}

	public int score() {
	    return fScore;
	}

	public Equipment equipment() {
	    return currentGear;
	}

	@Override
	public boolean equals(Object o) {
	    if ((this == o))
		return true;
	    if (o == null)
		return false;
	    if (!(o instanceof Node))
		return false;

	    Node tmp = (Node) o;

	    if (this.position.equals(tmp.position) && this.currentGear == tmp.currentGear)
		return true;

	    return false;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(position.x, position.y, currentGear);
	}
    }
}
