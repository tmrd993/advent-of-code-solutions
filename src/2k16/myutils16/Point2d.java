package myutils16;

import java.util.Objects;

public class Point2d {
    
    private final int X;
    private final int Y;
    
    public Point2d(int x, int y) {
	this.X = x;
	this.Y = y;
    }
    
    public Point2d add(Point2d that) {
	return new Point2d(this.X + that.X, this.Y + that.Y);
    }
    
    public int x() {
	return X;
    }
    
    public int y() {
	return Y;
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(X, Y);
    }
    
    @Override
    public String toString() {
	return "[" + X + ", " + Y + "]";
    }
    
    @Override
    public boolean equals(Object o) {
	if(this == o)
	    return true;
	if(o == null)
	    return false;
	if(!(o instanceof Point2d))
	    return false;
	
	Point2d tmp = (Point2d) o;
	
	return this.X == tmp.X && this.Y == tmp.Y;
    }
}
