package myutils16;

import java.util.List;

public class MathUtils {

    public static boolean isValidTriangle(int s1, int s2, int s3) {
	return (s1 + s2) > s3 && (s1 + s3) > s2 && (s2 + s3) > s1;
    }

    // x y z
    public static boolean isAlphabeticSorted(List<Character> items) {
	boolean isReverseSorted = true;
	boolean isForwardSorted = true;

	// reverse test
	for (int i = 0; i < items.size() - 1; i++) {
	    if (items.get(i) < items.get(i + 1)) {
		isReverseSorted = false;
		break;
	    }
	}

	// forward test
	for (int i = 0; i < items.size() - 1; i++) {
	    if (items.get(i) > items.get(i + 1)) {
		isForwardSorted = false;
	    }
	}

	return isReverseSorted || isForwardSorted;
    }

    public static boolean isAlphabeticSorted(String str) {
	boolean isReverseSorted = true;
	boolean isForwardSorted = true;

	// reverse test
	for (int i = 0; i < str.length() - 1; i++) {
	    if (str.charAt(i) < str.charAt(i + 1)) {
		isReverseSorted = false;
		break;
	    }
	}

	// forward test
	for (int i = 0; i < str.charAt(i) - 1; i++) {
	    if (str.charAt(i) > str.charAt(i + 1)) {
		isForwardSorted = false;
	    }
	}

	return isReverseSorted || isForwardSorted;
    }

    public static long factorial(int number) {
	long result = 1;

	for (int factor = 2; factor <= number; factor++) {
	    result *= factor;
	}

	return result;
    }
}
