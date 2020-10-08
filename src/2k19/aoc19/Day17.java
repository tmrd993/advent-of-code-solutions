package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;
import myutils19.Point2d;

public class Day17 {

    private List<Long> initialProgram;

    public Day17(File input) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(input);
    }

    public long run1() {
	List<List<Character>> grid = grid();
	long sum = 0;
	for (int row = 0; row < grid.size(); row++) {
	    for (int col = 0; col < grid.get(row).size(); col++) {
		List<Character> currentRow = grid.get(row);
		if (row > 0 && row < grid.size() - 1 && col > 0 && col < currentRow.size() - 1
			&& currentRow.get(col) == '#') {
		    char left = currentRow.get(col - 1);
		    char right = currentRow.get(col + 1);
		    char up = grid.get(row - 1).get(col);
		    char down = grid.get(row + 1).get(col);
		    if (currentRow.get(col) == left && left == right && right == up && up == down) {
			sum += row * col;
		    }
		}
	    }
	}
	return sum;
    }
    
    public int run2() {
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.memory().set(0, 2);
	
	Map<Point2d, Character> areaMap = areaMapping();
	String fullPath = fullPath(areaMap);
	List<String> subRoutines = getSubroutines(fullPath);
	String mainRoutine = mainRoutine(subRoutines, fullPath);
	
	long[] mainRoutineAscii = mainRoutine.chars().mapToLong(c -> (char) c).toArray();
	long[] funcA = subRoutines.get(0).chars().limit(subRoutines.get(0).length() - 1).mapToLong(c ->(char) c).toArray();
	long[] funcB = subRoutines.get(1).chars().limit(subRoutines.get(1).length() - 1).mapToLong(c ->(char) c).toArray();
	long[] funcC = subRoutines.get(2).chars().limit(subRoutines.get(2).length() - 1).mapToLong(c ->(char) c).toArray();
	
	computer.setInputValues(mainRoutineAscii);
	computer.setInputValues(10);
	computer.setInputValues(funcA);
	computer.setInputValues(10);
	computer.setInputValues(funcB);
	computer.setInputValues(10);
	computer.setInputValues(funcC);
	computer.setInputValues(10);
	computer.setInputValues((int) 'n');
	computer.setInputValues(10);
	
	computer.run();
	
	Queue<Long> output = computer.outputValues();
	// only the last value is important, the ones preceding it are just for visualization
	while(output.size() > 1) {
	    output.poll();
	}
	
	return output.poll().intValue();
    }

    private String fullPath(Map<Point2d, Character> areaMapping) {
	StringBuilder path = new StringBuilder();
	Point2d currentPos = areaMapping.entrySet().stream()
		.filter(s -> s.getValue() == '^' || s.getValue() == '>' || s.getValue() == '<' || s.getValue() == 'v')
		.findFirst().get().getKey();
	Set<Point2d> scaffoldPoints = areaMapping.entrySet().stream().filter(s -> s.getValue() == '#'
		|| s.getValue() == '^' || s.getValue() == 'v' || s.getValue() == '>' || s.getValue() == '<')
		.map(s -> s.getKey()).collect(Collectors.toSet());

	Set<Point2d> visitedPoints = new HashSet<>();

	Direction dir = Direction.of(areaMapping.get(currentPos));
	DirectionData dirData = new DirectionData(dir, ' ');
	Point2d nextPos = currentPos.add(directionVector(dir.directionChar()));
	if (!areaMapping.containsKey(nextPos) || areaMapping.get(nextPos) != '#') {
	    dirData = changeDirection(areaMapping, dirData.direction().directionChar(), currentPos);
	    path.append(dirData.lastTurn() + ",");
	}

	int stepsTaken = 0;
	while (!visitedPoints.equals(scaffoldPoints)) {
	    visitedPoints.add(currentPos);
	    char dirChar = dirData.direction().directionChar();
	    nextPos = nextPoint(dirChar, currentPos);
	    if (!areaMapping.containsKey(nextPos) || areaMapping.get(nextPos) == '.') {
		dirData = changeDirection(areaMapping, dirChar, currentPos);
		path.append(stepsTaken + ",");
		path.append(dirData.lastTurn() + ",");
		stepsTaken = 0;
	    } else {
		stepsTaken++;
		currentPos = nextPos;
	    }
	}
	return path.toString().substring(0, path.length() - 2);
    }

    private Point2d nextPoint(char dirChar, Point2d currentPoint) {
	if (dirChar == '^') {
	    return new Point2d(currentPoint.x(), currentPoint.y() - 1);
	} else if (dirChar == 'v') {
	    return new Point2d(currentPoint.x(), currentPoint.y() + 1);
	} else if (dirChar == '>') {
	    return new Point2d(currentPoint.x() + 1, currentPoint.y());
	} else if (dirChar == '<') {
	    return new Point2d(currentPoint.x() - 1, currentPoint.y());
	}

	throw new IllegalArgumentException(dirChar + " is not a valid direction");
    }

    private Map<Point2d, Character> areaMapping() {
	Map<Point2d, Character> areaMapping = new HashMap<>();
	List<List<Character>> grid = grid();

	for (int row = 0; row < grid.size(); row++) {
	    for (int col = 0; col < grid.get(row).size(); col++) {
		areaMapping.put(new Point2d(col, row), grid.get(row).get(col));
	    }
	}

	return areaMapping;
    }

    private DirectionData changeDirection(Map<Point2d, Character> areaMap, char direction, Point2d currentPos) {
	Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
	Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());
	Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
	Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);

	Day17.Direction nextDirection = Direction.of(direction);
	char lastTurn = ' ';

	if (direction == '^') {
	    if (areaMap.containsKey(left) && areaMap.get(left) == '#') {
		nextDirection = Day17.Direction.LEFT;
		lastTurn = 'L';
	    } else if (areaMap.containsKey(right) && areaMap.get(right) == '#') {
		nextDirection = Day17.Direction.RIGHT;
		lastTurn = 'R';
	    }
	} else if (direction == 'v') {
	    if (areaMap.containsKey(left) && areaMap.get(left) == '#') {
		nextDirection = Day17.Direction.LEFT;
		lastTurn = 'R';
	    } else if (areaMap.containsKey(right) && areaMap.get(right) == '#') {
		nextDirection = Day17.Direction.RIGHT;
		lastTurn = 'L';
	    }
	} else if (direction == '<') {
	    if (areaMap.containsKey(up) && areaMap.get(up) == '#') {
		nextDirection = Day17.Direction.UP;
		lastTurn = 'R';
	    } else if (areaMap.containsKey(down) && areaMap.get(down) == '#') {
		nextDirection = Day17.Direction.DOWN;
		lastTurn = 'L';
	    }
	} else if (direction == '>') {
	    if (areaMap.containsKey(up) && areaMap.get(up) == '#') {
		nextDirection = Day17.Direction.UP;
		lastTurn = 'L';
	    } else if (areaMap.containsKey(down) && areaMap.get(down) == '#') {
		nextDirection = Day17.Direction.DOWN;
		lastTurn = 'R';
	    }
	}

	return new DirectionData(nextDirection, lastTurn);
    }

    private Point2d directionVector(char direction) {
	switch (direction) {
	case '^':
	    return new Point2d(0, -1);
	case 'v':
	    return new Point2d(0, 1);
	case '<':
	    return new Point2d(-1, 0);
	case '>':
	    return new Point2d(1, 0);
	}
	throw new IllegalArgumentException(direction + " is not a valid direction");
    }

    private List<List<Character>> grid() {
	List<List<Character>> grid = new ArrayList<>();
	grid.add(new ArrayList<>());
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.run();
	Queue<Long> output = computer.outputValues();

	int row = 0;
	while (!output.isEmpty()) {
	    char currentChar = (char) output.poll().intValue();
	    if (currentChar == 10) {
		row++;
		grid.add(new ArrayList<>());
	    } else {
		grid.get(row).add(currentChar);
	    }
	}
	grid.remove(grid.size() - 1);
	grid.remove(grid.size() - 1);

	computer.run();

	return grid;
    }

    private List<String> getSubroutines(String fullPath) {

	List<String> splitCommands = splitCommands(fullPath);
	String repPath = fullPath.replace(fullPath, StaticUtils.repeat("X", fullPath.length()));

	// go through every possibility
	for (int i = 0; i < splitCommands.size(); i++) {
	    List<String> a = splitCommands.subList(0, i + 1);
	    for (int j = i + 1; j < splitCommands.size(); j++) {
		List<String> b = splitCommands.subList(i + 1, j + 1);
		for (int k = j + 1; k < splitCommands.size(); k++) {

		    for (int l = k; l < splitCommands.size(); l++) {
			List<String> c = splitCommands.subList(k + 1, l + 1);
			if (isValidLengthForRoutine(a, b, c)) {
			    String path = fullPath;
			    String pathA = routinePath(a);
			    String pathB = routinePath(b);
			    String pathC = routinePath(c);

			    path = path.replace(pathA, StaticUtils.repeat("X", pathA.length()));
			    path = path.replace(pathB, StaticUtils.repeat("X", pathB.length()));
			    path = path.replace(pathC, StaticUtils.repeat("X", pathC.length()));

			    if (path.equals(repPath)) {
				return List.of(pathA, pathB, pathC);
			    }
			}
		    }
		}
	    }
	}
	// return an empty list if no valid subroutines can be created
	return new ArrayList<>();
    }

    private String routinePath(List<String> subRoutine) {
	StringBuilder sb = new StringBuilder();
	for (String command : subRoutine) {
	    sb.append(command + ",");
	}
	return sb.toString();
    }

    private boolean isValidLengthForRoutine(List<String> a, List<String> b, List<String> c) {
	int sumA = a.stream().mapToInt(s -> s.length()).sum() + a.size() - 1;
	int sumB = b.stream().mapToInt(s -> s.length()).sum() + b.size() - 1;
	int sumC = c.stream().mapToInt(s -> s.length()).sum() + c.size() - 1;

	return sumA < 19 && sumB < 19 && sumC < 19;
    }

    private List<String> splitCommands(String command) {
	List<String> commands = new ArrayList<>();
	String[] splitCommands = command.split(",");
	for (int i = 0; i < splitCommands.length - 1; i += 2) {
	    commands.add(splitCommands[i] + "," + splitCommands[i + 1]);
	}
	return commands;
    }
    
    private String mainRoutine(List<String> subRoutines, String fullPath) {
	StringBuilder mainRoutine = new StringBuilder();
	
	int fromIndex = 0;
	for(int i = 0; i < fullPath.length(); i++) {
	    String currentRoutine = fullPath.substring(fromIndex, i + 1);
	    if(subRoutines.contains(currentRoutine)) {
		mainRoutine.append((char) (subRoutines.indexOf(currentRoutine) + 65) + ",");
		fromIndex = i + 1;
	    }
	}
	return mainRoutine.deleteCharAt(mainRoutine.length() - 1).toString();
    }  

    public static void main(String[] args) {
	Day17 test = new Day17(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 17\\InputFile.txt"));
	System.out.println(test.run2());
    }

    private static class DirectionData {
	private final char lastTurn;
	private Direction direction;

	public DirectionData(Direction dir, char lastTurn) {
	    this.direction = dir;
	    this.lastTurn = lastTurn;
	}

	public char lastTurn() {
	    return lastTurn;
	}

	public Direction direction() {
	    return direction;
	}
    }

    private enum Direction {
	UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

	private final char direction;

	private static final Map<Character, Direction> directions = new HashMap<>(values().length, 1);

	static {
	    for (Direction direction : values())
		directions.put(direction.direction, direction);
	}

	Direction(char direction) {
	    this.direction = direction;
	}

	public char directionChar() {
	    return direction;
	}

	public static Direction of(char c) {
	    Direction dir = directions.get(c);
	    if (dir == null) {
		throw new IllegalArgumentException(c + " not mapped to any direction");
	    }
	    return dir;
	}
    }
}
