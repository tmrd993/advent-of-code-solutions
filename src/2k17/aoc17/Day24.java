package aoc17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

import myutils2k17.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Day24 {

    private final List<Component> components;

    public Day24(File input) {
	components = getComponents(input);
    }

    // set parameter part1 to false for solution of part 2
    public int highestBridgeStrength(boolean part1) {
	Queue<List<Component>> bridges = new LinkedList<>();
	for (Component component : components) {
	    if (component.leftPinType() == 0) {
		List<Component> bridge = new ArrayList<>();
		bridge.add(component);
		bridges.add(bridge);
	    }
	}

	List<List<Component>> validBridges = new ArrayList<>();
	while (!bridges.isEmpty()) {
	    List<Component> currentBridge = bridges.poll();
	    validBridges.add(currentBridge);
	    Component lastComponent = currentBridge.get(currentBridge.size() - 1);

	    for (Component component : components) {
		if (!currentBridge.contains(component)) {
		    Component tmpC = new Component(component);
		    List<Component> copyComponents = deepCopyComponents(currentBridge);
		    Component tmpLast = new Component(copyComponents.get(copyComponents.size() - 1));
		    if (lastComponent.left() == null && currentBridge.size() > 1) {
			tmpLast.setLeft(tmpC);
			copyComponents.add(tmpC);

			if (component.leftPinType() == lastComponent.leftPinType()) {
			    tmpC.setLeft(tmpLast);
			    bridges.add(copyComponents);
			} else if (component.rightPinType() == lastComponent.leftPinType()) {
			    tmpC.setRight(tmpLast);
			    bridges.add(copyComponents);
			}

		    } else if (lastComponent.right() == null) {
			tmpLast.setRight(tmpC);
			copyComponents.add(tmpC);

			if (component.leftPinType() == lastComponent.rightPinType()) {
			    tmpC.setLeft(lastComponent);
			    bridges.add(copyComponents);
			} else if (component.rightPinType() == lastComponent.rightPinType()) {
			    tmpC.setRight(lastComponent);
			    bridges.add(copyComponents);
			}
		    }
		}
	    }
	}

	int maxStrength = 0;
	if (part1) {
	    maxStrength = validBridges.stream()
		    .mapToInt(b -> b.stream().mapToInt(c -> c.leftPinType() + c.rightPinType()).sum()).max().getAsInt();
	} else {
	    int maxLen = validBridges.stream().mapToInt(s -> s.size()).max().getAsInt();
	    maxStrength = validBridges.stream().filter(b -> b.size() == maxLen)
		    .mapToInt(b -> b.stream().mapToInt(c -> c.leftPinType() + c.rightPinType()).sum()).max().getAsInt();
	}

	return maxStrength;
    }

    private List<Component> getComponents(File input) {
	List<Component> tmpComponents = new ArrayList<>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(input));
	    String line = "";
	    int id = 0;
	    while ((line = br.readLine()) != null) {
		tmpComponents.add(new Component(Integer.parseInt(line.substring(0, line.indexOf('/'))),
			Integer.parseInt(line.substring(line.indexOf('/') + 1)), id++));
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return Collections.unmodifiableList(tmpComponents);
    }

    private List<Component> deepCopyComponents(List<Component> components) {
	List<Component> secondList = new ArrayList<>();
	components.stream().forEach(com -> secondList.add(new Component(com)));
	return secondList;
    }

    public static void main(String[] args) {
	Day24 test = new Day24(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2017\\Day 24\\InputFile1.txt"));
	System.out.println(test.highestBridgeStrength(true));
    }

}
