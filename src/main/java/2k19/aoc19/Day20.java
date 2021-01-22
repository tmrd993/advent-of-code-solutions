package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import myutils19.Point2d;
import myutils19.StaticUtils;

public class Day20 {

    private List<String> rawData;
    private char[][] grid;

    public Day20(File inputFile) {
	rawData = StaticUtils.fileToStringList(inputFile);
	initGrid();
    }

    public int run1() {

	Point2d startingPos = getStartingPos();
	Point2d goalPos = getGoalPos();
	Map<Point2d, Point2d> teleportMap = teleportMapping();

	Queue<Node> queue = new LinkedList<>();
	queue.add(new Node(startingPos, 0, 0));

	Set<Point2d> visited = new HashSet<>();

	while (!queue.isEmpty()) {

	    Node current = queue.poll();
	    Point2d currentPos = current.pos;
	    visited.add(currentPos);

	    // arrived at goal
	    if (current.pos.equals(goalPos)) {
		return current.steps;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    // up
	    if (up.y() >= 0 && !visited.contains(up)) {

		// valid teleporter
		if (Character.isAlphabetic(grid[up.y()][up.x()]) && teleportMap.containsKey(up)) {
		    queue.add(new Node(teleportMap.get(up), current.steps + 1, 0));
		} else if (grid[up.y()][up.x()] == '.') {
		    queue.add(new Node(up, current.steps + 1, 0));
		}

	    }
	    // down
	    if (down.y() < grid.length && !visited.contains(down)) {
		// valid teleporter
		if (Character.isAlphabetic(grid[down.y()][down.x()]) && teleportMap.containsKey(down)) {
		    queue.add(new Node(teleportMap.get(down), current.steps + 1, 0));
		} else if (grid[down.y()][down.x()] == '.') {
		    queue.add(new Node(down, current.steps + 1, 0));
		}
	    }
	    // left
	    if (left.x() >= 0 && !visited.contains(left)) {
		// valid teleporter
		if (Character.isAlphabetic(grid[left.y()][left.x()]) && teleportMap.containsKey(left)) {
		    queue.add(new Node(teleportMap.get(left), current.steps + 1, 0));
		} else if (grid[left.y()][left.x()] == '.') {
		    queue.add(new Node(left, current.steps + 1, 0));
		}
	    }
	    // right
	    if (right.x() < grid[right.y()].length && !visited.contains(right)) {
		// valid teleporter
		if (Character.isAlphabetic(grid[right.y()][right.x()]) && teleportMap.containsKey(right)) {
		    queue.add(new Node(teleportMap.get(right), current.steps + 1, 0));
		} else if (grid[right.y()][right.x()] == '.') {
		    queue.add(new Node(right, current.steps + 1, 0));
		}
	    }
	}

	// no valid routes to target (this shouldn't happen with any valid input)
	return -1;
    }

    public int run2() {
	Map<Point2d, Point2d> teleportMap = teleportMapping();
	Map<Point2d, Point2d> outerDoors = outerDoors(teleportMap);
	Map<Point2d, Point2d> innerDoors = innerDoors(teleportMap);

	Point2d startingPos = getStartingPos();
	Point2d goalPos = getGoalPos();

	Node start = new Node(startingPos, 0, 0);
	Queue<Node> queue = new LinkedList<>();
	queue.add(start);

	List<Set<Point2d>> visitedLevels = new ArrayList<>();
	visitedLevels.add(new HashSet<>());

	while (!queue.isEmpty()) {

	    Node current = queue.poll();
	    Point2d currentPos = current.pos;
	    int currentLevel = current.level;

	    if (visitedLevels.size() < currentLevel + 1) {
		visitedLevels.add(new HashSet<>());
	    }

	    Set<Point2d> visited = visitedLevels.get(currentLevel);
	    visited.add(currentPos);

	    // arrived at destination
	    if (currentLevel == 0 && currentPos.equals(goalPos)) {
		return current.steps;
	    }

	    Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	    Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
	    Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	    Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());

	    // up
	    if (up.y() >= 0 && !visited.contains(up)) {

		// valid teleporter
		if (Character.isAlphabetic(grid[up.y()][up.x()])) {
		    if (outerDoors.containsKey(up) && currentLevel > 0) {
			queue.add(new Node(outerDoors.get(up), current.steps + 1, currentLevel - 1));
		    } else if (innerDoors.containsKey(up)) {
			queue.add(new Node(innerDoors.get(up), current.steps + 1, currentLevel + 1));
		    }
		} else if (grid[up.y()][up.x()] == '.') {
		    queue.add(new Node(up, current.steps + 1, currentLevel));
		}

	    }

	    if (down.y() >= 0 && !visited.contains(down)) {

		// valid teleporter
		if (Character.isAlphabetic(grid[down.y()][down.x()])) {
		    if (outerDoors.containsKey(down) && currentLevel > 0) {
			queue.add(new Node(outerDoors.get(down), current.steps + 1, currentLevel - 1));
		    } else if (innerDoors.containsKey(down)) {
			queue.add(new Node(innerDoors.get(down), current.steps + 1, currentLevel + 1));
		    }
		} else if (grid[down.y()][down.x()] == '.') {
		    queue.add(new Node(down, current.steps + 1, currentLevel));
		}

	    }

	    if (left.y() >= 0 && !visited.contains(left)) {

		// valid teleporter
		if (Character.isAlphabetic(grid[left.y()][left.x()])) {
		    if (outerDoors.containsKey(left) && currentLevel > 0) {
			queue.add(new Node(outerDoors.get(left), current.steps + 1, currentLevel - 1));
		    } else if (innerDoors.containsKey(left)) {
			queue.add(new Node(innerDoors.get(left), current.steps + 1, currentLevel + 1));
		    }
		} else if (grid[left.y()][left.x()] == '.') {
		    queue.add(new Node(left, current.steps + 1, currentLevel));
		}

	    }

	    if (right.y() >= 0 && !visited.contains(right)) {

		// valid teleporter
		if (Character.isAlphabetic(grid[right.y()][right.x()])) {
		    if (outerDoors.containsKey(right) && currentLevel > 0) {
			queue.add(new Node(outerDoors.get(right), current.steps + 1, currentLevel - 1));
		    } else if (innerDoors.containsKey(right)) {
			queue.add(new Node(innerDoors.get(right), current.steps + 1, currentLevel + 1));
		    }
		} else if (grid[right.y()][right.x()] == '.') {
		    queue.add(new Node(right, current.steps + 1, currentLevel));
		}

	    }

	}

	return -1;
    }

    private Map<Point2d, Point2d> outerDoors(Map<Point2d, Point2d> teleporterMap) {
	Map<Point2d, Point2d> outerDoors = new HashMap<>();

	int outerBorder = 1;

	for (int i = 0; i < grid[outerBorder].length; i++) {
	    Point2d upperPos = new Point2d(i, outerBorder);
	    if (teleporterMap.containsKey(upperPos)) {
		outerDoors.put(upperPos, teleporterMap.get(upperPos));
	    }

	    Point2d lowerPos = new Point2d(i, grid.length - 2);
	    if (teleporterMap.containsKey(lowerPos)) {
		outerDoors.put(lowerPos, teleporterMap.get(lowerPos));
	    }
	}

	for (int i = 0; i < grid[outerBorder].length; i++) {
	    Point2d leftPos = new Point2d(outerBorder, i);
	    // System.out.println(leftPos);
	    if (teleporterMap.containsKey(leftPos)) {
		outerDoors.put(leftPos, teleporterMap.get(leftPos));
	    }

	    Point2d rightPos = new Point2d(grid[outerBorder].length - 2, i);
	    if (teleporterMap.containsKey(rightPos)) {
		outerDoors.put(rightPos, teleporterMap.get(rightPos));
	    }
	}

	return outerDoors;

    }

    private Map<Point2d, Point2d> innerDoors(Map<Point2d, Point2d> teleporterMap) {
	Map<Point2d, Point2d> outerDoors = outerDoors(teleporterMap);

	return teleporterMap.entrySet().stream().filter(e -> !outerDoors.containsKey(e.getKey()))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // teleport from key to value
    private Map<Point2d, Point2d> teleportMapping() {
	Map<Point2d, Point2d> teleportMapping = new HashMap<>();

	Set<Point2d> processed = new HashSet<>();

	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		Point2d current = new Point2d(j, i);
		if (Character.isAlphabetic(grid[i][j]) && !processed.contains(current)) {
		    Point2d neighborTeleportPos = getNeighborTeleporterPos(current.x(), current.y());

		    processed.add(current);
		    processed.add(neighborTeleportPos);

		    // don't add start or end-teleporter
		    if (grid[current.y()][current.x()] == grid[neighborTeleportPos.y()][neighborTeleportPos.x()]) {
			continue;
		    }

		    Point2d possibleEntrance1 = getNearestOpenPassagePos(current);
		    Point2d possibleEntrance2 = possibleEntrance1 == null
			    ? getNearestOpenPassagePos(neighborTeleportPos)
			    : null;

		    Point2d entrance = possibleEntrance1 == null ? possibleEntrance2 : possibleEntrance1;
		    // character next to '.'
		    Point2d source = possibleEntrance1 == null ? neighborTeleportPos : current;
		    // character next to source
		    Point2d sourceNeighbor = source == neighborTeleportPos ? current : neighborTeleportPos;
		    String teleporterId = "";
		    if ((source.y() + 1 < grid.length && grid[source.y() + 1][source.x()] == '.')
			    || (source.x() + 1 < grid[source.y()].length && grid[source.y()][source.x() + 1] == '.')) {
			teleporterId = grid[sourceNeighbor.y()][sourceNeighbor.x()] + "" + grid[source.y()][source.x()];
		    } else {
			teleporterId = grid[source.y()][source.x()] + "" + grid[sourceNeighbor.y()][sourceNeighbor.x()];
		    }

		    for (int k = 0; k < grid.length; k++) {

			for (int l = 0; l < grid[k].length; l++) {
			    Point2d possibleTarget = new Point2d(l, k);

			    if (Character.isAlphabetic(grid[k][l]) && !processed.contains(possibleTarget)) {
				Point2d possibleEntrancePoint1 = getNearestOpenPassagePos(possibleTarget);
				Point2d targetNeighbor = getNeighborTeleporterPos(possibleTarget.x(),
					possibleTarget.y());
				Point2d possibleEntrancePoint2 = possibleEntrancePoint1 == null
					? getNearestOpenPassagePos(targetNeighbor)
					: null;

				Point2d dest = possibleEntrancePoint1 == null ? possibleEntrancePoint2
					: possibleEntrancePoint1;

				Point2d source2 = possibleEntrancePoint1 == null ? targetNeighbor : possibleTarget;
				Point2d source2Neighbor = source2 == targetNeighbor ? possibleTarget : targetNeighbor;

				String targetTeleporterId = "";
				if ((source2.y() + 1 < grid.length && grid[source2.y() + 1][source2.x()] == '.')
					|| (source2.x() + 1 < grid[source2.y()].length
						&& grid[source2.y()][source2.x() + 1] == '.')) {
				    targetTeleporterId = grid[source2Neighbor.y()][source2Neighbor.x()] + ""
					    + grid[source2.y()][source2.x()];
				} else {
				    targetTeleporterId = grid[source2.y()][source2.x()] + ""
					    + grid[source2Neighbor.y()][source2Neighbor.x()];
				}

				if (teleporterId.equals(targetTeleporterId)) {

				    teleportMapping.put(source, dest);
				    teleportMapping.put(source2, entrance);

				    processed.add(possibleTarget);
				    processed.add(targetNeighbor);
				}
			    }
			}
		    }
		}
	    }
	}

	return teleportMapping;
    }

    private Point2d getStartingPos() {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		// possible target
		Point2d teleporterNeighbor = isStartTeleporterPos(j, i);
		if (grid[i][j] == 'A' && teleporterNeighbor != null
			&& grid[teleporterNeighbor.y()][teleporterNeighbor.x()] == 'A') {
		    return getStartingPos(teleporterNeighbor, j, i);
		}
	    }
	}
	return null;
    }

    private Point2d getGoalPos() {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		// possible target
		Point2d teleporterNeighbor = isGoalTeleporterPos(j, i);
		if (grid[i][j] == 'Z' && teleporterNeighbor != null
			&& grid[teleporterNeighbor.y()][teleporterNeighbor.x()] == 'Z') {
		    return getStartingPos(teleporterNeighbor, j, i);
		}
	    }
	}
	return null;
    }

    private Point2d getStartingPos(Point2d neighbor, int x, int y) {
	Point2d possibleStartPos1 = getNearestOpenPassagePos(neighbor);
	Point2d possibleStartPos2 = getNearestOpenPassagePos(new Point2d(x, y));

	return possibleStartPos1 == null ? possibleStartPos2 : possibleStartPos1;
    }

    private Point2d getNearestOpenPassagePos(Point2d pos) {
	if (pos.x() - 1 >= 0 && grid[pos.y()][pos.x() - 1] == '.') {
	    return new Point2d(pos.x() - 1, pos.y());
	} else if (pos.x() + 1 < grid[pos.y()].length && grid[pos.y()][pos.x() + 1] == '.') {
	    return new Point2d(pos.x() + 1, pos.y());
	} else if (pos.y() - 1 >= 0 && grid[pos.y() - 1][pos.x()] == '.') {
	    return new Point2d(pos.x(), pos.y() - 1);
	} else if (pos.y() + 1 < grid.length && grid[pos.y() + 1][pos.x()] == '.') {
	    return new Point2d(pos.x(), pos.y() + 1);
	}
	// no open passages next to pos
	return null;
    }

    private Point2d getNeighborTeleporterPos(int x, int y) {
	// check all 4 directions for another teleport symbol
	if (x - 1 >= 0 && Character.isAlphabetic(grid[y][x - 1])) {
	    return new Point2d(x - 1, y);
	}
	if (x + 1 < grid[y].length && Character.isAlphabetic(grid[y][x + 1])) {
	    return new Point2d(x + 1, y);
	}
	if (y - 1 >= 0 && Character.isAlphabetic(grid[y - 1][x])) {
	    return new Point2d(x, y - 1);
	}
	if (y + 1 < grid.length && Character.isAlphabetic(grid[y + 1][x])) {
	    return new Point2d(x, y + 1);
	}

	return null;
    }

    private Point2d isStartTeleporterPos(int x, int y) {
	if (grid[y][x] != 'A') {
	    return null;
	}

	return getNeighborTeleporterPos(x, y);
    }

    private Point2d isGoalTeleporterPos(int x, int y) {
	if (grid[y][x] != 'Z') {
	    return null;
	}

	return getNeighborTeleporterPos(x, y);
    }

    private void initGrid() {
	grid = new char[rawData.size()][];
	for (int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    char[] row = new char[line.length()];
	    for (int j = 0; j < line.length(); j++) {
		row[j] = line.charAt(j);
	    }
	    grid[i] = row;
	}
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		System.out.print(grid[i][j]);
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) {
	Day20 test = new Day20(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 20\\InputFile.txt"));
	System.out.println(test.run2());
	
    }

    private static class Node {
	public int steps;
	public Point2d pos;
	public int level;

	public Node(Point2d pos, int steps, int level) {
	    this.pos = pos;
	    this.steps = steps;
	    this.level = level;
	}
    }

}
