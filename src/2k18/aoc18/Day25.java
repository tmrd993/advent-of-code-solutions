package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import myutils.Point4d;

public class Day25 {

    List<Point4d> points;

    public Day25(File inputFile) throws IOException {
	points = getPoints(inputFile);
    }

    public int run() {
	List<Set<Point4d>> constellations = new ArrayList<Set<Point4d>>();

	Set<Point4d> marked = new HashSet<Point4d>();

	for(int i = 0; i < points.size(); i++) {
	    Point4d currentPoint = points.get(i);
	    if(marked.contains(currentPoint))
		continue;

	    Queue<Point4d> queue = new LinkedList<Point4d>();
	    queue.add(currentPoint);

	    Set<Point4d> constellation = new HashSet<Point4d>();
	    constellation.add(currentPoint);
	    constellations.add(constellation);

	    while(!queue.isEmpty()) {
		Point4d current = queue.poll();
		marked.add(current);
		for(int j = i + 1; j < points.size(); j++) {
		    Point4d nextPoint = points.get(j);
		    if(current.distanceL1(nextPoint) <= 3 && !queue.contains(nextPoint) && !marked.contains(nextPoint)) {
			queue.add(nextPoint);
			constellation.add(nextPoint);
		    }
		}
	    }
	}
	return constellations.size();
    }

    private List<Point4d> getPoints(File inputFile) throws IOException {
	List<Point4d> tmpPoints = new ArrayList<Point4d>();

	Scanner sc = new Scanner(inputFile);

	while (sc.hasNextLine()) {
	    String line = sc.nextLine();
	    String[] lineElements = line.split(",");
	    Point4d point = new Point4d(Integer.parseInt(lineElements[0]), Integer.parseInt(lineElements[1]),
		    Integer.parseInt(lineElements[2]), Integer.parseInt(lineElements[3]));

	    tmpPoints.add(point);
	}
	sc.close();
	return tmpPoints;
    }

    public static void main(String[] args) throws IOException {

	Day25 test = new Day25(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 25\\InputFile1.txt"));

	int res1 = test.run();

	System.out.println(res1);
    }
}
