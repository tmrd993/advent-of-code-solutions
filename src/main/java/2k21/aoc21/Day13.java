package aoc21;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import utils.Point2d;
import utils.StaticUtils;

public class Day13 {

	private List<String> rawData;
	private Map<Point2d, Boolean> dotMapping;
	private List<String> foldInstructions;

	public Day13(File file) {
		rawData = StaticUtils.inputFileToStringList(file);
		initData();
	}

	private int run1() {
		Map<Point2d, Boolean> defCopyDotMapping = new HashMap<>(dotMapping);
		String foldInstruction = foldInstructions.get(0);
		
		defCopyDotMapping = fold(foldInstruction, defCopyDotMapping);

		return (int) defCopyDotMapping.entrySet().stream().filter(d -> d.getValue()).count();
	}
	
	private int run2() {
		Map<Point2d, Boolean> defCopyDotMapping = new HashMap<>(dotMapping);
	
		for(String instr : foldInstructions) {
			defCopyDotMapping = fold(instr, defCopyDotMapping);
		}
		
		printMap(defCopyDotMapping);

		return 0;
	}
	
	private Map<Point2d, Boolean> fold(String foldInstruction, Map<Point2d, Boolean> dotMapping) {
		if(!foldInstruction.startsWith("fold along")) {
			throw new IllegalArgumentException();
		}
		
		int section = Integer.parseInt(foldInstruction.substring(foldInstruction.indexOf('=') + 1));
		if(foldInstruction.contains("y")) {
			Set<Point2d> toFold = dotMapping.entrySet().stream().filter(d -> d.getKey().y() > section).map(d -> d.getKey()).collect(Collectors.toSet());
			toFold.stream().forEach(d -> dotMapping.remove(d));
			toFold.stream().forEach(d -> dotMapping.put(new Point2d(d.x(), d.y() - ((d.y() - section) * 2)), true));
		} else {
			Set<Point2d> toFold = dotMapping.entrySet().stream().filter(d -> d.getKey().x() > section).map(d -> d.getKey()).collect(Collectors.toSet());
			toFold.stream().forEach(d -> dotMapping.remove(d));
			toFold.stream().forEach(d -> dotMapping.put(new Point2d(d.x() - ((d.x() - section) * 2), d.y()), true));
		}
		
		return dotMapping;
		
	}
	
	@SuppressWarnings("unused")
	private void printMap(Map<Point2d, Boolean> dotMapping) {
		int xMax = dotMapping.entrySet().stream().mapToInt(h -> h.getKey().x()).max().getAsInt();
		int yMax = dotMapping.entrySet().stream().mapToInt(h -> h.getKey().y()).max().getAsInt();
		
		for(int i = 0; i <= yMax; i++) {
			for(int j = 0; j <= xMax; j++) {
				if(dotMapping.getOrDefault(new Point2d(j, i), false)) {
					System.out.print('#');
				} else {
					System.out.print(' ');
				}
			}
			System.out.println();
		}
	}

	private void initData() {
		dotMapping = new HashMap<>();
		foldInstructions = new ArrayList<>();

		boolean foldFlag = false;
		for (String line : rawData) {
			if (line.isBlank()) {
				foldFlag = true;
				continue;
			}

			if (foldFlag) {
				foldInstructions.add(line);
			} else {
				int x = Integer.parseInt(line.substring(0, line.indexOf(',')));
				int y = Integer.parseInt(line.substring(line.indexOf(',') + 1));

				dotMapping.put(new Point2d(x, y), true);
			}
		}

	}

	public static void main(String[] args) {
		Day13 test = new Day13(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day13\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
