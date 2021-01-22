package aoc19;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myutils19.StaticUtils;
import myutils19.IntCodeComputer;
import myutils19.PaintingRobot;
import myutils19.PaintingRobot.TurnDirection;
import myutils19.PanelColor;
import myutils19.Point2d;

public class Day11 {
    List<Long> initialProgram;
    
    public Day11(File input) {
	initialProgram = StaticUtils.commaSeperatedLongFileToList(input);
    }
    
    // set partFlag = true to output solution for part 2 on the console
    public Set<Point2d> run(boolean partFlag) {
	Set<Point2d> paintedPanelPositions = new HashSet<>();
	Map<Point2d, PanelColor> panelColorMapping = new HashMap<>();
	IntCodeComputer robotComputer = new IntCodeComputer(new ArrayList<>(initialProgram));
	PaintingRobot robot = new PaintingRobot(new Point2d(0, 0));
	
	// robot starts on a white panel for part 2
	if(partFlag) {
	    panelColorMapping.put(robot.pos(), PanelColor.WHITE);
	}
	
	while(!robotComputer.isShutdown()) {
	    panelColorMapping.putIfAbsent(robot.pos(), PanelColor.BLACK);
	    paintedPanelPositions.add(robot.pos());
	    int currentColorCode = panelColorMapping.get(robot.pos()).colorCode();
	    robotComputer.setInputValues(currentColorCode);
	    robotComputer.run();
	 
	    int colorValue = robotComputer.outputValues().poll().intValue();
	    int turnValue = robotComputer.outputValues().poll().intValue();
	    TurnDirection turnDirection = turnValue == 0 ? TurnDirection.LEFT : TurnDirection.RIGHT;
	 
	    panelColorMapping.put(robot.pos(), PanelColor.of(colorValue));
	    robot.turn90Degrees(turnDirection);
	    robot.move();
	}
	
	if(partFlag) {
	    printCoordinates(panelColorMapping);
	}
	
	return paintedPanelPositions;
    }
    
    private void printCoordinates(Map<Point2d, PanelColor> coordinates) {
	int xMin = coordinates.entrySet().stream().mapToInt(p -> p.getKey().x()).min().getAsInt();
	int xMax = coordinates.entrySet().stream().mapToInt(p -> p.getKey().x()).max().getAsInt();
	int yMin = coordinates.entrySet().stream().mapToInt(p -> p.getKey().y()).min().getAsInt();
	int yMax = coordinates.entrySet().stream().mapToInt(p -> p.getKey().y()).max().getAsInt();
	
	for(int i = yMin; i < yMax + 1; i++) {
	    for(int j = xMin; j < xMax + 1; j++) {
		Point2d p = new Point2d(j, i);
		if(coordinates.containsKey(p)) {
		    if(coordinates.get(p) == PanelColor.WHITE) {
			System.out.print('#');
		    }
		    else {
			System.out.print('.');
		    }
		}
		else {
		    System.out.print('.');
		}
	    }
	    System.out.println();
	}
    }
    
    public static void main(String[] args) {
	Day11 test = new Day11(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2019\\Day 11\\InputFile.txt"));
	test.run(true);
    }

}
