package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import myutils20.CustomsGroup;
import myutils20.CustomsPerson;
import myutils20.StaticUtils;

public class Day6 {

    private List<String> rawData;

    public Day6(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }
    
    public int run1() {
	List<CustomsGroup> groups = getGroups();
	
	int count = 0;
	for(CustomsGroup group : groups) {
	    Set<Character> answers = new HashSet<>();
	    group.getPeople().stream().forEach(p -> {
		p.rawAnswers().chars().forEach(c -> answers.add((char) c));
	    });
	    count += answers.size();
	}
	
	return count;
    }
    
    public int run2() {
	int count = 0;
	List<CustomsGroup> groups = getGroups();
	
	for(CustomsGroup group : groups) {
	    List<Set<Character>> groupAnswers = new ArrayList<>();
	    group.getPeople().stream().forEach(p -> {
		Set<Character> answers = new HashSet<>();
		p.rawAnswers().chars().forEach(c -> answers.add((char) c));
		groupAnswers.add(answers);
	    });
	    
	    Set<Character> uniqueAnswers = groupAnswers.get(0);
	    for(int i = 1; i < groupAnswers.size(); i++) {
		uniqueAnswers.retainAll(groupAnswers.get(i));
	    }
	    count += uniqueAnswers.size();
	}
	
	return count;
    }

    private List<CustomsGroup> getGroups() {
	List<CustomsGroup> groups = new ArrayList<>();

	CustomsGroup currentGroup = new CustomsGroup();
	for (int i = 0; i < rawData.size(); i++) {
	    if (rawData.get(i).length() == 0) {
		groups.add(currentGroup);
		currentGroup = new CustomsGroup();
	    } else {
		currentGroup.addPerson(new CustomsPerson(rawData.get(i)));
	    }
	}
	
	groups.add(currentGroup);

	return groups;
    }

    public static void main(String[] args) {
	Day6 test = new Day6(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 6\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
