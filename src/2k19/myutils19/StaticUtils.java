package myutils19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static int distanceL1(Point2d p1, Point2d p2) {
	return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }

    public static String padWithZeroToLengthFive(String str) {
	while (str.length() < 5) {
	    str = "0" + str;
	}
	return str;
    }

    public static List<List<Integer>> permutations(int[] a){
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
	for(int n : arr) {
	    list.add(n);
	}
	return list;
    }
}
