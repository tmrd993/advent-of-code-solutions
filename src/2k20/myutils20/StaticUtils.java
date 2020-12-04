package myutils20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    
    public static List<String> inputFileToStringList(File inputFile) {
	List<String> inputList = new ArrayList<>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(inputFile));
	    String line = "";
	    while((line = br.readLine()) != null) {
		inputList.add(line);
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	return inputList;
    }
    
    public static String extractLeftSideDigits(String str) {
	String res = "";
	for(int i = 0; i < str.length(); i++) {
	    if(Character.isDigit(str.charAt(i))) {
		res = res + str.charAt(i);
	    }
	}
	return res;
    }
}
