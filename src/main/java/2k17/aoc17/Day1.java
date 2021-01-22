package aoc17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day1 {

    private List<Integer> numbers;

    public Day1(File inputFile) throws IOException {
	numbers = getNumbers(inputFile);
    }

    // part 1
    public int run() {

	int sum = 0;

	System.out.println(numbers);

	for (int i = 0; i < numbers.size(); i++) {
	    if (i == numbers.size() - 1) {
		if (numbers.get(i) == numbers.get(0)) {
		    sum += numbers.get(i);
		}
	    } else {
		if (numbers.get(i) == numbers.get(i + 1))
		    sum += numbers.get(i);
	    }
	}

	return sum;
    }

    //part 2
    public int run2() {
	int sum = 0;
	int halfway = numbers.size() / 2;

	for(int i = 0; i < numbers.size(); i++) {
	    int cmpIndex = (i + halfway) % numbers.size();
	    if(numbers.get(i) == numbers.get(cmpIndex))
		sum += numbers.get(i);
	}

	return sum;
    }

    private List<Integer> getNumbers(File inputFile) throws IOException {
	List<Integer> numbers = new ArrayList<Integer>();

	BufferedReader br = new BufferedReader(new FileReader(inputFile));

	int digit = 0;

	while ((digit = br.read()) != -1) {
	    numbers.add(Integer.parseInt(Character.toString((char) digit)));
	}

	br.close();

	return numbers;
    }

    public static void main(String[] args) throws IOException {

	//Day1 test = new Day1(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 1\\InputFile1.txt"));



    }

}
