package aoc15;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import myutils15.StaticUtils;

public class Day24 {

    private List<Integer> nums;
    private final int partitionLenPt1 = 3;
    private final int subSetSizePt1;
    private final int partitionLenPt2 = 4;
    private final int subSetSizePt2;

    public Day24(File input) {
	nums = StaticUtils.fileToStringList(input).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
	subSetSizePt1 = nums.stream().mapToInt(num -> num).sum() / partitionLenPt1;
	subSetSizePt2 = nums.stream().mapToInt(num -> num).sum() / partitionLenPt2;
	Collections.reverse(nums);
    }

    public long run1() {
	return quantumEntanglement(subSetSizePt1, true);
    }
    
    // this one is kind of slow... (~3 mins on my old notebook)
    public long run2() {
	return quantumEntanglement(subSetSizePt2, false);
    }

    public Long quantumEntanglement(int subSetSize, boolean isPart1) {
	List<Integer> possibleFirstGroupSizes = IntStream.rangeClosed(1, nums.size() - 2)
		.filter(size -> hasPossibleSubset(size, 0, 0, 0, subSetSize)).boxed().collect(Collectors.toList());

	for (int size : possibleFirstGroupSizes) {
	    AtomicReference<Long> lowestVal = new AtomicReference<>(Long.MAX_VALUE);
	    if (isPart1) {
		calculateBalancedWeightsPt1(lowestVal, 0, 0, 0, 0, size, 0, 1, subSetSize);
	    } else {
		calculateBalancedWeightsPt2(lowestVal, 0, 0, 0, 0, 0, size, 0, 1, subSetSize);
	    }

	    // if the value was changed, return it
	    if (lowestVal.get() < Long.MAX_VALUE) {
		return lowestVal.get();
	    }
	}

	return -1L;
    }

    // returns true if there is a possible subset where
    // the sum of elements equals the subSetSize with a given amount of elements
    private boolean hasPossibleSubset(int numOfElements, int sum, int index, int takenElements, int subSetSize) {
	if (index >= nums.size()) {
	    return false;
	}

	if (sum == subSetSize) {
	    if (numOfElements == takenElements) {
		return true;
	    } else {
		return false;
	    }
	}

	if (sum > subSetSize || takenElements > numOfElements) {
	    return false;
	}

	boolean takeCurrent = hasPossibleSubset(numOfElements, sum + nums.get(index), index + 1, takenElements + 1,
		subSetSize);
	boolean skipCurrent = hasPossibleSubset(numOfElements, sum, index + 1, takenElements, subSetSize);

	return takeCurrent || skipCurrent;
    }

    private void calculateBalancedWeightsPt1(AtomicReference<Long> lowestVal, int index, int leftSum, int middleSum,
	    int rightSum, int numOfElementsToTake, int takenElements, long leftProduct, int subSetSize) {

	if (takenElements > numOfElementsToTake || leftSum > subSetSize || rightSum > subSetSize
		|| middleSum > subSetSize) {
	    return;
	}

	if (leftSum == subSetSize && middleSum == subSetSize && rightSum == subSetSize
		&& takenElements == numOfElementsToTake) {
	    if (leftProduct < lowestVal.get()) {
		lowestVal.set(leftProduct);
	    }
	    return;
	}

	if (index < nums.size()) {
	    calculateBalancedWeightsPt1(lowestVal, index + 1, leftSum + nums.get(index), middleSum, rightSum,
		    numOfElementsToTake, takenElements + 1, leftProduct * (long) nums.get(index), subSetSize);
	    calculateBalancedWeightsPt1(lowestVal, index + 1, leftSum, middleSum + nums.get(index), rightSum,
		    numOfElementsToTake, takenElements, leftProduct, subSetSize);
	    calculateBalancedWeightsPt1(lowestVal, index + 1, leftSum, middleSum, rightSum + nums.get(index),
		    numOfElementsToTake, takenElements, leftProduct, subSetSize);
	}

    }

    private void calculateBalancedWeightsPt2(AtomicReference<Long> lowestVal, int index, int firstSum, int secondSum,
	    int thirdSum, int fourthSum, int numOfElementsToTake, int takenElements, long leftProduct, int subSetSize) {

	if (takenElements > numOfElementsToTake || firstSum > subSetSize || secondSum > subSetSize
		|| thirdSum > subSetSize || fourthSum > subSetSize || lowestVal.get() <= leftProduct) {
	    return;
	}

	if (firstSum == subSetSize && secondSum == subSetSize && thirdSum == subSetSize && fourthSum == subSetSize
		&& takenElements == numOfElementsToTake) {
	    if (leftProduct < lowestVal.get()) {
		lowestVal.set(leftProduct);
		//System.out.println(lowestVal.get());
	    }
	    return;
	}

	if (index < nums.size()) {
	    calculateBalancedWeightsPt2(lowestVal, index + 1, firstSum + nums.get(index), secondSum, thirdSum,
		    fourthSum, numOfElementsToTake, takenElements + 1, leftProduct * (long) nums.get(index),
		    subSetSize);
	    calculateBalancedWeightsPt2(lowestVal, index + 1, firstSum, secondSum + nums.get(index), thirdSum,
		    fourthSum, numOfElementsToTake, takenElements, leftProduct, subSetSize);
	    calculateBalancedWeightsPt2(lowestVal, index + 1, firstSum, secondSum, thirdSum + nums.get(index),
		    fourthSum, numOfElementsToTake, takenElements, leftProduct, subSetSize);
	    calculateBalancedWeightsPt2(lowestVal, index + 1, firstSum, secondSum, thirdSum,
		    fourthSum + nums.get(index), numOfElementsToTake, takenElements, leftProduct, subSetSize);
	}
    }

    public static void main(String[] args) {
	Day24 test = new Day24(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 24\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
