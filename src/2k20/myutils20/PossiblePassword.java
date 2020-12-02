package myutils20;

// object representing a possible password for day 2
public class PossiblePassword {
    
    private final int minCount;
    private final int maxCount;
    private final char letter;
    private final String password;
    
    public PossiblePassword(int min, int max, char letter, String password) {
	minCount = min;
	maxCount = max;
	this.letter = letter;
	this.password = password;
    }

    public int getMinCount() {
	return minCount;
    }

    public int getMaxCount() {
	return maxCount;
    }

    public char getLetter() {
	return letter;
    }

    public String getPassword() {
	return password;
    }
    
    @Override
    public String toString() {
	return minCount + "-" + maxCount + " " + letter + ": " + password; 
    }
}
