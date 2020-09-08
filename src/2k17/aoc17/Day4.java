package aoc17;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import myutils2k17.CustomString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Day4 {

    private List<CharSequence[]> phrases;

    public Day4(File input) throws IOException {
	phrases = getPhrases(input);
    }

    /**
     *
     * @param partFlag
     *            false for part 1, true for part 2
     * @return number of valid passphrases
     */
    public int numOfValidPhrases(boolean partFlag) {

	List<CharSequence[]> tmpPhrases = new ArrayList<>();
	tmpPhrases = phrases;

	if (partFlag) {
	    // copy the phrases into a new array of type CustomString
	    tmpPhrases = new ArrayList<>();
	    for (CharSequence[] phrase : phrases) {
		CharSequence[] tmpPhrase = new CustomString[phrase.length];
		for (int i = 0; i < phrase.length; i++) {
		    tmpPhrase[i] = new CustomString(phrase[i].toString());
		}
		tmpPhrases.add(tmpPhrase);
	    }
	}

	int count = 0;
	for (CharSequence[] phrase : tmpPhrases) {
	    Set<CharSequence> uniqueWords = new HashSet<>();
	    for (CharSequence word : phrase) {
		uniqueWords.add(word);
	    }

	    if (phrase.length == uniqueWords.size())
		count++;
	}

	return count;
    }

    private List<CharSequence[]> getPhrases(File input) throws IOException {

	List<CharSequence[]> phrases = new ArrayList<>();
	BufferedReader br = new BufferedReader(new FileReader(input));

	String line = "";
	while ((line = br.readLine()) != null) {
	    phrases.add(line.split(" "));
	}

	br.close();
	return phrases;
    }

    public static void main(String[] args) throws IOException {
	Day4 test = new Day4(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 4\\InputFile.txt"));
	int res1 = test.numOfValidPhrases(false);
	int res2 = test.numOfValidPhrases(true);
	System.out.println(res1 + "\n" + res2);
    }

}
