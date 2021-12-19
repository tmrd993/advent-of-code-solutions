package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import utils.Point2d;
import utils.StaticUtils;
import utils.Pair;

public class Day15 {
	
	private Map<Point2d, Integer> points;
	private Map<Point2d, Integer> pointsPt2;
	
	public Day15(File input) {
		initMap(input);
		initMapPt2(input);
	}
	
	public int run(Map<Point2d, Integer> points) {
		
		Point2d start = new Point2d(0, 0);
		
		PriorityQueue<Pair<Point2d, Integer>> queue = new PriorityQueue<>(Comparator.comparing(Pair::v));
		queue.add(new Pair<>(start, 0));
		
		Map<Point2d, Point2d> cameFrom = new HashMap<>();
		Map<Point2d, Integer> costSoFar = new HashMap<>();
		cameFrom.put(start, null);
		costSoFar.put(start, 0);
		
		int xMax = points.entrySet().stream().mapToInt(p -> p.getKey().x()).max().getAsInt();
		int yMax = points.entrySet().stream().mapToInt(p -> p.getKey().y()).max().getAsInt();
		Point2d target = new Point2d(xMax, yMax);
		
		while(!queue.isEmpty()) {
			Pair<Point2d, Integer> current = queue.poll();
			Point2d currentPos = current.k();
			int currentCost = costSoFar.get(currentPos);
						
			if(currentPos.equals(target)) {
				return currentCost;
			}
			
			Set<Point2d> neighbors = getNeighbors(points, currentPos);

			for(Point2d next : neighbors) {
				int nextCost =  currentCost + points.get(next);
				if(!costSoFar.containsKey(next) || costSoFar.get(next) > nextCost) {
					costSoFar.put(next, nextCost);
					int prio = currentPos.distanceL1(next) + nextCost;
					queue.add(new Pair<>(next, prio));
					cameFrom.put(next, currentPos);
				}
			}
		}
		
		// no path found, shouldn't happen with valid input
		return -1;
	}
	
	private Set<Point2d> getNeighbors(Map<Point2d, Integer> points, Point2d currentPos) {
		Point2d up = new Point2d(currentPos.x(), currentPos.y() - 1);
		Point2d down = new Point2d(currentPos.x(), currentPos.y() + 1);
		Point2d left = new Point2d(currentPos.x() - 1, currentPos.y());
		Point2d right = new Point2d(currentPos.x() + 1, currentPos.y());
		
		Set<Point2d> neighbors = new HashSet<>();
		if(points.containsKey(up)) {
			neighbors.add(up);
		}
		if(points.containsKey(down)) {
			neighbors.add(down);
		}
		if(points.containsKey(left)) {
			neighbors.add(left);
		}
		if(points.containsKey(right)) {
			neighbors.add(right);
		}
		
		return neighbors;
	}
	
	private void initMap(File input) {
		points = new HashMap<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String line = "";
			
			int row = 0;
			int col = 0;
			while((line = br.readLine()) != null) {
				
				for(int i = 0; i < line.length(); i++) {
					points.put(new Point2d(col++, row), Integer.parseInt(Character.toString(line.charAt(i))));
				}
				row++;
				col = 0;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initMapPt2(File input) {
		pointsPt2 = new HashMap<>();
	
		List<String> lines = StaticUtils.inputFileToStringList(input);
	
		int row = 0;
		int col = 0;
		int baseIncr = 0;
		for(int i = 0; i < 5; i++) {
			
			for(String line : lines) {
				
				for(int j = 0; j < line.length() * 5; j++) {
					int nextRiskLvl = nextRiskLvl(line, j, baseIncr);
					pointsPt2.put(new Point2d(col++, row), nextRiskLvl);
				}
	
				row++;
				col = 0;
				
			}
			baseIncr++;	
		}
	}
	
	private int nextRiskLvl(String line, int index, int baseIncr) {
		
		int riskLvl = Integer.parseInt(Character.toString(line.charAt(index % line.length())));
		for(int i = 0; i < ((index / line.length()) + baseIncr); i++) {
			riskLvl++;
			if(riskLvl == 10) {
				riskLvl = 1;
			}
		}
		
		return riskLvl;
	}
	
	@SuppressWarnings("unused")
	private void printMap(Map<Point2d, Integer> points) {
		int xMax = points.entrySet().stream().mapToInt(e -> e.getKey().x()).max().getAsInt();
		int yMax = points.entrySet().stream().mapToInt(e -> e.getKey().y()).max().getAsInt();
		
		for(int i = 0; i <= yMax; i++) {
			for(int j = 0; j <= xMax; j++) {
				System.out.print(points.get(new Point2d(j, i)));
			}
			System.out.println();
		}
		
	}

	public static void main(String[] args) {
		Day15 test = new Day15(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day15\\InputFile1.txt"));
		System.out.println(test.run(test.points));
		System.out.println(test.run(test.pointsPt2));

	}

}
