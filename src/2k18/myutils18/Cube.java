package myutils18;

import java.util.Objects;

public class Cube {
    private int xMax;
    private int xMin;
    private int yMax;
    private int yMin;
    private int zMax;
    private int zMin;
    private int intersections;

    public Cube(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
	this.xMin = xMin;
	this.xMax = xMax;
	this.yMin = yMin;
	this.yMax = yMax;
	this.zMin = zMin;
	this.zMax = zMax;
    }

    public int xMax() {
	return xMax;
    }

    public int xMin() {
	return xMin;
    }

    public int yMax() {
	return yMax;
    }

    public int yMin() {
	return yMin;
    }

    public int zMax() {
	return zMax;
    }

    public int zMin() {
	return zMin;
    }

    public void incrIntersections() {
	intersections++;
    }

    public int getIntersections() {
	return intersections;
    }

    // cube area contains a bot if it's possible to move into the area with
    // steps <= range
    public boolean containsBot(Nanobot bot) {

	//cube is small enough to be a point
	if(xMin == xMax && yMin == yMax && zMin == zMax) {
	    return bot.position().distanceL1(new Point3d(xMin, yMin, zMin)) <= bot.range();
	}

	int distanceTo = 0;
	Point3d botPos = bot.position();

	if (botPos.x() < xMin || botPos.x() > xMax) {
	    int towardsMin = (int) Math.abs(botPos.x() - xMin);
	    int towardsMax = (int) Math.abs(botPos.x() - xMax);
	    distanceTo += Math.min(towardsMin, towardsMax);
	}

	if (botPos.y() < yMin || botPos.y() > yMax) {
	    int towardsMin = (int) Math.abs(botPos.y() - yMin);
	    int towardsMax = (int) Math.abs(botPos.y() - yMax);
	    distanceTo += Math.min(towardsMin, towardsMax);
	}

	if (botPos.z() < zMin || botPos.z() > zMax) {
	    int towardsMin = (int) Math.abs(botPos.z() - zMin);
	    int towardsMax = (int) Math.abs(botPos.z() - zMax);
	    distanceTo += Math.min(towardsMin, towardsMax);
	}
	return distanceTo <= bot.range();
    }

    @Override
    public int hashCode() {
	return Objects.hash(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (!(o instanceof Cube))
	    return false;

	Cube tmp = (Cube) o;

	if (this.xMin == tmp.xMin && this.xMax == tmp.xMax && this.yMin == tmp.yMin && this.yMax == tmp.yMax
		&& this.zMin == tmp.zMin && this.zMax == tmp.zMax)
	    return true;
	return false;
    }
}
