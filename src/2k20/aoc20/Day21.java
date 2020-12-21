package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import myutils20.StaticUtils;
import myutils20.Pair;

public class Day21 {

    private List<String> rawData;
    private List<Pair<Set<String>, Set<String>>> foodAllergenList;

    public Day21(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
	foodAllergenList = foodAllergenList();
    }

    public int run1() {

	Set<String> noAllergenIngs = getNonMatchingIngredients();

	int count = 0;
	for (Pair<Set<String>, Set<String>> p : foodAllergenList) {

	    for (String ingr : noAllergenIngs) {
		if (p.k().contains(ingr)) {
		    count++;
		}
	    }

	}

	return count;
    }

    public String run2() {

	Set<String> noAllergenIngs = getNonMatchingIngredients();
	// remove ingredients with no allergens
	foodAllergenList.stream().forEach(p -> p.k().removeAll(noAllergenIngs));

	Map<String, String> ingredientAllergenTable = new HashMap<>();
	List<String> allergens = foodAllergenList.stream().flatMap(s -> s.v().stream()).distinct()
		.collect(Collectors.toList());
	List<String> ingredients = foodAllergenList.stream().flatMap(s -> s.k().stream()).distinct()
		.collect(Collectors.toList());

	// map ingredients to possible allergens
	Map<String, Set<String>> possibleMatches = possibleAllergenMatchTable();

	while (ingredientAllergenTable.size() < allergens.size()) {

	    // try to eliminate impossible entries
	    for (String ingr : ingredients) {
		if (possibleMatches.containsKey(ingr)) {
		    Set<String> possibleAllergens = possibleMatches.get(ingr);
		    boolean isInEverySet = true;
		    Set<String> nonMatchingAllergens = new HashSet<>();
		    for (String possibleAllergen : possibleAllergens) {

			// if the current allergen is possible, it should be in every ingredient list
			// marked with that allergen
			List<Set<String>> targetIngredients = foodAllergenList.stream()
				.filter(p -> p.v().contains(possibleAllergen)).map(p -> p.k())
				.collect(Collectors.toList());

			for (Set<String> targetIngredient : targetIngredients) {
			    // if the allergen was not in a set, it's impossible to match with the current
			    // ingredient
			    // put if in a set to remove it later with the rest
			    if (!targetIngredient.contains(ingr)) {
				isInEverySet = false;
				nonMatchingAllergens.add(possibleAllergen);
			    }
			}

		    }

		    // remove impossible allergens
		    if (!isInEverySet) {
			nonMatchingAllergens.stream().forEach(a -> possibleMatches.get(ingr).remove(a));
		    }

		}

	    }

	    // clean up the table if possible, search for values that already map to only 1
	    // allergen
	    if (possibleMatches.entrySet().stream().filter(p -> p.getValue().size() == 1).count() > 0) {
		String targetIngr = possibleMatches.entrySet().stream().filter(p -> p.getValue().size() == 1)
			.map(p -> p.getKey()).findFirst().get();
		String targetAllergen = possibleMatches.entrySet().stream().filter(p -> p.getValue().size() == 1)
			.map(p -> p.getValue().stream().findFirst().get()).findFirst().get();

		foodAllergenList.stream().filter(p -> p.k().contains(targetIngr))
			.forEach(p -> p.k().remove(targetIngr));
		foodAllergenList.stream().filter(p -> p.v().contains(targetAllergen))
			.forEach(p -> p.v().remove(targetAllergen));
		ingredientAllergenTable.put(targetIngr, targetAllergen);
		possibleMatches.remove(targetIngr);
		possibleMatches.entrySet().stream().forEach(p -> p.getValue().remove(targetAllergen));
	    }
	}

	return ingredientAllergenTable.entrySet().stream().sorted(Map.Entry.<String, String>comparingByValue())
		.map(e -> e.getKey()).collect(Collectors.joining(","));
    }

    // returns a set of ingredients that have no allergens
    private Set<String> getNonMatchingIngredients() {

	Map<String, Set<String>> possibleMatches = possibleAllergenMatchTable();
	List<String> allergenList = foodAllergenList.stream().flatMap(s -> s.v().stream()).distinct()
		.collect(Collectors.toList());

	for (String allergen : allergenList) {
	    List<Set<String>> foodsContainingAllergen = foodAllergenList.stream().filter(p -> p.v().contains(allergen))
		    .map(p -> p.k()).collect(Collectors.toList());

	    // no way to eliminate entries if we have no sets to compare to
	    if (foodsContainingAllergen.size() < 2) {
		continue;
	    }

	    Set<String> allPossibleIngr = foodsContainingAllergen.stream().flatMap(s -> s.stream())
		    .collect(Collectors.toSet());

	    // any ingredient that ISN'T in all of the sets is not matching with the
	    // currently processed allergen
	    Set<String> nonMatchingFood = new HashSet<>();
	    for (String ingr : allPossibleIngr) {
		boolean isInAllLists = true;
		for (Set<String> ingrList : foodsContainingAllergen) {
		    if (!ingrList.contains(ingr)) {
			isInAllLists = false;
		    }
		}

		if (!isInAllLists) {
		    nonMatchingFood.add(ingr);
		}
	    }

	    // remove the allergen from any ingredients that are impossible to match with it
	    for (String ingr : nonMatchingFood) {
		possibleMatches.get(ingr).remove(allergen);
	    }
	}

	Set<String> noAllergenIngs = possibleMatches.entrySet().stream().filter(e -> e.getValue().isEmpty())
		.map(e -> e.getKey()).collect(Collectors.toSet());

	return noAllergenIngs;
    }

    private Map<String, Set<String>> possibleAllergenMatchTable() {
	Map<String, Set<String>> possibleMatches = new HashMap<>();

	for (Pair<Set<String>, Set<String>> p : foodAllergenList) {

	    Set<String> ingredients = p.k();
	    for (String ingr : ingredients) {
		possibleMatches.putIfAbsent(ingr, new HashSet<>());
		Set<String> allergens = p.v();
		Set<String> currentIngrAllergens = possibleMatches.get(ingr);
		allergens.stream().forEach(s -> currentIngrAllergens.add(s));
	    }

	}

	return possibleMatches;
    }

    private List<Pair<Set<String>, Set<String>>> foodAllergenList() {
	List<Pair<Set<String>, Set<String>>> foodAllergenList = new ArrayList<>();

	for (String line : rawData) {
	    String ingr = line.substring(0, line.indexOf('(') - 1);
	    String allergen = line.substring(line.indexOf('(') + "contains".length() + 2, line.indexOf(')'));
	    Set<String> ingredients = Arrays.stream(ingr.split(" ")).collect(Collectors.toSet());
	    Set<String> allergens = Arrays.stream(allergen.split(", ")).collect(Collectors.toSet());
	    Pair<Set<String>, Set<String>> foodAllergenPair = new Pair<>(ingredients, allergens);

	    foodAllergenList.add(foodAllergenPair);
	}

	return foodAllergenList;
    }

    public static void main(String[] args) {
	Day21 test = new Day21(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 21\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
