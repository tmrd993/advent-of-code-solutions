package aoc20;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;

import myutils20.StaticUtils;

public class Day19 {

    private List<String> rawData;
    private List<String> messages;
    private Map<Integer, List<List<Integer>>> messageMapping;
    private Map<Integer, Character> baseRuleMapping;

    public Day19(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
	messages = getMessages();
	initMapping();
    }

    public int run1() {
	String regex = createZeroRegex();
	Pattern pattern = Pattern.compile(regex);

	return (int) messages.stream().filter(s -> pattern.matcher(s).matches()).count();
    }

    public int run2() {

	String baseRegex = createZeroRegex();
	Pattern basePattern = Pattern.compile(baseRegex);

	Map<Integer, Boolean> matchTable = new HashMap<>();
	for (int i = 0; i < messages.size(); i++) {
	    matchTable.put(i, false);
	}

	for (int i = 0; i < messages.size(); i++) {
	    if (basePattern.matcher(messages.get(i)).matches()) {
		matchTable.put(i, true);
	    }
	}

	for (int i = 0; i < 10; i++) {
	    for (int j = 0; j < 10; j++) {
		
		String nextRegex = createZeroRegex();
		Pattern nextPattern = Pattern.compile(nextRegex);

		for (int k = 0; k < messages.size(); k++) {
		    if (nextPattern.matcher(messages.get(k)).matches()) {
			matchTable.put(k, true);
		    }
		}
		addToEight(1);
	    }
	    messageMapping.put(8, defaultRule(8));
	    addToEleven(1);
	}

	return (int) matchTable.entrySet().stream().filter(s -> s.getValue() == true).count();
    }

    // adds another layer for rule 8 according to information in part 2
    private void addToEight(int n) {
	for (int i = 0; i < n; i++) {
	    messageMapping.get(8).get(0).add(42);
	}
    }

    // adds another layer for rule 11 according to information in part 2
    private void addToEleven(int n) {
	for (int i = 0; i < n; i++) {
	    messageMapping.get(11).get(0).add(31);
	    messageMapping.get(11).get(0).add(0, 42);
	}
    }

    private List<List<Integer>> defaultRule(int num) {
	List<List<Integer>> rules = new ArrayList<>();
	List<Integer> rule = new ArrayList<>();
	if (num == 8) {
	    rule.add(42);
	} else if (num == 11) {
	    rule.add(42);
	    rule.add(31);
	}
	rules.add(rule);
	return rules;
    }

    private String createZeroRegex() {
	Map<Integer, String> regMap = new HashMap<>();

	return createRegex(0, regMap);
    }

    private String createRegex(int index, Map<Integer, String> regMap) {
	if (baseRuleMapping.containsKey(index)) {
	    regMap.putIfAbsent(index, Character.toString(baseRuleMapping.get(index)));
	}

	if (regMap.containsKey(index)) {
	    return regMap.get(index);
	}

	String reg = "(";
	List<List<Integer>> subRules = messageMapping.get(index);
	if (subRules.size() == 1) {
	    List<Integer> subSubRules = subRules.get(0);
	    for (int i = 0; i < subSubRules.size(); i++) {
		String middle = createRegex(subSubRules.get(i), regMap);
		regMap.putIfAbsent(subSubRules.get(i), middle);
		reg = reg + middle;
	    }
	    reg = reg + ")";

	} else {
	    List<Integer> subSubRuleLeft = subRules.get(0);
	    List<Integer> subSubRuleRight = subRules.get(1);

	    for (int i = 0; i < subSubRuleLeft.size(); i++) {
		String middle = createRegex(subSubRuleLeft.get(i), regMap);
		regMap.putIfAbsent(subSubRuleLeft.get(i), middle);
		reg = reg + middle;
	    }

	    reg = reg + "|";

	    for (int i = 0; i < subSubRuleRight.size(); i++) {
		String middle = createRegex(subSubRuleRight.get(i), regMap);
		regMap.putIfAbsent(subSubRuleRight.get(i), middle);
		reg = reg + middle;
	    }

	    reg = reg + ")";

	}

	return reg;
    }

    private void initMapping() {
	messageMapping = new HashMap<>();
	baseRuleMapping = new HashMap<>();

	for (String rule : rawData) {
	    if (rule.length() == 0) {
		break;
	    }

	    int leftArgument = Integer.parseInt(rule.substring(0, rule.indexOf(':')));
	    String[] rightArguments = rule.substring(rule.indexOf(':') + 2).split(" | ");
	    List<List<Integer>> rules = new ArrayList<>();

	    List<Integer> subRules = new ArrayList<>();
	    for (int i = 0; i < rightArguments.length; i++) {
		if (rightArguments[i].trim().equals("|")) {
		    rules.add(subRules);
		    subRules = new ArrayList<>();
		} else if (rightArguments[i].trim().contains("a") || rightArguments[i].trim().contains("b")) {
		    baseRuleMapping.put(leftArgument, rightArguments[i].trim().charAt(1));
		} else {
		    subRules.add(Integer.parseInt(rightArguments[i].trim()));
		}
	    }

	    if (subRules.size() > 0) {
		rules.add(subRules);
	    }

	    messageMapping.put(leftArgument, rules);
	}
	baseRuleMapping.keySet().stream().forEach(s -> messageMapping.remove(s));
    }

    private List<String> getMessages() {
	List<String> messages = new ArrayList<>();
	boolean isMessage = false;
	for (int i = 0; i < rawData.size(); i++) {
	    if (rawData.get(i).length() == 0) {
		isMessage = true;
		continue;
	    }
	    if (isMessage) {
		messages.add(rawData.get(i));
	    }
	}

	return messages;
    }

    public static void main(String[] args) {
	Day19 test = new Day19(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 19\\InputFile1.txt"));

	System.out.println(test.run2());

    }

}
