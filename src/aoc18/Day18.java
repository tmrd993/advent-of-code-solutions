package aoc18;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day18 {

    private final char OPEN_GROUND = '.';
    private final char LUMBERYARD = '#';
    private final char TREES = '|';
    private Map<Point, Character> allAcres;

    public Day18(File inputFile) throws IOException {
	allAcres = new HashMap<Point, Character>();
	fillAcres(inputFile);
    }

    // execute with argument 10 for solution of part 1
    // too slow for part 2
    public int run(int minutesToRun) {

	for (int i = 0; i < minutesToRun; i++) {
	    Map<Point, Character> nextAcres = new HashMap<Point, Character>();

	    for (Map.Entry<Point, Character> entry : allAcres.entrySet()) {
		Point currentPoint = entry.getKey();
		char newAcre = getNewAcre(currentPoint);
		nextAcres.put(currentPoint, newAcre);
	    }
	    allAcres = nextAcres;
	}

	int lumberCount = 0;
	int treesCount = 0;
	for (Map.Entry<Point, Character> entry : allAcres.entrySet()) {
	    if (entry.getValue() == LUMBERYARD)
		lumberCount++;
	    else if (entry.getValue() == TREES)
		treesCount++;
	}

	return lumberCount * treesCount;
    }

    // part 2
    // runs part 1 in increments of 100 minutes until a cycle gets detected,
    // calculates the 1.000.000.000th number using modular arithmetic
    public int run2() {
	Map<Integer, Integer> lumberVals = new HashMap<Integer, Integer>();

	int minutesToRun = 1000000000;
	int target = 0;

	for (int i = 100; i <= minutesToRun; i += 100) {
	    int val = run(100);

	    if (lumberVals.containsKey(val)) {
		int repeatingVals = (i - lumberVals.get(val)) / 100;
		int minutesLeft = minutesToRun - (i - (100 * repeatingVals)) - 200;
		int indexOfTargetValue = minutesLeft % repeatingVals;

		List<Integer> repeatingNumbers = new ArrayList<Integer>();
		repeatingNumbers.add(val);
		for (int j = 0; j < repeatingVals - 1; j++) {
		    repeatingNumbers.add(run(100));
		}

		target = repeatingNumbers.get(indexOfTargetValue);
		break;

	    } else
		lumberVals.put(val, i);
	}
	return target;
    }

    private char getNewAcre(Point currentPoint) {
	char currentAcre = allAcres.get(currentPoint);

	Map<Character, Integer> adjacentCounts = new HashMap<Character, Integer>();
	adjacentCounts.put(OPEN_GROUND, 0);
	adjacentCounts.put(LUMBERYARD, 0);
	adjacentCounts.put(TREES, 0);
	adjacentCounts.put('0', 0);

	// neighbors of current acre (cardinal directions)
	char n = allAcres.getOrDefault(new Point(currentPoint.x, currentPoint.y - 1), '0');
	char s = allAcres.getOrDefault(new Point(currentPoint.x, currentPoint.y + 1), '0');
	char w = allAcres.getOrDefault(new Point(currentPoint.x - 1, currentPoint.y), '0');
	char e = allAcres.getOrDefault(new Point(currentPoint.x + 1, currentPoint.y), '0');
	char ne = allAcres.getOrDefault(new Point(currentPoint.x + 1, currentPoint.y - 1), '0');
	char nw = allAcres.getOrDefault(new Point(currentPoint.x - 1, currentPoint.y - 1), '0');
	char se = allAcres.getOrDefault(new Point(currentPoint.x + 1, currentPoint.y + 1), '0');
	char sw = allAcres.getOrDefault(new Point(currentPoint.x - 1, currentPoint.y + 1), '0');

	adjacentCounts.put(n, adjacentCounts.get(n) + 1);
	adjacentCounts.put(s, adjacentCounts.get(s) + 1);
	adjacentCounts.put(w, adjacentCounts.get(w) + 1);
	adjacentCounts.put(e, adjacentCounts.get(e) + 1);
	adjacentCounts.put(ne, adjacentCounts.get(ne) + 1);
	adjacentCounts.put(nw, adjacentCounts.get(nw) + 1);
	adjacentCounts.put(se, adjacentCounts.get(se) + 1);
	adjacentCounts.put(sw, adjacentCounts.get(sw) + 1);

	if (currentAcre == OPEN_GROUND) {
	    return adjacentCounts.get(TREES) >= 3 ? TREES : currentAcre;
	}

	if (currentAcre == TREES) {
	    return adjacentCounts.get(LUMBERYARD) >= 3 ? LUMBERYARD : currentAcre;
	}

	// current acre is a lumberyard
	return adjacentCounts.get(LUMBERYARD) >= 1 && adjacentCounts.get(TREES) >= 1 ? LUMBERYARD : OPEN_GROUND;
    }

    private void fillAcres(File inputFile) throws IOException {
	Scanner sc = new Scanner(inputFile);

	String currentLine = "";
	int row = 0;
	int col = 0;
	while (sc.hasNext()) {
	    currentLine = sc.nextLine();

	    for (int i = 0; i < currentLine.length(); i++) {
		allAcres.put(new Point(col++, row), currentLine.charAt(i));
	    }
	    row++;
	    col = 0;
	}
	sc.close();
    }

    @SuppressWarnings("unused")
    private void printGrid() {
	int bounds = allAcres.entrySet().stream().mapToInt(n -> n.getKey().y).max()
		.orElseThrow(NoSuchElementException::new) + 1;

	for (int i = 0; i < bounds; i++) {
	    for (int j = 0; j < bounds; j++) {
		System.out.print(allAcres.get(new Point(j, i)));
	    }
	    System.out.println();
	}
    }

    public static void main(String[] args) throws IOException {
	Day18 test = new Day18(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 18\\InputFile1.txt"));

	System.out.println(test.run2());
    }
}
