package myutils18;

import java.util.Objects;

public class Point4d {

    private final int a1;
    private final int a2;
    private final int a3;
    private final int a4;

    public Point4d(int a1, int a2, int a3, int a4) {
	this.a1 = a1;
	this.a2 = a2;
	this.a3 = a3;
	this.a4 = a4;
    }

    // manhattan distance
    public int distanceL1(Point4d target) {
	return Math.abs(this.a1 - target.a1) + Math.abs(this.a2 - target.a2) + Math.abs(this.a3 - target.a3)
		+ Math.abs(this.a4 - target.a4);
    }

    @Override
    public String toString() {
	return "[" + a1 + "," + a2 + "," + a3 + "," + a4 + "]";
    }

    @Override
    public boolean equals(Object o) {
	if(this == o)
	    return true;
	if(o == null)
	    return false;
	if(!(o instanceof Point4d))
	    return false;

	Point4d tmp = (Point4d) o;

	if(this.a1 == tmp.a1 && this.a2 == tmp.a2 && this.a3 == tmp.a3 && this.a4 == tmp.a4)
	    return true;

	return false;
    }

    @Override
    public int hashCode() {
	return Objects.hash(a1, a2, a3, a4);
    }

}
