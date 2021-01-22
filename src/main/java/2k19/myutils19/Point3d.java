package myutils19;

import java.util.Objects;

public class Point3d {
    
    private final int x;
    private final int y;
    private final int z;
    
    public Point3d(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }
    
    public int x() {
	return x;
    }
    
    public int y() {
	return y;
    }
    
    public int z() {
	return z;
    }
    
    @Override
    public String toString() {
	return "[" + x + ", " + y + ", " + z + "]";
    }
    
    @Override
    public boolean equals(Object o) {
	if(this == o) {
	    return true;
	}
	if(o == null || !(o instanceof Point3d)) {
	    return false;
	}
	
	Point3d that = (Point3d) o;
	return this.x == that.x && this.y == that.y && this.z == that.z;
    }
    
    @Override
    public int hashCode() {
	return Objects.hash(x, y, z);
    }

}
