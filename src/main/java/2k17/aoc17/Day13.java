package aoc17;

import java.util.Map;

import myutils17.SecurityLayer;

import java.io.File;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day13 {

    private Map<Integer, SecurityLayer> securityLayers;

    public Day13(File input) {
	securityLayers = getSecurityLayers(input);
    }

    // part 2
    public int delayForPerfectPass() {
	Map<Integer, SecurityLayer> secLayers = deepCopyMap(securityLayers);

	boolean pass = false;
	int delay = 0;
	while (!pass) {
	    pass = runTrip(secLayers, delay++);
	    //System.out.println(delay);
	}

	return delay - 1;
    }

    private boolean runTrip(Map<Integer, SecurityLayer> secLayers, int delay) {
	for (Map.Entry<Integer, SecurityLayer> e : secLayers.entrySet()) {
	    int depth = e.getKey();
	    int range = e.getValue().range();
	    if ((delay + depth) % ((range - 1) * 2) == 0)
		return false;

	}
	return true;
    }

    // part 1
    public int getTripSeverity() {
	Map<Integer, SecurityLayer> secLayers = deepCopyMap(securityLayers);

	int maxDepth = secLayers.entrySet().stream().mapToInt(s -> s.getKey()).max().getAsInt();
	int finalSeverity = 0;

	for (int i = 0; i <= maxDepth; i++) {
	    if (secLayers.containsKey(i)) {
		if (secLayers.get(i).intrusionDetected(0)) {
		    finalSeverity += secLayers.get(i).intrusionSeverity();
		}
	    }
	    secLayers.entrySet().stream().forEach(s -> s.getValue().advanceScanner());
	}

	return finalSeverity;
    }

    private Map<Integer, SecurityLayer> deepCopyMap(Map<Integer, SecurityLayer> source) {
	Map<Integer, SecurityLayer> copy = new HashMap<>();
	source.entrySet().stream().forEach(
		s -> copy.put(s.getKey(), new SecurityLayer(s.getValue().range(), s.getValue().depth())));
	return copy;
    }

    private Map<Integer, SecurityLayer> getSecurityLayers(File input) {
	Map<Integer, SecurityLayer> secLayers = new HashMap<>();

	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));

	    String line;
	    while ((line = br.readLine()) != null) {
		int depth = Integer.parseInt(line.substring(0, line.indexOf(':')));
		int range = Integer.parseInt(line.substring(line.indexOf(':') + 2));
		secLayers.put(depth, new SecurityLayer(range, depth));
	    }

	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return secLayers;
    }

    public static void main(String[] args) {
	Day13 test = new Day13(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 13\\InputFile1.txt"));
	System.out.println(test.delayForPerfectPass());
    }

}
