package aoc17;

import java.util.Map;

import myutils17.NetworkPacket;
import myutils17.Point2d;
import myutils17.NetworkPacket.Direction;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Day19 {

    private Map<Point2d, Character> gridElements;
    private final Point2d START_POS;
    private NetworkPacket packet;
    private Integer stepsTaken;

    public Day19(File input) {
	gridElements = getGridElements(input);
	START_POS = new Point2d(1, 0);
	packet = new NetworkPacket(START_POS);
	stepsTaken = 0;
    }

    // part 2
    public int getStepcount() {
	if (stepsTaken == 0)
	    getLetters();
	return stepsTaken.intValue();
    }

    // part 1
    public String getLetters() {
	StringBuilder letters = new StringBuilder();

	boolean endFound = false;
	while (!endFound) {
	    if (gridElements.get(packet.getPosition()) == '+') {
		packet.setDirection(updateDirection(packet));
	    }
	    if (Character.isAlphabetic(gridElements.get(packet.getPosition()))) {
		letters.append(gridElements.get(packet.getPosition()));
	    }
	    packet.move();
	    stepsTaken++;

	    if (gridElements.get(packet.getPosition()) == ' ') {
		endFound = true;
	    }
	}
	return letters.toString();
    }

    private NetworkPacket.Direction updateDirection(NetworkPacket packet) {
	if (gridElements.get(packet.getPosition()) != '+')
	    return packet.getDirection();

	Direction direction = packet.getDirection();
	Point2d left = new Point2d(packet.getPosition().x() - 1, packet.getPosition().y());
	Point2d right = new Point2d(packet.getPosition().x() + 1, packet.getPosition().y());
	Point2d up = new Point2d(packet.getPosition().x(), packet.getPosition().y() - 1);
	Point2d down = new Point2d(packet.getPosition().x(), packet.getPosition().y() + 1);

	if (direction != Direction.UP && gridElements.get(down) == '|')
	    return Direction.DOWN;
	if (direction != Direction.DOWN && gridElements.get(up) == '|')
	    return Direction.UP;
	if (direction != Direction.LEFT && gridElements.get(right) == '-')
	    return Direction.RIGHT;
	if (direction != Direction.RIGHT && gridElements.get(left) == '-') {
	    return Direction.LEFT;
	}
	return direction;
    }

    private Map<Point2d, Character> getGridElements(File input) {
	Map<Point2d, Character> gridElements = new HashMap<>();
	int row = 0;

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		for (int i = 0; i < line.length(); i++) {
		    gridElements.put(new Point2d(i, row), line.charAt(i));
		}
		row++;
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return gridElements;

    }

    public static void main(String[] args) {
	Day19 test = new Day19(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 19\\InputFile1.txt"));
	System.out.println(test.getLetters() + "   " + test.getStepcount());
    }

}
