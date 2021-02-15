package myutils15.graph;

import java.util.HashMap;
import java.util.Map;

public class Graph{
    private Map<String, Vertex> vertices;
    
    public Graph() {
	vertices = new HashMap<>();
    }
    
    public void addVertex(String label) {
	vertices.putIfAbsent(label, new Vertex(label));
    }
    
    public void addEdge(String fromLabel, String toLabel, int weight) {
	Vertex from = vertices.get(fromLabel);
	Vertex to = vertices.get(toLabel);
	from.addEdge(new Edge(to, weight));
	to.addEdge(new Edge(from, weight));
    }
    
    public Vertex getVertex(String label) {
	return vertices.get(label);
    }
    
}
