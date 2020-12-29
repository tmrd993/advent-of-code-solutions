package myutils19;

public class Triple<L, M, R> {
    private L l;
    private M m;
    private R r;
    
    public Triple(L l, M m, R r) {
	this.l = l;
	this.m = m;
	this.r = r;
    }
    
    public L l() {
	return l;
    }
    
    public M m() {
	return m;
    }
    
    public R r() {
	return r;
    }

}
