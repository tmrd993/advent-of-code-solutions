package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import myutils19.Point2d;
import myutils19.IntCodeComputer;
import myutils19.StaticUtils;
import myutils19.TileType;

public class Day13 {

    private List<Long> initialProgram;

    public Day13(File input) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(input);
    }

    public int run1() {
	Map<Point2d, TileType> tileMappings = new HashMap<>();
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.run();
	Queue<Long> outputVals = computer.outputValues();
	while (!outputVals.isEmpty()) {
	    int x = outputVals.poll().intValue();
	    int y = outputVals.poll().intValue();
	    int tileId = outputVals.poll().intValue();
	    tileMappings.put(new Point2d(x, y), TileType.of(tileId));
	}

	int blockTileCount = (int) tileMappings.entrySet().stream().map(t -> t.getValue())
		.filter(tile -> tile == TileType.BLOCK).count();
	return blockTileCount;
    }

    public int run2() {
	int score = 0;
	IntCodeComputer computer = new IntCodeComputer(new ArrayList<>(initialProgram));
	computer.replace(0, 2);

	Point2d padPosition = null;
	Point2d ballPosition = null;
	
	//initialize the full board, get the ball and pad positions
	computer.run();
	Queue<Long> initialOutput = computer.outputValues();
	while(!initialOutput.isEmpty()) {
	    int x = initialOutput.poll().intValue();
	    int y = initialOutput.poll().intValue();
	    int id = initialOutput.poll().intValue();
	    TileType tp = TileType.of(id);
	    if(tp == TileType.BALL) {
		ballPosition = new Point2d(x, y);
	    }
	    else if(tp == TileType.PADDLE) {
		padPosition = new Point2d(x, y);
	    }
	}
	// computer waits for input at this point
	int joystickVal = 0;
	if(ballPosition.x() > padPosition.x()) {
	    joystickVal = 1;
	}
	else if(ballPosition.x() < padPosition.x()) {
	    joystickVal = -1;
	}
	
	computer.setInputValues(joystickVal);

	int joystickPosition = 0;
	while (!computer.isShutdown()) {
	    computer.run();
	    // flush buffer
	    Queue<Long> outputValues = computer.outputValues();
	    while (!outputValues.isEmpty()) {
		int x = outputValues.poll().intValue();
		int y = outputValues.poll().intValue();
		int id = outputValues.poll().intValue();
		if (x == -1 && y == 0) {
		    score = id;
		} else {
		    TileType tp = TileType.of(id);
		    if(tp == TileType.PADDLE) {
			padPosition = new Point2d(x, y);
		    }
		    else if(tp == TileType.BALL) {
			joystickPosition = x > padPosition.x() ?  1 : (x < padPosition.x() ? -1 : 0);
		    }
		}
	    }
	    if(computer.isStandby())
		computer.setInputValues(joystickPosition);
	}
	return score;
    }

    public static void main(String[] args) {
	Day13 test = new Day13(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 13\\InputFile.txt"));
	System.out.println(test.run2());
    }

}
