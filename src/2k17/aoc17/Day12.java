package aoc17;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import myutils2k17.Graph;

public class Day12 {

    private Graph<Integer> programGraph;

    public Day12(File input) {
	programGraph = getProgramGraph(input);
    }

    // part 2
    public int getGroupCount() {
	Set<Integer> programs = programGraph.getVertices();
	Set<Integer> marked = new HashSet<>();

	int count = 0;
	for (Integer program : programs) {
	    if (!marked.contains(program)) {
		getProgramCount(program, marked);
		count++;
	    }
	}
	return count;
    }

    // part 1
    public int getProgramCount() {
	// number of children + self
	return getProgramCount(0, new HashSet<Integer>()) + 1;
    }

    private int getProgramCount(int currentProgram, Set<Integer> marked) {
	if (marked.contains(currentProgram)) {
	    return -1;
	}

	marked.add(currentProgram);

	int count = 0;

	for (Integer child : programGraph.getAdjList(currentProgram)) {
	    count += getProgramCount(child, marked) + 1;
	}

	return count;
    }

    private Graph<Integer> getProgramGraph(File input) {
	Graph<Integer> programGraph = new Graph<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line;

	    while ((line = br.readLine()) != null) {
		int parent = Integer.parseInt(line.substring(0, line.indexOf('<') - 1));
		programGraph.addVertex(parent);

		String[] children = line.substring(line.indexOf('>') + 2, line.length()).split(", ");
		for (String child : children) {
		    programGraph.addVertex(Integer.parseInt(child));
		    programGraph.addEdge(parent, Integer.parseInt(child));
		}

	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return programGraph;
    }

    public static void main(String[] args) {
	Day12 test = new Day12(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 12\\InputFile1.txt"));
	System.out.println(test.getGroupCount());
    }

}
