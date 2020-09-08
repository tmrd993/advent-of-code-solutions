package myutils;

import java.util.Objects;

public class Point3d {

    private final int x;
    private final int y;
    private final int z;

    public Point3d(int x2, int y2, int z2) {
	this.x = x2;
	this.y = y2;
	this.z = z2;
    }

    public Point3d(Point3d p) {
	x = p.x;
	y = p.y;
	z = p.z;
    }

    public double distanceL1(Point3d p1) {
	return Math.abs(x - p1.x) + Math.abs(y - p1.y) + Math.abs(z - p1.z);
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
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (!(o instanceof Point3d))
	    return false;

	Point3d tmp = (Point3d) o;

	if(this.x == tmp.x && this.y == tmp.y && this.z == tmp.z)
	    return true;

	return false;
    }

    @Override
    public int hashCode() {
	return Objects.hash(x, y, z);
    }
}
