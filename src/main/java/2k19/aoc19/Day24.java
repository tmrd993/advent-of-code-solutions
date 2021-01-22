package aoc19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import myutils19.IncomparablePair;
import myutils19.Point2d;

public class Day24 {

    private final int minsToRun = 200;
    private final int gridDimensions = 5;
    private final char[][] input = new char[][] { { '.', '#', '.', '.', '#' }, { '#', '.', '.', '#', '#' },
	    { '#', '#', '.', '.', '#' }, { '#', '#', '.', '#', '#' }, { '#', '.', '.', '#', '#' } };

    public long run1() {

	Set<String> states = new HashSet<>();
	char[][] stateBefore = new char[input.length][input[0].length];
	for (int i = 0; i < input.length; i++) {
	    for (int j = 0; j < input[i].length; j++) {
		stateBefore[i][j] = input[i][j];
	    }
	}

	char[][] state = new char[input.length][input[0].length];

	while (!states.contains(Arrays.deepToString(stateBefore))) {
	    states.add(Arrays.deepToString(stateBefore));
	    state = new char[input.length][input[0].length];
	    for (int i = 0; i < stateBefore.length; i++) {
		for (int j = 0; j < stateBefore[i].length; j++) {
		    char currentSpace = stateBefore[i][j];
		    int bugCount = bugCount(j, i, stateBefore);
		    if (currentSpace == '#' && bugCount != 1) {
			state[i][j] = '.';
		    } else if (currentSpace == '.' && (bugCount == 1 || bugCount == 2)) {
			state[i][j] = '#';
		    } else {
			state[i][j] = currentSpace;
		    }
		}
	    }

	    stateBefore = state;
	}

	int pow = 0;
	long count = 0;
	for (int i = 0; i < stateBefore.length; i++) {
	    for (int j = 0; j < stateBefore[i].length; j++) {
		if (stateBefore[i][j] == '#') {
		    count += Math.pow(2, pow);
		}
		pow++;
	    }
	}

	return count;
    }

    public int run2() {

	char[][] levelZero = new char[input.length][input[0].length];
	for (int i = 0; i < input.length; i++) {
	    for (int j = 0; j < input[i].length; j++) {
		levelZero[i][j] = input[i][j];
	    }
	}

	levelZero[2][2] = '?';

	Map<Integer, char[][]> levelMapping = new HashMap<>();
	levelMapping.put(0, levelZero);

	// add bug positions
	Set<IncomparablePair<Integer, Point2d>> bugPositions = bugPositions(levelMapping);
	// also add their neighbors, we only need to check neighbors of bugs, anything
	// else has no chance of becoming a bug anyways
	addNeighborPositions(bugPositions, levelMapping);

	for (int i = 0; i < minsToRun; i++) {
	    Set<IncomparablePair<Integer, Point2d>> toRemove = new HashSet<>();
	    Set<IncomparablePair<Integer, Point2d>> toAdd = new HashSet<>();

	    for (IncomparablePair<Integer, Point2d> e : bugPositions) {
		int x = e.v().x();
		int y = e.v().y();
		int level = e.k();

		int bugCount = bugCount(x, y, level, levelMapping);
		if (levelMapping.get(level)[y][x] == '#' && bugCount != 1) {
		    toRemove.add(e);
		} else if (levelMapping.get(level)[y][x] == '.') {
		    toRemove.add(e);
		    if (bugCount == 1 || bugCount == 2) {
			toAdd.add(e);
		    }
		}

	    }

	    // update the grids
	    toRemove.stream().forEach(e -> {
		int level = e.k();
		int x = e.v().x();
		int y = e.v().y();
		levelMapping.get(level)[y][x] = '.';
	    });

	    toAdd.stream().forEach(e -> {
		int level = e.k();
		int x = e.v().x();
		int y = e.v().y();
		levelMapping.get(level)[y][x] = '#';
	    });

	    bugPositions.removeAll(toRemove);
	    bugPositions.addAll(toAdd);

	    addNeighborPositions(bugPositions, levelMapping);

	}

	return (int) levelMapping.entrySet().stream().flatMap(e -> Arrays.stream(e.getValue()))
		.flatMap(e -> new String(e).chars().mapToObj(c -> (char) c)).filter(c -> c == '#').count();
    }

    // brute forced solution that manually checks every single possible position.
    // might refactor this later if I get the chance
    public void addNeighborPositions(Set<IncomparablePair<Integer, Point2d>> bugPositions,
	    Map<Integer, char[][]> levelMapping) {
	Set<IncomparablePair<Integer, Point2d>> bugPositionsTmp = new HashSet<>();
	for (IncomparablePair<Integer, Point2d> e : bugPositions) {
	    Point2d currentPoint = e.v();
	    int level = e.k();
	    levelMapping.putIfAbsent(level + 1, emptyGrid());
	    levelMapping.putIfAbsent(level - 1, emptyGrid());

	    int x = currentPoint.x();
	    int y = currentPoint.y();

	    if (x == 0) {
		// left
		bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(1, 2)));
		// right
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		if (y == 0) {
		    // up
		    bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(2, 1)));
		    // down
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		} else if (y == gridDimensions - 1) {
		    // up
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    // down
		    bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(2, 3)));
		} else {
		    // up
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    // down
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		}
	    } else if (y == 0) {
		// up
		bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(2, 1)));
		// down
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		// left
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		if (x == gridDimensions - 1) {
		    // right
		    bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(3, 2)));
		} else {
		    // right
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		}
	    } else if (x == gridDimensions - 1) {
		// up
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		// right
		bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(3, 2)));
		// left
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		if (y == gridDimensions - 1) {
		    // down
		    bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(2, 3)));
		} else {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		}
	    } else if (y == gridDimensions - 1) {
		// left
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		// up
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		// down
		bugPositionsTmp.add(new IncomparablePair<>(level - 1, new Point2d(2, 3)));
		// right
		bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
	    } else {
		// neighbors of inner grid
		if (x == 2 && y == 1) {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		    // check inner grid below
		    for (int i = 0; i < gridDimensions; i++) {
			bugPositionsTmp.add(new IncomparablePair<>(level + 1, new Point2d(i, 0)));
		    }
		} else if (x == 2 && y == 3) {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		    // check inner grid above
		    for (int i = 0; i < gridDimensions; i++) {
			bugPositionsTmp.add(new IncomparablePair<>(level + 1, new Point2d(i, gridDimensions - 1)));
		    }
		} else if (x == 1 && y == 2) {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    // check inner grid to the right
		    for (int i = 0; i < gridDimensions; i++) {
			bugPositionsTmp.add(new IncomparablePair<>(level + 1, new Point2d(0, i)));
		    }
		} else if (x == 3 && y == 2) {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    // check inner grid to the left
		    for (int i = 0; i < gridDimensions; i++) {
			bugPositionsTmp.add(new IncomparablePair<>(level + 1, new Point2d(gridDimensions - 1, i)));
		    }
		}
		// regular 4 way check, no need for inner or outer levels
		else {
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x + 1, y)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y + 1)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x, y - 1)));
		    bugPositionsTmp.add(new IncomparablePair<>(level, new Point2d(x - 1, y)));
		}
	    }
	}
	bugPositions.addAll(bugPositionsTmp);
    }

    public Set<IncomparablePair<Integer, Point2d>> bugPositions(Map<Integer, char[][]> levelMapping) {
	Set<IncomparablePair<Integer, Point2d>> bugPositions = new HashSet<>();

	for (Map.Entry<Integer, char[][]> e : levelMapping.entrySet()) {
	    int level = e.getKey();
	    char[][] grid = e.getValue();

	    for (int i = 0; i < gridDimensions; i++) {
		for (int j = 0; j < gridDimensions; j++) {
		    if (grid[i][j] == '#') {
			bugPositions.add(new IncomparablePair<>(level, new Point2d(j, i)));
		    }
		}
	    }
	}

	return bugPositions;
    }

    // returns the amount of bugs adjacent to the current position
    public int bugCount(int x, int y, char[][] input) {
	int count = 0;

	if (x > 0 && input[y][x - 1] == '#') {
	    count++;
	}
	if (x < input[y].length - 1 && input[y][x + 1] == '#') {
	    count++;
	}
	if (y > 0 && input[y - 1][x] == '#') {
	    count++;
	}
	if (y < input.length - 1 && input[y + 1][x] == '#') {
	    count++;
	}

	return count;
    }

    // bugCount for part 2
    public int bugCount(int x, int y, int level, Map<Integer, char[][]> levelMapping) {

	levelMapping.putIfAbsent(level - 1, emptyGrid());
	levelMapping.putIfAbsent(level + 1, emptyGrid());

	char[][] outer = levelMapping.get(level - 1);
	char[][] inner = levelMapping.get(level + 1);
	char[][] current = levelMapping.get(level);

	int count = 0;

	// single cells above the inner mid grid
	char aboveMid = outer[1][2]; // 8
	char leftMid = outer[2][1]; // 12
	char rightMid = outer[2][3]; // 14
	char belowMid = outer[3][2]; // 18

	if (x == 0) {
	    // left
	    count = leftMid == '#' ? count + 1 : count;
	    // right
	    count = current[y][x + 1] == '#' ? count + 1 : count;
	    if (y == 0) {
		// up
		count = aboveMid == '#' ? count + 1 : count;
		// down
		count = current[y + 1][x] == '#' ? count + 1 : count;
	    } else if (y == gridDimensions - 1) {
		// up
		count = current[y - 1][x] == '#' ? count + 1 : count;
		// down
		count = belowMid == '#' ? count + 1 : count;
	    } else {
		// up
		count = current[y - 1][x] == '#' ? count + 1 : count;
		// down
		count = current[y + 1][x] == '#' ? count + 1 : count;
	    }

	} else if (y == 0) {
	    // up
	    count = aboveMid == '#' ? count + 1 : count;
	    // down
	    count = current[y + 1][x] == '#' ? count + 1 : count;
	    // left
	    count = current[y][x - 1] == '#' ? count + 1 : count;

	    if (x == gridDimensions - 1) {
		// right
		count = rightMid == '#' ? count + 1 : count;
	    } else {
		// right
		count = current[y][x + 1] == '#' ? count + 1 : count;
	    }
	} else if (x == gridDimensions - 1) {
	    // up
	    count = current[y - 1][x] == '#' ? count + 1 : count;
	    // right
	    count = rightMid == '#' ? count + 1 : count;
	    // left
	    count = current[y][x - 1] == '#' ? count + 1 : count;

	    if (y == gridDimensions - 1) {
		// down
		count = belowMid == '#' ? count + 1 : count;
	    } else {
		count = current[y + 1][x] == '#' ? count + 1 : count;
	    }
	} else if (y == gridDimensions - 1) {
	    // left
	    count = current[y][x - 1] == '#' ? count + 1 : count;
	    // up
	    count = current[y - 1][x] == '#' ? count + 1 : count;
	    // down
	    count = belowMid == '#' ? count + 1 : count;
	    // right
	    count = current[y][x + 1] == '#' ? count + 1 : count;
	} else {
	    char up = current[y - 1][x];
	    char down = current[y + 1][x];
	    char left = current[y][x - 1];
	    char right = current[y][x + 1];

	    // neighbors of inner grid
	    if (x == 2 && y == 1) {
		count = up == '#' ? count + 1 : count;
		count = left == '#' ? count + 1 : count;
		count = right == '#' ? count + 1 : count;
		// check inner grid below
		for (int i = 0; i < gridDimensions; i++) {
		    count = inner[0][i] == '#' ? count + 1 : count;
		}

	    } else if (x == 2 && y == 3) {
		count = down == '#' ? count + 1 : count;
		count = left == '#' ? count + 1 : count;
		count = right == '#' ? count + 1 : count;
		// check inner grid above
		for (int i = 0; i < gridDimensions; i++) {
		    count = inner[gridDimensions - 1][i] == '#' ? count + 1 : count;
		}

	    } else if (x == 1 && y == 2) {
		count = up == '#' ? count + 1 : count;
		count = down == '#' ? count + 1 : count;
		count = left == '#' ? count + 1 : count;
		// check inner grid to the right
		for (int i = 0; i < gridDimensions; i++) {
		    count = inner[i][0] == '#' ? count + 1 : count;
		}

	    } else if (x == 3 && y == 2) {
		count = up == '#' ? count + 1 : count;
		count = down == '#' ? count + 1 : count;
		count = right == '#' ? count + 1 : count;
		// check inner grid to the left
		for (int i = 0; i < gridDimensions; i++) {
		    count = inner[i][gridDimensions - 1] == '#' ? count + 1 : count;
		}
	    }

	    // regular 4 way check, no need for inner or outer levels
	    else {
		count = up == '#' ? count + 1 : count;
		count = down == '#' ? count + 1 : count;
		count = left == '#' ? count + 1 : count;
		count = right == '#' ? count + 1 : count;
	    }
	}

	return count;
    }

    private char[][] emptyGrid() {
	char[][] grid = new char[gridDimensions][gridDimensions];
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		grid[i][j] = '.';
	    }
	}
	grid[2][2] = '?';

	return grid;
    }

    public static void main(String[] args) {
	Day24 test = new Day24();
	System.out.println(test.run2());

    }

}
