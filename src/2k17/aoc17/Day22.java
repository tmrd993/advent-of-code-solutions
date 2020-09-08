package aoc17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import myutils17.Point2d;
import myutils17.VirusCarrier;

public class Day22 {

    private Map<Point2d, Character> initialGrid;
    private final char INFECTED = '#';
    private final char CLEAN = '.';
    private final char FLAGGED = 'F';
    private final char WEAKENED = 'W';

    public Day22(File input) {
	initialGrid = getInitialGrid(input);
    }

    // returns the number of activities that cause an infection. run with
    // numOfBursts = 10000 for solution of part 1
    public int infectionCount(int numOfBursts) {
	int infectionCount = 0;

	int x0 = (initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1) / 2;
	int y0 = (initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1) / 2;

	VirusCarrier carrier = new VirusCarrier(new Point2d(x0, y0));

	for (int i = 0; i < numOfBursts; i++) {
	    //printGrid();
	    //System.out.println();
	    initialGrid.putIfAbsent(carrier.position(), CLEAN);

	    char currentNode = initialGrid.get(carrier.position());

	    if (currentNode == INFECTED) {
		carrier.turnRight();
		initialGrid.put(carrier.position(), CLEAN);
	    }

	    else {
		carrier.turnLeft();
		initialGrid.put(carrier.position(), INFECTED);
		infectionCount++;
	    }

	    carrier.move();
	}
	return infectionCount;
    }

    public int infectionCountPart2(int numOfBursts) {
	int infectionCount = 0;

	int x0 = (initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1) / 2;
	int y0 = (initialGrid.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt() + 1) / 2;

	VirusCarrier carrier = new VirusCarrier(new Point2d(x0, y0));

	for (int i = 0; i < numOfBursts; i++) {
	    //printGrid();
	    //System.out.println();
	    initialGrid.putIfAbsent(carrier.position(), CLEAN);

	    char currentNode = initialGrid.get(carrier.position());

	    if (currentNode == INFECTED) {
		carrier.turnRight();
		initialGrid.put(carrier.position(), FLAGGED);
	    }

	    else if(currentNode == CLEAN) {
		carrier.turnLeft();
		initialGrid.put(carrier.position(), WEAKENED);
	    }

	    else if(currentNode == WEAKENED) {
		initialGrid.put(carrier.position(), INFECTED);
		infectionCount++;
	    }

	    else if(currentNode == FLAGGED) {
		// reverse the direction by turning left twice
		carrier.turnLeft();
		carrier.turnLeft();
		initialGrid.put(carrier.position(), CLEAN);
	    }

	    carrier.move();
	}
	return infectionCount;


    }

    // visualization aid
    @SuppressWarnings("unused")
    private void printGrid() {
	int xMin = initialGrid.entrySet().stream().mapToInt(s -> s.getKey().x()).min().getAsInt();
	int yMin = initialGrid.entrySet().stream().mapToInt(s -> s.getKey().y()).min().getAsInt();
	int xMax = initialGrid.entrySet().stream().mapToInt(s -> s.getKey().x()).max().getAsInt();
	int yMax = initialGrid.entrySet().stream().mapToInt(s -> s.getKey().y()).max().getAsInt();

	for(int i = yMin; i < yMax + 1; i++) {
	    for(int j = xMin; j < xMax + 1; j++) {
		Character currentNode = initialGrid.get(new Point2d(j, i));
		char prNode = currentNode == null ? '.' : currentNode;
		System.out.print(prNode);
	    }
	    System.out.println();
	}
    }

    private Map<Point2d, Character> getInitialGrid(File input) {
	Map<Point2d, Character> initialGrid = new HashMap<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    int x = 0;
	    int y = 0;
	    String line = "";
	    while ((line = br.readLine()) != null) {
		for (int i = 0; i < line.length(); i++) {
		    initialGrid.put(new Point2d(x, y), line.charAt(i));
		    x++;
		}
		x = 0;
		y++;
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return initialGrid;
    }

    public static void main(String[] args) {

	Day22 test = new Day22(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 22\\InputFile1.txt"));

	System.out.println(test.infectionCountPart2(10000000));
    }
}
