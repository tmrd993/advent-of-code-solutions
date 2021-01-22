package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils16.StaticUtils;

public class Day7 {

    private List<String> ipList;

    public Day7(File input) {
	ipList = StaticUtils.inputToList(input);
    }

    private static String[] splitAtBrackets(String ip) {
	List<String> parts = new ArrayList<>();

	int index = 0;
	StringBuilder part = new StringBuilder();
	while (index < ip.length()) {
	    if (ip.charAt(index) == '[') {
		index++;
		part = new StringBuilder();
		while (ip.charAt(index) != ']') {
		    part.append(ip.charAt(index++));
		}
		parts.add(part.toString());

	    }
	    index++;
	}

	String[] partsArr = new String[parts.size()];
	return parts.toArray(partsArr);
    }

    public int run1() {
	int count = 0;
	for (String ip : ipList) {
	    if (supportsTLS(ip))
		count++;
	}
	return count;
    }
    
    public int run2() {
	int count = 0;
	for(String ip : ipList) {
	    if(supportsSSL(ip))
		count++;
	}
	return count;
    }

    private boolean supportsTLS(String ip) {
	String[] insideBrackets = splitAtBrackets(ip);
	String[] outsideBrackets = ip.split("\\[(.*?)\\]");

	// check if abba is inside brackets
	for (String subIp : insideBrackets) {
	    for (int i = 0; i < subIp.length() - 3; i++) {
		if (isABBA(subIp.substring(i, i + 4))) {
		    return false;
		}
	    }
	}
	// check if abba is outside brackets
	for (String subIp : outsideBrackets) {
	    for (int i = 0; i < subIp.length() - 3; i++) {
		if (isABBA(subIp.substring(i, i + 4))) {
		    return true;
		}
	    }
	}

	return false;
    }
    
    private boolean supportsSSL(String ip) {
	String[] insideBrackets = splitAtBrackets(ip);
	String[] outsideBrackets = ip.split("\\[(.*?)\\]");
	
	for(String subIp : outsideBrackets) {
	    for(int i = 0; i < subIp.length() - 2; i++) {
		String currentSubIp = subIp.substring(i, i + 3);
		if(isABA(currentSubIp)) {
		    for(String subIpInBracket : insideBrackets) {
			for(int j = 0; j < subIpInBracket.length() - 2; j++) {
			    if(isBAB(subIpInBracket.substring(j, j + 3), currentSubIp)) {
				return true;
			    }
			}
		    }
		}
	    }
	}
	return false;
    }

    private boolean isABBA(String str) {
	return (str.charAt(0) != str.charAt(1)) && (str.charAt(2) == str.charAt(1) && str.charAt(0) == str.charAt(3));
    }
    
    private boolean isABA(String str) {
	return (str.charAt(0) == str.charAt(2)) && str.charAt(0) != str.charAt(1);
    }
    
    private boolean isBAB(String str, String aba) {
	return (aba.charAt(0) == str.charAt(1)) && (aba.charAt(1) == str.charAt(0) && aba.charAt(1) == str.charAt(2));
    }

    public static void main(String[] args) {
	Day7 test = new Day7(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 7\\InputFile1.txt"));
	System.out.println(test.run2());

    }

}
