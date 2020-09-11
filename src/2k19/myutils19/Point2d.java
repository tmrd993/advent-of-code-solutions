package myutils19;

import java.util.Objects;

public class Point2d {
    private final int x;
    private final int y;
    
    public Point2d(int x, int y) {
	this.x = x;
	this.y = y;
    }
    
    public int x() {
	return x;
    }
    
    public int y() {
	return y;
    }
    
    @Override
    public boolean equals(Object o) {
	if(this == o) {
	    return true;
	}
	if(o == null || !(o instanceof Point2d)) {
	    return false;
	}
	Point2d that = (Point2d) o;
	return this.x == that.x && this.y == that.y;
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
	return "[" + x + ", " + y + "]";
    }

    
}
