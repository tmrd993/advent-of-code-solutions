package aoc19;

import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import myutils19.Pair;
import myutils19.Point2d;
import myutils19.StaticUtils;

public class Day3 {
    private List<String> instructionsWire1;
    private List<String> instructionsWire2;
    
    public Day3(File input) {
	setupWireInstructions(input);
    }
    
    public int run1() {
	Set<Pair<Point2d, Integer>> coordinatesWire1 = getWireCoordinates(instructionsWire1);
	Set<Pair<Point2d, Integer>> coordinatesWire2 = getWireCoordinates(instructionsWire2);
	Set<Pair<Point2d, Integer>> intersections = new HashSet<>(coordinatesWire1);
	intersections.retainAll(coordinatesWire2);
	return intersections.stream().mapToInt(p -> StaticUtils.distanceL1(p.k(), new Point2d(0, 0))).min().getAsInt();
    }
    
    public int run2() {
	Set<Pair<Point2d, Integer>> coordinatesWire1 = getWireCoordinates(instructionsWire1);
	Set<Pair<Point2d, Integer>> coordinatesWire2 = getWireCoordinates(instructionsWire2);
	Set<Pair<Point2d, Integer>> intersections = new HashSet<>(coordinatesWire1);
	intersections.retainAll(coordinatesWire2);
	
	int minSteps = Integer.MAX_VALUE;
	for(Pair<Point2d, Integer> entry : intersections) {
	    int w1 = coordinatesWire1.stream().filter(p -> p.k().equals(entry.k())).findFirst().get().v();
	    int w2 = coordinatesWire2.stream().filter(p -> p.k().equals(entry.k())).findFirst().get().v();
	    if(w1 + w2 < minSteps) {
		minSteps = w1 + w2;
	    }
	}
	
	return minSteps;
    }
    
    private void setupWireInstructions(File input) {
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    instructionsWire1 = Arrays.asList(br.readLine().split(","));
	    instructionsWire2 = Arrays.asList(br.readLine().split(","));
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    private Set<Pair<Point2d, Integer>> getWireCoordinates(List<String> instructions) {
	Set<Pair<Point2d, Integer>> visited = new HashSet<>();
	Point2d currentPoint = new Point2d(0, 0);
	int stepCount = 0;
	for(String instruction : instructions) {
	    char dir = instruction.charAt(0);
	    int walkAmount = Integer.parseInt(instruction.substring(1));
	    currentPoint = move(visited, currentPoint, walkAmount, dir, stepCount);
	    stepCount += walkAmount;
	}
	return visited;
    }
    
    private Point2d move(Set<Pair<Point2d, Integer>> visited, Point2d point, int moveAmount, char direction, int stepCount) {
	for(int i = 0; i < moveAmount; i++) {
	    switch(direction) {
	    case 'U':
		point = new Point2d(point.x(), point.y() + 1);
		break;
	    case 'D':
		point = new Point2d(point.x(), point.y() - 1);
		break;
	    case 'L':
		point = new Point2d(point.x() - 1, point.y());
		break;
	    case 'R':
		point = new Point2d(point.x() + 1, point.y());
		break;
	    }
	    stepCount++;
	    visited.add(new Pair<Point2d, Integer>(point, stepCount));
	}
	return point;
    }

    public static void main(String[] args) {
	Day3 test = new Day3(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 3\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
