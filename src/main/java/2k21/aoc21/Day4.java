package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 {
	
	private List<Integer> drawingNumbers;
	private List<int[][]> grids;
	
	public Day4(File file) {
		drawingNumbers = getDrawingNumbers(file);
		grids = getGrids(file);
	}
	
	public int run1() {
		Set<Integer> drawnNumbers = new HashSet<>();
		drawnNumbers.addAll(drawingNumbers.stream().limit(4).collect(Collectors.toList()));
		for(int i = 4; i < drawingNumbers.size(); i++) {
			int num = drawingNumbers.get(i);
			drawnNumbers.add(num);
			
			for(int[][] grid : grids) {
				Set<Integer> cmpList = new HashSet<>();
				boolean hasWon = checkForWin(grid, drawnNumbers, cmpList);
				
				if(hasWon) {
					int unmarkedSum = Stream.of(grid).flatMapToInt(row -> Arrays.stream(row)).filter(n -> !drawnNumbers.contains(n)).sum();
					return unmarkedSum * num;
				}
				
			}
			
		}
		
		
		return -1;
	}
	
	public int run2() {
		Set<Integer> drawnNumbers = new HashSet<>();
		drawnNumbers.addAll(drawingNumbers.stream().limit(4).collect(Collectors.toList()));
		// set of indices of grids that have already won
		Set<Integer> wonGrids = new HashSet<>();
		for(int i = 4; i < drawingNumbers.size(); i++) {
			int num = drawingNumbers.get(i);
			drawnNumbers.add(num);
			
			for(int j = 0; j < grids.size(); j++) {
				if(wonGrids.contains(j)) {
					continue;
				}
				
				int[][] grid = grids.get(j);
				Set<Integer> cmpList = new HashSet<>();
				boolean hasWon = checkForWin(grid, drawnNumbers, cmpList);
				
				if(hasWon) {
					if(wonGrids.size() == grids.size() - 1) {
						int unmarkedSum = Stream.of(grid).flatMapToInt(row -> Arrays.stream(row)).filter(n -> !drawnNumbers.contains(n)).sum();
						return unmarkedSum * num;
					}
					wonGrids.add(j);
				}
			}
		}
		
		
		return -1;
	}
	
	private boolean checkForWin(int[][] grid, Set<Integer> drawnNumbers, Set<Integer> cmpList) {
		// check all rows
		for(int i = 0; i < grid.length; i++) {
			int[] row = grid[i];
			Arrays.stream(row).forEach(num -> {
				cmpList.add(num);
			});
			
			if(drawnNumbers.containsAll(cmpList)) {
				return true;
			}
			
			cmpList.clear();
		}
		
		// check all columns
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid.length; j++) {
				cmpList.add(grid[j][i]);
			}
			
			if(drawnNumbers.containsAll(cmpList)) {
				return true;
			}
			
			cmpList.clear();
		}
		
		
		return false;
	}

	
	private List<Integer> getDrawingNumbers(File file) {
		List<Integer> nums = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				if(line.isBlank()) {
					break;	
				}
				nums.addAll(Arrays.stream(line.split(",")).map(n -> Integer.parseInt(n)).collect(Collectors.toList()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return nums;
	}
	
	private List<int[][]> getGrids(File file) {
		List<int[][]> grids = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			int skipRows = 1;
			//skip drawing numbers
			while ((line = br.readLine()) != null && skipRows > 0) {
				skipRows--;
			}
			
			int[][] grid = new int[5][5];
			int row = 0;
			int col = 0;
			while((line = br.readLine()) != null) {
				if(line.isBlank()) {
					grids.add(grid);
					grid = new int[5][5];
					row = 0;
					continue;
				}
				
				Integer[] nums = Arrays.stream(line.split(" ")).filter(n -> !n.isBlank()).map(n -> Integer.parseInt(n)).toArray(Integer[]::new);
				for(int num : nums) {
					grid[row][col++] = num; 
				}
				col = 0;
				row++;
			}
			grids.add(grid);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return grids;
	}

	public static void main(String[] args) {
		Day4 test = new Day4(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day4\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
