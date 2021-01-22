package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myutils19.Pair;
import myutils19.StaticUtils;

public class Day14 {

    private List<String> rawData;
    private Map<Pair<String, Integer>, List<Pair<String, Integer>>> recipes;
    private Map<String, Integer> resultingChemicalCreateAmounts;

    public Day14(File input) {
	rawData = StaticUtils.fileToStringList(input);
	recipes = getRecipes();
	resultingChemicalCreateAmounts = new HashMap<>();
	recipes.entrySet().stream().forEach(e -> resultingChemicalCreateAmounts.put(e.getKey().k(), e.getKey().v()));
    }

    public long run1(int multiplier) {
	Pair<String, Integer> fuel = new Pair<String, Integer>("FUEL", multiplier);
	Map<Pair<String, Integer>, List<Pair<String, Integer>>> chemMap = getRecipes();
	Map<String, Long> spareMaterials = new HashMap<>();
	long cost = oreCost(fuel, chemMap, spareMaterials, fuel.v());
	return cost;
    }
    
    public long run2() {
	int multiplier = 1;
	while(run1(multiplier) < 1000000000000L) {
	    multiplier += 1000;
	}
	while(run1(multiplier) > 1000000000000L) {
	    multiplier--;
	}
	return multiplier;
    }

    private long oreCost(Pair<String, Integer> chemical,
	    Map<Pair<String, Integer>, List<Pair<String, Integer>>> chemMap, Map<String, Long> spareMaterials,
	    long multiplier) {

	List<Pair<String, Integer>> chemicalParts = chemMap.get(chemical);
	
	long oreCount = 0;
	for (Pair<String, Integer> chemPart : chemicalParts) {
	    String chemPartName = chemPart.k();
	    long chemPartNeededAmount = chemPart.v() * multiplier;

	    spareMaterials.putIfAbsent(chemPartName, 0L);
	    long storedAmount = spareMaterials.get(chemPartName);

	    long chemPartNeededAmountAfterUsingStorage = Math.max(0, chemPartNeededAmount - storedAmount);
	    storedAmount = Math.max(0, storedAmount - chemPartNeededAmount);
	    spareMaterials.put(chemPartName, storedAmount);

	    int chemPartCreatedAmount = resultingChemicalCreateAmounts.get(chemPartName);

	    // skip if storage had enough materials
	    if (chemPartNeededAmountAfterUsingStorage == 0) {
		continue;
	    }

	    long amountToCreate = (chemPartNeededAmountAfterUsingStorage + chemPartCreatedAmount - 1)
		    / chemPartCreatedAmount;
	    long restAmount = (chemPartCreatedAmount * amountToCreate) - chemPartNeededAmountAfterUsingStorage;

	    if (isBaseChemical(chemPart)) {
		int oreCostPerCreate = chemMap.get(chemPart).get(0).v();
		long usedOre = oreCostPerCreate * amountToCreate;
		spareMaterials.put(chemPartName, spareMaterials.get(chemPartName) + restAmount);
		oreCount += usedOre;
	    } else {
		spareMaterials.put(chemPartName, spareMaterials.get(chemPartName) + restAmount);
		oreCount += oreCost(chemPart, chemMap, spareMaterials, amountToCreate);
	    }
	}

	return oreCount;
    }

    private boolean isBaseChemical(Pair<String, Integer> chemical) {
	return recipes.get(chemical).size() == 1 && recipes.get(chemical).get(0).k().equals("ORE");
    }

    // returns a mapping of chemicals to lists of their recipes
    private Map<Pair<String, Integer>, List<Pair<String, Integer>>> getRecipes() {
	Map<Pair<String, Integer>, List<Pair<String, Integer>>> recipes = new HashMap<>();
	for (String recipeData : rawData) {
	    String[] splitData = recipeData.split(", |=> ");
	    String result = splitData[splitData.length - 1].trim();
	    Pair<String, Integer> resultingReaction = new Pair<>(result.substring(result.indexOf(' ') + 1),
		    Integer.parseInt(result.substring(0, result.indexOf(' '))));
	    List<Pair<String, Integer>> reactionComponents = new ArrayList<>();
	    for (int i = 0; i < splitData.length - 1; i++) {
		String reaction = splitData[i];
		reaction = reaction.trim();
		Pair<String, Integer> reactionPair = new Pair<>(reaction.substring(reaction.indexOf(' ') + 1),
			Integer.parseInt(reaction.substring(0, reaction.indexOf(' '))));
		reactionComponents.add(reactionPair);
	    }
	    recipes.put(resultingReaction, reactionComponents);
	}
	return recipes;
    }

    public static void main(String[] args) {
	Day14 test = new Day14(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 14\\InputFile.txt"));
	System.out.println(test.run2());
    }
}
