package aoc21;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.Point2d;
import utils.StaticUtils;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Day5 {
	
	private List<String> rawData;
	
	public Day5(File file) {
		rawData = StaticUtils.inputFileToStringList(file);
	}
	
	public int run(boolean part1) {
	
		List<Point2d> points = new ArrayList<>();
		for(String line : rawData) {
			String pA = line.substring(0, line.indexOf('-') - 1);
			String pB = line.substring(line.indexOf('>') + 2);
			
			Point2d a = new Point2d(Integer.parseInt(pA.substring(0, pA.indexOf(','))), Integer.parseInt(pA.substring(pA.indexOf(',') + 1)));
			Point2d b = new Point2d(Integer.parseInt(pB.substring(0, pB.indexOf(','))), Integer.parseInt(pB.substring(pB.indexOf(',') + 1)));
			
			// only check horizontal/vertical lines
			if(a.x() == b.x()) {
				addPointsOnLine(a, b, points, false);
			} else if (a.y() == b.y()) {
				addPointsOnLine(a, b, points, true);
			} else {
				if(!part1)
					addPointsOnLineDiag(a, b, points);
			}
		}
		
		return (int) points
					.stream()
					.collect(groupingBy(p -> p, counting()))
					.entrySet()
					.stream()
					.filter(p -> p.getValue() > 1)
					.count();
	}
	
	private void addPointsOnLine(Point2d a, Point2d b, List<Point2d> points, boolean isHorizontal) {
		if(isHorizontal) {
			if(a.x() < b.x()) {
				
				for(int i = a.x(); i <= b.x(); i++) {
					points.add(new Point2d(i, a.y()));
				}
				
			} else {
				
				for(int i = b.x(); i <= a.x(); i++) {
					points.add(new Point2d(i, a.y()));
				}
				
			}
	
		} else {
			if(a.y() < b.y()) {
				
				for(int i = a.y(); i <= b.y(); i++) {
					points.add(new Point2d(a.x(), i));
				}
				
			} else {
				
				for(int i = b.y(); i <= a.y(); i++) {
					points.add(new Point2d(a.x(), i));
				}	
				
			}
		}
	}
	
	private void addPointsOnLineDiag(Point2d a, Point2d b, List<Point2d> points) {
		Point2d temp = a;
		
		while(temp.x() != b.x() && temp.y() != b.y()) {
			points.add(temp);
			
			int tempNextX = temp.x() < b.x() ? temp.x() + 1 : temp.x() - 1;
			int tempNextY = temp.y() < b.y() ? temp.y() + 1 : temp.y() - 1;
			
			temp = new Point2d(tempNextX, tempNextY);
		}
		points.add(temp);
	}


	public static void main(String[] args) {
		Day5 test = new Day5(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day5\\InputFile1.txt"));
		System.out.println(test.run(true));
		System.out.println(test.run(false));
	}

}
