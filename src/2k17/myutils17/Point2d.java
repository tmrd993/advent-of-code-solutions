package myutils2k17;

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
    public String toString() {
	return "[" + x + ", " + y +"]";
    }

    /**
     *
     * @param target
     * @return manhattan distance to target
     */
    public int distanceL1(Point2d target) {
	return Math.abs(this.x - target.x) + Math.abs(this.y - target.y);
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

	return this.x == tmp.x && this.y == tmp.y;
    }

    @Override
    public int hashCode() {
	return Objects.hash(x, y);
    }

}
