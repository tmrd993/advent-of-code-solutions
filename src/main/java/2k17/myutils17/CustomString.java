package myutils17;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Timucin Merdin
 * 
 *         Custom String class that treats anagrams as equal Strings
 */
public class CustomString implements CharSequence {
    private final String STR;

    public CustomString(String str) {
	this.STR = str;
    }

    @Override
    public String toString() {
	return STR;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (!(o instanceof CustomString))
	    return false;

	CustomString tmp = (CustomString) o;

	if (this.STR.length() != tmp.STR.length())
	    return false;

	Set<Character> uniqueChars = new HashSet<>();
	for (int i = 0; i < this.STR.length(); i++)
	    uniqueChars.add(this.STR.charAt(i));

	for (int i = 0; i < this.STR.length(); i++) {
	    if (!uniqueChars.contains(tmp.STR.charAt(i)))
		return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	// make sure that all anagrams have the same hashcode
	char[] chars = STR.toCharArray();
	Arrays.sort(chars);
	String st = new String(chars);
	return Objects.hash(st);
    }

    @Override
    public char charAt(int index) {
	return STR.charAt(index);
    }

    @Override
    public int length() {
	return STR.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
	return STR.subSequence(start, end);
    }
}
