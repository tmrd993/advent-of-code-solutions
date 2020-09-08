package aoc17;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day21 {

    private char[][] fractalGrid;
    // rules for 2x2 grids
    private Map<String, String> rulesTwo;
    // rules for 3x3 grids
    private Map<String, String> rulesThree;

    public Day21(File input) {
	rulesTwo = new HashMap<>();
	rulesThree = new HashMap<>();
	fillRules(input);
    }

    // part 1
    public int numOfOnAfterN(int n) {
	// initial state
	fractalGrid = new char[][] { { '.', '#', '.' }, { '.', '.', '#' }, { '#', '#', '#' } };
	for (int i = 0; i < n; i++) {
	    enhance();
	}
	int count = 0;
	for (char[] row : fractalGrid) {
	    for (char c : row)
		if (c == '#')
		    count++;
	}
	return count;
    }


    // enhances the fractal grid once (iterates once)
    private void enhance() {
	int currentSize = fractalGrid.length;
	int subSize = currentSize % 2 == 0 ? 2 : 3;
	List<char[][]> subGrids = getSubGrids(fractalGrid, subSize);
	List<char[][]> enhancedSubGrids = getEnhancedSubGrids(subGrids);

	// construct the final grid using the enhanced subgrids
	int finalSize = currentSize + (int) Math.sqrt(enhancedSubGrids.size());
	char[][] nextFractalGrid = new char[finalSize][finalSize];

	int nextSubSize = enhancedSubGrids.get(0).length;

	int currentGridIndex = 0;
	for (int i = 0; i < finalSize; i += nextSubSize) {
	    for (int j = 0; j < finalSize; j += nextSubSize) {
		int row = 0;
		int column = 0;
		for (int k = i; k < i + nextSubSize; k++) {
		    for (int l = j; l < j + nextSubSize; l++) {
			nextFractalGrid[k][l] = enhancedSubGrids.get(currentGridIndex)[row][column++];
		    }
		    row++;
		    column = 0;
		}
		currentGridIndex++;
	    }
	}
	fractalGrid = nextFractalGrid;
    }

    // searches for matches in the rule lists and returns a list of enhanced
    // subgrids
    private List<char[][]> getEnhancedSubGrids(List<char[][]> subGrids) {
	List<char[][]> enhancedGrids = new ArrayList<>();
	int subSize = subGrids.get(0).length;

	Map<String, String> rules = null;
	if (subSize == 2)
	    rules = rulesTwo;
	else
	    rules = rulesThree;

	// search for a valid enhancement rule
	for (char[][] subGrid : subGrids) {

	    boolean found = false;

	    // rotate original grid 4 times (one additional rotation to get it
	    // into the initial state)
	    for (int i = 0; i < 4; i++) {
		String formattedGrid = formattedGrid(subGrid);
		if (rules.containsKey(formattedGrid)) {
		    found = true;
		    enhancedGrids.add(parseGrid(rules.get(formattedGrid), subSize + 1));
		    break;
		}
		rotateRight(subGrid);
	    }

	    // flip the grid if no matches are found
	    if (!found) {
		flip(subGrid);
		// rotate flipped grid 3 times
		for (int i = 0; i < 3; i++) {
		    String rotatedGrid = formattedGrid(subGrid);
		    if (rules.containsKey(rotatedGrid)) {
			found = true;
			enhancedGrids.add(parseGrid(rules.get(rotatedGrid), subSize + 1));
			break;
		    }
		    rotateRight(subGrid);
		}
	    }
	}

	return enhancedGrids;
    }

    private List<char[][]> getSubGrids(char[][] grid, int subSize) {
	List<char[][]> subGrids = new ArrayList<>();

	for (int i = 0; i < grid.length; i += subSize) {
	    for (int j = 0; j < grid.length; j += subSize) {
		char[][] subGrid = new char[subSize][];
		int row = 0;
		int column = 0;
		for (int k = i; k < i + subSize; k++) {
		    subGrid[row] = new char[subSize];
		    for (int l = j; l < j + subSize; l++) {
			subGrid[row][column++] = grid[k][l];
		    }
		    row++;
		    column = 0;
		}
		subGrids.add(subGrid);
	    }
	}

	return subGrids;
    }

    // returns the formatted grid (rows seperated by slashes) as a grid
    private char[][] parseGrid(String formattedGrid, int size) {
	char[][] grid = new char[size][size];
	// System.out.println(formattedGrid + " " + size);
	int column = 0;
	int row = 0;
	for (int i = 0; i < formattedGrid.length(); i++) {
	    if (formattedGrid.charAt(i) == '/') {
		row++;
		column = 0;
	    } else
		grid[row][column++] = formattedGrid.charAt(i);
	}
	return grid;
    }

    // returns the grid formatted as rows seperated by slashes
    private String formattedGrid(char[][] grid) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid.length; j++) {
		sb.append(grid[i][j]);
	    }
	    if (i < grid.length - 1)
		sb.append("/");
	}
	return sb.toString();
    }

    // fills the rules lists
    private void fillRules(File input) {
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    while ((line = br.readLine()) != null) {
		if (line.length() > 20) {
		    rulesThree.put(line.substring(0, line.indexOf('=') - 1), line.substring(line.indexOf('>') + 2));
		} else
		    rulesTwo.put(line.substring(0, line.indexOf('=') - 1), line.substring(line.indexOf('>') + 2));
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // rotates the grid 90Åã clockwise
    public static void rotateRight(char[][] grid) {
	char[][] tmpGrid = deepCopyGrid(grid);

	int columnTmpGrid = 0;
	int row = 0;
	int column = 0;
	for (int i = 0; i < grid.length; i++) {
	    for (int j = grid.length - 1; j >= 0; j--) {
		grid[row][column++] = tmpGrid[j][columnTmpGrid];
		if (column == grid.length) {
		    column = 0;
		    row++;
		    columnTmpGrid++;
		}
	    }
	}
    }

    // flips the grid elements (reverses every row)
    private static char[][] flip(char[][] grid) {
	for (char[] subA : grid) {
	    for (int i = 0; i < grid.length / 2; i++) {
		char tmp = subA[i];
		subA[i] = subA[grid.length - i - 1];
		subA[grid.length - i - 1] = tmp;
	    }
	}

	// Arrays.stream(grid).map(Arrays::toString).forEach(System.out::println);
	return grid;
    }

    private static char[][] deepCopyGrid(char[][] input) {
	if (input == null)
	    return null;
	char[][] result = new char[input.length][];
	for (int r = 0; r < input.length; r++) {
	    result[r] = input[r].clone();
	}
	return result;
    }

    public static void main(String[] args) {
	Day21 test = new Day21(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 21\\InputFile1.txt"));

	System.out.println(test.numOfOnAfterN(18));
	
    }
}
