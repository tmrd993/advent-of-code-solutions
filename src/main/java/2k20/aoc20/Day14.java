package aoc20;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myutils20.StaticUtils;

public class Day14 {

    private List<String> rawData;

    public Day14(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public long run1() {
	String mask = "";
	Map<Integer, Long> registers = new HashMap<>();

	for (String com : rawData) {
	    if (com.startsWith("ma")) {
		mask = com.substring(com.indexOf('=') + 2);
	    } else {
		int registerIndex = Integer.parseInt(com.substring(com.indexOf('[') + 1, com.indexOf(']')));
		long val = Long.parseLong(com.substring(com.indexOf('=') + 2));
		long updatedVal = applyMask(mask, val);

		registers.put(registerIndex, updatedVal);
	    }
	}

	return registers.entrySet().stream().mapToLong(e -> e.getValue()).sum();
    }
    
    public long run2() {
	String mask = "";
	Map<Long, Long> registers = new HashMap<>();
	
	for(String com : rawData) {
	    if(com.startsWith("ma")) {
		mask = com.substring(com.indexOf('=') + 2);
		
	    } else {
		long registerIndex = Integer.parseInt(com.substring(com.indexOf('[') + 1, com.indexOf(']')));
		long val = Long.parseLong(com.substring(com.indexOf('=') + 2));
		
		String maskedRegister = applyMaskPt2(mask, Long.toBinaryString(registerIndex));
		
		Set<Long> registerIndicies = getRegisters(maskedRegister);
		
		for(Long register : registerIndicies) {
		    registers.put(register, val);
		}
	    }
	}
	
	return registers.entrySet().stream().mapToLong(e -> e.getValue()).sum();
    }

    private long applyMask(String mask, long value) {
	for (int i = mask.length() - 1; i >= 0; i--) {
	    char val = mask.charAt(i);
	    long shiftLen = mask.length() - i - 1;
	    if (val != 'X') {
		long digit = Character.digit(val, 10);
		long posMask = (1L << shiftLen);

		value = ((value & ~posMask) | ((digit << shiftLen) & posMask));
	    }
	}

	return value;
    }

    private String applyMaskPt2(String mask, String value) {
	String updatedVal = "";
	
	int maskIndex = mask.length() - 1;
	for(int i = value.length() - 1; i >= 0; i--) {
	    if(mask.charAt(maskIndex) == '1') {
		updatedVal = '1' + updatedVal;
	    } else if(mask.charAt(maskIndex) == '0') {
		updatedVal = value.charAt(i) + updatedVal;
	    } else {
		updatedVal = mask.charAt(maskIndex) + updatedVal;
	    }
	    maskIndex--;
	}
	
	for(int i = maskIndex; i >= 0; i--) {
	    updatedVal = mask.charAt(i) + updatedVal;
	}
	
	return updatedVal;
    }

    private Set<Long> getRegisters(String mask) {
	Set<Long> masks = new HashSet<>();

	maskPermutations(masks, mask, 0, "");

	return masks;
    }

    private void maskPermutations(Set<Long> masks, String baseMask, int index, String mask) {
	if (mask.length() == baseMask.length()) {
	    masks.add(Long.parseLong(mask, 2));
	}

	if (index >= baseMask.length())
	    return;

	if (baseMask.charAt(index) == 'X') {
	    maskPermutations(masks, baseMask, index + 1, mask + '1');
	    maskPermutations(masks, baseMask, index + 1, mask + '0');
	} else {
	    maskPermutations(masks, baseMask, index + 1, mask + baseMask.charAt(index));
	}
    }

    public static void main(String[] args) {
	Day14 test = new Day14(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 14\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
