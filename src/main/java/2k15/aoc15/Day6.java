package aoc15;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myutils15.StaticUtils;

public class Day6 {

    private List<String> instructions;

    public Day6(File input) {
	instructions = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	boolean[][] grid = new boolean[1000][1000];
	Pattern p = Pattern.compile("\\d+,\\d+");
	for (String instr : instructions) {
	    Matcher m = p.matcher(instr);
	    int[] range1 = Arrays.stream(nextMatch(m).split(",")).mapToInt(Integer::parseInt).toArray();
	    int[] range2 = Arrays.stream(nextMatch(m).split(",")).mapToInt(Integer::parseInt).toArray();
	    updateGridPt1(grid, range1, range2, instr);
	}

	return countLights(grid);
    }
    
    public int run2() {
	int[][] grid = new int[1000][1000];
	Pattern p = Pattern.compile("\\d+,\\d+");
	for (String instr : instructions) {
	    Matcher m = p.matcher(instr);
	    int[] range1 = Arrays.stream(nextMatch(m).split(",")).mapToInt(Integer::parseInt).toArray();
	    int[] range2 = Arrays.stream(nextMatch(m).split(",")).mapToInt(Integer::parseInt).toArray();
	    updateGridPt2(grid, range1, range2, instr);
	}
	
	return measureBrightness(grid);
    }
    
    private void updateGridPt2(int[][] grid, int[] range1, int[] range2, String instruction) {
	int x0 = range1[0];
	int y0 = range1[1];
	int x1 = range2[0];
	int y1 = range2[1];
	
	for(int i = y0; i <= y1; i++) {
	    for(int j = x0; j <= x1; j++) {
		int brightness = grid[i][j];
		if(instruction.contains("on")) {
		    grid[i][j] = brightness + 1;
		} else if(instruction.contains("off")) {
		    grid[i][j] = brightness == 0 ? brightness : brightness - 1;
		} else {
		    grid[i][j] = brightness + 2;
		}
	    }
	}
    }
    
    private int measureBrightness(int[][] grid) {
	return Arrays.stream(grid).flatMapToInt(arr -> Arrays.stream(arr)).sum();
    }
    
    
    private int countLights(boolean[][] grid) {
	int count = 0;
	for(int i = 0; i < grid.length; i++) {
	    for(int j = 0; j < grid[i].length; j++) {
		if(grid[i][j]) {
		    count++;
		}
	    }
	}
	return count;
    }
    
    private void updateGridPt1(boolean[][] grid, int[] range1, int[] range2, String instruction) {
	int x0 = range1[0];
	int y0 = range1[1];
	int x1 = range2[0];
	int y1 = range2[1];
	
	for(int i = y0; i <= y1; i++) {
	    for(int j = x0; j <= x1; j++) {
		if(instruction.contains("on")) {
		    grid[i][j] = true;
		} else if(instruction.contains("off")) {
		    grid[i][j] = false;
		} else {
		    grid[i][j] = !grid[i][j];
		}
	    }
	}
    }

    private String nextMatch(Matcher m) {
	m.find();
	return m.group();
    }

    public static void main(String[] args) {
	Day6 test = new Day6(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 6\\InputFile.txt"));
	System.out.println(test.run2());
	
    }

}
