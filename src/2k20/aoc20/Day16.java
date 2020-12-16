package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import myutils20.StaticUtils;

public class Day16 {

    private List<String> rawData;
    private List<Set<Integer>> fieldBounds;
    private List<Integer> myTicket;
    private List<List<Integer>> nearbyTickets;

    public Day16(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
	initLists();
    }

    public int run1() {
	Set<Integer> bounds = fieldBounds.stream().flatMap(s -> s.stream()).collect(Collectors.toSet());

	return nearbyTickets.stream().flatMap(s -> s.stream()).filter(n -> !bounds.contains(n)).mapToInt(s -> s).sum();
    }

    public long run2() {
	List<List<Integer>> tickets = validTickets();
	int ticketCount = tickets.size();

	List<Map<Integer, Integer>> hitCountTables = hitCountTables(tickets);

	// map column numbers to actual field indicies
	Map<Integer, Integer> indexTable = new HashMap<>();

	// go through every column and remove impossible fields by process of
	// elimination
	List<Set<Entry<Integer, Integer>>> hitCounts = hitCountTables.stream().map(m -> m.entrySet())
		.collect(Collectors.toList());
	for (int i = 0; i < fieldBounds.size(); i++) {
	    int indexA = -1;
	    int indexB = -1;

	    for (int j = 0; j < hitCounts.size(); j++) {
		int numOfMaxHits = (int) hitCounts.get(j).stream().filter(e -> e.getValue() == ticketCount).count();
		if (numOfMaxHits == 1) {
		    Map.Entry<Integer, Integer> target = hitCounts.get(j).stream()
			    .filter(e -> e.getValue() == ticketCount).findFirst().get();
		    indexA = j;
		    indexB = target.getKey();
		    indexTable.put(indexA, indexB);
		    break;
		}
	    }
	    int targetKey = indexB;
	    // delete found field from every row
	    for (int j = 0; j < hitCounts.size(); j++) {
		Map.Entry<Integer, Integer> target = hitCounts.get(j).stream().filter(e -> e.getKey() == targetKey)
			.findFirst().get();
		hitCounts.get(j).remove(target);
	    }
	}

	// calculate the result
	int departureFieldCount = 5;
	long result = 1;
	for (int i = 0; i < myTicket.size(); i++) {
	    if (indexTable.get(i) <= departureFieldCount) {
		result *= (long) myTicket.get(i);
	    }
	}

	return result;
    }

    // for every column (field of ticket) count the number of possible fields it can be
    // return a list of maps for every column (field of ticket)
    private List<Map<Integer, Integer>> hitCountTables(List<List<Integer>> tickets) {
	List<Map<Integer, Integer>> hitCountTables = new ArrayList<>();
	for (int i = 0; i < tickets.get(0).size(); i++) {
	    Map<Integer, Integer> hitCountTable = new HashMap<>();
	    for (int j = 0; j < tickets.size(); j++) {
		int field = tickets.get(j).get(i);

		for (int k = 0; k < fieldBounds.size(); k++) {
		    hitCountTable.putIfAbsent(k, 0);
		    if (fieldBounds.get(k).contains(field)) {
			hitCountTable.put(k, hitCountTable.get(k) + 1);
		    }
		}

	    }
	    hitCountTables.add(hitCountTable);
	}
	
	return hitCountTables;
    }

    private List<List<Integer>> validTickets() {
	List<List<Integer>> validTickets = new ArrayList<>();
	Set<Integer> bounds = fieldBounds.stream().flatMap(s -> s.stream()).collect(Collectors.toSet());

	for (List<Integer> ticket : nearbyTickets) {
	    boolean isValid = true;
	    for (int field : ticket) {
		if (!bounds.contains(field)) {
		    isValid = false;
		    break;
		}
	    }
	    if (isValid) {
		validTickets.add(ticket);
	    }
	}
	return validTickets;
    }

    private void initLists() {
	int index = 0;
	String line = rawData.get(index);
	fieldBounds = new ArrayList<>();
	while (line.length() > 0) {
	    int minA = Integer.parseInt(line.substring(line.indexOf(':') + 2, line.indexOf('-')));
	    int maxA = Integer.parseInt(line.substring(line.indexOf('-') + 1, line.lastIndexOf("or") - 1));
	    int minB = Integer.parseInt(line.substring(line.lastIndexOf("or") + 3, line.lastIndexOf('-')));
	    int maxB = Integer.parseInt(line.substring(line.lastIndexOf('-') + 1));

	    Set<Integer> nums = new HashSet<>();
	    for (int i = minA; i <= maxA; i++) {
		nums.add(i);
	    }
	    for (int i = minB; i <= maxB; i++) {
		nums.add(i);
	    }

	    fieldBounds.add(nums);
	    index++;
	    line = rawData.get(index);
	}

	myTicket = Arrays.stream(rawData.get(index + 2).split(",")).map(Integer::parseInt).collect(Collectors.toList());

	index += 5;

	nearbyTickets = new ArrayList<>();
	for (int i = index; i < rawData.size(); i++) {
	    nearbyTickets
		    .add(Arrays.stream(rawData.get(i).split(",")).map(Integer::parseInt).collect(Collectors.toList()));
	}
    }

    public static void main(String[] args) {
	Day16 test = new Day16(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 16\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
