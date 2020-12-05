package aoc20;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import myutils20.StaticUtils;

public class Day5 {
    
    private List<String> commands;
    
    public Day5(File inputFile) {
	commands = StaticUtils.inputFileToStringList(inputFile);
    }
    
    public int run1() {
	int maxId = Integer.MIN_VALUE;
	for(String command : commands) {
	    int id = getId(command);
	    if(id > maxId) {
		maxId = id;
	    }
	}
	return maxId;
    }
    
    public int run2() {
	List<Integer> seatIds = commands.stream().map(com -> getId(com)).collect(Collectors.toList());
	Collections.sort(seatIds);
	
	for(int i = 0; i < seatIds.size() - 1; i++) {
	    if(seatIds.get(i + 1) != seatIds.get(i) + 1) {
		return seatIds.get(i) + 1;
	    }
	}
	
	return -1;
    }
    
    private int getId(String command) {
	String rowCommand = command.substring(0, command.length() - 3);
	String colCommand = command.substring(command.length() - 3);
	
	return (binSearchForSeats(rowCommand, 0, 127) * 8) + binSearchForSeats(colCommand, 0, 7);
    }
    
    private int binSearchForSeats(String com, int min, int max) {
	int mid = max / 2;
	for(int i = 0; i < com.length(); i++) {
	    mid = min + ((max - min) / 2);
	    char command = com.charAt(i);
	    if(command == 'F' || command == 'L') {
		max = mid;
	    }
	    else if(command == 'B' || command == 'R') {
		min = mid + 1;
	    }
	}
	
	char finalCom = com.charAt(com.length() - 1);
	
	return finalCom == 'F' || finalCom == 'L' ? min : max;
    }

    public static void main(String[] args) {
	Day5 test = new Day5(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 5\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
