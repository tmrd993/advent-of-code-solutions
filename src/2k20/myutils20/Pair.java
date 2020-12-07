package myutils20;

public class Pair<K, V> {
    private K k;
    private V v;
    
    public Pair(K k, V v) {
	this.k = k;
	this.v = v;
    }
    
    public K k() {
	return k;
    }
    
    public V v() {
	return v;
    }
}
