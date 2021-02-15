package aoc15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import myutils15.graph.Graph;
import myutils15.graph.Vertex;
import myutils15.Pair;
import myutils15.graph.Edge;

public class Day9 {

    private Set<String> locations;
    private Graph distanceGraph;

    public Day9(File input) {
	locations = new HashSet<>();
	distanceGraph = initGraph(input);
    }

    public int run1() {
	List<Pair<String, Integer>> pathsWithSteps = getFullPathsWithSteps();
	
	return pathsWithSteps.stream().mapToInt(p -> p.v()).min().getAsInt();
    }
    
    public int run2() {
	List<Pair<String, Integer>> pathsWithSteps = getFullPathsWithSteps();
	
	return pathsWithSteps.stream().mapToInt(p -> p.v()).max().getAsInt();
    }
    
    private List<Pair<String, Integer>> getFullPathsWithSteps() {
	List<Pair<String, Integer>> pathsWithSteps = new ArrayList<>();
	for (String location : locations) {
	    Vertex startVertex = distanceGraph.getVertex(location);
	    Set<String> visited = new HashSet<>();
	    visited.add(location);
	    computeDistances(startVertex, location, visited, 0, pathsWithSteps);
	}
	
	return pathsWithSteps;
    }

    private void computeDistances(Vertex start, String route, Set<String> visited, int weight, List<Pair<String, Integer>> pathsWithSteps) {
	if(visited.size() == locations.size()) {
	    pathsWithSteps.add(new Pair<>(route, weight));
	}
	
	for(Edge n : start.edges()) {
	    if(!visited.contains(n.to().id())) {
		 String nextRoute = route + "->" + n.to().id();
		 Set<String> nextVisited = new HashSet<>(visited);
		 nextVisited.add(n.to().id());
		 computeDistances(n.to(), nextRoute, nextVisited, weight + n.weight(), pathsWithSteps);
	    }
	}
    }

    public Graph initGraph(File input) {
	distanceGraph = new Graph();

	try (BufferedReader br = new BufferedReader(new FileReader(input))) {
	    String line = "";
	    while ((line = br.readLine()) != null) {
		String source = line.substring(0, line.indexOf("to") - 1);
		String destination = line.substring(line.indexOf("to") + 3, line.indexOf('=') - 1);
		int weight = Integer.parseInt(line.substring(line.indexOf('=') + 2));
		distanceGraph.addVertex(source);
		distanceGraph.addVertex(destination);
		distanceGraph.addEdge(source, destination, weight);
		locations.add(source);
		locations.add(destination);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return distanceGraph;
    }

    public static void main(String[] args) {
	Day9 test = new Day9(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 9\\InputFile1.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }

}
