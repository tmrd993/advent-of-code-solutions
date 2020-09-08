package aoc18;

import java.util.Arrays;

public class Day11 {
    private int serialNumber;
    private int[][] grid;

    public Day11() {
	serialNumber = 5093;
	grid = new int[300][300];
    }

    // run with subGridSize 3 (3x3) for solution of part 1
    // returns an array with coordinates (X,Y) and the size identifier
    public int[] run(int subGridSize) {
	calculatePowerLevelsGrid();
	int max = Integer.MIN_VALUE;
	int[] maxCoordinate = new int[3];
	for (int i = 0; i < grid.length - (subGridSize - 1); i++) {
	    for (int j = 0; j < grid[0].length - (subGridSize - 1); j++) {
		int sum = 0;
		for (int k = i; k < i + subGridSize; k++) {
		    for (int l = j; l < j + subGridSize; l++) {
			sum += grid[k][l];
		    }
		}
		if (sum > max) {
		    max = sum;
		    maxCoordinate[0] = i + 1;
		    maxCoordinate[1] = j + 1;
		    maxCoordinate[2] = max;
		}
	    }
	}
	return maxCoordinate;
    }

    // part2
    // slow but acceptable, could run considerably faster if we use a summed
    // area table
    public int[] run2() {
	int[] max = new int[] { 0, 0, Integer.MIN_VALUE };
	int maxIdentifier = 0;
	for (int i = 1; i <= 300; i++) {
	    System.out.println(i);
	    int[] current = run(i);
	    if (current[2] > max[2]) {
		max = current;
		maxIdentifier = i;
	    }
	}
	return new int[] { max[0], max[1], maxIdentifier };
    }

    private void calculatePowerLevelsGrid() {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[0].length; j++) {
		grid[i][j] = calculatePowerLevel(i + 1, j + 1, serialNumber);
	    }
	}
    }

    private static int getHundredsDigit(int num) {
	for (int i = 0; i < 2; i++) {
	    num /= 10;
	}
	return num % 10;
    }

    private static int calculatePowerLevel(int x, int y, int sn) {
	int rackId = x + 10;
	int powerLevel = ((rackId * y) + sn) * rackId;
	powerLevel = getHundredsDigit(powerLevel);

	return powerLevel - 5;
    }

    public static void main(String[] main) {
	Day11 test = new Day11();
	int[] co = test.run2();
	System.out.println(Arrays.toString(co));

    }
}
