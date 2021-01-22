package aoc17;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Day9 {

    private List<Character> groups;
    private int garbageCharacterCount;

    public Day9(File input) {
	groups = getGroups(input);
    }

    public int getScore() {
	int score = 0;

	int scoreWeight = 0;
	int currentIndex = 0;
	while(currentIndex < groups.size()) {
	    char currentChar = groups.get(currentIndex);
	    if(currentChar == '{') {
		scoreWeight++;
	    }
	    else if(currentChar == '}') {
		score += scoreWeight;
		scoreWeight--;
	    }
	    else if(currentChar == '!') {
		currentIndex++;
	    }
	    else if(currentChar == '<') {
		currentIndex = getOutOfGarbage(currentIndex + 1);
	    }

	    currentIndex++;
	}

	return score;
    }

    public int getGarbageCount() {
	getScore();
	return garbageCharacterCount;
    }

    private int getOutOfGarbage(int index) {
	char currentChar = groups.get(index);
	while(currentChar != '>') {

	    if(currentChar == '!') {
		index++;
		garbageCharacterCount -= 1;
	    }

	    index++;
	    currentChar = groups.get(index);
	    garbageCharacterCount++;
	}
	return index - 1;
    }

    private List<Character> getGroups(File input) {
	List<Character> groups = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input)));

	    int c = 0;
	    while((c = br.read()) != -1) {
		groups.add((char) c);
	    }

	    br.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return groups;
    }

    public static void main(String[] args) {
	Day9 test = new Day9(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 9\\InputFile1.txt"));
	System.out.println(test.getScore() + "   " + test.getGarbageCount());
    }

}
