package aoc16;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myutils16.StaticUtils;

public class Day6 {
    
    private List<String> messages;
    
    public Day6(File input) {
	messages = StaticUtils.inputToList(input);
    }
    /**
     * 
     * @param partFlag true for part1, false for part 2
     * @return message
     */
    public String run(boolean partFlag) {
	StringBuilder msg = new StringBuilder();
	int msgLen = messages.get(0).length();
	
	for(int i = 0; i < msgLen; i++) {
	    Map<Character, Integer> frequencies = new HashMap<>();
	    for(String message : messages) {
		char currCh = message.charAt(i);
		frequencies.putIfAbsent(currCh, 0);
		frequencies.put(currCh, frequencies.get(currCh) + 1);
	    }
	    if(partFlag)
		msg.append(Collections.max(frequencies.entrySet(), Map.Entry.comparingByValue()).getKey());
	    else
		msg.append(Collections.min(frequencies.entrySet(), Map.Entry.comparingByValue()).getKey());
	}
	
	return msg.toString();
    }
    
    public static void main(String[] args) {
	Day6 test = new Day6(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 6\\InputFile1.txt"));
	System.out.println(test.run(false));
    }

}
