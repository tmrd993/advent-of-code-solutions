package utils;

public class Triple<L, M, R> {
    private L l;
    private M m;
    private R r;
    
    public Triple(L l, M m, R r) {
	this.l = l;
	this.m = m;
	this.r = r;
    }
    
    public L getLeft() {
	return l;
    }
    
    public M getMiddle() {
	return m;
    }
    
    public R getRight() {
	return r;
    }

    @Override
    public String toString() {
	return "R: " + r + ", M: " + m + ", R: " + r; 
    }
}
