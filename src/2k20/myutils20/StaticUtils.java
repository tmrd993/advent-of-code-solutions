package myutils20;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaticUtils {
    
    public static List<Integer> inputFileToIntList(File inputFile) {
	List<Integer> numbers = new ArrayList<>();
	try {
	    Scanner sc = new Scanner(inputFile);
	    while(sc.hasNextInt()) {
		numbers.add(sc.nextInt());
	    }
	    sc.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return numbers;
    }
}
