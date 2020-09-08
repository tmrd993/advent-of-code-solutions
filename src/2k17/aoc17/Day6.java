package aoc17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    List<Integer> memoryBanks;

    public Day6(String input) {
	memoryBanks = getMemoryBanks(input);
    }

    /**
     *
     * @param partFlag
     *            false for part 1, true for part 2
     * @return cycles for partFlag == false, cycles in infinite loop for
     *         partFlag == true
     */
    public int redistributionCycles(boolean partFlag) {
	// defensive copy
	List<Integer> memBanks = new ArrayList<Integer>(memoryBanks);
	Map<List<Integer>, Integer> uniqueConfigs = new HashMap<>();
	uniqueConfigs.put(memBanks, 0);

	// only relevant for part 2
	int cycleDifference = 0;

	int memBankSize = memBanks.size();
	int cycles = 0;
	boolean cycleFound = false;

	while (!cycleFound) {
	    // get bank with highest amount of blocks
	    int targetIndex = indexOfBiggestBank(memBanks);

	    int runningIndex = (targetIndex + 1) % memBankSize;
	    // redistribute blocks
	    int blocksToDistribute = memBanks.get(targetIndex);
	    for (int i = 0; i < blocksToDistribute; i++) {
		memBanks.set(targetIndex, memBanks.get(targetIndex) - 1);
		memBanks.set(runningIndex, memBanks.get(runningIndex) + 1);
		runningIndex = (runningIndex + 1) % memBankSize;
	    }

	    if (uniqueConfigs.containsKey(memBanks)) {
		cycleFound = true;
		cycleDifference = cycles - uniqueConfigs.get(memBanks);
	    } else {
		uniqueConfigs.put(memBanks, cycles);
	    }

	    cycles++;

	}

	return partFlag ? cycleDifference : cycles;
    }

    private List<Integer> getMemoryBanks(String input) {
	List<Integer> tmpBanks = new ArrayList<>();
	String[] memBanks = input.split("\t");
	Arrays.stream(memBanks).mapToInt(s -> Integer.parseInt(s)).forEach(tmpBanks::add);
	return tmpBanks;

    }

    private int indexOfBiggestBank(List<Integer> memBanks) {
	int max = memBanks.stream().mapToInt(s -> s).max().getAsInt();
	for (int i = 0; i < memBanks.size(); i++) {
	    if (memBanks.get(i) == max)
		return i;
	}
	return -1;
    }

    public static void main(String[] args) {
	String input = "0	5	10	0	11	14	13	4	11	8	8	7	1	4	12	11";
	//String sampleInput = "0	2	7	0";

	Day6 test = new Day6(input);

	System.out.println(test.redistributionCycles(true));

    }

}
