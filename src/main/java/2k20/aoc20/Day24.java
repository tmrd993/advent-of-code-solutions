package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import myutils20.StaticUtils;
import myutils20.Point3d;

public class Day24 {

    private List<String> rawData;
    private List<List<String>> commands;
    private final Set<String> directions = Set.of("e", "se", "sw", "w", "nw", "ne");
    private final int cycleCountPt2 = 100;

    public Day24(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
	commands = getCommands();
    }

    public int run1() {
	return (int) getPaintTable().entrySet().stream().filter(e -> e.getValue()).count();
    }

    public int run2() {
	Map<Point3d, Boolean> paintTable = getPaintTable();

	for (int i = 0; i < cycleCountPt2; i++) {

	    List<Point3d> blackTiles = paintTable.entrySet().stream().filter(e -> e.getValue()).map(e -> e.getKey())
		    .collect(Collectors.toList());

	    Map<Point3d, Boolean> paintTableCpy = new HashMap<>();
	    Set<Point3d> examinedTiles = new HashSet<>();

	    for (Point3d blackTile : blackTiles) {
		paintTableCpy.put(blackTile, true);
		List<Point3d> adjacentPositions = getAdjacent(blackTile);
		int adjacentBlackTileCount = countBlackTiles(adjacentPositions, paintTable);

		if (adjacentBlackTileCount == 0 || adjacentBlackTileCount > 2) {
		    paintTableCpy.put(blackTile, false);
		}

		examinedTiles.add(blackTile);

		for (Point3d neighbor : adjacentPositions) {
		    if (!examinedTiles.contains(neighbor)) {
			int blackTileCount = countBlackTiles(getAdjacent(neighbor), paintTable);
			// is white
			if ((!paintTable.containsKey(neighbor) || !paintTable.get(neighbor)) && blackTileCount == 2) {
			    paintTableCpy.put(neighbor, true);
			} else if (paintTable.get(neighbor) && (blackTileCount == 0 || blackTileCount > 2)) {
			    paintTableCpy.put(neighbor, false);
			}
			examinedTiles.add(neighbor);
		    }
		}

	    }

	    paintTable = paintTableCpy;
	}

	return (int) paintTable.entrySet().stream().filter(e -> e.getValue()).count();
    }

    private int countBlackTiles(List<Point3d> adjacentPositions, Map<Point3d, Boolean> paintTable) {
	int count = 0;
	for (Point3d tile : adjacentPositions) {
	    paintTable.putIfAbsent(tile, false);
	    if (paintTable.get(tile)) {
		count++;
	    }
	}
	return count;
    }

    private List<Point3d> getAdjacent(Point3d pos) {
	return List.of(new Point3d(pos.x() + 1, pos.y() - 1, pos.z()), new Point3d(pos.x(), pos.y() - 1, pos.z() + 1),
		new Point3d(pos.x() - 1, pos.y(), pos.z() + 1), new Point3d(pos.x() - 1, pos.y() + 1, pos.z()),
		new Point3d(pos.x(), pos.y() + 1, pos.z() - 1), new Point3d(pos.x() + 1, pos.y(), pos.z() - 1));
    }

    private Map<Point3d, Boolean> getPaintTable() {
	// map positions to boolean values. false = white, true = black
	Map<Point3d, Boolean> paintTable = new HashMap<>();

	for (List<String> commandList : commands) {
	    Point3d targetPos = moveToFromOrigin(commandList);
	    paintTable.putIfAbsent(targetPos, false);
	    paintTable.put(targetPos, !paintTable.get(targetPos));
	}

	return paintTable;
    }

    public Point3d moveToFromOrigin(List<String> directions) {
	Point3d pos = new Point3d(0, 0, 0);

	for (String cmd : directions) {
	    if (cmd.equals("e")) {
		pos = new Point3d(pos.x() + 1, pos.y() - 1, pos.z());
	    } else if (cmd.equals("se")) {
		pos = new Point3d(pos.x(), pos.y() - 1, pos.z() + 1);
	    } else if (cmd.equals("sw")) {
		pos = new Point3d(pos.x() - 1, pos.y(), pos.z() + 1);
	    } else if (cmd.equals("w")) {
		pos = new Point3d(pos.x() - 1, pos.y() + 1, pos.z());
	    } else if (cmd.equals("nw")) {
		pos = new Point3d(pos.x(), pos.y() + 1, pos.z() - 1);
	    } else if (cmd.equals("ne")) {
		pos = new Point3d(pos.x() + 1, pos.y(), pos.z() - 1);
	    } else {
		throw new IllegalArgumentException(cmd + " is not mapped to any direction");
	    }
	}

	return pos;
    }

    private List<List<String>> getCommands() {
	List<List<String>> cmds = new ArrayList<>();

	for (String line : rawData) {
	    List<String> subCmds = new ArrayList<>();
	    for (int i = 0; i < line.length(); i++) {
		if (i < line.length() - 1) {
		    if (directions.contains(Character.toString(line.charAt(i)) + line.charAt(i + 1))) {
			subCmds.add(Character.toString(line.charAt(i)) + line.charAt(i + 1));
			i++;
		    } else {
			subCmds.add(Character.toString(line.charAt(i)));
		    }

		} else {
		    subCmds.add(Character.toString(line.charAt(i)));
		}
	    }

	    cmds.add(subCmds);
	}

	return cmds;
    }

    public static void main(String[] args) {
	Day24 test = new Day24(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 24\\InputFile1.txt"));
	System.out.println(test.run2());
    }

}
