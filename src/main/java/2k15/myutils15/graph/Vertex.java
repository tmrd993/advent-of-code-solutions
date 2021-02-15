package myutils15.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Vertex {
    private String id;
    private Set<Edge> edges;

    public Vertex(String id) {
	this.id = id;
	edges = new HashSet<>();
    }

    public boolean addEdge(Edge edge) {
	return edges.add(edge);
    }

    public String id() {
	return id;
    }

    public List<Edge> edges() {
	return new ArrayList<>(edges);
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || !(o instanceof Vertex)) {
	    return false;
	}

	Vertex tmp = (Vertex) o;
	return tmp.id == this.id;
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(id);
    }
}
