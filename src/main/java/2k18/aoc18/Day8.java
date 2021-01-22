package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day8 {

    public static List<Integer> inputToArray(File file) throws IOException {
	List<Integer> nums = new ArrayList<Integer>();
	Scanner sc = new Scanner(file);
	while (sc.hasNextInt()) {
	    nums.add(sc.nextInt());
	}
	sc.close();

	return nums;
    }

    // part 1, convoluted approach that returns a list of lists containing every
    // node and its meta data
    public static List<List<Integer>> getAllNodeContents(List<Integer> numbers) {
	List<List<Integer>> allNodes = new ArrayList<List<Integer>>();
	List<Integer> defensiveCopy = numbers.stream().collect(Collectors.toList());
	getAllNodeContents(defensiveCopy, 0, allNodes);
	return allNodes;
    }

    private static void getAllNodeContents(List<Integer> numbers, int current, List<List<Integer>> allNodes) {
	// no more elements available
	if (numbers.size() == 0)
	    return;

	int numOfChildren = numbers.get(current);
	int metaDataAmount = numbers.get(current + 1);
	// found a leaf
	if (numOfChildren == 0) {
	    List<Integer> nodeContents = new ArrayList<Integer>();
	    int range = metaDataAmount + 2;
	    while (range > 0) {
		// System.out.print(numbers.get(current) + " ");
		nodeContents.add(numbers.remove(current));
		range--;
	    }
	    // System.out.println();
	    allNodes.add(nodeContents);
	    return;
	}
	// found a parent
	else {
	    for (int i = 0; i < numOfChildren; i++) {
		getAllNodeContents(numbers, current + 2, allNodes);
	    }
	    List<Integer> nodeContents = new ArrayList<Integer>();
	    int range = metaDataAmount + 2;
	    while (range > 0) {
		// System.out.print(numbers.get(current) + " ");
		nodeContents.add(numbers.remove(current));
		range--;
	    }
	    allNodes.add(nodeContents);
	    // System.out.println();
	    return;
	}
    }

    // function to get meta sum from a list of nodes
    public static int getMetaSum(List<List<Integer>> allNodeContents) {
	int sum = 0;
	for (List<Integer> l : allNodeContents) {
	    for (int i = 2; i < l.size(); i++) {
		sum += l.get(i);
	    }
	}
	return sum;
    }

    // part 1 quick and easy approach without seperate lists
    static int sum = 0;

    public static int metaSum(List<Integer> nums, int current) {
	int childCount = nums.get(current);
	int metaDataAmount = nums.get(current + 1);

	if (childCount == 0) {
	    int p = current + 2;
	    for (int i = 0; i < metaDataAmount; i++) {
		sum += nums.get(p);
		p++;
	    }
	    return p;
	}

	else {
	    int p = current + 2;
	    for (int i = 0; i < childCount; i++) {
		p = metaSum(nums, p);
	    }

	    for (int i = 0; i < metaDataAmount; i++) {
		sum += nums.get(p);
		p++;
	    }
	    return p;
	}
    }

    public static int metaSum(List<Integer> nums) {
	sum = 0;
	metaSum(nums, 0);
	return sum;
    }

    // part 2
    public Day8() throws IOException {
	List<Integer> numbers = inputToArray(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 8\\InputFile.txt"));
	root = new Node(numbers, 0, null);
	root.buildTree(numbers, 0);
    }

    // root of the tree
    Node root;

    private static class Node {
	private List<Integer> metaData;
	private List<Node> children;
	private int childCount;
	private int metaDataAmount;

	public Node(List<Integer> nums, int startIndex, Node parent) {
	    if (parent != null) {
		parent.children.add(this);
	    }

	    childCount = nums.get(startIndex);
	    metaDataAmount = nums.get(startIndex + 1);

	    metaData = new ArrayList<Integer>();
	    if (childCount > 0)
		children = new ArrayList<Node>();
	}

	public int buildTree(List<Integer> numbers, int current) {
	    // create all children
	    int nextNode = current + 2;
	    for (int i = 0; i < childCount; i++) {
		Node child = new Node(numbers, nextNode, this);
		nextNode = child.buildTree(numbers, nextNode);
	    }

	    // get metadata of this node
	    for (int i = 0; i < metaDataAmount; i++) {
		metaData.add(numbers.get(nextNode));
		nextNode++;
	    }
	    return nextNode;
	}
    }

    // prints out the entire tree
    // not part of any solution, just a visualization aid
    public static void dfs(Node root) {
	for (Integer metaData : root.metaData) {
	    System.out.print(metaData + " ");
	}
	System.out.println();
	if (root.children == null)
	    return;
	for (Node child : root.children) {
	    dfs(child);
	}
    }

    public static int getRootValue(Node root) {
	int sum = 0;
	if (root.childCount == 0) {
	    return root.metaData.stream().mapToInt(n -> n.intValue()).sum();
	} else {
	    for (int i : root.metaData) {
		if (i > root.childCount)
		    continue;
		sum += getRootValue(root.children.get(i - 1));
	    }
	}
	return sum;
    }

    public static void main(String[] args) throws IOException {
	Day8 unitTest = new Day8();
	dfs(unitTest.root);
	System.out.println(getRootValue(unitTest.root));
    }
}