package aoc17;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import myutils2k17.Particle;
import myutils2k17.Triple;

import java.util.ArrayList;
import java.util.Arrays;

public class Day20 {

    private List<Particle> particles;

    public Day20(File input) {
	particles = getParticles(input);

    }

    // part 2
    public int nonCollidingParticleCount() {

	boolean allCollisionsResolved = false;
	int nonCollisionLimit = 0;
	while(!allCollisionsResolved) {
	    boolean collisionDetected = false;
	    List<Particle> collisions = new ArrayList<>();
	    for(Particle particle : particles) {
		particle.move();
	    }

	    for(int i = 0; i < particles.size(); i++) {
		for(int j = i + 1; j < particles.size(); j++) {
		    if(particles.get(i).hasCollided(particles.get(j))) {
			collisions.add(particles.get(i));
			collisions.add(particles.get(j));
			collisionDetected = true;
		    }
		}
	    }

	    particles.removeAll(collisions);

	    if(collisionDetected) {
		nonCollisionLimit = 0;
	    }
	    else {
		nonCollisionLimit++;
		if(nonCollisionLimit == 100) {
		    allCollisionsResolved = true;
		}
	    }
	}


	return particles.size();
    }

    // part 1
    public int closestParticleToOriginAfterInf() {
	int indexOfParticle = 0;
	int minAcceleration = Integer.MAX_VALUE;

	for (int i = 0; i < particles.size(); i++) {
	    Particle current = particles.get(i);
	    int accelerationAbs = Math.abs(current.getAcceleration().getFirst())
		    + Math.abs(current.getAcceleration().getSecond()) + Math.abs(current.getAcceleration().getThird());
	    if (accelerationAbs < minAcceleration) {
		indexOfParticle = i;
		minAcceleration = accelerationAbs;
	    }
	}

	return indexOfParticle;
    }

    private List<Particle> getParticles(File input) {
	List<Particle> particles = new ArrayList<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";

	    while ((line = br.readLine()) != null) {
		String[] splitData = line.trim().split(", ");

		int[] position = Arrays.stream(
			splitData[0].substring(splitData[0].indexOf('<') + 1, splitData[0].indexOf('>')).split(","))
			.mapToInt(Integer::parseInt).toArray();
		int[] velocity = Arrays.stream(
			splitData[1].substring(splitData[1].indexOf('<') + 1, splitData[1].indexOf('>')).split(","))
			.mapToInt(Integer::parseInt).toArray();
		int[] acceleration = Arrays.stream(
			splitData[2].substring(splitData[2].indexOf('<') + 1, splitData[2].indexOf('>')).split(","))
			.mapToInt(Integer::parseInt).toArray();

		Triple<Integer, Integer, Integer> tPos = new Triple<>(position[0], position[1], position[2]);
		Triple<Integer, Integer, Integer> tVel = new Triple<>(velocity[0], velocity[1], velocity[2]);
		Triple<Integer, Integer, Integer> tAcc = new Triple<>(acceleration[0], acceleration[1],
			acceleration[2]);

		particles.add(new Particle(tPos, tVel, tAcc));
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return particles;
    }

    public static void main(String[] args) {
	Day20 test = new Day20(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 20\\InputFile1.txt"));
	//System.out.println(test.closestParticleToOriginAfterInf());

	List<Particle> ps = new ArrayList<>();
	ps.add(new Particle(new Triple<Integer, Integer, Integer>(1, 2, 3), new Triple<Integer, Integer, Integer>(1, 2, 3),new Triple<Integer, Integer, Integer>(1, 2, 3)));
	ps.add(new Particle(new Triple<Integer, Integer, Integer>(1, 2, 5), new Triple<Integer, Integer, Integer>(5, 2, 3),new Triple<Integer, Integer, Integer>(5, 2, 3)));
	ps.add(new Particle(new Triple<Integer, Integer, Integer>(1, 2, 5), new Triple<Integer, Integer, Integer>(1, 4, 3),new Triple<Integer, Integer, Integer>(13, 2, 3)));
	ps.add(new Particle(new Triple<Integer, Integer, Integer>(11, 42, 3), new Triple<Integer, Integer, Integer>(61, 2, 3),new Triple<Integer, Integer, Integer>(61, 21, 3)));
	ps.add(new Particle(new Triple<Integer, Integer, Integer>(121, 42, 3), new Triple<Integer, Integer, Integer>(1, 245, 3),new Triple<Integer, Integer, Integer>(1, 422, 3)));

	List<Particle> ps2 = new ArrayList<>();
	ps2.add(ps.get(0));
	ps2.add(ps.get(0));

	ps.removeAll(ps2);
	//ps.stream().forEach(System.out::println);

	System.out.println(test.nonCollidingParticleCount());
    }

}
