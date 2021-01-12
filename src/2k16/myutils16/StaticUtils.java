package myutils16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StaticUtils {

    // standard 26 letter english alphabet
    public static List<Character> alphabet = IntStream.range(65, 91).mapToObj(n -> (char) n)
	    .collect(Collectors.toList());

    public static List<String> inputToList(File input) {
	List<String> list = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";

	    while ((line = br.readLine()) != null) {
		list.add(line);
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return list;
    }
    
    public static String bytesToHex(byte[] bytes) {
	StringBuilder sb = new StringBuilder();

	for (byte b : bytes) {
	    int high = (b >> 4) & 0x0F;
	    int low = b & 0x0F;

	    sb.append(intToHexDigit(high));
	    sb.append(intToHexDigit(low));
	}
	return sb.toString();
    }
    
    private static char intToHexDigit(int val) {
	if (val < 0 || val > 15) {
	    throw new IllegalArgumentException();
	}

	return "0123456789abcdef".charAt(val);
    }

}
