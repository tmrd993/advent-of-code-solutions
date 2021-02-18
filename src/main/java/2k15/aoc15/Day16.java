package aoc15;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import myutils15.StaticUtils;

public class Day16 {

    private List<String> rawData;
    private List<String> requirements = List.of("children: 3", "cats: 7", "samoyeds: 2", "pomeranians: 3", "akitas: 0",
	    "vizslas: 0", "goldfish: 5", "trees: 3", "cars: 2", "perfumes: 1");

    public Day16(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	List<String> possibleSues = new ArrayList<>(rawData);
	Set<String> impossibleSues = new HashSet<>();
	for (String sue : possibleSues) {

	    for (String req : requirements) {
		String compound = req.substring(0, req.indexOf(':'));
		if (sue.contains(compound) && !sue.contains(req)) {
		    impossibleSues.add(sue);
		}
	    }
	}

	possibleSues.removeAll(impossibleSues);

	return Integer.parseInt(possibleSues.get(0).substring("Sue".length() + 1, possibleSues.get(0).indexOf(':')));
    }

    public int run2() {
	List<String> possibleSues = new ArrayList<>(rawData);
	Set<String> impossibleSues = new HashSet<>();
	for (String sue : possibleSues) {
	    Map<String, Integer> sueCompounds = Arrays.stream(sue.substring(sue.indexOf(':') + 2).split(","))
		    .collect(Collectors.toMap(s -> s.trim().substring(0, s.trim().indexOf(':')),
			    s -> Integer.parseInt(s.substring(s.indexOf(':') + 2))));
	    for (String req : requirements) {
		String compound = req.substring(0, req.indexOf(':'));
		int compoundAmount = Integer.parseInt(Character.toString(req.charAt(req.length() - 1)));
		if ((compound.equals("trees") || compound.equals("cats"))) {
		    if (sue.contains(compound) && sueCompounds.get(compound) <= compoundAmount)
			impossibleSues.add(sue);
		} else if ((compound.equals("pomeranians") || compound.equals("goldfish"))) {
		    if (sue.contains(compound) && sueCompounds.get(compound) >= compoundAmount)
			impossibleSues.add(sue);
		} else if (sue.contains(compound) && !sue.contains(req)) {
		    impossibleSues.add(sue);
		}
	    }
	}

	possibleSues.removeAll(impossibleSues);

	return Integer.parseInt(possibleSues.get(0).substring("Sue".length() + 1, possibleSues.get(0).indexOf(':')));

    }

    public static void main(String[] args) {
	Day16 test = new Day16(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 16\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
