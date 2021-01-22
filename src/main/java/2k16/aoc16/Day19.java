package aoc16;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import myutils16.DoublyLinkedList;
import myutils16.DoublyLinkedList.Node;

public class Day19 {

    private final int input = 3014603;

    public int run1() {
	DoublyLinkedList elves = new DoublyLinkedList();
	IntStream.rangeClosed(1, input).forEach(elves::addLast);

	DoublyLinkedList.Node current = elves.getHead();

	while (elves.size() > 1) {
	    Node next = current.next == null ? elves.getHead() : current.next;
	    elves.removeNode(next);
	    current = current.next == null ? elves.getHead() : current.next;
	}

	return elves.get(0);
    }

    public int run2() {
	List<Integer> elves = new ArrayList<>();
	IntStream.rangeClosed(1, input).forEach(elves::add);

	Set<Integer> removed = new HashSet<>();
	int index = 0;
	int offset = 0;
	int size = elves.size();
	while (size > 1) {
	    int removeIndex = (index + (size / 2) + offset);

	    if (removeIndex >= elves.size() || removed.contains(elves.get(index))) {
		offset = 0;
		// filter out removed items, removing only when we actually need to reduces the
		// complexity from N^2 to N * logN
		elves = removeElves(elves, index, removed);
		index = 0;
	    } else {
		removed.add(elves.get(removeIndex));
		offset++;
		index++;
		size--;
	    }
	}

	return elves.get(0);
    }
    
    private List<Integer> removeElves(List<Integer> elves, int index, Set<Integer> removed) {
	List<Integer> elvesTmp = new ArrayList<>();
	for (int i = 0; i < elves.size(); i++) {
	    int elf = elves.get(index);
	    if (!removed.contains(elf)) {
		elvesTmp.add(elf);
	    }
	    index = (index + 1) % elves.size();
	}
	return elvesTmp;
    }

    public static void main(String[] args) {
	Day19 test = new Day19();
	System.out.println(test.run2());

    }

}
