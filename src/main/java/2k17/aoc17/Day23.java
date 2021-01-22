package aoc17;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.stream.IntStream;

public class Day23 {

    private List<String> instructions;
    private Map<Character, Long> registers;
    private final int NUM_OF_REGISTERS = 8;

    public Day23(File input) {
	instructions = getInstructions(input);
    }

    public int numOfMulInstr() {
	int count = 0;
	registers = new HashMap<>();
	int a = 'a';
	IntStream.range(a, a + NUM_OF_REGISTERS).forEach(num -> registers.put((char) num, 0L));

	int currentIndex = 0;
	while (currentIndex >= 0 && currentIndex < instructions.size()) {
	    String currentInstruction = instructions.get(currentIndex);

	    if (currentInstruction.substring(0, 3).equals("mul")) {
		count++;
	    }

	    currentIndex += executeInstruction(currentInstruction, registers);
	}

	return count;
    }

    // instructions translated to control flow statements
    public int optimizedCom() {
	int b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;
	int a = 1;

	b = 65;
	c = b;

	if (a != 0) {
	    b = (b * 100) + 100000;
	    c = b + 17000;
	}

	// at this point:
	// a == 1
	// b == 106500
	// c == 123500

	// outer loop terminates when b == c
	// c never changes, serves as termination condition
	// b only gets +17 added for every execution of the outer loop

	do {
	    f = 1;
	    d = 2;
	    do { // jnz g -13
		e = 2;

		do { // jnz g - 8
		    g = (d * e) - b;

		    if (g == 0)
			f = 0;

		    e++;

		    g = e - b;
		} while (g != 0);

		d++;

		g = d - b;
	    } while (g != 0);

	    // increments h if register b contains a non-prime number (checked by printing all values)
	    if (f == 0) {
		h++;
		System.out.println("b: " + b + "  c: " + c + "  d: " + d + "  e: " + e + "  f: " + f + "  g: " + g);
	    }

	    // c does not change, b only gets increased by 17
	    // if b == c, the method returns h which is the number of non-prime numbers encountered in the loop
	    g = b - c;

	    if (g == 0)
		return h;

	    b += 17;

	} while (true);
    }

    // part 2
    public int hValue() {
	int count = 0;
	for(int i = 106500; i <= 123500; i += 17) {
	    if(!isPrimeNumber(i))
		count++;
	}
	return count;
    }

    private static boolean isPrimeNumber(int num) {
	for(int i = 2; i < num; i++) {
	    if(num % i == 0)
		return false;
	}
	return true;
    }

    private long executeInstruction(String instruction, Map<Character, Long> registers) {
	String cmd = instruction.substring(0, 3);
	char register = instruction.charAt(4);
	String value = instruction.substring(6);

	// System.out.println(instruction + " val at " + register + " : " +
	// registers.get(register));

	if (cmd.equals("set")) {
	    Day18.set(register, value, registers);
	} else if (cmd.equals("sub")) {
	    Day18.sub(register, value, registers);
	} else if (cmd.equals("mul")) {
	    Day18.mul(register, value, registers);
	} else if (cmd.equals("jnz")) {
	    Long offset = Day18.jnz(register, value, registers);
	    if (offset == null) {
		return 1;
	    }

	    return offset;
	}

	return 1;
    }

    private List<String> getInstructions(File input) {
	List<String> instructions = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		instructions.add(line);
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return instructions;
    }

    public static void main(String[] args) {
	Day23 test = new Day23(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 23\\InputFile1.txt"));
	// System.out.println(test.hValue());
	System.out.println(test.hValue());

    }

}
