package aoc17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Day18 {

    private Map<Character, Long> registers;
    private List<String> instructions;

    public Day18(File input) {
	instructions = getInstructions(input);
	registers = getRegisters();
    }

    // part 2
    public int numOfSndCmdProgram1() {
	int count = 0;

	Map<Character, Long> program1 = new HashMap<>();
	Map<Character, Long> program2 = new HashMap<>();

	for (Map.Entry<Character, Long> e : registers.entrySet()) {
	    program1.putIfAbsent(e.getKey(), 0L);
	    program2.putIfAbsent(e.getKey(), 0L);
	}

	program2.put('p', 1L);

	// if both counters reach 4, the program ends.
	int deadLockCounterP1 = 0;
	int deadLockCounterP2 = 0;

	// current indicies for program 1 and 2
	int indexP1 = 0;
	int indexP2 = 0;

	// received values for program 1 and 2 (program 1 sends values to queue
	// 2, program 2 sends values to queue 1)
	Queue<Long> rcvP1 = new LinkedList<>();
	Queue<Long> rcvP2 = new LinkedList<>();

	int indexP1Before = 0;
	int indexP2Before = 0;
	while ((indexInRange(indexP1, instructions.size()) || (indexInRange(indexP2, instructions.size())))) {

	    String instructionP1 = null;
	    String instructionP2 = null;

	    if (indexInRange(indexP1, instructions.size())) {
		instructionP1 = instructions.get(indexP1);
		indexP1 += executeInstruction(instructionP1, program1, rcvP2, rcvP1);
	    }

	    int sndCountP1 = rcvP2.size();

	    if (indexInRange(indexP2, instructions.size())) {
		instructionP2 = instructions.get(indexP2);
		indexP2 += executeInstruction(instructionP2, program2, rcvP1, rcvP2);
	    }

	    // check if program 2 sent a value to program 1
	    if (sndCountP1 != rcvP2.size())
		count++;

	    // check if programs are in deadlock
	    if (indexP1Before == indexP1)
		deadLockCounterP1++;
	    else
		deadLockCounterP1 = 0;
	    if (indexP2Before == indexP2)
		deadLockCounterP2++;
	    else
		deadLockCounterP2 = 0;

	    if (deadLockCounterP1 >= 4 && deadLockCounterP2 >= 4)
		break;

	    indexP1Before = indexP1;
	    indexP2Before = indexP2;

	}

	return count;
    }

    private long executeInstruction(String instruction, Map<Character, Long> registers, Queue<Long> rcvValues,
	    Queue<Long> sndValues) {
	String cmd = instruction.substring(0, 3);
	char register = instruction.charAt(4);

	if (cmd.equals("snd")) {
	    sndValues.offer(snd(register, registers));
	} else if (cmd.equals("set")) {
	    set(register, instruction.substring(6), registers);
	} else if (cmd.equals("add")) {
	    add(register, instruction.substring(6), registers);
	} else if (cmd.equals("mul")) {
	    mul(register, instruction.substring(6), registers);
	} else if (cmd.equals("mod")) {
	    mod(register, instruction.substring(6), registers);
	} else if (cmd.equals("rcv")) {
	    if (rcvValues.isEmpty()) {
		return 0;
	    }
	    registers.put(register, rcvValues.poll());

	} else if (cmd.equals("jgz")) {
	    Long offset = jgz(register, instruction.substring(6), registers);
	    if (offset == null)
		return 1;
	    else
		return offset;
	}

	return 1;
    }

    private static boolean indexInRange(int index, int maxSize) {
	return index >= 0 && index < maxSize;
    }

    // part 1
    public long firstRcvValue() {
	int firstRcv = 0;
	long lastSound = 0;

	int index = 0;
	while (index < instructions.size() && index >= 0) {

	    String instruction = instructions.get(index);
	    String cmd = instruction.substring(0, 3);
	    char register = instruction.charAt(4);

	    if (cmd.equals("snd")) {
		lastSound = snd(register, registers);
	    } else if (cmd.equals("set")) {
		set(register, instruction.substring(6), registers);
	    } else if (cmd.equals("add")) {
		add(register, instruction.substring(6), registers);
	    } else if (cmd.equals("mul")) {
		mul(register, instruction.substring(6), registers);
	    } else if (cmd.equals("mod")) {
		mod(register, instruction.substring(6), registers);
	    } else if (cmd.equals("rcv")) {
		Long rcvVal = rcv(register, lastSound, registers);
		if (rcvVal != null)
		    return lastSound;
	    } else if (cmd.equals("jgz")) {
		Long offset = jgz(register, instruction.substring(6), registers);
		if (offset == null)
		    index++;
		else
		    index += offset;
		continue;
	    }
	    index++;
	}
	return firstRcv;
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

    public static void set(char x, String y, Map<Character, Long> registers) {
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}
	// System.out.println("set: " + value);
	registers.put(x, value);
    }

    private static void add(char x, String y, Map<Character, Long> registers) {
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}
	// System.out.println("add: " + value);
	registers.put(x, registers.get(x) + value);
    }

    public static void sub(char x, String y, Map<Character, Long> registers) {
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}
	// System.out.println("add: " + value);
	registers.put(x, registers.get(x) - value);
    }

    public static Long jnz(char x, String y, Map<Character, Long> registers) {
	// check if offset value is a register or a value
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}

	// check if instruction takes a register or a value
	Long registerValue = Character.isDigit(x) ? Long.parseLong(Character.toString(x)) : null;
	if (registerValue == null) {
	    return registers.get(x) != 0 ? value : null;
	}
	// System.out.println("jgz: " + value + " " + x);

	return registerValue != 0 ? value : null;
    }

    public static void mul(char x, String y, Map<Character, Long> registers) {
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}
	// System.out.println("mul: " + value);
	registers.put(x, registers.get(x) * value);
    }

    private static void mod(char x, String y, Map<Character, Long> registers) {
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}
	// System.out.println("mod: " + value);
	registers.put(x, registers.get(x) % value);
    }

    private static Long rcv(char x, long lastPlayedSound, Map<Character, Long> registers) {
	// System.out.println("rcv: " + registers.get(x));
	return registers.get(x) != 0 ? lastPlayedSound : null;
    }

    private static Long jgz(char x, String y, Map<Character, Long> registers) {
	// check if offset value is a register or a value
	Long value = isNumeric(y) ? Long.parseLong(y) : null;
	if (value == null) {
	    value = registers.get(y.charAt(0));
	}

	// check if instruction takes a register or a value
	Long registerValue = Character.isDigit(x) ? Long.parseLong(Character.toString(x)) : null;
	if (registerValue == null) {
	    return registers.get(x) > 0 ? value : null;
	}
	// System.out.println("jgz: " + value + " " + x);

	return registerValue > 0 ? value : null;
    }

    private static long snd(char x, Map<Character, Long> registers) {
	return Character.isDigit(x) ? Integer.parseInt(Character.toString(x)) : registers.get(x);
    }

    public static boolean isNumeric(String str) {
	return str.matches("-?\\d+(\\.\\d+)?");
    }

    private Map<Character, Long> getRegisters() {
	if (instructions.isEmpty())
	    return null;

	Map<Character, Long> registers = new HashMap<>();
	for (String instruction : instructions) {
	    char register = instruction.charAt(4);
	    if (!Character.isDigit(register))
		registers.putIfAbsent(register, 0L);
	}

	return registers;
    }

    public static void main(String[] args) {
	Day18 test = new Day18(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 18\\InputFile1.txt"));
	// long res1 = test.firstRcvValue();
	int res2 = test.numOfSndCmdProgram1();
	System.out.println(res2);

    }

}
