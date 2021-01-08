package myutils19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StaticUtils {

    /**
     * reads the contents of a file line by line and returns the contents as a list
     * of strings
     */
    public static List<String> fileToStringList(File file) {
	List<String> content = new ArrayList<>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		content.add(line);
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return content;
    }

    public static List<Integer> commaSeperatedIntegerFileToList(File file) {
	List<Integer> nums = new ArrayList<>();
	try {
	    Scanner sc = new Scanner(file);
	    sc.useDelimiter(",");
	    while (sc.hasNext()) {
		nums.add(Integer.parseInt(sc.next()));
	    }
	    sc.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return nums;
    }

    public static List<Long> commaSeperatedLongFileToList(File file) {
	List<Long> nums = new ArrayList<>();
	try {
	    Scanner sc = new Scanner(file);
	    sc.useDelimiter(",");
	    while (sc.hasNext()) {
		nums.add(Long.parseLong(sc.next()));
	    }
	    sc.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return nums;
    }

    public static List<Integer> digitFileToList(File input) {
	List<Integer> digits = new ArrayList<>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		line.chars().map(c -> (char) c).forEach(c -> digits.add(Character.getNumericValue(c)));
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return digits;
    }

    public static int distanceL1(Point2d p1, Point2d p2) {
	return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }

    public static String padWithZeroToLengthFive(String str) {
	while (str.length() < 5) {
	    str = "0" + str;
	}
	return str;
    }

    public static List<List<Integer>> permutations(int[] a) {
	List<List<Integer>> perms = new ArrayList<>();
	permutations(a, a.length, a.length, perms);
	return perms;
    }

    // heaps algorithm to compute all permutations of an array
    private static void permutations(int a[], int size, int n, List<List<Integer>> perms) {
	if (size == 1) {
	    perms.add(intArrayToIntegerList(Arrays.copyOfRange(a, 0, n)));
	}

	for (int i = 0; i < size; i++) {
	    permutations(a, size - 1, n, perms);
	    if (size % 2 == 1) {
		int temp = a[0];
		a[0] = a[size - 1];
		a[size - 1] = temp;
	    }

	    else {
		int temp = a[i];
		a[i] = a[size - 1];
		a[size - 1] = temp;
	    }
	}
    }

    private static List<Integer> intArrayToIntegerList(int[] arr) {
	List<Integer> list = new ArrayList<>(arr.length);
	for (int n : arr) {
	    list.add(n);
	}
	return list;
    }

    public static double calcRotationAngleInDegrees(Point2d centerPt, Point2d targetPt) {
	// calculate the angle theta from the deltaY and deltaX values
	// (atan2 returns radians values from [-PI,PI])
	// 0 currently points EAST.
	// NOTE: By preserving Y and X param order to atan2, we are expecting
	// a CLOCKWISE angle direction.
	double theta = Math.atan2(targetPt.y() - centerPt.y(), targetPt.x() - centerPt.x());

	// rotate the theta angle clockwise by 90 degrees
	// (this makes 0 point NORTH)
	// NOTE: adding to an angle rotates it clockwise.
	// subtracting would rotate it counter-clockwise
	theta += Math.PI / 2.0;

	// convert from radians to degrees
	// this will give you an angle from [0->270],[-180,0]
	double angle = Math.toDegrees(theta);

	// convert to positive range [0-360)
	// since we want to prevent negative angles, adjust them now.
	// we can assume that atan2 will not return a negative value
	// greater than one partial rotation
	if (angle < 0) {
	    angle += 360;
	}
	return angle;
    }

    public static long leastCommonMultiple(long number1, long number2) {
	if (number1 == 0 || number2 == 0) {
	    return 0;
	}
	long absNumber1 = Math.abs(number1);
	long absNumber2 = Math.abs(number2);
	long absHigherNumber = Math.max(absNumber1, absNumber2);
	long absLowerNumber = Math.min(absNumber1, absNumber2);
	long lcm = absHigherNumber;
	while (lcm % absLowerNumber != 0) {
	    lcm += absHigherNumber;
	}
	return lcm;
    }

    public static String repeat(String str, int n) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < n; i++) {
	    sb.append(str);
	}
	return sb.toString();
    }
}
