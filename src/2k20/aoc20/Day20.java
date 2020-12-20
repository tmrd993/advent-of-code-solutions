package aoc20;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import myutils20.PuzzlePiece;
import myutils20.StaticUtils;

public class Day20 {

    private List<String> rawData;
    private List<PuzzlePiece> puzzlePieces;

    public Day20(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
	puzzlePieces = getPuzzlePieces();
    }

    public long run1() {
	long result = 1;
	for (int i = 0; i < puzzlePieces.size(); i++) {
	    PuzzlePiece currentPiece = puzzlePieces.get(i);
	    int count = 0;
	    for (int j = 0; j < puzzlePieces.size(); j++) {
		if (!currentPiece.equals(puzzlePieces.get(j)) && currentPiece.canConnect(puzzlePieces.get(j))) {
		    count++;
		}
	    }
	    if (count == 2) {
		result *= (long) currentPiece.id();
	    }
	}

	return result;
    }

    public int run2() {
	Map<Integer, Integer> pieceIndexTable = new HashMap<>();
	puzzlePieces.stream().forEach(p -> pieceIndexTable.put(p.id(), puzzlePieces.indexOf(p)));

	PuzzlePiece basePiece = puzzlePieces.get(0);
	basePiece.setFlippable(false);
	Set<PuzzlePiece> processedPieces = new HashSet<>();
	Queue<PuzzlePiece> queue = new LinkedList<>();
	queue.add(basePiece);

	while (!queue.isEmpty()) {
	    PuzzlePiece currentPiece = queue.poll();
	    processedPieces.add(currentPiece);

	    for (PuzzlePiece piece : puzzlePieces) {
		if (!processedPieces.contains(piece) && !currentPiece.equals(piece)) {
		    boolean canConnect = currentPiece.canConnect(piece);

		    if (canConnect)
			queue.add(piece);
		}
	    }
	}

	for (int i = 0; i < puzzlePieces.size(); i++) {
	    PuzzlePiece currentPiece = puzzlePieces.get(i);
	    for (int j = 0; j < puzzlePieces.size(); j++) {
		if (!currentPiece.equals(puzzlePieces.get(j))) {
		    currentPiece.canConnect(puzzlePieces.get(j));
		}
	    }
	}

	// final grid with borders removed, id = 1 arbitrarily chosen
	PuzzlePiece completedPuzzle = new PuzzlePiece(getFullGrid(pieceIndexTable), 1);

	if (seamonsterCount(completedPuzzle) == 0) {
	    boolean foundRightOrientation = false;
	    for (int i = 0; i < 4; i++) {
		completedPuzzle.rotate90CW();
		if (seamonsterCount(completedPuzzle) > 0) {
		    foundRightOrientation = true;
		    break;
		}
	    }

	    if (!foundRightOrientation) {
		completedPuzzle.horizontalFlip();
		for (int i = 0; i < 4; i++) {
		    completedPuzzle.rotate90CW();
		    if (seamonsterCount(completedPuzzle) > 0) {
			foundRightOrientation = true;
			break;
		    }
		}
	    }
	}

	int seamonsterCount = seamonsterCount(completedPuzzle);
	int seamonsterTileCount = 15;
	
	char[][] finalGrid = completedPuzzle.grid();
	
	int rhombusCount = 0;
	for(int i = 0; i < finalGrid.length; i++) {
	    for(int j = 0; j < finalGrid[i].length; j++) {
		if(finalGrid[i][j] == '#') {
		    rhombusCount++;
		}
	    }
	}

	return rhombusCount - (seamonsterCount * seamonsterTileCount);
    }

    private int seamonsterCount(PuzzlePiece piece) {
	char[][] grid = piece.grid();
	int count = 0;
	int seaMonsterWidth = 20;
	int seaMonsterHeight = 3;
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		if (i < grid.length - seaMonsterHeight + 1 && j < grid[i].length - seaMonsterWidth + 1 && i >= 1) {
		    if (posHasMonster(grid, j, i)) {
			count++;
		    }
		}
	    }
	}

	return count;
    }

    private boolean posHasMonster(char[][] grid, int x, int y) {
	if (grid[y + 1][x + 1] == '#' && grid[y + 1][x + 4] == '#' && grid[y][x + 5] == '#' && grid[y][x + 6] == '#'
		&& grid[y + 1][x + 7] == '#' && grid[y + 1][x + 10] == '#' && grid[y][x + 11] == '#'
		&& grid[y][x + 12] == '#' && grid[y + 1][x + 13] == '#' && grid[y + 1][x + 16] == '#'
		&& grid[y][x + 17] == '#' && grid[y][x + 18] == '#' && grid[y][x + 19] == '#'
		&& grid[y - 1][x + 18] == '#') {
	    return true;
	}

	return false;
    }

    private char[][] getFullGrid(Map<Integer, Integer> pieceIndexTable) {
	PuzzlePiece upperLeftCorner = puzzlePieces.stream()
		.filter(p -> p.getConnections() == 2 && p.getTopConnection() == 0 && p.getLeftConnection() == 0)
		.findAny().get();

	char[][] grid = new char[150][150];
	PuzzlePiece current = upperLeftCorner;

	int dx = 0;
	int dy = 0;

	while (current.getBottomConnection() != 0) {
	    PuzzlePiece left = current;
	    while (current.getRightConnection() != 0) {
		int row = dy;
		int col = dx;
		for (int i = 1; i < current.grid().length - 1; i++) {
		    for (int j = 1; j < current.grid()[i].length - 1; j++) {
			grid[row][col++] = current.grid()[i][j];
		    }
		    row++;
		    col = dx;
		}
		dx += 10;
		current = puzzlePieces.get(pieceIndexTable.get(current.getRightConnection()));

		// get right most element
		if (current.getRightConnection() == 0) {
		    row = dy;
		    col = dx;
		    for (int i = 1; i < current.grid().length - 1; i++) {
			for (int j = 1; j < current.grid()[i].length - 1; j++) {
			    grid[row][col++] = current.grid()[i][j];
			}
			row++;
			col = dx;
		    }

		    dx = 0;
		    dy += 10;
		}

	    }
	    current = puzzlePieces.get(pieceIndexTable.get(left.getBottomConnection()));

	    // get the last row
	    if (current.getBottomConnection() == 0) {
		while (current.getRightConnection() != 0) {
		    int row = dy;
		    int col = dx;
		    for (int i = 1; i < current.grid().length - 1; i++) {
			for (int j = 1; j < current.grid()[i].length - 1; j++) {
			    grid[row][col++] = current.grid()[i][j];
			}
			row++;
			col = dx;
		    }
		    dx += 10;
		    current = puzzlePieces.get(pieceIndexTable.get(current.getRightConnection()));

		    // get right most element
		    if (current.getRightConnection() == 0) {
			row = dy;
			col = dx;
			for (int i = 1; i < current.grid().length - 1; i++) {
			    for (int j = 1; j < current.grid()[i].length - 1; j++) {
				grid[row][col++] = current.grid()[i][j];
			    }
			    row++;
			    col = dx;
			}

			dx = 0;
			dy += 10;
		    }
		}
	    }
	}

	return trimGridForPart2(grid);
    }

    private char[][] trimGridForPart2(char[][] grid) {
	int height = 0;
	for (int i = 0; i < grid.length; i++) {
	    if (grid[0][i] == '.' || grid[0][i] == '#') {
		height++;
	    }
	}

	char[][] result = new char[height][height];

	int row = 0;
	int col = 0;
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		if (grid[i][j] == '.' || grid[i][j] == '#') {
		    result[row][col++] = grid[i][j];
		}

	    }
	    if (grid[i][0] == '.' || grid[i][0] == '#') {
		row++;
		col = 0;
	    }
	}
	return result;
    }

    private List<PuzzlePiece> getPuzzlePieces() {
	List<PuzzlePiece> pieces = new ArrayList<>();

	int len = rawData.get(1).length();

	int id = 0;
	char[][] grid = new char[len][len];
	int row = 0;
	int col = 0;
	for (int i = 0; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    if (line.startsWith("T")) {
		id = Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.indexOf(':')));
	    } else if (line.length() > 0) {
		for (int j = 0; j < line.length(); j++) {
		    grid[row][col++] = line.charAt(j);
		}
		row++;
		col = 0;
	    } else {
		pieces.add(new PuzzlePiece(grid, id));
		grid = new char[len][len];
		row = 0;
	    }
	}

	pieces.add(new PuzzlePiece(grid, id));

	return pieces;
    }

    public static void main(String[] args) {
	Day20 test = new Day20(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 20\\InputFile1.txt"));

	System.out.println(test.run2());

    }

}
