package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myutils20.PossiblePassword;
import myutils20.StaticUtils;

public class Day2 {
    
    private List<String> passwordsWithRequirements;
    private List<PossiblePassword> possiblePasswords;

    public Day2(File inputFile) {
	passwordsWithRequirements = StaticUtils.inputFileToStringList(inputFile);
	possiblePasswords = getPossiblePasswords(passwordsWithRequirements);
    }
    
    public int run1() {
	return (int) possiblePasswords.stream().filter(p -> isValidPasswordPart1(p)).count();
    }
    
    public int run2() {
	return (int) possiblePasswords.stream().filter(p -> isValidPasswordPart2(p)).count();
    }
    
    private boolean isValidPasswordPart2(PossiblePassword possiblePassword) {
	int firstIndex = possiblePassword.getMinCount() - 1;
	int secondIndex = possiblePassword.getMaxCount() - 1;
	char letter = possiblePassword.getLetter();
	
	String password = possiblePassword.getPassword();
	
	int firstValuePresent = 0;
	int secondValuePresent = 0;
	
	if(firstIndex < password.length()) {
	    firstValuePresent = password.charAt(firstIndex) == letter ? 1 : 0;
	}
	
	if(secondIndex < password.length()) {
	    secondValuePresent = password.charAt(secondIndex) == letter ? 1 : 0;
	}
	
	return (firstValuePresent ^ secondValuePresent) == 1 ? true : false;
    }
    
    private boolean isValidPasswordPart1(PossiblePassword possiblePassword) {
	Map<Character, Integer> characterFrequencies = new HashMap<>();
	
	char letter = possiblePassword.getLetter();
	int minCount = possiblePassword.getMinCount();
	int maxCount = possiblePassword.getMaxCount();
	String password = possiblePassword.getPassword();
	
	for(int i = 0; i < password.length(); i++) {
	    characterFrequencies.putIfAbsent(password.charAt(i), 0);
	    characterFrequencies.put(password.charAt(i), characterFrequencies.get(password.charAt(i)) + 1);
	}
	
	characterFrequencies.putIfAbsent(letter, 0);
	int letterFrequency = characterFrequencies.get(letter);
	
	return letterFrequency >= minCount && letterFrequency <= maxCount;
    }
    
    private List<PossiblePassword> getPossiblePasswords(List<String> passwordsWithRequirements) {
	List<PossiblePassword> possiblePasswords = new ArrayList<>();
	
	for(String pass : passwordsWithRequirements) {
	    int minCount = Integer.parseInt(pass.substring(0, pass.indexOf('-')));
	    int maxCount = Integer.parseInt(pass.substring(pass.indexOf('-') + 1, pass.indexOf(' ')));
	    char letter = pass.charAt(pass.indexOf(' ') + 1);
	    String password = pass.substring(pass.indexOf(':') + 2);
	    
	    possiblePasswords.add(new PossiblePassword(minCount, maxCount, letter, password));
	}
	
	return possiblePasswords;
    }
    
    public static void main(String[] args) {
	Day2 test = new Day2(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 2\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
