package myutils16;

public class GridNode {
    private Point2d pos;
    private int size;
    private int avail;
    private int used;
    private int usePerc;
    
    public GridNode(Point2d pos, int size, int used, int avail, int usePerc) {
	this.pos = pos;
	this.size = size;
	this.used = used;
	this.avail = avail;
	this.usePerc = usePerc;
    }

    public Point2d getPos() {
	return pos;
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public int getAvail() {
	return avail;
    }

    public void setAvail(int avail) {
	this.avail = avail;
    }

    public int getUsed() {
	return used;
    }

    public void setUsed(int used) {
	this.used = used;
    }

    public int getUsePerc() {
	return usePerc;
    }

    public void setUsePerc(int usePerc) {
	this.usePerc = usePerc;
    }
    
    @Override
    public String toString() {
	return pos + " " + size + "T  " + used + "T  "+ avail + "T  " + usePerc + "%"; 
    }

}
