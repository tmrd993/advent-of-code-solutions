package aoc21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import utils.Point2d;

public class Day11 {
	
	private Map<Point2d, Integer> octopuses;
	
	public Day11(File file) {
		initOctopuses(file);
	}
	
	private int run(boolean isPart1) {
		
		int steps = isPart1 ? 100 : Integer.MAX_VALUE;
		int flashCount = 0;
		
		Map<Point2d, Integer> octopusesTmp = new HashMap<>(octopuses);
		
		for(int i = 0; i < steps; i++) {
			Queue<Point2d> flashQueue = new LinkedList<>();
			
			octopusesTmp.entrySet().stream().forEach(oct -> {
				int nextNum = octopusesTmp.get(oct.getKey()) + 1;
				octopusesTmp.put(oct.getKey(), nextNum);
				if(nextNum > 9) {
					flashQueue.add(oct.getKey());
				}
			});
			
			Set<Point2d> visited = new HashSet<>();
			visited.addAll(flashQueue);
			while(!flashQueue.isEmpty()) {
				flashCount++;
				Point2d currentP = flashQueue.poll();
				List<Point2d> adj = getAdj(currentP);
				
				adj.stream().filter(p -> octopusesTmp.containsKey(p)).forEach(p -> {
					int nextNum = octopusesTmp.get(p) + 1;
					octopusesTmp.put(p, nextNum);
					if(nextNum > 9 && !visited.contains(p)) {
						flashQueue.add(p);
						visited.add(p);
					}
				});
			}
			
			visited.stream().forEach(p -> {
				octopusesTmp.put(p, 0);
			});
			
			if(!isPart1 && visited.size() == octopusesTmp.size()) {
				return i + 1;
			}

		}
		
		return flashCount;
	}
	
	private List<Point2d> getAdj(Point2d p) {
		Point2d n = new Point2d(p.x(), p.y() - 1);
		Point2d s = new Point2d(p.x(), p.y() + 1);
		Point2d w = new Point2d(p.x() - 1, p.y());
		Point2d e = new Point2d(p.x() + 1, p.y());
		
		Point2d nw = new Point2d(p.x() - 1, p.y() - 1);
		Point2d ne = new Point2d(p.x() + 1, p.y() - 1);
		Point2d sw = new Point2d(p.x() - 1, p.y() + 1);
		Point2d se = new Point2d(p.x() + 1, p.y() + 1);

		return List.of(n, s, w, e, nw, ne, sw, se);
	}

	
	private void initOctopuses(File file) {
		octopuses = new HashMap<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			int row = 0;
			int col = 0;
			while ((line = br.readLine()) != null) {
				
				for(int i = 0; i < line.length(); i++) {
					octopuses.put(new Point2d(col++, row), Integer.parseInt(Character.toString(line.charAt(i))));
				}
				
				col = 0;
				row++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void printMap() {
		int xMax = octopuses.entrySet().stream().mapToInt(h -> h.getKey().x()).max().getAsInt();
		int yMax = octopuses.entrySet().stream().mapToInt(h -> h.getKey().y()).max().getAsInt();
		
		for(int i = 0; i <= yMax; i++) {
			for(int j = 0; j <= xMax; j++) {
				System.out.print(octopuses.get(new Point2d(j, i)));
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Day11 test = new Day11(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day11\\InputFile1.txt"));
		System.out.println(test.run(true));
		System.out.println(test.run(false));
	}



}
