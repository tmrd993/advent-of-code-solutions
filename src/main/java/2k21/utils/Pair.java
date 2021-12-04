package utils;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
	if(this == o)
	    return true;
	if(o == null)
	    return false;
	if(!(o instanceof Pair))
	    return false;

	@SuppressWarnings("unchecked")
	Pair<K, V> tmp = (Pair<K, V>) o;

	return this.k == tmp.k && this.v == tmp.v;
    }

    @Override
    public int hashCode() {
	return Objects.hash(k, v);
    }

    @Override
    public String toString() {
	return k.toString() + " " + v.toString();
    }

}
