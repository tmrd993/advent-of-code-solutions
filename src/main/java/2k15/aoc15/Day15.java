package aoc15;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.paukov.combinatorics.CombinatoricsFactory.createVector;
import static org.paukov.combinatorics.CombinatoricsFactory.createMultiCombinationGenerator;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import myutils15.StaticUtils;

public class Day15 {

    private List<String> rawData;
    private final Pattern NUMBER = Pattern.compile("-?\\d+");

    public Day15(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }

    public int run1() {
	List<Ingredient> ingredients = getIngredients();
	ICombinatoricsVector<Integer> vector = createVector(
		IntStream.range(0, rawData.size()).boxed().toArray(Integer[]::new));
	Generator<Integer> gen = createMultiCombinationGenerator(vector, 100);

	int max = 0;

	for (ICombinatoricsVector<Integer> combination : gen) {
	    Map<Integer, Long> ingredientFrequencyTable = combination.getVector().stream()
		    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	    int result = calculateCookieProperties(ingredients, ingredientFrequencyTable);
	    if (result > max) {
		max = result;
	    }

	}
	return max;
    }

    public int run2() {
	List<Ingredient> ingredients = getIngredients();
	ICombinatoricsVector<Integer> vector = createVector(
		IntStream.range(0, rawData.size()).boxed().toArray(Integer[]::new));
	Generator<Integer> gen = createMultiCombinationGenerator(vector, 100);

	int max = 0;

	for (ICombinatoricsVector<Integer> combination : gen) {
	    Map<Integer, Long> ingredientFrequencyTable = combination.getVector().stream()
		    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	    int calories = ingredientFrequencyTable.entrySet().stream()
		    .mapToInt(e -> e.getValue().intValue() * ingredients.get(e.getKey()).getCalories()).sum();
	    int result = calculateCookieProperties(ingredients, ingredientFrequencyTable);
	    if (calories == 500 && result > max) {
		max = result;
	    }

	}
	return max;
    }

    private int calculateCookieProperties(List<Ingredient> ingredients, Map<Integer, Long> ingredientFrequencyTable) {
	int cap = 0;
	int dur = 0;
	int fla = 0;
	int tex = 0;
	for (Entry<Integer, Long> entry : ingredientFrequencyTable.entrySet()) {
	    Ingredient currentIngredient = ingredients.get(entry.getKey());
	    int multiplier = entry.getValue().intValue();
	    cap += currentIngredient.getCapacity() * multiplier;
	    dur += currentIngredient.getDurability() * multiplier;
	    fla += currentIngredient.getFlavor() * multiplier;
	    tex += currentIngredient.getTexture() * multiplier;
	}

	return List.of(cap, dur, fla, tex).stream().filter(p -> p > 0).reduce(1, (a, b) -> a * b);
    }

    private List<Ingredient> getIngredients() {
	List<Ingredient> ingredients = new ArrayList<>();
	for (String ingredientData : rawData) {
	    Matcher matcher = NUMBER.matcher(ingredientData);
	    matcher.find();
	    int capacity = Integer.parseInt(matcher.group());
	    matcher.find();
	    int durability = Integer.parseInt(matcher.group());
	    matcher.find();
	    int flavor = Integer.parseInt(matcher.group());
	    matcher.find();
	    int texture = Integer.parseInt(matcher.group());
	    matcher.find();
	    int calories = Integer.parseInt(matcher.group());
	    ingredients.add(new Ingredient(capacity, durability, flavor, texture, calories));
	}
	return ingredients;
    }

    public static void main(String[] args) {
	Day15 test = new Day15(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 15\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

    private static class Ingredient {
	private final int capacity;
	private final int durability;
	private final int flavor;
	private final int texture;
	private final int calories;

	public Ingredient(int capacity, int durability, int flavor, int texture, int calories) {
	    this.capacity = capacity;
	    this.durability = durability;
	    this.flavor = flavor;
	    this.texture = texture;
	    this.calories = calories;
	}

	public int getCapacity() {
	    return capacity;
	}

	public int getDurability() {
	    return durability;
	}

	public int getFlavor() {
	    return flavor;
	}

	public int getTexture() {
	    return texture;
	}

	public int getCalories() {
	    return calories;
	}
    }

}
