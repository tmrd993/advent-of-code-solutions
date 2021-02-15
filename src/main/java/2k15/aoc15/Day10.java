package aoc15;

public class Day10 {
    private final String input = "1113222113";
    
    public int run1() {
	String out = generateSequences(40);
	return out.length();
    }
    
    public int run2() {
	String out = generateSequences(50);
	return out.length();
    }
    
    private String generateSequences(int amount) {
	String out = input;
	for(int i = 0; i < amount; i++) {
	    out = generateNextSequence(out);
	}
	return out;
    }
    
    private String generateNextSequence(String in) {
	StringBuffer out = new StringBuffer();
	for(int i = 0; i < in.length(); i++) {
	    int duplicates = 1;
	    
	    for(int j = i + 1; j < in.length(); j++) {
		if(in.charAt(j) == in.charAt(i)) {
		    duplicates++;
		} else {
		    break;
		}
	    }
	    i += duplicates - 1;
	    out.append(duplicates).append(in.charAt(i));
	}
	
	return out.toString();
    }
    
    public static void main(String[] args) {
	Day10 test = new Day10();
	System.out.println(test.run1());
    }

}
