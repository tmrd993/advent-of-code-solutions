package aoc21;

import java.io.File;
import java.util.List;

import utils.StaticUtils;


public class Day1 {
	
	private List<Integer> nums;
	
	public Day1(File file) {
		nums = StaticUtils.inputFileToIntList(file);
	}
	
	public int run1() {
		int count = 0;
		for(int i = 1; i < nums.size(); i++) {
			if(nums.get(i) > nums.get(i - 1)) {
				count++;
			}
		}
		return count;
	}
	
	public int run2() {
		int count = 0;
		for(int i = 0; i < nums.size() - 3; i++) {
			int sumA = nums.get(i) + nums.get(i + 1) + nums.get(i + 2);
			int sumB = nums.get(i + 1) + nums.get(i + 2) + nums.get(i + 3);
			
			if(sumB > sumA) {
				count++;
			}
		}
		
		return count;
	}

	public static void main(String[] args) {
		Day1 test = new Day1(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day1\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
