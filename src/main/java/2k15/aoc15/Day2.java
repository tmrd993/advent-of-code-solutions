package aoc15;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import myutils15.StaticUtils;
import static myutils15.StaticUtils.min;

public class Day2 {

    public List<String> rawData;

    public Day2(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	return rawData.stream().map(s -> Arrays.stream(s.split("x")).mapToInt(Integer::parseInt).toArray()).mapToInt(
		arr -> (surfaceArea(arr[0], arr[1], arr[2]) + min(arr[0] * arr[1], arr[1] * arr[2], arr[2] * arr[0])))
		.sum();
    }

    public int run2() {
	return rawData.stream().map(s -> Arrays.stream(s.split("x")).mapToInt(Integer::parseInt).sorted().toArray())
		.mapToInt(a -> ((2 * a[0]) + (2 * a[1])) + (a[0] * a[1] * a[2])).sum();
    }

    private int surfaceArea(int l, int w, int h) {
	return (2 * l * w) + (2 * w * h) + (2 * h * l);
    }

    public static void main(String[] args) {
	Day2 test = new Day2(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 2\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
