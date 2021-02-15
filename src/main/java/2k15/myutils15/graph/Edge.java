package myutils15.graph;

import java.util.Objects;

public class Edge {
    private Vertex to;
    private int weight;
    
    public Edge(Vertex to, int weight) {
	this.to = to;
	this.weight = weight;
    }
    
    public Vertex to() {
	return to;
    }
    
    public int weight() {
	return weight;
    }
    
    @Override
    public boolean equals(Object o) {
	if(this == o) {
	    return true;
	}
	if(o == null || !(o instanceof Edge)) {
	    return false;
	}
	
	Edge tmp = (Edge) o;
	return tmp.to.equals(this.to) && tmp.weight == this.weight;
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(weight, to);
    }

}
