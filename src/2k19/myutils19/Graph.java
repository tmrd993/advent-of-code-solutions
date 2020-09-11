package myutils19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<E> {
    private Map<E, List<E>> adjLists;

    public Graph() {
	adjLists = new HashMap<E, List<E>>();
    }

    public void addVertex(E e) {
	adjLists.putIfAbsent(e, new ArrayList<>());
    }

    public void addEdge(E e1, E e2) {
	adjLists.get(e1).add(e2);
	adjLists.get(e2).add(e1);
    }

    public void removeVertex(E e) {
	adjLists.values().stream().forEach(el -> el.remove(e));
	adjLists.remove(e);
    }

    public void removeEdge(E e1, E e2) {
	adjLists.get(e1).remove(e2);
	adjLists.get(e2).remove(e1);
    }

    public List<E> getAdjList(E e) {
	return adjLists.get(e);
    }

    public Set<E> getVertices() {
	return adjLists.keySet();
    }

}
