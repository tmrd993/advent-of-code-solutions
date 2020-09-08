package aoc18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Day5 {

    public static StringBuilder inputfileToString(File file) throws IOException {
	StringBuilder result = new StringBuilder("");
	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	int c = 0;
	while ((c = br.read()) != -1) {
	    result.append((char) c);
	}
	br.close();
	return result;
    }

    // part 1 solution
    public static int reactingPolymerLength(StringBuilder input) {
	int i = 0;
	while (i < input.length() - 1) {
	    if ((Character.isLowerCase(input.charAt(i)) && Character.isUpperCase(input.charAt(i + 1))
		    || (Character.isUpperCase(input.charAt(i)) && Character.isLowerCase(input.charAt(i + 1))))) {
		if (Character.toUpperCase(input.charAt(i)) == input.charAt(i + 1)
			|| Character.toLowerCase(input.charAt(i)) == input.charAt(i + 1)) {
		    input.delete(i, i + 2);
		    i = Math.max(0, i - 1);
		    continue;
		}
	    }
	    i++;
	}
	return input.length();
    }

    // part 2 solution
    // could be improved by using the resulting string from solution one
    public static int shortestPolymerLength(String input) {
	int shortest = Integer.MAX_VALUE;
	// ASCII a - z
	for (int i = 65; i <= 90; i++) {
	    if (input.contains(Character.toString((char) i))) {
		// replace ASCII a-z and A-Z
		String inputWithoutLetterI = input.replaceAll(Character.toString((char) i), "")
			.replaceAll(Character.toString((char) (i + 32)), "");
		int polymerLength = reactingPolymerLength(new StringBuilder(inputWithoutLetterI));
		if (shortest > polymerLength)
		    shortest = polymerLength;
	    }
	}
	return shortest;
    }

    public static void main(String[] args) throws IOException {
	StringBuilder input = inputfileToString(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 5\\InputFile.txt"));

	System.out.println(shortestPolymerLength(input.toString()));
    }
}