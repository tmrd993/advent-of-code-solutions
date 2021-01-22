package aoc17;

import java.util.List;

import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

import java.io.IOException;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {

    private final int DANCER_COUNT = 16;
    private Queue<String> danceCommands;

    public Day16(File input) {
	danceCommands = getDanceCommands(input);
    }

    /**
     * part 2
     *
     * @return permutation after 1.000.000.000 dances using cycle detection and
     *         modulo arithmetic
     */
    public String lastDance() {
	Set<String> uniqueDances = new LinkedHashSet<>();
	List<Character> dancers = getDancers();
	// add the initial state
	uniqueDances.add("abcdefghijklmnop");
	String dance = executeDance(dancers);

	// repeat till dances start to repeat
	while (!uniqueDances.contains(dance)) {
	    uniqueDances.add(dance);
	    dance = executeDance(dancers);
	}

	// calculate amount of dances left
	int repeatCount = uniqueDances.size();
	long dancesLeft = 1000000000L - repeatCount;

	// get the target index
	int indexOfTargetVal = (int) (dancesLeft % repeatCount);

	int count = 0;
	String target = "";
	for (String currentDance : uniqueDances) {
	    if (count == indexOfTargetVal)
		target = currentDance;
	    count++;
	}

	return target;
    }

    // part 1
    public String executeDance() {
	return executeDance(getDancers());
    }

    private String executeDance(List<Character> dancers) {
	Queue<String> commands = new LinkedList<>(danceCommands);

	while (!commands.isEmpty()) {
	    executeDanceCommand(commands.poll(), dancers);
	}

	return dancers.stream().map(s -> Character.toString(s)).collect(Collectors.joining());
    }

    private List<Character> getDancers() {
	int intValueA = 'a';
	return IntStream.range(0, DANCER_COUNT).mapToObj(s -> (char) (s + intValueA)).collect(Collectors.toList());
    }

    private void executeDanceCommand(String command, List<Character> dancers) {
	switch (command.charAt(0)) {
	case 's':
	    Collections.rotate(dancers, Integer.parseInt(command.substring(1)));
	    break;
	case 'x':
	    Collections.swap(dancers, Integer.parseInt(command.substring(1, command.indexOf('/'))),
		    Integer.parseInt(command.substring(command.indexOf('/') + 1)));
	    break;
	case 'p':
	    Collections.swap(dancers, dancers.indexOf(command.charAt(command.indexOf('/') - 1)),
		    dancers.indexOf(command.charAt(command.indexOf('/') + 1)));
	    break;
	default:
	    throw new IllegalArgumentException();
	}
    }

    private Queue<String> getDanceCommands(File input) {
	Queue<String> commands = new LinkedList<>();

	try {
	    Scanner sc = new Scanner(input);
	    sc.useDelimiter(",");

	    while (sc.hasNext()) {
		commands.offer(sc.next());
	    }

	    sc.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return commands;
    }

    public static void main(String[] args) {

	Day16 test = new Day16(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 16\\InputFile1.txt"));
	System.out.println(test.lastDance());

    }

}
