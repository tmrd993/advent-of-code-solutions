package myutils20;

import java.util.HashMap;
import java.util.Map;

// custom linked list for 2020, Day 23, Part 2, with exposed node for O(1) insertion time and a node table for instant access to every position
public class CustomLinkedList {

    private Node head;
    private Node tail;
    public Node currentNode;
    private int size;
    public Map<Integer, Node> nodeTable;

    public CustomLinkedList() {
	head = null;
	tail = null;
	currentNode = null;
	size = 0;
	nodeTable = new HashMap<>();
    }

    public void addLast(int e) {
	if (size == 0) {
	    head = new Node(e);
	    tail = head;
	    currentNode = head;
	    tail.next = head;
	    nodeTable.put(e, currentNode);
	} else {
	    Node last = tail;
	    Node el = new Node(e);
	    last.next = el;
	    el.next = head;
	    tail = el;
	    nodeTable.put(e, el);
	}

	size++;
    }

    // add all elements of l to this list to the right of element pos
    public void addAll(Node pos, CustomLinkedList l) {
	if (pos == tail) {
	    Node oldTail = tail;
	    tail = l.tail;
	    tail.next = head;
	    oldTail.next = l.head;
	} else {
	    Node oldNext = pos.next;
	    pos.next = l.head;
	    l.tail.next = oldNext;
	}

	nodeTable.putAll(l.nodeTable);

	size += l.size;
    }

    // remove 3 elements to the right of node pos and return them as a seperate
    // customlinkedlist
    public CustomLinkedList removeNextThree(Node pos) {
	CustomLinkedList removed = new CustomLinkedList();

	Node first = pos.next;
	Node last = first;
	removed.addLast(first.data);
	nodeTable.remove(first.data);
	for (int i = 0; i < 2; i++) {
	    last = last.next;
	    removed.addLast(last.data);
	    nodeTable.remove(last.data);
	}

	if (last == tail) {

	    tail = pos;
	    tail.next = head;

	} else if (last == head) {

	    head = head.next;
	    tail = pos;
	    tail.next = head;

	} else if (first == tail) {
	    head = head.next.next;
	    tail = pos;
	    tail.next = head;
	} else if (first == head) {
	    head = head.next.next.next;
	    tail.next = head;
	} else {
	    pos.next = last.next;
	}

	size -= 3;

	return removed;
    }

    @Override
    public String toString() {
	StringBuilder res = new StringBuilder("[");
	Node current = head;
	for (int i = 0; i < size - 1; i++) {

	    res.append(current.data + ", ");
	    current = current.next;
	}
	res.append(current.data + "]");
	return res.toString();
    }

    public static class Node {
	public int data;
	public Node next;

	public Node(int value) {
	    data = value;
	}
    }
}
