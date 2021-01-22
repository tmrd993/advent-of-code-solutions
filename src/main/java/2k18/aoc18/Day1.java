package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day1 {
    // part2
    public static long firstRepeatingFrequency(List<String> frequencyShifts) {
	Set<Long> uniqueFrequencies = new HashSet<Long>();
	long currentFrequency = 0;
	uniqueFrequencies.add(currentFrequency);
	while (true) {
	    for (String st : frequencyShifts) {
		char currentOp = st.charAt(0);
		int num = Integer.parseInt(st.substring(1));

		switch (currentOp) {
		case '+':
		    currentFrequency += num;
		    break;
		case '-':
		    currentFrequency -= num;
		    break;
		default:
		    System.err.println("unknown operator encountered at " + st);
		}

		if (uniqueFrequencies.contains(currentFrequency))
		    return currentFrequency;
		else
		    uniqueFrequencies.add(currentFrequency);
	    }
	}
    }

    // part 1
    public static long resultingFrequency(List<String> frequencyShifts) {
	long result = 0;
	for (String st : frequencyShifts) {
	    char currentOp = st.charAt(0);
	    int num = Integer.parseInt(st.substring(1));

	    switch (currentOp) {
	    case '+':
		result += num;
		break;
	    case '-':
		result -= num;
		break;
	    default:
		System.err.println("unknown operator encountered for String " + st);
	    }
	}
	return result;
    }

    public static List<String> inputFileToList(File inputFile) throws IOException {
	Scanner sc = new Scanner(inputFile);

	List<String> commandList = new ArrayList<String>();
	while (sc.hasNextLine()) {
	    commandList.add(sc.nextLine());
	}
	sc.close();
	return commandList;
    }

    public static void main(String[] args) throws IOException {
	File input = new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 1\\InputFile.txt");
	List<String> frequencyShifts = inputFileToList(input);

	long result = firstRepeatingFrequency(frequencyShifts);

	System.out.println(result);
    }
}