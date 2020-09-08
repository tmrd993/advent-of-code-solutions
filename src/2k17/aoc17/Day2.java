package aoc17;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;

public class Day2 {
    


    private List<List<Integer>> rows;

    public Day2(File input) throws IOException {
	rows = getRows(input);
    }

    // part 1
    public int getChecksum() {
	return rows.stream().map(s -> s.stream().mapToInt(num -> num).summaryStatistics())
		.mapToInt(i -> i.getMax() - i.getMin()).sum();
    }

    // part 2
    public int getDivisibleSum() {
	int sum = 0;
	for(List<Integer> row : rows) {
	    for(int i = 0; i < row.size(); i++) {
		for(int j = i + 1; j < row.size(); j++) {
		    int num1 = row.get(i);
		    int num2 = row.get(j);

		    if(num1 % num2 == 0) {
			sum += num1 / num2;
			break;
		    }
		    else if(num2 % num1 == 0) {
			sum += num2 / num1;
			break;
		    }
		}
	    }
	}
	return sum;
    }

    private List<List<Integer>> getRows(File input) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(input));

	List<List<Integer>> rows = new ArrayList<List<Integer>>();

	String line = "";
	while ((line = br.readLine()) != null) {
	    List<Integer> row = new ArrayList<Integer>();
	    String[] lineNumsSeperated = line.split("\t");
	    Arrays.stream(lineNumsSeperated).mapToInt(Integer::parseInt).forEach(row::add);
	    rows.add(row);
	}
	br.close();

	return rows;
    }

    public static void main(String[] args) throws IOException {

	Day2 test = new Day2(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 2\\InputFile1.txt"));
	test.getChecksum();
    }

}
