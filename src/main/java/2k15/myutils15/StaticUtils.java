package myutils15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class StaticUtils {
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

    public static List<Character> fileToCharList(File input) {
	List<Character> chars = new ArrayList<>();
	BufferedReader reader;
	try {
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), Charset.forName("UTF-8")));
	    int c;
	    while ((c = reader.read()) != -1) {
		char character = (char) c;
		chars.add(character);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return chars;
    }

    public static int min(int a, int b, int c) {
	return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    public static String md5(String toHash) {
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    toHash = toHexString(md.digest(toHash.getBytes()));
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}

	return toHash;
    }
    
    public static byte[] md5AsByteArray(String toHash) {
	byte[] bytes = new byte[toHash.length()];
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    bytes = md.digest(toHash.getBytes());
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}

	return bytes;
    }

    public static String toHexString(byte[] messageDigest) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < messageDigest.length; i++) {
	    if ((0xff & messageDigest[i]) < 0x10) {
		sb.append('0');
	    }
	    sb.append(Integer.toHexString(0xff & messageDigest[i]));
	}
	return sb.toString();
    }
    
    public static List<String> permutations(String str) {
	List<String> permutations = new ArrayList<>();
	permutations(str, "", permutations);
	return permutations;
    }
    
    private static void permutations(String str, String tmp, List<String> permutations) {
	if(str.isBlank()) {
	    permutations.add(tmp);
	    return;
	}
	
	for(int i = 0; i < str.length(); i++) {
	    String rest = str.substring(0, i) + str.substring(i + 1);
	    permutations(rest, tmp + str.charAt(i), permutations);
	}
    }
}
