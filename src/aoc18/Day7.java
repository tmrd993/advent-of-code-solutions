package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Day7 {
    // index of the relevant instruction characters
    private static final int INDEX_C1 = 5;
    private static final int INDEX_C2 = 36;

    // part 1 solution
    public static String orderOfSteps(List<String> input) {
	Set<Character> rightSide = new HashSet<Character>();
	List<Character> leftSide = new ArrayList<Character>();

	Map<Character, Integer> prequisiteMap = new HashMap<Character, Integer>();
	Map<Character, List<Character>> instructionTree = new HashMap<Character, List<Character>>();

	for (String instruction : input) {
	    leftSide.add(instruction.charAt(INDEX_C1));
	    rightSide.add(instruction.charAt(INDEX_C2));

	    if (prequisiteMap.containsKey(instruction.charAt(INDEX_C2))) {
		prequisiteMap.put(instruction.charAt(INDEX_C2), prequisiteMap.get(instruction.charAt(INDEX_C2)) + 1);
	    } else {
		prequisiteMap.put(instruction.charAt(INDEX_C2), 1);
	    }

	    if (instructionTree.containsKey(instruction.charAt(INDEX_C1))) {
		instructionTree.get(instruction.charAt(INDEX_C1)).add(instruction.charAt(INDEX_C2));
	    } else {
		List<Character> adjList = new ArrayList<Character>();
		adjList.add(instruction.charAt(INDEX_C2));
		instructionTree.put(instruction.charAt(INDEX_C1), adjList);
	    }
	}

	// find characters that don't have any prequisites (step can be
	// completed immediately)
	Set<Character> sourceCharacters = new HashSet<Character>();
	for (Character c : leftSide) {
	    if (!rightSide.contains(c)) {
		sourceCharacters.add(c);
	    }
	}
	StringBuilder result = new StringBuilder();

	PriorityQueue<Character> instructionQueue = new PriorityQueue<Character>();
	for (Character c : sourceCharacters) {
	    instructionQueue.add(c);
	}

	while (!instructionQueue.isEmpty()) {
	    char currentInstruction = instructionQueue.poll();

	    result.append(currentInstruction);

	    if (instructionTree.containsKey(currentInstruction)) {
		for (Character c : instructionTree.get(currentInstruction)) {
		    if (prequisiteMap.get(c) == 1) {
			instructionQueue.add(c);
		    } else {
			prequisiteMap.put(c, prequisiteMap.get(c) - 1);
		    }
		}
	    }
	}
	return result.toString();
    }

    // part 2 solution
    public static long getTimeToFinish(List<String> instructions) {
	// map that holds instruction character and the current number of
	// seconds required to finish task
	Map<Character, Integer> instructionWeights = new HashMap<Character, Integer>();
	// A takes 60 + 1 seconds, B takes 60 + 2 seconds ...
	// change this to 1 for solution of example
	int secsToFinish = 61;
	for (int i = 'A'; i <= 'Z'; i++) {
	    instructionWeights.put((char) i, secsToFinish++);
	}

	Set<Character> uniqueChars = new HashSet<Character>();

	Map<Character, List<Character>> instructionTree = new HashMap<Character, List<Character>>();
	Map<Character, Integer> prequisiteMap = new HashMap<Character, Integer>();
	Set<Character> rightSide = new HashSet<Character>();
	List<Character> leftSide = new ArrayList<Character>();

	for (String instruction : instructions) {
	    char index1 = instruction.charAt(INDEX_C1);
	    char index2 = instruction.charAt(INDEX_C2);
	    uniqueChars.add(index1);
	    uniqueChars.add(index2);
	    leftSide.add(index1);
	    rightSide.add(index2);
	    if (prequisiteMap.containsKey(index2)) {
		prequisiteMap.put(index2, prequisiteMap.get(index2) + 1);
	    } else {
		prequisiteMap.put(index2, 1);
	    }

	    if (instructionTree.containsKey(index1)) {
		instructionTree.get(index1).add(index2);
	    } else {
		List<Character> adjList = new ArrayList<Character>();
		adjList.add(index2);
		instructionTree.put(index1, adjList);
	    }
	}

	Set<Character> sourceChars = new HashSet<Character>();

	for (Character c : leftSide) {
	    if (!rightSide.contains(c)) {
		sourceChars.add(c);
	    }
	}

	// change length of both arrays to 2 for solution of example
	Character[] workers = new Character[5];

	boolean[] workersUnavailable = new boolean[5];

	// holds available tasks
	PriorityQueue<Character> instructionQueue = new PriorityQueue<Character>();
	// add the source tasks (source tasks can be completed immediately)
	for (Character c : sourceChars) {
	    instructionQueue.add(c);
	}

	if (instructionQueue.size() > 5) {
	    for (int i = 0; i < 5; i++) {
		workers[i] = instructionQueue.poll();
		workersUnavailable[i] = true;
	    }
	} else {
	    int worksize = instructionQueue.size();
	    for (int i = 0; i < worksize; i++) {
		workers[i] = instructionQueue.poll();
		workersUnavailable[i] = true;
	    }
	}

	StringBuilder result = new StringBuilder();
	long totalTime = 0;
	while (!instructionQueue.isEmpty() || working(workersUnavailable)) {
	    // give available workers tasks if there are any
	    int availableWorkers = availablePositions(workersUnavailable);
	    if (availableWorkers > 0) {
		int availableWork = instructionQueue.size();
		int maximumAmount = Math.min(availableWorkers, availableWork);

		while (maximumAmount > 0) {
		    int availablePosition = availableWorkerPos(workers);
		    workers[availablePosition] = instructionQueue.poll();
		    workersUnavailable[availablePosition] = true;
		    maximumAmount--;
		}
	    }

	    // every worker works for 1 second
	    for (int i = 0; i < workers.length; i++) {
		if (workers[i] != null) {
		    instructionWeights.put(workers[i], instructionWeights.get(workers[i]) - 1);
		}
	    }

	    // check if any worker has finished
	    for (int i = 0; i < workers.length; i++) {
		if (workers[i] != null) {
		    // task of worker i done
		    if (instructionWeights.get(workers[i]) == 0) {
			// append current task to result and free current worker
			char currentInstruction = workers[i];
			result.append(currentInstruction);
			workers[i] = null;
			workersUnavailable[i] = false;

			// put available adjacent tasks into list
			if (instructionTree.containsKey(currentInstruction)) {
			    for (Character c : instructionTree.get(currentInstruction)) {
				if (prequisiteMap.get(c) == 1) {
				    instructionQueue.add(c);
				} else {
				    prequisiteMap.put(c, prequisiteMap.get(c) - 1);
				}
			    }
			}
		    }
		}
	    }
	    totalTime++;
	}
	return totalTime;
    }

    // helper function for part 2
    // returns the number of available workers
    private static int availablePositions(boolean[] workersUnavailable) {
	int count = 0;
	for (int i = 0; i < workersUnavailable.length; i++) {
	    if (!workersUnavailable[i])
		count++;
	}
	return count;
    }

    // helper function for part 2
    // returns true if at least one worker is currently working
    private static boolean working(boolean[] workersUnavailable) {
	for (boolean w : workersUnavailable) {
	    if (w)
		return true;
	}
	return false;
    }

    // helper method for part 2
    // returns the position of an available worker
    private static int availableWorkerPos(Character[] workers) {
	for (int i = 0; i < workers.length; i++) {
	    if (workers[i] == null)
		return i;
	}
	return -1;
    }

    public static void main(String[] args) throws IOException {
	List<String> input = aoc18.Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 7\\InputFile.txt"));

	// part 1
	// System.out.println(orderOfSteps(input));

	// part 2
	System.out.println(getTimeToFinish(input));

    }
}