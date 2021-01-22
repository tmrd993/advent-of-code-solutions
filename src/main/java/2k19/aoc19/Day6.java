package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import myutils19.Graph;
import myutils19.StaticUtils;

public class Day6 {

    private final Graph<String> orbitalMap;
    private final List<String> orbitList;

    public Day6(File input) {
	orbitList = StaticUtils.fileToStringList(input);
	orbitalMap = setupGraph();
    }

    public int run1() {
	String root = "COM";
	int depth = 0;
	Set<String> visited = new HashSet<>();
	return dfsOrbitCount(root, depth, visited);
    }

    public int run2() {
	String root = "YOU";
	String target = "SAN";
	return bfs(root, target);
    }

    private int bfs(String root, String target) {
	List<String> path = new ArrayList<>();
	path.add(root);
	
	Queue<List<String>> queue = new LinkedList<>();
	queue.add(path);
	
	Set<String> visited = new HashSet<>();
	
	while(!queue.isEmpty()) {
	    List<String> shortestPath = queue.poll();
	    String orbital = shortestPath.get(shortestPath.size() - 1);
	    
	    visited.add(orbital);
	    
	    List<String> orbitalChildren = orbitalMap.getAdjList(orbital);
	    if(orbitalChildren.contains(target)) {
		return shortestPath.size() - 2;
	    }
	    
	    for(String child : orbitalChildren) {
		if(!visited.contains(child)) {
		    List<String> updatedPath = new ArrayList<>(shortestPath);
		    updatedPath.add(child);
		    queue.add(updatedPath);
		}
	    }
	}
	// no path to target found
	return -1;
    }

    private int dfsOrbitCount(String root, int depth, Set<String> visited) {
	if (visited.contains(root))
	    return 0;

	List<String> children = orbitalMap.getAdjList(root);
	visited.add(root);

	int count = depth;
	for (String child : children) {
	    count += dfsOrbitCount(child, depth + 1, visited);
	}
	return count;
    }

    private Graph<String> setupGraph() {
	Graph<String> orbitalMap = new Graph<>();

	for (String orbital : orbitList) {
	    String orbitalLeft = orbital.substring(0, orbital.indexOf(")"));
	    String orbitalRight = orbital.substring(orbital.indexOf(")") + 1);

	    orbitalMap.addVertex(orbitalLeft);
	    orbitalMap.addVertex(orbitalRight);

	    orbitalMap.addEdge(orbitalLeft, orbitalRight);
	}
	return orbitalMap;
    }

    @SuppressWarnings("unused")
    private void printGraph() {
	Set<String> vertices = orbitalMap.getVertices();
	for (String vertex : vertices) {
	    System.out.println(vertex + ": " + orbitalMap.getAdjList(vertex));
	}
    }

    public static void main(String[] args) {
	Day6 test = new Day6(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 6\\InputFile.txt"));
	System.out.println(test.run2());
    }
}
