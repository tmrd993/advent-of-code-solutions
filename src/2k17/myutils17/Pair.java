package myutils2k17;

import java.util.Objects;

public class Pair<K, V> {

    private K key;
    private V val;

    public Pair(K key, V value) {
	this.key = key;
	this.val = value;
    }

    public K key() {
	return key;
    }

    public V value() {
	return val;
    }

    public void setKey(K key)
    {
	this.key = key;
    }

    public void setValue(V val) {
	this.val = val;
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

	return this.key == tmp.key && this.val == tmp.val;
    }

    @Override
    public int hashCode() {
	return Objects.hash(key, val);
    }

    @Override
    public String toString() {
	return key.toString() + " " + val.toString();
    }



}
