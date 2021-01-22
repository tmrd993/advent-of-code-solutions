package aoc16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import myutils16.Point2d;

public class Day2 {
    
    //pad for part 1
    private char[][] pad = {{'1', '2', '3'},
	    		   {'4', '5', '6'}, 
	    		   {'7', '8', '9'}};
    //pad for part 2
    private char[][] actualPad = {{'0', '0', '1', '0', '0'},
	                          {'0', '2', '3', '4', '0'},
                                  {'5', '6', '7', '8' ,'9'},
                                  {'0', 'A', 'B', 'C', '0'},
                                  {'0', '0', 'D', '0', '0'}};
    private List<String> instructions;
    private final int PAD_BOUNDS = 3;
    private final int ACTUAL_PAD_BOUNDS = 5;
    
    public Day2(File input) {
	instructions = getInstructions(input);
    }
    
    /**
     * 
     * @param partFlag true for part 1, false for part 2
     * @return password
     */
    public String run1(boolean partFlag) {
	int bounds = partFlag ? PAD_BOUNDS : ACTUAL_PAD_BOUNDS;
	char[][] usedPad = partFlag ? pad : actualPad;
	StringBuilder sb = new StringBuilder();
	Point2d currentPos = partFlag ? new Point2d(1, 1) : new Point2d(0, 2);
	for(String instr : instructions) {
	    for(int i = 0; i < instr.length(); i++) {
		currentPos = move(currentPos, instr.charAt(i), bounds, usedPad);
	    }
	    sb.append(usedPad[currentPos.y()][currentPos.x()]);
	}
	return sb.toString();
    }
    
    private Point2d move(Point2d currentPos, char direction, int bounds, char[][] pad) {
	if(direction == 'U' && currentPos.y() > 0 && pad[currentPos.y() - 1][currentPos.x()] != '0') {
	    currentPos = new Point2d(currentPos.x(), currentPos.y() - 1);
	}
	else if(direction == 'D' && currentPos.y() < bounds - 1 && pad[currentPos.y() + 1][currentPos.x()] != '0') {
	    currentPos = new Point2d(currentPos.x(), currentPos.y() + 1);
	}
	else if(direction == 'L' && currentPos.x() > 0 && pad[currentPos.y()][currentPos.x() - 1] != '0') {
	    currentPos = new Point2d(currentPos.x() - 1, currentPos.y());
	}
	else if(direction == 'R' && currentPos.x() < bounds - 1 && pad[currentPos.y()][currentPos.x() + 1] != '0') {
	    currentPos = new Point2d(currentPos.x() + 1, currentPos.y());
	}
	return currentPos;
    } 
    
    public List<String> getInstructions(File input) {
	List<String> instructions = new ArrayList<>();
	
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while((line = br.readLine()) != null) {
		instructions.add(line);
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	return instructions;
    }
    
    public static void main(String[] args) {
	Day2 test = new Day2(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 2\\InputFile1.txt"));
	System.out.println(test.run1(false));
    }
    
    
    

}
