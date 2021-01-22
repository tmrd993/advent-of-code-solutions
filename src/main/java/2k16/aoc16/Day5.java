package aoc16;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import myutils16.StaticUtils;

public class Day5 {

    private final String INPUT = "wtnhxymk";

    public String run1() {
	StringBuilder password = new StringBuilder();
	int index = 0;

	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    while (password.length() < 8) {
		// System.out.println(index);
		byte[] res = md.digest((INPUT + index).getBytes("UTF-8"));
		String hex = StaticUtils.bytesToHex(res);
		if (hex.substring(0, 5).equals("00000")) {
		    password.append(hex.charAt(5));
		}
		index++;
	    }

	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}

	return password.toString();
    }

    public String run2() {
	char[] password = new char[8];
	Set<Integer> marked = new HashSet<>();
	int index = 0;

	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    while (marked.size() < 8) {
		// System.out.println(index);
		byte[] res = md.digest((INPUT + index).getBytes("UTF-8"));
		String hex = StaticUtils.bytesToHex(res);
		if (hex.substring(0, 5).equals("00000")) {
		    if(!Character.isDigit(hex.charAt(5))) {
			index++;
			continue;
		    }
		    
		    int pos = Character.digit(hex.charAt(5), 10);
		    if(pos < 8 && !marked.contains(pos)) {
			marked.add(pos);
			password[pos] = hex.charAt(6);
		    }
		}
		index++;
	    }

	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	
	return new String(password);
    }

    public static void main(String[] args) {
	Day5 test = new Day5();
	System.out.println(test.run2());
    }
}
