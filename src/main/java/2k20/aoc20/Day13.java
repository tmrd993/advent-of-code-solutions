package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import myutils20.StaticUtils;

public class Day13 {

    private List<String> rawData;

    public Day13(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	int mins = Integer.parseInt(rawData.get(0));
	List<Integer> ids = Arrays.stream(rawData.get(1).split(",")).filter(s -> !s.equals("x")).map(Integer::parseInt)
		.collect(Collectors.toList());

	int minTime = Integer.MAX_VALUE;
	int minId = 0;
	for (int id : ids) {
	    int tmpId = id;
	    while (tmpId < mins) {
		tmpId += id;
	    }

	    if (tmpId < minTime) {
		minTime = tmpId;
		minId = id;
	    }
	}

	return (minTime - mins) * minId;
    }

    public long run2() {
	List<String> rawIds = Arrays.stream(rawData.get(1).split(",")).collect(Collectors.toList());
	List<Integer> ids = rawIds.stream().filter(s -> !s.equals("x")).map(Integer::parseInt)
		.collect(Collectors.toList());
	
	Map<Integer, Integer> idOffsets = offsetTable(rawIds);
	List<Long> lcmNumbers = new ArrayList<>();
	lcmNumbers.add((long) ids.get(0));
	

	return getTimestamp(ids, idOffsets, ids.get(0), ids.get(0), 1, lcmNumbers, idOffsets.get(ids.get(1)));
    }
    
    private long getTimestamp(List<Integer> ids, Map<Integer, Integer> idOffsets, long startNum, long jumpCount, int cmpIndex, List<Long> lcmNumbers, int totalOffset) {
	if(cmpIndex >= ids.size()) {
	    return startNum;
	}
	
	long cmpNum = ids.get(cmpIndex);
	
	while((startNum + totalOffset) % cmpNum != 0) {
	    startNum += jumpCount;
	}
	
	lcmNumbers.add(cmpNum);
	jumpCount = StaticUtils.lcm(lcmNumbers);
	
	if(cmpIndex + 1 >= ids.size())
	    return startNum;
	
	long count = getTimestamp(ids, idOffsets, startNum, jumpCount, cmpIndex + 1, lcmNumbers, totalOffset + idOffsets.get(ids.get(cmpIndex + 1)));
	
	return count;
    }

    private Map<Integer, Integer> offsetTable(List<String> rawIds) {
	Map<Integer, Integer> idOffset = new HashMap<>();

	int offset = 1;
	for (int i = 1; i < rawIds.size(); i++) {
	    if (rawIds.get(i).equals("x")) {
		offset++;
	    } else {
		idOffset.put(Integer.parseInt(rawIds.get(i)), offset);
		offset = 1;
	    }
	}
	
	return idOffset;
    }

    public static void main(String[] args) {
	Day13 test = new Day13(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 13\\InputFile1.txt"));
	System.out.println(test.run2());

    }

}
