package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day15 {

    int gridHeight;
    int gridWidth;
    private char[][] grid;
    private File inputFile;

    public Day15(File input) throws IOException {
	gridHeight = getGridHeight(input);
	gridWidth = getGridWidth(input);
	grid = initiateGrid(input);
	inputFile = input;
    }

    private char[][] initiateGrid(File input) throws IOException {
	char[][] grid = new char[gridHeight][gridWidth];

	Scanner sc = new Scanner(input);

	for (int i = 0; i < gridHeight; i++) {
	    String currentLine = sc.nextLine();
	    for (int j = 0; j < gridWidth; j++) {
		grid[i][j] = currentLine.charAt(j);
	    }
	}
	sc.close();
	return grid;
    }

    public static int getGridHeight(File input) throws IOException {
	int height = 0;
	Scanner sc = new Scanner(input);
	while (sc.hasNextLine()) {
	    sc.nextLine();
	    height++;
	}
	sc.close();
	return height;
    }

    public static int getGridWidth(File input) throws IOException {
	int width = 0;
	Scanner sc = new Scanner(input);
	String line = sc.nextLine();
	width = line.length();
	sc.close();
	return width;
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	for (int i = 0; i < gridHeight; i++) {
	    for (int j = 0; j < gridWidth; j++) {
		System.out.print(grid[i][j]);
	    }
	    System.out.println();
	}
    }

    // generates (creates unit objects) all units on the grid and returns a list
    // containing all units
    // (goblins and elves)
    private List<Unit> getAllUnits(int elfAttackPower, int goblinAttackPower) {
	List<Unit> units = new ArrayList<Unit>();
	for (int i = 0; i < gridHeight; i++) {
	    for (int j = 0; j < gridWidth; j++) {
		if (grid[i][j] == 'G') {
		    units.add(new Unit(Unit.Type.Goblin, new Point(j, i), goblinAttackPower));
		} else if (grid[i][j] == 'E') {
		    units.add(new Unit(Unit.Type.Elf, new Point(j, i), elfAttackPower));
		}
	    }
	}
	return units;
    }

    // part 2
    // could be faster if we used binary search instead of looping through every
    // possibility
    // the bfs is pretty fast though so it's not neccessary. might change it in
    // the future
    public int run2() throws IOException {
	int result = 0;
	int minAttack = 4;
	int maxAttack = 200;

	// number of elves that need to survive combat
	long elfCountMax = getAllUnits(1, 1).stream().filter(u -> u.type.equals(Unit.Type.Elf)).count();

	for (int i = minAttack; i <= maxAttack; i++) {
	    grid = initiateGrid(inputFile);
	    int elfAttackPower = i;
	    int goblinAttackPower = 3;
	    List<Unit> units = getAllUnits(elfAttackPower, goblinAttackPower);

	    List<Unit> activeGoblins = new ArrayList<Unit>();
	    List<Unit> activeElves = new ArrayList<Unit>();

	    for (Unit unit : units) {
		if (unit.type.equals(Unit.Type.Elf))
		    activeElves.add(unit);
		else
		    activeGoblins.add(unit);
	    }

	    int finalRound = runBattle(units, activeElves, activeGoblins);

	    if (activeElves.size() == elfCountMax) {
		int hitpointSum = 0;
		for (Unit elf : activeElves) {
		    hitpointSum += elf.hitpoints;
		}
		result = hitpointSum * finalRound;
		break;
	    }
	}
	return result;
    }

    // part 1
    public int run() {
	int elfAttackPower = 3;
	int goblinAttackPower = 3;
	List<Unit> units = getAllUnits(elfAttackPower, goblinAttackPower);
	List<Unit> activeGoblins = new ArrayList<Unit>();
	List<Unit> activeElves = new ArrayList<Unit>();

	for (Unit unit : units) {
	    if (unit.type.equals(Unit.Type.Elf))
		activeElves.add(unit);
	    else
		activeGoblins.add(unit);
	}

	int finalRound = runBattle(units, activeElves, activeGoblins);

	Unit.Type winningUnit = activeElves.size() > activeGoblins.size() ? Unit.Type.Elf : Unit.Type.Goblin;

	// calculate the battle outcome
	int hitpointSum = 0;
	for (Unit unit : units) {
	    if (unit.isAlive()) {
		hitpointSum += unit.hitpoints;
	    }
	}

	System.out.println(winningUnit + " wins");
	return hitpointSum * finalRound;
    }

    private int runBattle(List<Unit> units, List<Unit> activeElves, List<Unit> activeGoblins) {
	int roundCounter = 0;
	while (!activeGoblins.isEmpty() && !activeElves.isEmpty()) {

	    units.sort(new UnitComparatorByReadingOrder());
	    for (Unit unit : units) {
		if (unit.isAlive()) {
		    // check if enemies are available
		    if (!enemiesAvailable(unit.type, units)) {
			roundCounter--;
			break;
		    }

		    // if no enemy is next to current unit
		    if (getLowestTarget(unit, units) == null) {

			Set<Point> targets = possibleTargets(unit);

			Point target = bfs(unit.position, targets);

			// enemies unreachable
			if (target == null)
			    continue;

			grid[unit.position.y][unit.position.x] = '.';
			unit.position = target;

			char unitChar = unit.type == Unit.Type.Elf ? 'E' : 'G';
			grid[unit.position.y][unit.position.x] = unitChar;
			// printGrid();

		    }

		    // check if target is next to unit after moving
		    Unit lowestTarget = getLowestTarget(unit, units);
		    if (lowestTarget != null) {
			unit.attack(lowestTarget);

			if (lowestTarget.hitpoints <= 0) {
			    lowestTarget.setLifeStatusDead();
			    if (lowestTarget.type.equals(Unit.Type.Elf)) {
				activeElves.remove(lowestTarget);
			    } else {
				activeGoblins.remove(lowestTarget);
			    }

			    grid[lowestTarget.position.y][lowestTarget.position.x] = '.';
			}
		    }
		}
	    }
	    roundCounter++;
	}
	return roundCounter;
    }

    // returns true if at least one enemy is alive regardless if it's reachable
    // or not
    private boolean enemiesAvailable(Unit.Type type, List<Unit> units) {
	for (Unit unit : units) {
	    if (unit.isAlive() && !unit.type.equals(type)) {
		return true;
	    }
	}
	return false;
    }

    // returns the target with the lowest hitpoints in the vicinity (up, down,
    // left, right) of the unit
    private Unit getLowestTarget(Unit unit, List<Unit> allUnits) {

	Set<Point> targetPositions = targetPositionsInVicinity(unit);

	// return null if no targets are nearby
	if (targetPositions.isEmpty())
	    return null;

	// get specific targets at target positions
	List<Unit> targetsInVicinity = new ArrayList<Unit>();
	for (Unit currentUnit : allUnits) {
	    if (targetPositions.contains(currentUnit.position) && currentUnit.isAlive()) {
		targetsInVicinity.add(currentUnit);
	    }
	}

	// sort all targets in vicinity by hitpoints
	targetsInVicinity.sort(new UnitComparatorByHitpoints());

	// store tied units (same hitpoints). store the unit with the lowest
	// hitpoints if there
	// aren't any tied units
	List<Unit> tiedUnits = new ArrayList<Unit>();
	for (int i = 0; i < targetsInVicinity.size(); i++) {
	    tiedUnits.add(targetsInVicinity.get(i));
	    if (i < targetsInVicinity.size() - 1
		    && targetsInVicinity.get(i).hitpoints != targetsInVicinity.get(i + 1).hitpoints) {
		break;
	    }
	}

	// sorts units with tied hitpoints by reading order
	tiedUnits.sort(new UnitComparatorByReadingOrder());

	return tiedUnits.isEmpty() ? null : tiedUnits.get(0);
    }

    // returns a set of units that are in attack range
    private Set<Point> targetPositionsInVicinity(Unit unit) {
	Set<Point> targetPositions = new HashSet<Point>();
	char targetTypeIdentifier = unit.type == Unit.Type.Elf ? 'G' : 'E';

	// add all valid attack targets in vicinity to a set
	if (grid[unit.position.y - 1][unit.position.x] == targetTypeIdentifier) {
	    targetPositions.add(new Point(unit.position.x, unit.position.y - 1));
	}
	if (grid[unit.position.y][unit.position.x - 1] == targetTypeIdentifier) {
	    targetPositions.add(new Point(unit.position.x - 1, unit.position.y));
	}
	if (grid[unit.position.y][unit.position.x + 1] == targetTypeIdentifier) {
	    targetPositions.add(new Point(unit.position.x + 1, unit.position.y));
	}
	if (grid[unit.position.y + 1][unit.position.x] == targetTypeIdentifier) {
	    targetPositions.add(new Point(unit.position.x, unit.position.y + 1));
	}
	return targetPositions;
    }

    // starts a breadth first search to find the shortest path to the nearest
    // target
    // returns the optimal step (the next point the unit should be at) towards
    // the nearest target on the shortest path in reading order
    // returns null if no targets are reachable or available
    public Point bfs(Point startingPos, Set<Point> possibleTargets) {
	if (possibleTargets.isEmpty())
	    return null;

	Queue<Node> nodes = new LinkedList<Node>();
	nodes.offer(new Node(startingPos, null, 0));
	Set<Point> examinedPoints = new HashSet<Point>();

	List<Node> optimalNodes = new ArrayList<Node>();

	while (!nodes.isEmpty()) {

	    Node current = nodes.poll();

	    examinedPoints.add(current.point);

	    // stop searching if the distance from the starting point is higher
	    // than a target thats already been found
	    if (optimalNodes.size() > 0 && current.distanceFromRoot >= optimalNodes.get(0).distanceFromRoot + 2) {
		break;
	    }

	    // add all relevant nodes in the vicinity (up, left, right, down) to
	    // the queue (the way the grid is set-up makes it impossible to be
	    // out of bounds here)
	    Point up = new Point(current.point.x, current.point.y - 1);
	    Point left = new Point(current.point.x - 1, current.point.y);
	    Point right = new Point(current.point.x + 1, current.point.y);
	    Point down = new Point(current.point.x, current.point.y + 1);

	    if (grid[current.point.y - 1][current.point.x] == '.' && !examinedPoints.contains(up)) {
		if (possibleTargets.contains(up)) {
		    if (optimalNodes.size() == 0
			    || optimalNodes.get(0).distanceFromRoot == current.distanceFromRoot + 1) {
			optimalNodes.add(new Node(up, current, current.distanceFromRoot + 1));
		    }
		}
		Node nUp = new Node(up, current, current.distanceFromRoot + 1);
		if (!nodes.contains(nUp))
		    nodes.offer(nUp);
	    }
	    if (grid[current.point.y][current.point.x - 1] == '.' && !examinedPoints.contains(left)) {
		if (possibleTargets.contains(left)) {
		    if (optimalNodes.size() == 0
			    || optimalNodes.get(0).distanceFromRoot == current.distanceFromRoot + 1) {
			optimalNodes.add(new Node(left, current, current.distanceFromRoot + 1));
		    }

		}
		Node nLeft = new Node(left, current, current.distanceFromRoot + 1);
		if (!nodes.contains(nLeft))
		    nodes.offer(nLeft);
	    }
	    if (grid[current.point.y][current.point.x + 1] == '.' && !examinedPoints.contains(right)) {
		if (possibleTargets.contains(right)) {
		    if (optimalNodes.size() == 0
			    || optimalNodes.get(0).distanceFromRoot == current.distanceFromRoot + 1) {
			optimalNodes.add(new Node(right, current, current.distanceFromRoot + 1));
		    }

		}

		Node nRight = new Node(right, current, current.distanceFromRoot + 1);
		if (!nodes.contains(nRight))
		    nodes.offer(nRight);
	    }
	    if (grid[current.point.y + 1][current.point.x] == '.' && !examinedPoints.contains(down)) {
		if (possibleTargets.contains(down)) {
		    if (optimalNodes.size() == 0
			    || optimalNodes.get(0).distanceFromRoot == current.distanceFromRoot + 1) {
			optimalNodes.add(new Node(down, current, current.distanceFromRoot + 1));
		    }

		}
		Node nDown = new Node(down, current, current.distanceFromRoot + 1);
		if (!nodes.contains(nDown))
		    nodes.offer(nDown);
	    }
	}

	// no target found
	if (optimalNodes.size() == 0) {
	    return null;
	}

	optimalNodes.sort(new NodeComparatorByReadingOrder());

	// search for the next optimal step (node where parent equals starting
	// point)
	Node tmp = optimalNodes.get(0);

	while (!tmp.parent.point.equals(startingPos)) {
	    tmp = tmp.parent;
	}

	return tmp.point;
    }

    // returns a set of available positions next to enemy units
    public Set<Point> possibleTargets(Unit unit) {
	char targetUnits = unit.type == Unit.Type.Elf ? 'G' : 'E';
	// stores available positions next to all enemy units
	Set<Point> targetPositions = new HashSet<Point>();

	for (int i = 0; i < gridHeight; i++) {
	    for (int j = 0; j < gridWidth; j++) {
		if (grid[i][j] == targetUnits) {
		    if (grid[i - 1][j] == '.')
			targetPositions.add(new Point(j, i - 1));
		    if (grid[i + 1][j] == '.')
			targetPositions.add(new Point(j, i + 1));
		    if (grid[i][j - 1] == '.')
			targetPositions.add(new Point(j - 1, i));
		    if (grid[i][j + 1] == '.')
			targetPositions.add(new Point(j + 1, i));
		}
	    }
	}
	return targetPositions;
    }

    public static class Point {
	private final int x;
	private final int y;

	public Point(int x, int y) {
	    this.x = x;
	    this.y = y;
	}

	@Override
	public String toString() {
	    return "[" + x + ", " + y + "]";
	}

	@Override
	public boolean equals(Object p) {
	    if (this == p)
		return true;
	    if (p == null)
		return false;
	    if (!(p instanceof Point))
		return false;

	    Point point = (Point) p;
	    return this.x == point.x && this.y == point.y;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(x, y);
	}
    }

    public static class Unit {
	enum Type {
	    Elf, Goblin;
	}

	private Type type;
	private int hitpoints;
	private int attackPower;
	private Point position;
	private boolean lifeStatus;

	public Unit(Type type, Point position, int attackPower) {
	    this.type = type;
	    this.position = position;
	    hitpoints = 200;
	    this.attackPower = attackPower;
	    lifeStatus = true;
	}

	public void attack(Unit opponent) {
	    opponent.hitpoints -= this.attackPower;
	}

	public int getHitpoints() {
	    return hitpoints;
	}

	public Point getPosition() {
	    return position;
	}

	public Type getType() {
	    return type;
	}

	public void setLifeStatusDead() {
	    lifeStatus = false;
	}

	public boolean isAlive() {
	    return lifeStatus;
	}

	public void setPosition(int x, int y) {
	    this.position = new Point(x, y);
	}

	public void setPosition(Point p) {
	    position = p;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null) {
		return false;
	    }
	    if (!(o instanceof Unit)) {
		return false;
	    }

	    Unit tmp = (Unit) o;
	    if (this.type == tmp.type && this.position.x == tmp.position.x && this.position.y == tmp.position.y
		    && this.hitpoints == tmp.hitpoints) {
		return true;
	    }
	    return false;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(type, position.x, position.y, hitpoints);
	}
    }

    public static class Node {
	private final Point point;
	private final Node parent;
	private final int distanceFromRoot;

	public Node(Point point, Node parent, int distanceFromRoot) {
	    this.point = point;
	    this.parent = parent;
	    this.distanceFromRoot = distanceFromRoot;
	}

	@Override
	public boolean equals(Object node) {
	    if (this == node)
		return true;
	    if (node == null)
		return false;
	    if (!(node instanceof Node))
		return false;

	    Node tmp = (Node) node;
	    if (this.point.x == tmp.point.x && this.point.y == tmp.point.y) {
		return true;
	    }
	    return false;
	}

	@Override
	public int hashCode() {
	    return (point.x + 37 * point.y) & 0x7FFFFFFF;
	}
    }

    // sort the units in reading order (top to bottom, left to right)
    public static class UnitComparatorByReadingOrder implements Comparator<Unit> {
	@Override
	public int compare(Unit unit1, Unit unit2) {
	    if (unit1.position.y == unit2.position.y) {
		if (unit1.position.x == unit2.position.x)
		    return 0;
		if (unit1.position.x < unit2.position.x)
		    return -1;
		return 1;
	    }

	    if (unit1.position.y < unit2.position.y)
		return -1;
	    return 1;
	}
    }

    public static class NodeComparatorByReadingOrder implements Comparator<Node> {

	@Override
	public int compare(Node node1, Node node2) {
	    if (node1.point.y == node2.point.y) {
		if (node1.point.x == node2.point.x)
		    return 0;
		if (node1.point.x < node2.point.x)
		    return -1;
		return 1;
	    }
	    if (node1.point.y < node2.point.y)
		return -1;
	    return 1;
	}
    }

    // sort by hitpoints, ascending
    public static class UnitComparatorByHitpoints implements Comparator<Unit> {
	@Override
	public int compare(Unit unit1, Unit unit2) {
	    return unit1.hitpoints - unit2.hitpoints;
	}
    }

    public static void main(String[] args) throws IOException {

	Day15 test = new Day15(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 15\\InputFile.txt"));
	// test.printGrid();
	int result = test.run2();
	// int result = test.run();
	System.out.println(result);
    }
}
