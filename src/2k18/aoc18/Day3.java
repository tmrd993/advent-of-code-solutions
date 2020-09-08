package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.geom.Point2D;

public class Day3 {
    private static int getId(String claim) {
	return Integer.parseInt(claim.substring(1, claim.indexOf("@") - 1));
    }

    private static int[] getCoordinates(String claim) {
	int x = Integer.parseInt(claim.substring(claim.indexOf("@") + 2, claim.indexOf(",")));
	int y = Integer.parseInt(claim.substring(claim.indexOf(",") + 1, claim.indexOf(":")));
	return new int[] { x, y };
    }

    private static int[] getDimensions(String claim) {
	int width = Integer.parseInt(claim.substring(claim.indexOf(":") + 2, claim.indexOf("x")));
	int height = Integer.parseInt(claim.substring(claim.indexOf("x") + 1, claim.length()));
	return new int[] { width, height };
    }

    // part 1
    public static Set<Point2D.Double> overlappingArea(List<String> fabricClaims) {
	Set<Point2D.Double> allCoordinates = new HashSet<Point2D.Double>();
	Set<Point2D.Double> overlappingCoordinates = new HashSet<Point2D.Double>();

	for (String claim : fabricClaims) {
	    int[] coordinates = getCoordinates(claim);
	    int[] dimensions = getDimensions(claim);
	    // starting coordinates for the current rectangle
	    int x = coordinates[0];
	    int y = coordinates[1];
	    // dimensions of the current rectangle
	    // rectangle
	    int width = dimensions[0];
	    int height = dimensions[1];

	    for (int i = y; i < y + height; i++) {
		for (int j = x; j < x + width; j++) {
		    Point2D.Double currentPoint = new Point2D.Double(j, i);
		    if (allCoordinates.contains(currentPoint)) {
			overlappingCoordinates.add(currentPoint);
		    }
		    allCoordinates.add(currentPoint);
		}
	    }
	}
	return overlappingCoordinates;
    }

    // part 2
    // expects the solution of part 1 as a parameter
    public static int getAreaIdWithoutOverlap(Set<Point2D.Double> overlapCoordinates, List<String> fabricClaims) {
	for (String claim : fabricClaims) {
	    int currentId = getId(claim);
	    int[] coordinates = getCoordinates(claim);
	    int[] dimensions = getDimensions(claim);
	    int x = coordinates[0];
	    int y = coordinates[1];
	    int width = dimensions[0];
	    int height = dimensions[1];
	    boolean hasOverlap = false;
	    for (int i = y; i < y + height; i++) {
		for (int j = x; j < x + width; j++) {
		    // if the current coordinate is inside the overlap set it
		    // can't be the target
		    if (overlapCoordinates.contains(new Point2D.Double(j, i))) {
			hasOverlap = true;
		    }
		}
	    }
	    if (!hasOverlap)
		return currentId;
	}
	// no overlaps or wrong input
	return -1;
    }

    public static void main(String[] args) throws IOException {
	List<String> fabricClaims = aoc18.Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 3\\InputFile.txt"));

	Set<Point2D.Double> overlappingCoordinates = overlappingArea(fabricClaims);

	System.out.println(getAreaIdWithoutOverlap(overlappingCoordinates, fabricClaims));
    }
}