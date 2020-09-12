package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.IntCodeComputer;
import myutils19.StaticUtils;

public class Day7 {

    private final int numOfComputers = 5;
    List<Integer> initialProgram;

    public Day7(File input) {
	initialProgram = StaticUtils.commaSeperatedIntegerFileToList(input);
    }

    public long run1() {
	int[] possiblePhaseVals = new int[] { 0, 1, 2, 3, 4 };
	List<List<Integer>> phaseValPermutations = StaticUtils.permutations(possiblePhaseVals);

	long maxThrusterStrength = 0;
	for (List<Integer> phasePermutation : phaseValPermutations) {
	    List<IntCodeComputer> computers = getComputers();
	    long outputValue = 0;
	    for (int i = 0; i < computers.size(); i++) {
		IntCodeComputer computer = computers.get(i);
		computer.setInputValues(phasePermutation.get(i), outputValue);
		computer.run();
		outputValue = computer.mostRecentOutputValue().get();
		if (outputValue > maxThrusterStrength) {
		    maxThrusterStrength = outputValue;
		}
	    }
	}
	return maxThrusterStrength;
    }

    public long run2() {
	int[] possiblePhaseVals = new int[] { 5, 6, 7, 8, 9 };
	List<List<Integer>> phaseValPermutations = StaticUtils.permutations(possiblePhaseVals);

	long maxThrusterStrength = 0;
	for (List<Integer> phaseVals : phaseValPermutations) {
	    List<IntCodeComputer> computers = getComputers();
	    IntCodeComputer computerE = computers.get(numOfComputers - 1);
	    phaseInit(computers, phaseVals);
	    int index = 0;
	    long outputVal = 0;
	    while (!computerE.isShutdown()) {
		IntCodeComputer computer = computers.get(index % numOfComputers);
		computer.setInputValues(outputVal);
		computer.run();
		outputVal = computer.mostRecentOutputValue().get();
		index++;
	    }
	    long thrusterOutput = computerE.mostRecentOutputValue().get();
	    if(thrusterOutput > maxThrusterStrength) {
		maxThrusterStrength = thrusterOutput;
	    }
	}
	return maxThrusterStrength;
    }

    private void phaseInit(List<IntCodeComputer> computers, List<Integer> phaseValues) {
	for (int i = 0; i < numOfComputers; i++) {
	    IntCodeComputer computer = computers.get(i);
	    computer.setInputValues(phaseValues.get(i));
	    computer.run();
	}
    }

    private List<IntCodeComputer> getComputers() {
	List<IntCodeComputer> computers = new ArrayList<>();
	for (int i = 0; i < numOfComputers; i++) {
	    computers.add(new IntCodeComputer(new ArrayList<>(initialProgram)));
	}
	return computers;
    }

    public static void main(String[] args) {
	Day7 test = new Day7(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 7\\InputFile.txt"));
	System.out.println(test.run1());
    }

}
