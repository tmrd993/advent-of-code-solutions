package aoc16;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {

    private String inputStr = "01110110101001000";
    private int lenPt1 = 272;
    private int lenPt2 = 35651584;

    private String run(boolean part2) {

	String checksum = inputStr;
	// stretch
	while (checksum.length() < (part2 ? lenPt2 : lenPt1)) {
	    checksum = stretch(checksum);
	}
	// remove unneeded substring
	checksum = checksum.substring(0, checksum.length() - Math.abs(checksum.length() - (part2 ? lenPt2 : lenPt1)));

	// calculate checksum
	while (checksum.length() % 2 == 0) {
	    checksum = checksum(checksum);
	}

	return checksum;
    }

    private String checksum(String input) {
	if (input.length() % 2 != 0) {
	    throw new IllegalArgumentException("input has wrong length");
	}

	StringBuilder res = new StringBuilder();
	for (int i = 0; i < input.length() - 1; i += 2) {
	    if(input.charAt(i) == input.charAt(i + 1)) {
		res.append("1");
	    } else {
		res.append("0");
	    }
	}

	return res.toString();
    }

    private String stretch(String input) {
	return new StringBuffer().append(input).append("0")
		.append(IntStream.rangeClosed(0, input.length() - 1)
			.mapToObj(i -> Character.toString(input.charAt(input.length() - 1 - i)))
			.map(s -> s.equals("0") ? "1" : "0").collect(Collectors.joining()))
		.toString();
    }

    public static void main(String[] args) {
	Day16 test = new Day16();
	System.out.println(test.run(true));
    }

}
