package aoc15;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import myutils15.StaticUtils;

public class Day14 {
    private List<String> rawData;
    private final Pattern NUMBER = Pattern.compile("\\d+");
    private final int timeLimit = 2503;
    
    public Day14(File input) {
	rawData = StaticUtils.fileToStringList(input);
    }
    
    public int run1() {
	List<Reindeer> reindeers = getReindeers();
	for(int i = 0; i < timeLimit; i++) {
	    reindeers.stream().forEach(Reindeer::tick);
	}
	
	return reindeers.stream().mapToInt(Reindeer::distanceTravelled).max().getAsInt();
    }
    
    public int run2() {
	List<Reindeer> reindeers = getReindeers();
	for(int i = 0; i < timeLimit; i++) {
	    reindeers.stream().forEach(Reindeer::tick);
	    int max = reindeers.stream().mapToInt(Reindeer::distanceTravelled).max().getAsInt();
	    reindeers.stream().filter(r -> r.distanceTravelled == max).forEach(Reindeer::givePoint);
	}
	
	return reindeers.stream().mapToInt(Reindeer::points).max().getAsInt();
    }
    
    private List<Reindeer> getReindeers() {
	return rawData.stream().map(s -> {
	    Matcher matcher = NUMBER.matcher(s);
	    matcher.find();
	    int speed = Integer.parseInt(matcher.group());
	    matcher.find();
	    int stopAt = Integer.parseInt(matcher.group());
	    matcher.find();
	    int stopFor = Integer.parseInt(matcher.group());
	    
	    return new Reindeer(speed, stopAt, stopFor);
	}).collect(Collectors.toList());
    }

    public static void main(String[] args) {
	Day14 test = new Day14(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2015\\Day 14\\InputFile.txt"));
	System.out.println(test.run1());
	System.out.println(test.run2());
    }
    
    private static class Reindeer {
	private final int speed;
	private final int stopAt;
	private final int stopFor;
	
	private int restCounter;
	private int speedCounter;
	
	private int distanceTravelled;
	private int points;
	
	public Reindeer(int speed, int stopAt, int stopFor) {
	    this.speed = speed;
	    this.stopAt = stopAt;
	    this.stopFor = stopFor;
	    restCounter = stopFor;
	    speedCounter = stopAt;
	    distanceTravelled = 0;
	}
	
	public void tick() {
	    if(speedCounter == 0) {
		restCounter--;
		if(restCounter == 0) {
		    restCounter = stopFor;
		    speedCounter = stopAt;
		}
	    } else {
		distanceTravelled += speed;
		speedCounter--;
	    }
	}
	
	@Override
	public String toString() {
	    return speed + " " + stopAt + " " + stopFor;
	}
	
	public int distanceTravelled() {
	    return distanceTravelled;
	}
	
	public void givePoint() {
	    points++;
	}
	
	public int points() {
	    return points;
	}
    }
    
}
