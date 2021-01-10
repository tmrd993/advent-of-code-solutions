package aoc19;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.LongStream;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day25 {

    private List<Long> initialProgram;
    private final long[] north = { 'n', 'o', 'r', 't', 'h', 10 };
    private final long[] south = { 's', 'o', 'u', 't', 'h', 10 };
    private final long[] west = { 'w', 'e', 's', 't', 10 };
    private final long[] east = { 'e', 'a', 's', 't', 10 };
    private final long[] inv = { 'i', 'n', 'v', 10 };
    private final long[] take = { 't', 'a', 'k', 'e', ' ' };
    private final long[] drop = { 'd', 'r', 'o', 'p', ' ' };

    public Day25(File inputFile) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(inputFile);
    }

    // manual solution by playing the interactive game!
    public void run1() {

	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));

	boolean gameOver = false;
	while (!gameOver) {
	    computer.run();

	    String output = "";
	    Queue<Long> outputQueue = computer.outputValues();
	    while (!outputQueue.isEmpty()) {
		output += (char) outputQueue.poll().intValue();
	    }

	    System.out.println(output);
	    System.out.println("1: NORTH\n2: SOUTH\n3: EAST\n4: WEST\n5: PICK\n6: DROP\n7: INV\n8: QUIT");

	    String choiceStr = "";
	    try {
		InputStreamReader streamReader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		choiceStr = bufferedReader.readLine();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    int choice = Integer.parseInt(choiceStr);

	    if (choice == 1) {
		computer.setInputValues(north);
	    } else if (choice == 2) {
		computer.setInputValues(south);
	    } else if (choice == 3) {
		computer.setInputValues(east);
	    } else if (choice == 4) {
		computer.setInputValues(west);
	    } else if (choice == 5) {
		String item = "";
		try {
		    System.out.println("Enter the name of the item you want to pick up: ");
		    InputStreamReader streamReader = new InputStreamReader(System.in);
		    BufferedReader bufferedReader = new BufferedReader(streamReader);
		    item = bufferedReader.readLine();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		computer.setInputValues(composeItemCmd(item, take));
		System.out.println("picked up " + item);
	    } else if (choice == 6) {
		String item = "";
		try {
		    System.out.println("Enter the name of the item you want to drop: ");
		    InputStreamReader streamReader = new InputStreamReader(System.in);
		    BufferedReader bufferedReader = new BufferedReader(streamReader);
		    item = bufferedReader.readLine();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		computer.setInputValues(composeItemCmd(item.trim(), drop));
		System.out.println("dropped " + item);
	    } else if (choice == 7) {
		computer.setInputValues(inv);
	    } else if (choice == 8) {
		gameOver = true;
		System.out.println("Quitting...");
	    }

	}

    }

    // should be used with cmd == take or drop
    private long[] composeItemCmd(String item, long[] cmd) {
	return LongStream.concat(LongStream.concat(Arrays.stream(cmd), item.chars().asLongStream()), LongStream.of(10))
		.toArray();
    }

    public static void main(String[] args) {
	Day25 test = new Day25(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 25\\InputFile.txt"));
	test.run1();

    }

}
