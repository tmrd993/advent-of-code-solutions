package aoc15;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import myutils15.StaticUtils;

public class Day19 {

    private List<String> rawData;
    private List<String> replacements;
    private String startInput;

    public Day19(File input) {
	rawData = StaticUtils.fileToStringList(input);
	replacements = new ArrayList<>();
	initInputs();
    }

    public int run1() {
	Set<String> distinctMolecules = new HashSet<>();
	saveDistinctMolecules(startInput, distinctMolecules);

	return distinctMolecules.size();
    }

    public int run2() {
	Map<String, String> decomposeMapping = decomposeMapping();

	String fullMolecule = startInput;

	Queue<String> queue = new LinkedList<>();
	queue.add(fullMolecule);

	List<Map.Entry<String, String>> entries = decomposeMapping.entrySet().stream().collect(Collectors.toList());

	int count = 0;
	while (!queue.isEmpty()) {
	    String molecule = queue.poll();

	    for (Map.Entry<String, String> entry : entries) {
		if (!entry.getValue().equals("e") && molecule.contains(entry.getKey())) {
		    queue.add(molecule.replaceFirst(entry.getKey(), entry.getValue()));
		    count++;
		    break;
		}
	    }

	    // try again with a different pick order
	    if (queue.isEmpty() && molecule.length() > 3) {
		queue.add(fullMolecule);
		Collections.shuffle(entries);
		count = 0;
	    }
	}

	return count + 1;
    }

    // just an inverted view of the input mapping
    private Map<String, String> decomposeMapping() {
	Map<String, String> decomposeMapping = new HashMap<>();

	for (String replacement : replacements) {
	    String left = replacement.substring(0, replacement.indexOf('=') - 1);
	    String right = replacement.substring(replacement.indexOf('>') + 2);
	    decomposeMapping.put(right, left);
	}

	return decomposeMapping;
    }

    private void saveDistinctMolecules(String input, Set<String> distinctMolecules) {
	for (String replacement : replacements) {

	    String leftHandArgument = replacement.substring(0, replacement.indexOf('=') - 1);
	    String rightHandArgument = replacement.substring(replacement.indexOf('>') + 2);
	    for (int i = 0; i < input.length() - (leftHandArgument.length() - 1); i++) {

		if (input.substring(i, i + leftHandArgument.length()).equals(leftHandArgument)) {
		    distinctMolecules.add(input.substring(0, i) + rightHandArgument
			    + input.substring(i + leftHandArgument.length(), input.length()));
		}
	    }
	}
    }

    private void initInputs() {
	for (int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    if (line.isBlank()) {
		startInput = rawData.get(i + 1);
		break;
	    }

	    replacements.add(line);
	}
    }

    public static void main(String[] args) {
	Day19 test = new Day19(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 19\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());

    }

}
