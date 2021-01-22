package aoc17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import myutils17.Graph;
import myutils17.Pair;
import myutils17.Program;

public class Day7 {

    private Graph<Program> prGraph;
    private List<String> programInfo;

    public Day7(File input) {
	prGraph = new Graph<Program>();
	programInfo = getProgramInfo(input);
	initGraph();
    }

    /**
     *
     * @return the required weight to balance the program
     *
     *         PART 2
     */
    public int getBalancedWeight() {
	// pair structure holding the program with the wrong weight and it's
	// neighbours's weights
	Pair<Program, List<Integer>> unbalancedPrograms = new Pair<Program, List<Integer>>(null,
		new ArrayList<Integer>());

	getBalancedWeight(getRoot(), new HashSet<Program>(), unbalancedPrograms);

	int result = getTargetBalance(unbalancedPrograms.key(), unbalancedPrograms.value());

	return result;
    }

    /**
     *
     * @param target
     *            program with wrong weight
     * @param neighbourWeights
     *            list containing neighbour's weights of target
     * @return the weight target should be at to balance the tree
     */
    private int getTargetBalance(Program target, List<Integer> neighbourWeights) {

	Map<Integer, Long> weightTable = neighbourWeights.stream().map(s -> s)
		.collect(Collectors.groupingBy(s -> s, Collectors.counting()));
	int key1 = weightTable.entrySet().stream().map(s -> s.getKey()).findAny().get();
	int key2 = weightTable.entrySet().stream().filter(s -> !s.getKey().equals(key1)).map(s -> s.getKey()).findAny()
		.get();

	int maxKey = weightTable.get(key1) > weightTable.get(key2) ? key1 : key2;
	int minKey = weightTable.get(key1) < weightTable.get(key2) ? key1 : key2;

	if (minKey > maxKey) {
	    return target.weight() - (Math.abs(Math.abs(maxKey) - Math.abs(minKey)));
	} else
	    return target.weight() + (Math.abs(Math.abs(maxKey) - Math.abs(minKey)));
    }

    /**
     *
     * @param prog
     *            current program to examine
     * @param marked
     *            set containing processed programs
     * @param unbalancedProgs
     *            Pair holding the wrong weight program and it's neighbours
     * @return the weight of program
     *
     *         recursively calculates the total weights of all programs
     */
    private int getBalancedWeight(Program prog, Set<Program> marked, Pair<Program, List<Integer>> unbalancedProgs) {

	// already processed or found target
	if (marked.contains(prog) || !unbalancedProgs.value().isEmpty()) {
	    return 0;
	}

	marked.add(prog);

	// no children
	if (prGraph.getAdjList(prog).isEmpty()) {
	    return prog.weight();
	}

	List<Pair<Program, Integer>> weightChildren = new ArrayList<>();
	for (Program child : prGraph.getAdjList(prog)) {
	    int weight = getBalancedWeight(child, marked, unbalancedProgs);
	    if (weight > 0) {
		weightChildren.add(new Pair<Program, Integer>(child, weight));
	    }
	}

	int weightSumChildren = getWeightSum(weightChildren, unbalancedProgs);

	return weightSumChildren + prog.weight();

    }

    /**
     *
     * @param weightOfChildren
     *            List of pairs holding the children and their total weight of a
     *            parent program
     * @param unbalancedPrograms
     *            pair holding the wrong weight program and it's neighbours
     * @return sum of weights of the children
     *
     */
    private int getWeightSum(List<Pair<Program, Integer>> weightOfChildren,
	    Pair<Program, List<Integer>> unbalancedPrograms) {
	int sum = weightOfChildren.stream().mapToInt(s -> s.value()).sum();

	Program child1 = null;
	Program child2 = null;
	int weight1 = 0;
	int weight2 = 0;

	// check balance of children if target not found already and set the
	// wrong weight program if it's found
	if (unbalancedPrograms.value().isEmpty()) {
	    for (int i = 0; i < weightOfChildren.size() - 1; i++) {
		if (!weightOfChildren.get(i).value().equals(weightOfChildren.get(i + 1).value())) {
		    child1 = weightOfChildren.get(i).key();
		    child2 = weightOfChildren.get(i + 1).key();
		    weight1 = weightOfChildren.get(i).value();
		    weight2 = weightOfChildren.get(i + 1).value();

		    List<Integer> unbalancedChildren = weightOfChildren.stream().map(s -> s.value())
			    .collect(Collectors.toList());
		    unbalancedPrograms.setValue(unbalancedChildren);

		    int count1 = 0;
		    int count2 = 0;
		    for (Pair<Program, Integer> p : weightOfChildren) {
			if (p.value().equals(weight1))
			    count1++;
			else if (p.value().equals(weight2))
			    count2++;
		    }

		    Program target = count1 > count2 ? child2 : child1;
		    unbalancedPrograms.setKey(target);
		    break;
		}
	    }

	}

	return sum;
    }

    /**
     * part 1
     *
     * @return name of the bottom program (root of the graph)
     */
    public Program getRoot() {
	Set<Program> vertices = new HashSet<>(prGraph.getVertices());

	for (Program vertex : prGraph.getVertices()) {
	    for (Program child : prGraph.getAdjList(vertex)) {
		vertices.remove(child);
	    }
	}

	return vertices.stream().findAny().get();
    }

    // fills the graph using the program information (puzzle input)
    private void initGraph() {
	// add vertices
	for (String line : programInfo) {
	    String programName = line.substring(0, line.indexOf('(') - 1);
	    int programWeight = Integer.parseInt(line.substring(line.indexOf('(') + 1, line.indexOf(')')));
	    prGraph.addVertex(new Program(programName, programWeight));
	}

	Set<Program> vertices = new HashSet<>(prGraph.getVertices());

	// add children
	for (String line : programInfo) {
	    if (line.contains("->")) {
		String parentProgram = line.substring(0, line.indexOf('(') - 1);
		int programWeight = Integer.parseInt(line.substring(line.indexOf('(') + 1, line.indexOf(')')));
		String[] programNames = line.substring(line.indexOf('>') + 2, line.length()).split(", ");
		Program parent = new Program(parentProgram, programWeight);
		for (String progName : programNames) {
		    Program child = vertices.stream().filter(s -> s.name().equals(progName)).findFirst().get();
		    prGraph.getAdjList(parent).add(child);
		}
	    }
	}
    }

    private List<String> getProgramInfo(File input) {
	List<String> programInformation = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));

	    String line = "";
	    while ((line = br.readLine()) != null) {
		programInformation.add(line);
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return programInformation;
    }

    public static void main(String[] args) {

	Day7 test = new Day7(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 7\\InputFile1.txt"));

	System.out.println(test.getRoot() + "\n" + test.getBalancedWeight());
    }

}
