package myutils19;

public class IncomparablePair<K, V> {
    private K k;
    private V v;
    
    public IncomparablePair(K k, V v) {
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
