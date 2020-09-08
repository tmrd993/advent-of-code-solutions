package aoc18;

import java.util.Stack;

import myutils18.DoublyLinkedList;
import myutils18.DoublyLinkedList.Node;

public class Day14 {

    private int recipeCount;
    private DoublyLinkedList recipes;

    public Day14(int recipeCount) {
	this.recipeCount = recipeCount;
	recipes = new DoublyLinkedList();
    }

    // part 1
    public void run() {
	recipes.addLast(3);
	recipes.addLast(7);
	Node firstElf = recipes.getHead();
	Node secondElf = recipes.getHead().next;

	while (recipes.size() < recipeCount + 11) {
	    int newRecipe = firstElf.data + secondElf.data;
	    addRecipes(recipes, newRecipe);

	    // move elves to new recipe positions
	    int limitFirstElf = firstElf.data + 1;
	    for (int i = 0; i < limitFirstElf; i++) {
		firstElf = firstElf.next;
		if (firstElf == null)
		    firstElf = recipes.getHead();
	    }

	    int limitSecondElf = secondElf.data + 1;
	    for (int i = 0; i < limitSecondElf; i++) {
		secondElf = secondElf.next;
		if (secondElf == null)
		    secondElf = recipes.getHead();
	    }
	}
	String resultRecipes = get10Recipes();
	System.out.println(resultRecipes);
    }

    private String get10Recipes() {
	StringBuffer resultRecipes = new StringBuffer();
	Node tmpNode = recipes.getHead();
	for (int i = 0; i < recipeCount; i++) {
	    tmpNode = tmpNode.next;
	}

	for (int i = 0; i < 10; i++) {
	    resultRecipes.append(tmpNode.data);
	    tmpNode = tmpNode.next;
	}
	return resultRecipes.toString();
    }

    private void addRecipes(DoublyLinkedList recipes, int newRecipe) {
	if (newRecipe == 0) {
	    recipes.addLast(0);
	    return;
	}

	Stack<Integer> recipeDigits = new Stack<Integer>();

	while (newRecipe > 0) {
	    recipeDigits.add(newRecipe % 10);
	    newRecipe /= 10;
	}

	while (!recipeDigits.isEmpty()) {
	    recipes.addLast(recipeDigits.pop());
	}
    }

    @SuppressWarnings("unused")
    private void printList() {

	for (Node tmp = recipes.getHead(); tmp != null; tmp = tmp.next) {
	    System.out.print(tmp.data + " ");
	}
    }

    // part 2
    public void run2() {
	recipes.addLast(3);
	recipes.addLast(7);
	Node firstElf = recipes.getHead();
	Node secondElf = recipes.getHead().next;

	int resultIndex = -1;
	int count = 0;
	;
	while (true) {
	    int newRecipe = firstElf.data + secondElf.data;
	    addRecipes(recipes, newRecipe);

	    // move elves to new recipe positions
	    int limitFirstElf = firstElf.data + 1;
	    for (int i = 0; i < limitFirstElf; i++) {
		firstElf = firstElf.next;
		if (firstElf == null)
		    firstElf = recipes.getHead();
	    }

	    int limitSecondElf = secondElf.data + 1;
	    for (int i = 0; i < limitSecondElf; i++) {
		secondElf = secondElf.next;
		if (secondElf == null)
		    secondElf = recipes.getHead();
	    }

	    // checking is expensive so only do it after adding 1.000.000 new
	    // recipes
	    if (count % 1000000 == 0) {
		resultIndex = indexOfScoreSequence();
	    }

	    if (resultIndex > 0)
		break;
	    count++;
	}
	System.out.println(resultIndex);
    }

    private int indexOfScoreSequence() {
	StringBuffer recipesAsString = new StringBuffer();
	for (Node tmpNode = recipes.getHead(); tmpNode != null; tmpNode = tmpNode.next) {
	    recipesAsString.append(tmpNode.data);
	}
	// System.out.println(recipesAsString.length());
	return recipesAsString.indexOf(Integer.toString(recipeCount));
    }

    public static void main(String[] args) {
	int input = 598701;
	// int sampleInput = 9;

	Day14 test = new Day14(input);
	// test.run();
	test.run2();
	// test.printList();
    }
}