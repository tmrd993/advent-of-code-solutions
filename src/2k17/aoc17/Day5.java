package aoc17;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class Day5 {

    private List<Integer> numbers;

    public Day5(File input) throws IOException {
	numbers = getNumbers(input);
    }

    /**
     *
     * @param partFlag false for part 1, true for part 2
     * @return number of steps it takes to leave range of list
     */
    public int getSteps(boolean partFlag) {
	// defensive copy
	List<Integer> tmpNumbers = new ArrayList<>(numbers);
	int steps = 0;
	int index = 0;

	while(index >= 0 && index < tmpNumbers.size()) {
	    int increaseBy = 1;
	    int oldIndex = index;
	    index += tmpNumbers.get(index);

	    if(partFlag && index - oldIndex >= 3) {
		increaseBy = -1;
	    }

	    tmpNumbers.set(oldIndex, tmpNumbers.get(oldIndex) + increaseBy);
	    steps++;
	}

	return steps;
    }

    private List<Integer> getNumbers(File input) throws IOException {
	List<Integer> nums = new ArrayList<>();
	Scanner sc = new Scanner(input);

	while(sc.hasNextInt()) {
	    nums.add(sc.nextInt());
	}

	sc.close();

	return nums;
    }



    public static void main(String[] args) throws IOException {
	Day5 test = new Day5(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 5\\InputFile.txt"));
	System.out.println(test.getSteps(true));



    }

}
