package aoc15;

import java.io.File;
import java.util.List;

import myutils15.StaticUtils;

public class Day8 {
    
    private List<String> inputStrings;
    private final int doubleQuoteCount = 2;
    
    public Day8(File input) {
	inputStrings = StaticUtils.fileToStringList(input);
    }
    
    public int run1() {
	int totalCharacterCount = inputStrings.stream().mapToInt(str -> str.length()).sum();
	int totalCharacterCountForStringLiterals = inputStrings.stream().mapToInt(str -> getCharacterCountPt1(str)).sum();
	
	return totalCharacterCount - totalCharacterCountForStringLiterals;
    }
    
    public int run2() {
	int totalCharacterCount = inputStrings.stream().mapToInt(str -> str.length()).sum();
	int totalCharacterCountForStringLiterals = inputStrings.stream().mapToInt(str -> getCharacterCountPt2(str)).sum();
	
	return totalCharacterCountForStringLiterals - totalCharacterCount;
    }
    
    private int getCharacterCountPt1(String str) {
	int characterCount = 0;
	int strLen = str.length();
	int currentIndex = 0;
	while(currentIndex < strLen) {
	    char currentCharacter = str.charAt(currentIndex);
	    if(currentCharacter == '\\' && currentIndex < strLen - 1) {
		if(str.charAt(currentIndex + 1) == '\\' || str.charAt(currentIndex + 1) == '\"') {
		    currentIndex += 2;
		} else if(str.charAt(currentIndex + 1) == 'x') {
		    currentIndex += 4;
		}
	    } else {
		currentIndex++;
	    }
	    
	    characterCount++;
	}
	
	return characterCount - doubleQuoteCount;
    }
    
    private int getCharacterCountPt2(String str) {
	int characterCount = 0;
	int strLen = str.length();
	for(int i = 0; i < strLen; i++) {
	    char currentChar = str.charAt(i);
	    if(currentChar == '\\' || currentChar == '\"') {
		characterCount += 2;
	    } else {
		characterCount++;
	    }
	}
	
	return characterCount + doubleQuoteCount;
    }

    public static void main(String[] args) {
	Day8 test = new Day8(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 8\\InputFile1.txt"));
	System.out.println(test.run2());
	
    }
   
}
