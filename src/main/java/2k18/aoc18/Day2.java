package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {
    // part 2
    public static String closestMatchingSuitNum(List<String> suitNumbers) {
	for (int i = 0; i < suitNumbers.size(); i++) {
	    for (int j = i + 1; j < suitNumbers.size(); j++) {
		int differingCharacters = 0;
		int indexOfMatch = 0;
		for (int k = 0; k < suitNumbers.get(i).length(); k++) {
		    if (suitNumbers.get(i).charAt(k) != suitNumbers.get(j).charAt(k)) {
			differingCharacters++;
			indexOfMatch = k;
			if (differingCharacters > 1)
			    continue;
		    }
		    if (k == suitNumbers.get(i).length() - 1 && differingCharacters == 1) {
			return suitNumbers.get(i).substring(0, indexOfMatch)
				+ suitNumbers.get(i).substring(indexOfMatch + 1);
		    }
		}
	    }
	}
	return null;
    }

    // part 1
    public static long suitNumChecksum(List<String> suitNumbers) {
	Map<Character, Integer> characterMap = new HashMap<Character, Integer>();
	int doubles = 0;
	int triples = 0;

	for (String suitNumber : suitNumbers) {
	    characterMap.clear();
	    boolean hasDouble = false;
	    boolean hasTriple = false;
	    // first pass: create the map with number of occurences
	    for (int i = 0; i < suitNumber.length(); i++) {
		char currentChar = suitNumber.charAt(i);
		if (characterMap.containsKey(currentChar)) {
		    characterMap.put(currentChar, characterMap.get(currentChar) + 1);
		} else {
		    characterMap.put(currentChar, 1);
		}
	    }
	    // second pass: check for doubles or triples
	    for (int i = 0; i < suitNumber.length(); i++) {
		char currentChar = suitNumber.charAt(i);
		if (!hasDouble && characterMap.get(currentChar) == 2) {
		    hasDouble = true;
		    doubles++;
		} else if (!hasTriple && characterMap.get(currentChar) == 3) {
		    hasTriple = true;
		    triples++;
		}
	    }
	}
	return doubles * triples;
    }

    public static void main(String[] args) throws IOException {
	List<String> suitNumbers = aoc18.Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 2\\InputFile.txt"));
	String correctBox = closestMatchingSuitNum(suitNumbers);
	System.out.println(correctBox);

    }
}