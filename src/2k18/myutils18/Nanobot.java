package myutils;

import java.util.Objects;

public class Nanobot {

    private Point3d position;
    private int range;

    public Nanobot(Point3d position, int range) {
	this.position = position;
	this.range = range;
    }

    public Point3d position() {
	return position;
    }

    public int range() {
	return range;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (!(o instanceof Nanobot))
	    return false;

	Nanobot tmp = (Nanobot) o;

	if(this.position.equals(tmp.position) && this.range == tmp.range)
	    return true;

	return false;
    }

    @Override
    public int hashCode() {
	return Objects.hash(position.x(), position.y(), position.z(), range);
    }

    @Override
    public String toString() {
	return position.toString() + " r: " + range;
    }


}
