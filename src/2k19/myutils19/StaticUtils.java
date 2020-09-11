package myutils19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	    while((line = br.readLine()) != null) {
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
	    while(sc.hasNext()) {
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
	while(str.length() < 5) {
	    str = "0" + str;
	}
	return str;
    }
}
