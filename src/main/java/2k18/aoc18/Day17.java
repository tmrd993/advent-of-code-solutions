package aoc18;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day17 {

    private Set<Point> clayCoordinates;
    // queue of positions of the tails of the water spring (the moving parts)
    private Queue<Point> waterTails;
    // settled water '~'
    private Set<Point> settledWater;
    // moving water '|'
    private Set<Point> movingWater;

    public Day17(File inputFile) throws IOException {
	clayCoordinates = getClayCoordinates(inputFile);
	settledWater = new HashSet<Point>();
	movingWater = new HashSet<Point>();
	waterTails = new LinkedList<Point>();
	// add the water source
	waterTails.add(new Point(500, 0));
    }

    // part 1 = true argument
    // part 2 = false argument
    public int run(boolean val) {
	int[] verticalLimits = getVerticalLimits();

	int yMax = verticalLimits[0];
	int yMin = verticalLimits[1];

	while (waterTails.size() > 0) {
	    Point currentTail = waterTails.poll();
	    Point belowTail = new Point(currentTail.x, currentTail.y + 1);
	    movingWater.add(currentTail);

	    // check if water can move down
	    if (!clayCoordinates.contains(belowTail) && !settledWater.contains(belowTail)) {
		if (movingWater.contains(belowTail)) {
		    continue;
		}

		//check maximum bound, otherwise the water will flow infinitely
		if (belowTail.y <= yMax)
		    waterTails.add(new Point(currentTail.x, currentTail.y + 1));

	    } else {
		// check if current position is closed (can be filled
		// horizontally with water)
		if (isHorizontallyFillable(currentTail)) {
		    fillHorizontalArea(currentTail);
		    waterTails.add(new Point(currentTail.x, currentTail.y - 1));
		} else {
		    // split water into one or two tails
		    splitWater(currentTail);
		}
	    }
	}

	return val == true
		? (int) movingWater.stream().filter(n -> n.y >= yMin).count()
			+ (int) settledWater.stream().filter(n -> n.y >= yMin).count()
		: (int) settledWater.stream().filter(n -> n.y >= yMin).count();

    }

    //adds one or two new tails to the queue depending on the left and right bounds (walls or water)
    private void splitWater(Point currentPosition) {
	Point toLeft = new Point(currentPosition.x - 1, currentPosition.y);
	Point toRight = new Point(currentPosition.x + 1, currentPosition.y);

	while (!clayCoordinates.contains(toLeft)) {

	    Point below = new Point(toLeft.x, toLeft.y + 1);

	    if (clayCoordinates.contains(below) || settledWater.contains(below)) {
		movingWater.add(toLeft);
	    } else {
		waterTails.add(toLeft);
		break;
	    }
	    toLeft = new Point(toLeft.x - 1, toLeft.y);
	}

	while (!clayCoordinates.contains(toRight)) {

	    if (movingWater.contains(toRight)) {
		waterTails.add(toRight);
		break;
	    }

	    Point below = new Point(toRight.x, toRight.y + 1);

	    if (clayCoordinates.contains(below) || settledWater.contains(below)) {
		movingWater.add(toRight);
	    } else {
		waterTails.add(toRight);
		break;
	    }
	    toRight = new Point(toRight.x + 1, toRight.y);
	}
    }

    private void fillHorizontalArea(Point currentPosition) {
	Point toLeft = new Point(currentPosition.x - 1, currentPosition.y);
	Point toRight = new Point(currentPosition.x + 1, currentPosition.y);

	settledWater.add(currentPosition);
	movingWater.remove(currentPosition);

	while (!clayCoordinates.contains(toLeft)) {
	    settledWater.add(toLeft);
	    movingWater.remove(toLeft);
	    toLeft = new Point(toLeft.x - 1, toLeft.y);
	}

	while (!clayCoordinates.contains(toRight)) {
	    settledWater.add(toRight);
	    movingWater.remove(toRight);
	    toRight = new Point(toRight.x + 1, toRight.y);
	}
    }

    private Set<Point> getClayCoordinates(File inputFile) throws IOException {
	Set<Point> clayCoordinates = new HashSet<Point>();

	Scanner sc = new Scanner(inputFile);
	while (sc.hasNext()) {
	    String[] coordinates = sc.nextLine().split(",");
	    String coordinate1 = coordinates[0];
	    String coordinate2 = coordinates[1].trim();

	    // input is either x=NNN, y=NNN..NNN or y=NNN, x=NNN..NNN
	    int co1 = Integer.parseInt(coordinate1.substring(2, coordinate1.length()));
	    int co2Min = Integer.parseInt(coordinate2.substring(2, coordinate2.indexOf('.')));
	    int co2Max = Integer.parseInt(coordinate2.substring(coordinate2.indexOf('.') + 2, coordinate2.length()));

	    for (int i = co2Min; i <= co2Max; i++) {
		Point p = coordinate1.charAt(0) == 'x' ? new Point(co1, i) : new Point(i, co1);
		clayCoordinates.add(p);
	    }
	}

	sc.close();

	return clayCoordinates;
    }

    //just a visualization aid, not part of any solution
    private void printGrid() {
	int[] xLimits = getHorizontalLimits();
	int[] yLimits = getVerticalLimits();

	for (int i = 0; i < yLimits[0] + 1; i++) {
	    for (int j = xLimits[1] - 1; j <= xLimits[0] + 1; j++) {
		if (clayCoordinates.contains(new Point(j, i)))
		    System.out.print('#');
		else if (movingWater.contains(new Point(j, i)))
		    System.out.print('|');
		else if (settledWater.contains(new Point(j, i)))
		    System.out.print('~');
		else
		    System.out.print('.');
	    }
	    System.out.println();
	}

    }

    // returns an array consisting of two numbers, [0] = largest y-coordinate,
    // [1] = lowest y-coordinate
    private int[] getVerticalLimits() {
	int[] yLimits = new int[2];

	int largestY = Integer.MIN_VALUE;
	int lowestY = Integer.MAX_VALUE;

	for (Point p : clayCoordinates) {
	    if (p.y > largestY)
		largestY = p.y;
	    if (p.y < lowestY)
		lowestY = p.y;
	}

	yLimits[0] = largestY;
	yLimits[1] = lowestY;

	return yLimits;

    }

    private int[] getHorizontalLimits() {
	int[] xLimits = new int[2];

	int largestX = Integer.MIN_VALUE;
	int lowestX = Integer.MAX_VALUE;

	for (Point p : clayCoordinates) {
	    if (p.x > largestX)
		largestX = p.x;
	    if (p.x < lowestX)
		lowestX = p.x;
	}

	xLimits[0] = largestX;
	xLimits[1] = lowestX;

	return xLimits;
    }

    // returns true if the current position is a closed area that can be filled
    // completely with water to the left and right
    private boolean isHorizontallyFillable(Point currentPosition) {

	Point toLeft = new Point(currentPosition.x - 1, currentPosition.y);
	Point toRight = new Point(currentPosition.x + 1, currentPosition.y);

	while (!clayCoordinates.contains(toLeft)) {
	    Point down = new Point(toLeft.x, toLeft.y + 1);
	    if (!clayCoordinates.contains(down) && !settledWater.contains(down)) {
		return false;
	    }
	    toLeft = new Point(toLeft.x - 1, toLeft.y);
	}

	while (!clayCoordinates.contains(toRight)) {
	    Point down = new Point(toRight.x, toRight.y + 1);
	    if (!clayCoordinates.contains(down) && !settledWater.contains(down)) {
		return false;
	    }
	    toRight = new Point(toRight.x + 1, toRight.y);
	}

	return true;
    }

    public static void main(String[] args) throws IOException {

	Day17 test = new Day17(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 17\\InputFile1.txt"));

	int res = test.run(true);

	test.printGrid();

	System.out.println(res);

    }
}
