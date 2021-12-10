package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import utils.Point2d;

public class Day9 {

	private Map<Point2d, Integer> heightMap;
	
	public Day9(File file) {
		heightMap = initHeightMap(file);
		
	}
	
	private int run1() {
		return heightMap
				.entrySet()
				.stream()
				.filter(p -> isLowPoint(p.getKey()))
				.mapToInt(p -> heightMap.get(p.getKey()) + 1)
				.sum();
	}
	
	private boolean isLowPoint(Point2d point) {
		Point2d n = new Point2d(point.x(), point.y() - 1);
		Point2d s = new Point2d(point.x(), point.y() + 1);
		Point2d w = new Point2d(point.x() - 1, point.y());
		Point2d e = new Point2d(point.x() + 1, point.y());
		
		int h = heightMap.get(point);
		
		return h < heightMap.getOrDefault(n, 10) 
				&& h < heightMap.getOrDefault(s, 10) 
				&& h < heightMap.getOrDefault(w, 10) 
				&& h < heightMap.getOrDefault(e, 10);
	}
	
	private int run2() {
		
		List<Point2d> lowPoints = heightMap
				.entrySet()
				.stream()
				.filter(e -> isLowPoint(e.getKey()))
				.map(e -> e.getKey())
				.collect(Collectors.toList());
		
		List<List<Integer>> basins = new ArrayList<>();
		
		for(Point2d lowPoint : lowPoints) {
			List<Integer> basin = new ArrayList<>();
			Queue<Point2d> pointQueue = new LinkedList<>();
			
			pointQueue.add(lowPoint);
			
			// flood fill
			Set<Point2d> visited = new HashSet<>();
			visited.add(lowPoint);
			while(!pointQueue.isEmpty()) {
				Point2d currentPoint = pointQueue.poll();
				basin.add(heightMap.get(currentPoint));
				
				Point2d n = new Point2d(currentPoint.x(), currentPoint.y() - 1);
				Point2d s = new Point2d(currentPoint.x(), currentPoint.y() + 1);
				Point2d w = new Point2d(currentPoint.x() - 1, currentPoint.y());
				Point2d e = new Point2d(currentPoint.x() + 1, currentPoint.y());
				
				if(heightMap.containsKey(n) && !visited.contains(n) && heightMap.get(n) < 9) {
					pointQueue.add(n);
					visited.add(n);
				}
				if(heightMap.containsKey(s) && !visited.contains(s) && heightMap.get(s) < 9) {
					pointQueue.add(s);
					visited.add(s);
				}
				if(heightMap.containsKey(w) && !visited.contains(w) && heightMap.get(w) < 9) {
					pointQueue.add(w);
					visited.add(w);
				}
				if(heightMap.containsKey(e) && !visited.contains(e) && heightMap.get(e) < 9) {
					pointQueue.add(e);
					visited.add(e);
				}
			}
			
			basins.add(basin);
			
		}
		
		basins.sort((l1, l2) -> Integer.compare(l2.size(), l1.size()));
		
		return basins.stream().limit(3).mapToInt(l -> l.size()).reduce(1, (a, b) -> a * b);
	}
	
	private Map<Point2d, Integer> initHeightMap(File input) {
		Map<Point2d, Integer> heightMap = new HashMap<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String line = "";
			int row = 0;
			int col = 0;
			while ((line = br.readLine()) != null) {
				
				for(int i = 0; i < line.length(); i++) {
					heightMap.put(new Point2d(col++, row), Integer.parseInt(Character.toString(line.charAt(i))));
				}
				
				col = 0;
				row++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return heightMap;
	}
	
	@SuppressWarnings("unused")
	private void printMap() {
		int xMax = heightMap.entrySet().stream().mapToInt(h -> h.getKey().x()).max().getAsInt();
		int yMax = heightMap.entrySet().stream().mapToInt(h -> h.getKey().y()).max().getAsInt();
		
		for(int i = 0; i <= yMax; i++) {
			for(int j = 0; j <= xMax; j++) {
				System.out.print(heightMap.get(new Point2d(j, i)));
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Day9 test = new Day9(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day9\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}



}
