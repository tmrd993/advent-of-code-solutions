package aoc21;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import utils.StaticUtils;

public class Day7 {
	
	private List<Integer> nums;

	public Day7(File file) {
		nums = StaticUtils.commaSeperatedIntegerFileToList(file);
		nums.sort(Comparator.naturalOrder());
	}
	
	private int run1() {
		
		
		int optimalDistance = 0;
		
		// optimalDistance = median
		int n = nums.size();
		if(nums.size() % 2 == 0) {
			optimalDistance = nums.get((n + 1) / 2 - 1);
		} else {
			optimalDistance = (nums.get((n + 1) / 2 - 1) + nums.get(n / 2)) / 2;
		}
		
		final int finalOptimalDistance = optimalDistance;
		int fuel = nums.stream().mapToInt(num -> Math.abs(num - finalOptimalDistance)).sum();
		
		return fuel;
	}
	
	// brute force with gauss sum
	private long run2() {
		
		int max = nums.get(nums.size() - 1);
		
		long fuel = 0;
		long minFuel = Integer.MAX_VALUE;
		for(int i = 1; i < max; i++) {
			
			for(int num : nums) {
				int distance = Math.abs(num - i);
				fuel += (((int) Math.pow(distance, 2) + distance)) / 2;
			}
			
			if(minFuel > fuel) {
				minFuel = fuel;
				fuel = 0;
			}
			
		}
		
		
		
		return minFuel;
	}

	public static void main(String[] args) {
		Day7 test = new Day7(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day7\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}



}
