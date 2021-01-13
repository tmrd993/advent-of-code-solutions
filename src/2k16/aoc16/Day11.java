package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import myutils16.StaticUtils;

public class Day11 {

    List<String> rawData;

    public Day11(File input) {
	rawData = StaticUtils.inputToList(input);
    }

    // part2Flag == true for solution for part 2
    // part 2 takes around 30 seconds to run. might optimize later but for now, I
    // think it's fine given the constraints
    public int run(boolean part2Flag) {
	List<Integer> initialFloorStates = getInitialState();

	int finalBitCount = initialFloorStates.stream().mapToInt(n -> Integer.bitCount(n)).sum();

	if (part2Flag) {
	    int floor1 = initialFloorStates.get(0) | (0b1111 << finalBitCount);
	    initialFloorStates.set(0, floor1);
	    finalBitCount += 4;
	}

	// floor indexing starts at 0
	AreaState initialState = new AreaState(initialFloorStates, 0, 0);
	Queue<AreaState> queue = new LinkedList<>();
	queue.add(initialState);

	Set<AreaState> visitedStates = new HashSet<>();

	while (!queue.isEmpty()) {
	    AreaState currentState = queue.poll();
	    List<Integer> currentFloorStates = currentState.state;
	    int currentSteps = currentState.stepsTaken;
	    int currentFloor = currentState.currentFloor;

	    // done
	    if (Integer.bitCount(currentFloorStates.get(3)) == finalBitCount) {
		return currentSteps;
	    }

	    List<Integer> equipmentIndicies = getOneBitIndicies(currentFloorStates.get(currentFloor), finalBitCount);
	    // add every possible move to the queue
	    // can go up
	    int currentFloorState = currentFloorStates.get(currentFloor);
	    if (currentFloor < 3) {
		int nextFloor = currentFloor + 1;
		int nextFloorState = currentFloorStates.get(nextFloor);
		// take one at a time
		for (int index : equipmentIndicies) {
		    int nextStateCurrentFloor = clearBit(currentFloorState, index);
		    int nextStateNextFloor = setBit(nextFloorState, index);

		    // next state is valid
		    if (isSafeToMove(finalBitCount, nextStateCurrentFloor)
			    && isSafeToMove(finalBitCount, nextStateNextFloor)) {
			List<Integer> nextFloorStates = new ArrayList<>(currentFloorStates);
			nextFloorStates.set(currentFloor, nextStateCurrentFloor);
			nextFloorStates.set(nextFloor, nextStateNextFloor);
			AreaState nextState = new AreaState(nextFloorStates, currentSteps + 1, currentFloor + 1);
			if (!visitedStates.contains(nextState)) {
			    queue.add(nextState);
			    visitedStates.add(nextState);
			}
		    }
		}
		// take two at a time
		for (int i = 0; i < equipmentIndicies.size() - 1; i++) {
		    for (int j = i + 1; j < equipmentIndicies.size(); j++) {
			int nextStateCurrentFloor = clearBit(currentFloorState, equipmentIndicies.get(i));
			nextStateCurrentFloor = clearBit(nextStateCurrentFloor, equipmentIndicies.get(j));
			int nextStateNextFloor = setBit(nextFloorState, equipmentIndicies.get(i));
			nextStateNextFloor = setBit(nextStateNextFloor, equipmentIndicies.get(j));

			if (isSafeToMove(finalBitCount, nextStateCurrentFloor)
				&& isSafeToMove(finalBitCount, nextStateNextFloor)) {
			    List<Integer> nextFloorStates = new ArrayList<>(currentFloorStates);
			    nextFloorStates.set(currentFloor, nextStateCurrentFloor);
			    nextFloorStates.set(nextFloor, nextStateNextFloor);
			    AreaState nextState = new AreaState(nextFloorStates, currentSteps + 1, currentFloor + 1);
			    if (!visitedStates.contains(nextState)) {
				queue.add(nextState);
				visitedStates.add(nextState);
			    }
			}
		    }
		}
	    }

	    // can go down
	    if (currentFloor > 0) {
		int nextFloor = currentFloor - 1;
		int nextFloorState = currentFloorStates.get(nextFloor);
		// take one at a time
		for (int index : equipmentIndicies) {
		    int nextStateCurrentFloor = clearBit(currentFloorState, index);
		    int nextStateNextFloor = setBit(nextFloorState, index);

		    // next state is valid
		    if (isSafeToMove(finalBitCount, nextStateCurrentFloor)
			    && isSafeToMove(finalBitCount, nextStateNextFloor)) {
			List<Integer> nextFloorStates = new ArrayList<>(currentFloorStates);
			nextFloorStates.set(currentFloor, nextStateCurrentFloor);
			nextFloorStates.set(nextFloor, nextStateNextFloor);
			AreaState nextState = new AreaState(nextFloorStates, currentSteps + 1, currentFloor - 1);
			if (!visitedStates.contains(nextState)) {
			    queue.add(nextState);
			    visitedStates.add(nextState);
			}
		    }
		}
		// take two at a time
		for (int i = 0; i < equipmentIndicies.size() - 1; i++) {
		    for (int j = i + 1; j < equipmentIndicies.size(); j++) {
			int nextStateCurrentFloor = clearBit(currentFloorState, equipmentIndicies.get(i));
			nextStateCurrentFloor = clearBit(nextStateCurrentFloor, equipmentIndicies.get(j));
			int nextStateNextFloor = setBit(nextFloorState, equipmentIndicies.get(i));
			nextStateNextFloor = setBit(nextStateNextFloor, equipmentIndicies.get(j));

			if (isSafeToMove(finalBitCount, nextStateCurrentFloor)
				&& isSafeToMove(finalBitCount, nextStateNextFloor)) {
			    List<Integer> nextFloorStates = new ArrayList<>(currentFloorStates);
			    nextFloorStates.set(currentFloor, nextStateCurrentFloor);
			    nextFloorStates.set(nextFloor, nextStateNextFloor);
			    AreaState nextState = new AreaState(nextFloorStates, currentSteps + 1, currentFloor - 1);
			    if (!visitedStates.contains(nextState)) {
				queue.add(nextState);
				visitedStates.add(nextState);
			    }
			}
		    }
		}
	    }
	}

	return -1;
    }

    // check if the next floor state is valid
    private boolean isSafeToMove(int bitCount, int nextState) {
	boolean hasExposedChip = false;
	boolean hasGenerator = false;
	for (int i = 0; i < bitCount; i++) {
	    if (i % 2 == 0) {
		if (getBit(nextState, i) == 1 && getBit(nextState, i + 1) == 0) {
		    hasExposedChip = true;

		    if (hasGenerator) {
			return false;
		    }
		}
	    } else if (i % 2 != 0 && getBit(nextState, i) == 1) {
		hasGenerator = true;

		if (hasExposedChip) {
		    return false;
		}
	    }
	}

	return true;
    }

    // get the initial state of the floors as a list of 4 integers. Each bit
    // represents a generator or a microchip. (generators left, chips right)
    private List<Integer> getInitialState() {
	List<Integer> initialState = new ArrayList<>();

	Map<String, Integer> indexTable = new HashMap<>();
	int indexIncr = -2;

	for (String line : rawData) {
	    int floorState = 0;
	    // empty floor
	    if (line.contains("nothing")) {
		initialState.add(floorState);
		continue;
	    }

	    String[] equipment = Arrays
		    .stream(line.substring(line.indexOf("contains") + "contains".length() + 1).split(",|and"))
		    .filter(s -> !s.isBlank()).map(String::trim).toArray(String[]::new);

	    for (String equipName : equipment) {
		String equipTmp = equipName.contains("-") ? equipName.substring(0, equipName.indexOf('-'))
			: equipName.substring(0, equipName.lastIndexOf(' '));

		if (!indexTable.containsKey(equipTmp)) {
		    indexIncr += 2;
		    indexTable.put(equipTmp, indexIncr);
		}

		if (equipName.contains("generator")) {
		    floorState |= (1 << (indexTable.get(equipTmp) + 1));
		} else {
		    floorState |= (1 << indexTable.get(equipTmp));
		}

	    }
	    initialState.add(floorState);
	}

	return initialState;
    }

    public static void main(String[] args) {
	Day11 test = new Day11(new File("\\C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 11\\InputFile1.txt"));
	System.out.println(test.run(true));

    }

    private int getBit(int num, int position) {
	return (num >> position) & 1;
    }

    private int setBit(int num, int position) {
	return num | (1 << position);
    }

    private int clearBit(int num, int position) {
	return num & ~(1 << position);
    }

    // returns a list of indicies corresponding to the positions (from right to
    // left) of the bits set to 1
    private List<Integer> getOneBitIndicies(int num, int len) {
	List<Integer> indicies = new ArrayList<>();
	for (int i = 0; i < len; i++) {
	    if (getBit(num, i) == 1) {
		indicies.add(i);
	    }
	}

	return indicies;
    }

    private static class AreaState {
	public List<Integer> state;
	public int stepsTaken;
	public int currentFloor;

	public AreaState(List<Integer> state, int stepsTaken, int currentFloor) {
	    this.state = state;
	    this.stepsTaken = stepsTaken;
	    this.currentFloor = currentFloor;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || !(o instanceof AreaState)) {
		return false;
	    }

	    AreaState tmp = (AreaState) o;
	    return this.currentFloor == tmp.currentFloor && this.state.equals(tmp.state);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(currentFloor, state);
	}
    }

}
