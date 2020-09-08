package aoc17;

import myutils2k17.CircularLinkedList;

public class Day17 {

    private final int STEP_COUNT;

    public Day17(int input) {
	STEP_COUNT = input;
    }

    // part 1, too slow for part 2
    public int getValueAfterLast() {
	CircularLinkedList<Integer> numbers = new CircularLinkedList<>();
	numbers.addFirst(0);

	int currentValue = 1;
	CircularLinkedList<Integer>.Node currentNode = numbers.head();
	while (numbers.size() < 2018) {
	    for (int i = 0; i < STEP_COUNT + 1; i++) {
		currentNode = currentNode.next;
	    }
	    numbers.addToCurrentPosition(currentNode, currentValue++);
	}
	return currentNode.next.next.data;
    }

    // part 2
    public int getSecondValue() {
	int currentIndex = 1;
	int targetValue = 1;
	for (int i = 2; i <= 50000000 - 1; i++) {
	    currentIndex = (currentIndex + STEP_COUNT) % i;
	    if (currentIndex == 0) {
		targetValue = i;
	    }
	    currentIndex++;
	}
	return targetValue;
    }

    public static void main(String[] args) {
	int input = 335;
	//int sampleInput = 2;

	Day17 test = new Day17(input);
	System.out.println(test.getSecondValue());

    }


}
