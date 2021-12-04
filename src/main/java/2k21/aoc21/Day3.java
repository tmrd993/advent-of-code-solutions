package aoc21;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import utils.StaticUtils;

public class Day3 {
	
	private List<String> rawData;
	
	public Day3(File input) {
		rawData = StaticUtils.inputFileToStringList(input);
	}
	
	public int run1() {
		StringBuilder gamma = new StringBuilder();
		StringBuilder epsilon = new StringBuilder();
		
		int numOfBits = rawData.get(0).length();
		
		for(int i = 0; i < numOfBits; i++) {
			final int index = i;
			int countOn = (int) rawData.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 1).count();
			int countOff = (int) rawData.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 0).count();
			
			gamma.append(countOn > countOff ? 1 : 0);
			epsilon.append(countOn > countOff ? 0 : 1);
		}
		
		return Integer.parseInt(gamma.toString(), 2) * Integer.parseInt(epsilon.toString(), 2);
	}
	
	public int run2() {
		
		String oxy = "";
		String coTwo = "";
		
		List<String> oxyList = new ArrayList<>(rawData);
		int numOfBits = rawData.get(0).length();
		for(int i = 0; i < numOfBits; i++) {
			final int index = i;
			int countOn = (int) oxyList.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 1).count();
			int countOff = (int) oxyList.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 0).count();
			
			String removeBit = "";
			if(countOn >= countOff) {
			 	removeBit = "0";
			} else {
				removeBit = "1";
			}
			
		
			
			final String finalRemoveBit = removeBit;
			oxyList = oxyList.stream().filter(n -> n.charAt(index) != finalRemoveBit.charAt(0)).collect(Collectors.toList());
			
			if(oxyList.size() == 1) {
				oxy = oxyList.get(0);
				break;
			}
		}
		
		List<String> coTwoList = new ArrayList<>(rawData);
		for(int i = 0; i < numOfBits; i++) {
			final int index = i;
			int countOn = (int) coTwoList.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 1).count();
			int countOff = (int) coTwoList.stream().mapToInt(elem -> Integer.parseInt(Character.toString(elem.charAt(index)))).filter(n -> n == 0).count();
			
			String removeBit = "";
			if(countOn >= countOff) {
			 	removeBit = "1";
			} else {
				removeBit = "0";
			}
			
			final String finalRemoveBit = removeBit;
			coTwoList = coTwoList.stream().filter(n -> n.charAt(index) != finalRemoveBit.charAt(0)).collect(Collectors.toList());
			
			if(coTwoList.size() == 1) {
				coTwo = coTwoList.get(0);
				break;
			}
		}
	
		return Integer.parseInt(oxy, 2) * Integer.parseInt(coTwo, 2);
	}
	
	public static void main(String[] args) {
		Day3 test = new Day3(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day3\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
