package aoc20;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myutils20.CustomLinkedList;
import myutils20.CustomLinkedList.Node;

public class Day23 {

    private final String input = "469217538";
    private final int movesPt1 = 100;
    private final int movesPt2 = 10000000;

    public String run1() {
	Set<Integer> nums = new HashSet<>();

	List<Integer> circle = new LinkedList<>();
	input.chars().forEach(c -> circle.add(Integer.parseInt(Character.toString((char) c))));

	nums.addAll(circle);
	int maxNum = nums.stream().max(Comparator.naturalOrder()).get();
	int minNum = nums.stream().min(Comparator.naturalOrder()).get();

	int currentIndex = 0;
	int circleLen = circle.size();

	for (int i = 0; i < movesPt1; i++) {
	    List<Integer> nextThree = new ArrayList<>();

	    int tookFromFront = 0;
	    for (int j = 1; j <= 3; j++) {
		int targetInd = ((currentIndex + j) % circleLen);
		nextThree.add(circle.get(targetInd));
		if (targetInd < currentIndex) {
		    tookFromFront++;
		}
	    }

	    circle.removeAll(nextThree);

	    int rightNum = circle.get((currentIndex - tookFromFront + 1) % (circleLen - 3));

	    int dest = circle.get(currentIndex - tookFromFront) - 1;

	    if (dest < minNum) {
		dest = maxNum;
	    }

	    while (nextThree.contains(dest)) {
		dest = dest - 1 < minNum ? maxNum : dest - 1;
	    }

	    circle.addAll(circle.indexOf(dest) + 1, nextThree);
	    currentIndex = circle.indexOf(rightNum);
	}

	int start = circle.indexOf(1);
	String result = "";
	for (int i = 1; i < circleLen; i++) {
	    result += circle.get((start + i) % circleLen);
	}

	return result;
    }

    public long run2() {
	CustomLinkedList circle = new CustomLinkedList();
	input.chars().forEach(c -> circle.addLast(Integer.parseInt(Character.toString((char) c))));

	int min = 1;
	int max = 9;

	for (int i = max + 1; i <= 1000000; i++) {
	    circle.addLast(i);
	}

	max = 1000000;

	Node currentElement = circle.currentNode;
	Map<Integer, Node> nodeTable = circle.nodeTable;

	for (int i = 0; i < movesPt2; i++) {

	    CustomLinkedList removedThree = circle.removeNextThree(currentElement);

	    Set<Integer> removed = Set.of(removedThree.currentNode.data, removedThree.currentNode.next.data,
		    removedThree.currentNode.next.next.data);

	    int dest = currentElement.data - 1;

	    if (dest < min) {
		dest = max;
	    }

	    while (removed.contains(dest)) {
		dest = dest - 1 < min ? max : dest - 1;
	    }

	    circle.addAll(nodeTable.get(dest), removedThree);

	    currentElement = currentElement.next;

	}

	return (long) nodeTable.get(1).next.data * (long) nodeTable.get(1).next.next.data;
    }

    public static void main(String[] args) {
	Day23 test = new Day23();
	System.out.println(test.run1());

    }

}
