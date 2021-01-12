package aoc16;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myutils16.Point2d;
import myutils16.StaticUtils;

public class Day8 {

    private List<String> instructions;
    private final int WIDTH = 50;
    private final int HEIGHT = 6;
    private final char ON = '#';
    private char[][] grid;

    public Day8(File input) {
	instructions = StaticUtils.inputToList(input);
    }

    /**
     * Also prints the letters for part 2
     * 
     * @return number of ON pixels after instructions are done
     */
    public int run() {
	grid = new char[HEIGHT][WIDTH];
	for (String instruction : instructions) {
	    executeInstruction(instruction, grid);
	}

	int onCount = 0;
	for(char[] row : grid) {
	    for(int i = 0; i < row.length; i++)
		if(row[i] == ON)
		    onCount++;
	}
	
	for(int i = 0; i < grid.length; i++) {
	    for(int j = 0; j < grid[i].length; j++) {
		System.out.print(grid[i][j]);
	    }
	    System.out.println();
	}
	
	return onCount;
    }

    private void executeInstruction(String instruction, char[][] grid) {
	// rect command
	if (instruction.contains("rect")) {
	    int a = Integer.parseInt(instruction.substring(instruction.indexOf(' ') + 1, instruction.indexOf('x')));
	    int b = Integer.parseInt(instruction.substring(instruction.indexOf('x') + 1));
	    fillRect(grid, a, b);
	}

	// rotate command
	else {
	    int rowOrCol = Integer
		    .parseInt(instruction.substring(instruction.indexOf('=') + 1, instruction.indexOf("by") - 1));
	    int rotateBy = Integer.parseInt(instruction.substring(instruction.indexOf("by") + 3));
	    if (instruction.contains("row")) {
		rotateRow(grid, rowOrCol, rotateBy);
	    } else {
		rotateCol(grid, rowOrCol, rotateBy);
	    }
	}
    }

    private void rotateRow(char[][] grid, int row, int rotateBy) {
	Map<Point2d, Character> updatedColumn = new HashMap<>();
	for (int i = 0; i < WIDTH; i++) {
	    Point2d updatedPos = new Point2d(((i + rotateBy) % WIDTH), row);
	    updatedColumn.put(updatedPos, grid[row][i]);
	}
	for (Map.Entry<Point2d, Character> e : updatedColumn.entrySet()) {
	    Point2d pos = e.getKey();
	    grid[pos.y()][pos.x()] = e.getValue();
	}
    }

    private void rotateCol(char[][] grid, int col, int rotateBy) {
	Map<Point2d, Character> updatedColumn = new HashMap<>();
	for (int i = 0; i < HEIGHT; i++) {
	    Point2d updatedPos = new Point2d(col, ((i + rotateBy) % HEIGHT));
	    updatedColumn.put(updatedPos, grid[i][col]);
	}
	for (Map.Entry<Point2d, Character> e : updatedColumn.entrySet()) {
	    Point2d pos = e.getKey();
	    grid[pos.y()][pos.x()] = e.getValue();
	}
    }

    // turns on pixels in an AxB rectangle starting from 0,0
    private void fillRect(char[][] grid, int a, int b) {
	for (int i = 0; i < b; i++) {
	    for (int j = 0; j < a; j++) {
		grid[i][j] = ON;
	    }
	}
    }

    public static void main(String[] args) {
	Day8 test = new Day8(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 8\\InputFile1.txt"));
	System.out.println(test.run());

	

    }

}
