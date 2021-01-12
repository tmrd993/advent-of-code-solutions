package myutils16;

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
    
    public void setK(K k) {
	this.k = k;
    }
    
    public void setV(V v) {
	this.v = v;
    }

}
