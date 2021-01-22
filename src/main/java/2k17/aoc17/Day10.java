package aoc17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import myutils17.CircularLinkedList;

public class Day10 {

    private List<Integer> lengths;
    private List<Integer> lengthsAscii;
    private CircularLinkedList<Integer> numbers;
    private String input;

    public Day10(String input) {
	this.input = input;
	lengthsAscii = getLengthsASCII(input);
	numbers = new CircularLinkedList<Integer>();
	IntStream.range(0, 256).boxed().forEach(i -> numbers.addLast(i));
    }

    // runs a single round of the knot hash algorithm
    /**
     *
     * @param numbers
     *            circular list of numbers 0 - 255
     * @param currentPosition
     *            should be 0 for solution of part 1
     * @param skipSize
     *            should be 0 for solution of part 1
     * @return the product of the first two numbers in the resulting list
     */
    private int run(CircularLinkedList<Integer> numbers, int currentPosition, int skipSize) {
	lengths = getLengths(input);
	for (Integer length : lengths) {
	    numbers.reverse(currentPosition, length);
	    currentPosition = (currentPosition + length + skipSize) % numbers.size();
	    skipSize++;
	}

	return numbers.get(0) * numbers.get(1);
    }

    // part 2
    public String knotHash() {
	CircularLinkedList<Integer> nums = new CircularLinkedList<>(numbers);
	lengthsAscii.addAll(Arrays.asList(17, 31, 73, 47, 23));

	int currentPosition = 0;
	int skipSize = 0;
	for (int i = 0; i < 64; i++) {
	    for (Integer length : lengthsAscii) {
		nums.reverse(currentPosition, length);
		currentPosition = (currentPosition + length + skipSize) % nums.size();
		skipSize++;
	    }
	}

	List<Integer> denseHash = new ArrayList<>();
	for (int i = 0; i < nums.size(); i += 16) {
	    int hashBlock = nums.get(i);
	    for (int j = i + 1; j < i + 16; j++) {
		hashBlock = hashBlock ^ nums.get(j);
	    }
	    denseHash.add(hashBlock);
	}

	StringBuilder hash = new StringBuilder();
	for (Integer i : denseHash) {
	    String numHex = Integer.toHexString(i);
	    if (numHex.length() == 1) {
		numHex = "0" + numHex;
	    }
	    hash.append(numHex);
	}

	return hash.toString();
    }

    // part 1
    public int getProductOfFirstTwoNumbers() {
	// defensive copy
	CircularLinkedList<Integer> nums = new CircularLinkedList<>(numbers);
	return run(nums, 0, 0);
    }

    private List<Integer> getLengthsASCII(String input) {
	char[] bytes = input.toCharArray();
	List<Integer> lengths = new ArrayList<>();

	for (char c : bytes) {
	    lengths.add((int) c);
	}

	return lengths;
    }

    private List<Integer> getLengths(String input) {
	if (!input.contains(",")) {
	    throw new IllegalArgumentException(
		    "Input needs to be a comma separated string for this part of the puzzle");
	}
	List<Integer> lengths = new ArrayList<>();
	Arrays.stream(input.split(",")).map(Integer::parseInt).forEach(lengths::add);
	return lengths;
    }

    public static void main(String[] args) {

	String puzzleInput = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118";

	Day10 test = new Day10(puzzleInput);
	System.out.println(test.knotHash());

    }
}
