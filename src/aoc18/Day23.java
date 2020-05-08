package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.IntStream;

import myutils.Cube;
import myutils.Nanobot;
import myutils.Point3d;

// part 2 should be a general solution for all official inputs. I have tried
// some, but not all.
public class Day23 {
    private List<Nanobot> nanoBots;

    public Day23(File inputFile) throws IOException {
	nanoBots = getNanoBots(inputFile);
    }

    // part 1
    public int run() {
	Nanobot maxRangeBot = getMaxRangeBot();

	int count = 0;
	for (Nanobot bots : nanoBots) {
	    double dist = maxRangeBot.position().distanceL1(bots.position());
	    if (dist <= maxRangeBot.range())
		count++;
	}
	return count;
    }

    // part 2
    // finds the distance by first creating a cubical bounding box and
    // repeatedly splits the correct box (the one with the most intersections)
    // into 8 equal pieces until the cube becomes a unit cube (both sides length
    // == 1).
    // for every unit cube, it checks all 8 points of the cube for the amount of
    // intersections with bots and updates the current best distance and the
    // current max intersections until every
    // cube has been fully processed. Using the current max intersections, any
    // cube that has a lower amount of intersections gets discarded
    public int run2() {
	// find the bounding box
	int xMin = Integer.MAX_VALUE;
	int xMax = Integer.MIN_VALUE;
	int yMin = Integer.MAX_VALUE;
	int yMax = Integer.MIN_VALUE;
	int zMin = Integer.MAX_VALUE;
	int zMax = Integer.MIN_VALUE;

	for (Nanobot bot : nanoBots) {
	    if (bot.position().x() < xMin)
		xMin = (int) bot.position().x();
	    if (bot.position().x() > xMax)
		xMax = (int) bot.position().x();
	    if (bot.position().y() < yMin)
		yMin = (int) bot.position().y();
	    if (bot.position().y() > yMax)
		yMax = (int) bot.position().y();
	    if (bot.position().z() < zMin)
		zMin = (int) bot.position().z();
	    if (bot.position().z() > zMax)
		zMax = (int) bot.position().z();
	}

	// turn bounding box into a cube
	int maxPowTwo = nextPower(getMax(xMax, yMax, zMax));
	xMax = maxPowTwo + xMin;
	yMax = maxPowTwo + yMin;
	zMax = maxPowTwo + zMin;

	Cube current = new Cube(xMin, xMax, yMin, yMax, zMin, zMax);

	// priority queue with descending ordering based on intersections inside
	// cubes
	Queue<Cube> cubes = new PriorityQueue<Cube>(10, new CubeComparator());
	cubes.add(current);

	int currentMaxIntersections = 0;
	int currentMaxDistance = Integer.MAX_VALUE;
	while (!cubes.isEmpty()) {

	    current = cubes.poll();

	    xMin = current.xMin();
	    xMax = current.xMax();
	    yMin = current.yMin();
	    yMax = current.yMax();
	    zMin = current.zMin();
	    zMax = current.zMax();

	    boolean isUnitCube = xMax - xMin <= 1 && yMax - yMin <= 1 && zMax - zMin <= 1;
	    List<Cube> splitCubes = new ArrayList<Cube>();
	    // cube can't be split anymore
	    if (isUnitCube) {
		// check all 8 points of the unit cube
		splitCubes.add(new Cube(xMin, xMin, yMin, yMin, zMin, zMin));
		splitCubes.add(new Cube(xMax, xMax, yMin, yMin, zMin, zMin));
		splitCubes.add(new Cube(xMin, xMin, yMax, yMax, zMin, zMin));
		splitCubes.add(new Cube(xMax, xMax, yMax, yMax, zMin, zMin));
		splitCubes.add(new Cube(xMin, xMin, yMin, yMin, zMax, zMax));
		splitCubes.add(new Cube(xMax, xMax, yMin, yMin, zMax, zMax));
		splitCubes.add(new Cube(xMin, xMin, yMax, yMax, zMax, zMax));
		splitCubes.add(new Cube(xMax, xMax, yMax, yMax, zMax, zMax));

		Cube bestCube = null;
		int maxIntersections = 0;
		// calculate amount of intersections of all 8 points (smallest
		// possible cubes)
		for (Cube cube : splitCubes) {
		    int intersections = 0;
		    for (Nanobot bot : nanoBots) {
			if (cube.containsBot(bot)) {
			    cube.incrIntersections();
			    intersections++;
			}
		    }

		    if (intersections > maxIntersections) {
			maxIntersections = intersections;
			bestCube = cube;
		    }

		}
		currentMaxIntersections = bestCube.getIntersections();
		currentMaxDistance = Math.abs(bestCube.xMin()) + Math.abs(bestCube.yMin()) + Math.abs(bestCube.zMin());

	    } else {
		// split cube into 8 equal pieces
		splitCubes.add(new Cube(xMin, (xMin + xMax) / 2, yMin, (yMin + yMax) / 2, zMin, (zMin + zMax) / 2));
		splitCubes.add(new Cube(xMin, (xMin + xMax) / 2, (yMin + yMax) / 2, yMax, zMin, (zMin + zMax) / 2));
		splitCubes.add(new Cube((xMin + xMax) / 2, xMax, yMin, (yMin + yMax) / 2, zMin, (zMin + zMax) / 2));
		splitCubes.add(new Cube((xMin + xMax) / 2, xMax, (yMin + yMax) / 2, yMax, zMin, (zMin + zMax) / 2));
		splitCubes.add(new Cube((xMin + xMax) / 2, xMax, (yMin + yMax) / 2, yMax, (zMin + zMax) / 2, zMax));
		splitCubes.add(new Cube(xMin, (xMin + xMax) / 2, (yMin + yMax) / 2, yMax, (zMin + zMax) / 2, zMax));
		splitCubes.add(new Cube((xMin + xMax) / 2, xMax, yMin, (yMin + yMax) / 2, (zMin + zMax) / 2, zMax));
		splitCubes.add(new Cube(xMin, (xMin + xMax) / 2, yMin, (yMin + yMax) / 2, (zMin + zMax) / 2, zMax));
	    }

	    // calculate amount of intersections of all 8 sub-cubes
	    for (Cube cube : splitCubes) {
		for (Nanobot bot : nanoBots) {
		    if (cube.containsBot(bot)) {
			cube.incrIntersections();
		    }
		}

		if (cube.getIntersections() > currentMaxIntersections && !isUnitCube) {
		    cubes.add(cube);
		}
	    }

	}
	return currentMaxDistance;
    }

    private Nanobot getMaxRangeBot() {
	Nanobot maxRangeBot = null;
	double maxRange = Double.MIN_VALUE;
	for (Nanobot bot : nanoBots) {
	    if (bot.range() > maxRange) {
		maxRange = bot.range();
		maxRangeBot = bot;
	    }
	}
	return maxRangeBot;
    }

    private List<Nanobot> getNanoBots(File inputFile) throws IOException {
	List<Nanobot> nanoBots = new ArrayList<Nanobot>();
	Scanner sc = new Scanner(inputFile);

	while (sc.hasNext()) {
	    String currentLine = sc.nextLine();
	    double x = Double
		    .parseDouble(currentLine.substring(currentLine.indexOf('<') + 1, currentLine.indexOf(',')));
	    double y = Double.parseDouble(currentLine.substring(currentLine.indexOf(',') + 1,
		    currentLine.indexOf(',', currentLine.indexOf(',') + 1)));
	    double z = Double.parseDouble(currentLine
		    .substring(currentLine.indexOf(',', currentLine.indexOf(',') + 1) + 1, currentLine.indexOf('>')));
	    double range = Double
		    .parseDouble(currentLine.substring(currentLine.indexOf('r') + 2, currentLine.length()));

	    nanoBots.add(new Nanobot(new Point3d((int) x, (int) y, (int) z), (int) range));
	}

	sc.close();
	return nanoBots;
    }

    public int nextPower(int n) {
	int pow = 2;
	int base = 1;

	while (pow < n) {
	    pow = (int) Math.pow(2, base++);
	}
	return pow;
    }

    public int getMax(int a, int b, int c) {
	return IntStream.of(a, b, c).max().getAsInt();
    }

    public static void main(String[] args) throws IOException {

	Day23 test = new Day23(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 23\\InputFile1.txt"));

	int res2 = test.run2();

	System.out.println(res2);
    }

    public class CubeComparator implements Comparator<Cube> {

	@Override
	public int compare(Cube c1, Cube c2) {
	    if (c1.getIntersections() > c2.getIntersections()) {
		return 1;
	    }

	    return -1;
	}
    }
}
