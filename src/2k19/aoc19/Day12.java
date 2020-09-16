package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myutils19.StaticUtils;
import myutils19.Point3d;

public class Day12 {

    private List<String> rawCoordinates;
    private List<Point3d> initialCoordinates;

    public Day12(File input) {
	rawCoordinates = StaticUtils.fileToStringList(input);
	initialCoordinates = getInitialMoonCoordinates();
    }

    public int run1() {
	List<Point3d> moonCoordinates = getInitialMoonCoordinates();
	List<Point3d> velocities = new ArrayList<>();
	velocities.addAll(
		List.of(new Point3d(0, 0, 0), new Point3d(0, 0, 0), new Point3d(0, 0, 0), new Point3d(0, 0, 0)));

	for (int i = 0; i < 1000; i++) {
	    updateVelocities(moonCoordinates, velocities);
	    updateCoordinates(moonCoordinates, velocities);
	    // printData(moonCoordinates, velocities);
	}

	int totalEnergyInSystem = 0;
	for (int i = 0; i < moonCoordinates.size(); i++) {
	    Point3d moonPos = moonCoordinates.get(i);
	    Point3d velocity = velocities.get(i);
	    int potE = Math.abs(moonPos.x()) + Math.abs(moonPos.y()) + Math.abs(moonPos.z());
	    int kinE = Math.abs(velocity.x()) + Math.abs(velocity.y()) + Math.abs(velocity.z());
	    totalEnergyInSystem += potE * kinE;
	}

	return totalEnergyInSystem;
    }

    public long run2() {

	List<Point3d> moonCoordinates = getInitialMoonCoordinates();
	List<Point3d> velocities = new ArrayList<>();
	velocities.addAll(
		List.of(new Point3d(0, 0, 0), new Point3d(0, 0, 0), new Point3d(0, 0, 0), new Point3d(0, 0, 0)));

	// kinetic energy of seperate axes oscillate with a set frequency
	// calculate step at which x, y and z velocities turn to zero and have the same
	// position as x0, y0 and z0
	// extrapolate to find the actual initial state
	// example: x == 0 at every 2nd step, y == 0 at every 4th step and z == 0 at
	// every 6th step
	// which means all three velocities are at 0 on the 12th step
	updateVelocities(moonCoordinates, velocities);
	updateCoordinates(moonCoordinates, velocities);

	// find stepcount x
	int zeroVelStepCountX = 1;
	while (!foundZeroVelX(moonCoordinates, velocities)) {
	    zeroVelStepCountX++;
	    for (int i = 0; i < moonCoordinates.size(); i++) {
		for (int j = 0; j < moonCoordinates.size(); j++) {
		    if (i != j) {
			int x1 = moonCoordinates.get(i).x();
			int x2 = moonCoordinates.get(j).x();
			int v1x = velocities.get(i).x();
			v1x = x1 > x2 ? v1x - 1 : (x1 == x2 ? v1x : v1x + 1);
			velocities.set(i, new Point3d(v1x, 0, 0));
		    }
		}
	    }
	    updateCoordinates(moonCoordinates, velocities);
	}

	moonCoordinates = getInitialMoonCoordinates();
	updateVelocities(moonCoordinates, velocities);
	updateCoordinates(moonCoordinates, velocities);

	int zeroVelStepCountY = 1;
	while (!foundZeroVelY(moonCoordinates, velocities)) {
	    zeroVelStepCountY++;
	    for (int i = 0; i < moonCoordinates.size(); i++) {
		for (int j = 0; j < moonCoordinates.size(); j++) {
		    if (i != j) {
			int y1 = moonCoordinates.get(i).y();
			int y2 = moonCoordinates.get(j).y();
			int v1y = velocities.get(i).y();
			v1y = y1 > y2 ? v1y - 1 : (y1 == y2 ? v1y : v1y + 1);
			velocities.set(i, new Point3d(0, v1y, 0));
		    }
		}
	    }
	    updateCoordinates(moonCoordinates, velocities);
	}

	moonCoordinates = getInitialMoonCoordinates();
	updateVelocities(moonCoordinates, velocities);
	updateCoordinates(moonCoordinates, velocities);

	int zeroVelStepCountZ = 1;
	while (!foundZeroVelZ(moonCoordinates, velocities)) {
	    zeroVelStepCountZ++;
	    for (int i = 0; i < moonCoordinates.size(); i++) {
		for (int j = 0; j < moonCoordinates.size(); j++) {
		    if (i != j) {
			int z1 = moonCoordinates.get(i).z();
			int z2 = moonCoordinates.get(j).z();
			int v1z = velocities.get(i).z();
			v1z = z1 > z2 ? v1z - 1 : (z1 == z2 ? v1z : v1z + 1);
			velocities.set(i, new Point3d(0, 0, v1z));
		    }
		}
	    }
	    updateCoordinates(moonCoordinates, velocities);
	}
	return StaticUtils.leastCommonMultiple(StaticUtils.leastCommonMultiple(zeroVelStepCountX, zeroVelStepCountY), zeroVelStepCountZ);
    }

    private boolean foundZeroVelX(List<Point3d> moonCoordinates, List<Point3d> velocities) {
	for (Point3d velocity : velocities) {
	    if (velocity.x() != 0)
		return false;
	}
	for (int i = 0; i < moonCoordinates.size(); i++) {
	    if (moonCoordinates.get(i).x() != initialCoordinates.get(i).x())
		return false;
	}

	return true;
    }

    private boolean foundZeroVelY(List<Point3d> moonCoordinates, List<Point3d> velocities) {
	for (Point3d velocity : velocities) {
	    if (velocity.y() != 0)
		return false;
	}
	for (int i = 0; i < moonCoordinates.size(); i++) {
	    if (moonCoordinates.get(i).y() != initialCoordinates.get(i).y())
		return false;
	}

	return true;
    }

    private boolean foundZeroVelZ(List<Point3d> moonCoordinates, List<Point3d> velocities) {
	for (Point3d velocity : velocities) {
	    if (velocity.z() != 0)
		return false;
	}
	for (int i = 0; i < moonCoordinates.size(); i++) {
	    if (moonCoordinates.get(i).z() != initialCoordinates.get(i).z())
		return false;
	}

	return true;
    }

    @SuppressWarnings("unused")
    private void printData(List<Point3d> coordinates, List<Point3d> velocities) {
	for (int i = 0; i < coordinates.size(); i++) {
	    System.out.println("Pos: " + coordinates.get(i) + ", Velocity: " + velocities.get(i));
	}
    }

    private void updateCoordinates(List<Point3d> coordinates, List<Point3d> velocities) {
	for (int i = 0; i < coordinates.size(); i++) {
	    Point3d currentMoonPos = coordinates.get(i);
	    Point3d currentMoonVelocity = velocities.get(i);
	    coordinates.set(i, new Point3d(currentMoonPos.x() + currentMoonVelocity.x(),
		    currentMoonPos.y() + currentMoonVelocity.y(), currentMoonPos.z() + currentMoonVelocity.z()));
	}
    }

    private void updateVelocities(List<Point3d> coordinates, List<Point3d> velocities) {
	for (int i = 0; i < coordinates.size(); i++) {
	    for (int j = 0; j < coordinates.size(); j++) {
		if (i != j) {
		    int x1 = coordinates.get(i).x();
		    int y1 = coordinates.get(i).y();
		    int z1 = coordinates.get(i).z();

		    int x2 = coordinates.get(j).x();
		    int y2 = coordinates.get(j).y();
		    int z2 = coordinates.get(j).z();

		    int v1x = velocities.get(i).x();
		    int v1y = velocities.get(i).y();
		    int v1z = velocities.get(i).z();

		    v1x = x1 > x2 ? v1x - 1 : (x1 == x2 ? v1x : v1x + 1);
		    v1y = y1 > y2 ? v1y - 1 : (y1 == y2 ? v1y : v1y + 1);
		    v1z = z1 > z2 ? v1z - 1 : (z1 == z2 ? v1z : v1z + 1);

		    velocities.set(i, new Point3d(v1x, v1y, v1z));
		}
	    }
	}
    }

    private List<Point3d> getInitialMoonCoordinates() {
	List<Point3d> coordinates = new ArrayList<>();
	for (String rawCoordinate : rawCoordinates) {
	    int x = Integer
		    .parseInt(rawCoordinate.substring(rawCoordinate.indexOf('=') + 1, rawCoordinate.indexOf(',')));
	    int y = Integer.parseInt(rawCoordinate.substring(rawCoordinate.indexOf('y') + 2,
		    rawCoordinate.indexOf(',', rawCoordinate.indexOf('y'))));
	    int z = Integer
		    .parseInt(rawCoordinate.substring(rawCoordinate.indexOf('z') + 2, rawCoordinate.indexOf('>')));
	    coordinates.add(new Point3d(x, y, z));
	}

	return coordinates;
    }

    public static void main(String[] args) {
	Day12 test = new Day12(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 12\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
