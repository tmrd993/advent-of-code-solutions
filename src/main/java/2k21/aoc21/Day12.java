package aoc21;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import utils.StaticUtils;

public class Day12 {

	private List<String> rawData;

	public Day12(File file) {
		rawData = StaticUtils.inputFileToStringList(file);
	}

	private int run1() {

		Map<String, List<String>> adjLists = getAdjLists();

		Queue<String> queue = new LinkedList<>();
		queue.add(", start");

		int count = 0;
		while (!queue.isEmpty()) {
			String current = queue.poll();
			List<String> adjList = adjLists.get(current.substring(current.lastIndexOf(',') + 2));

			if (current.contains("end")) {
				count++;
				continue;
			}

			for (String adj : adjList) {
				if (this.isSmallCave(adj)) {
					if (!current.contains(adj)) {
						queue.add(current + ", " + adj);
					}
				} else {
					queue.add(current + ", " + adj);
				}

			}

		}

		return count;
	}

	private int run2() {

		Map<String, List<String>> adjLists = getAdjLists();

		Queue<String> queue = new LinkedList<>();
		queue.add(", start");
		Queue<Boolean> isAllowedQueue = new LinkedList<>();
		isAllowedQueue.add(true);

		int count = 0;
		while (!queue.isEmpty()) {
			String current = queue.poll();
			List<String> adjList = adjLists.get(current.substring(current.lastIndexOf(',') + 2));
			boolean isAllowedSmallCave = isAllowedQueue.poll();

			if (current.contains("end")) {
				count++;
				continue;
			}

			for (String adj : adjList) {
				if (this.isSmallCave(adj)) {
					if (!current.contains(adj)) {
						queue.add(current + ", " + adj);
						isAllowedQueue.add(isAllowedSmallCave);
					} else if (isAllowedSmallCave && !adj.equals("start")) {
						queue.add(current + ", " + adj);
						isAllowedQueue.add(false);
					}
				} else {
					queue.add(current + ", " + adj);
					isAllowedQueue.add(isAllowedSmallCave);
				}

			}

		}

		return count;
	}

	private boolean isSmallCave(String caveStr) {
		return caveStr.toLowerCase().equals(caveStr);
	}

	private Map<String, List<String>> getAdjLists() {
		Map<String, List<String>> adjLists = new HashMap<>();

		for (String line : rawData) {
			String left = line.substring(0, line.indexOf('-'));
			String right = line.substring(line.indexOf('-') + 1);
			adjLists.putIfAbsent(left, new ArrayList<>());
			adjLists.putIfAbsent(right, new ArrayList<>());

			adjLists.get(left).add(right);
			adjLists.get(right).add(left);
		}

		return adjLists;
	}

	public static void main(String[] args) {
		Day12 test = new Day12(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day12\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
