package aoc21;

import java.io.File;
import java.util.List;

import utils.Pair;
import utils.StaticUtils;
import utils.Triple;

public class Day2 {
	
	private List<String> cmds;
	
	public Day2(File input) {
		cmds = StaticUtils.inputFileToStringList(input);
	}
	
	public int run1() {
		Pair<Integer, Integer> pos = new Pair<>(0, 0);
		for(String cmd : cmds) {
			pos = execCmdPt1(cmd, pos);
		}
		
		return pos.k() * pos.v();
	}
	
	public int run2() {
		// horizontal pos, depth, aim ...
		Triple<Integer, Integer, Integer> pos = new Triple<>(0, 0, 0);
		for(String cmd : cmds) {
			pos = execCmdPt2(cmd, pos);
		}
		
		return pos.getLeft() * pos.getMiddle();
	}
	
	private Triple<Integer, Integer, Integer> execCmdPt2(String cmd, Triple<Integer, Integer, Integer> pos) {
		int amount = Integer.parseInt(cmd.substring(cmd.indexOf(" ") + 1));
		if(cmd.contains("forward")) {
			pos = new Triple<>(pos.getLeft() + amount, (pos.getMiddle() + (amount * pos.getRight())), pos.getRight());
		} else if(cmd.contains("down")) {
			pos = new Triple<>(pos.getLeft(), pos.getMiddle(), pos.getRight() + amount);
		} else if(cmd.contains("up")) {
			pos = new Triple<>(pos.getLeft(), pos.getMiddle(), pos.getRight() - amount);
		} else {
			throw new IllegalArgumentException();
		}
		return pos;
	}
	
	private Pair<Integer, Integer> execCmdPt1(String cmd, Pair<Integer, Integer> pos) {
		String amount = cmd.substring(cmd.indexOf(" ") + 1);
		if(cmd.contains("forward")) {
			pos = new Pair<>(pos.k() + Integer.parseInt(amount), pos.v()); 
		} else if(cmd.contains("down")) {
			pos = new Pair<>(pos.k(), pos.v() - Integer.parseInt(amount)); 
		} else if(cmd.contains("up")) {
			pos = new Pair<>(pos.k(), pos.v() + Integer.parseInt(amount)); 
		} else {
			throw new IllegalArgumentException();
		}
		
		return pos;
	}
	
	public static void main(String[] args) {
		Day2 test = new Day2(new File("C:\\Users\\tmerdin\\aoc\\aoc21\\day2\\InputFile1.txt"));
		System.out.println(test.run1());
		System.out.println(test.run2());
	}

}
