package aoc15;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Day11 {
    private final String input = "hxbxwxba";
    private final Set<Integer> forbiddenCharacters = Set.of((int) 'i', (int) 'o', (int) 'l');
    // ascii for 'a'
    private final int LIMIT_LOW = 97;
    // ascii for 'z'
    private final int LIMIT_HIGH = 122;
    
    public String run1() {
	int[] password = input.chars().toArray();
	return nextPassword(password);
    }
    
    public String run2() {
	int[] password = run1().chars().toArray();
	incrementPassword(password, password.length - 1);
	return nextPassword(password);
    }
    
    private String nextPassword(int[] password) {
	while(!hasIncreasingTriple(password) || !hasTwoPairs(password)) {
	    incrementPassword(password, password.length - 1);
	}
	
	return Arrays.stream(password).mapToObj(digit -> new String(Character.toString((char) digit))).collect(Collectors.joining());
    }
    
    private void incrementPassword(int[] password, int digit) {
	if(digit < 0 || digit >= password.length) {
	    return;
	}
	
	while(forbiddenCharacters.contains(password[digit])) {
	    password[digit]++;
	}
	
	if(password[digit] == LIMIT_HIGH) {
	    password[digit] = LIMIT_LOW;
	    incrementPassword(password, digit - 1);
	} else {
	    password[digit]++;
	}
    }
    
    private boolean hasIncreasingTriple(int[] password) {
	for(int i = 0; i < password.length - 2; i++) {
	    if(password[i] == password[i + 1] - 1 && password[i] == password[i + 2] - 2) {
		return true;
	    }
	}
	return false;
    }
    
    private boolean hasTwoPairs(int[] password) {
	int pairCount = 0;
	for(int i = 0; i < password.length - 1; i++) {
	    int current = password[i];
	    int next = password[i + 1];
	    
	    if(current == next) {
		pairCount++;
		i++;
	    }
	}
	return pairCount == 2;
    }
    
    public static void main(String[] args) {
	Day11 test = new Day11();
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
