package aoc16;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import myutils16.BalanceBot;
import myutils16.StaticUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Day10 {

    private List<String> instructions;
    private List<BalanceBot> bots;
    private final int targetChipLow = 17;
    private final int targetChipHigh = 61;

    public Day10(File input) {
	instructions = StaticUtils.inputToList(input);
	bots = getBots();
	setInitialChips();
    }

    /**
     * 
     * @param partFlag true for part 1, false for part 2
     * @return
     */
    public int run(boolean partFlag) {
	Map<Integer, Integer> outputBins = new HashMap<>();
	List<String> tmpInstr = new ArrayList<>(instructions);
	// while instructions are available
	while (!tmpInstr.isEmpty()) {
	    
	    //check if output 0, 1, 2 are populated
	    if(!partFlag && outputBins.containsKey(0) && outputBins.containsKey(1) && outputBins.containsKey(2)) {
		return outputBins.get(0) * outputBins.get(1) * outputBins.get(2);
	    }
	    
	    List<String> toRemove = new ArrayList<>();
	    for (String instr : tmpInstr) {
		int sourceBotId = Integer.parseInt(instr.substring(instr.indexOf("t") + 2, instr.indexOf("g") - 1));
		BalanceBot sourceBot = bots.stream().filter(b -> b.id() == sourceBotId).findFirst().get();
		// current bot needs two chips
		if (sourceBot.hasTwoChips()) {
		    
		    if(partFlag && isTargetBot(sourceBot)) {
			return sourceBot.id();
		    }
		    
		    String targetLow = instr.substring(instr.indexOf("to") + 3, instr.indexOf("and") - 1);
		    String targetHigh = instr.substring(instr.indexOf("to", instr.indexOf("high")) + 3);
		    int targetLowId = Integer.parseInt(targetLow.substring(targetLow.indexOf(" ") + 1));
		    int targetHighId = Integer.parseInt(targetHigh.substring(targetHigh.indexOf(" ") + 1));
		    if(targetLow.contains("output")) {
			outputBins.putIfAbsent(targetLowId, sourceBot.getLow());
		    }
		    else {
			BalanceBot targetBot = bots.stream().filter(b -> b.id() == targetLowId).findFirst().get();
			targetBot.addChip(sourceBot.getLow());
			if(partFlag && isTargetBot(targetBot)) {
			    return targetBot.id();
			}
		    }
		    
		    if(targetHigh.contains("output")) {
			outputBins.putIfAbsent(targetHighId, sourceBot.getHigh());
		    }
		    else {
			BalanceBot targetBot = bots.stream().filter(b -> b.id() == targetHighId).findFirst().get();
			targetBot.addChip(sourceBot.getHigh());
			if(partFlag && isTargetBot(targetBot)) {
			    return targetBot.id();
			}
		    }
		    
		    sourceBot.deleteChips();
		    toRemove.add(instr);
		}
	    }
	    tmpInstr.removeAll(toRemove);
	}

	return 0;
    }

    private boolean isTargetBot(BalanceBot bot) {
	if(!bot.hasTwoChips())
	    return false;
	return bot.getLow() == targetChipLow && bot.getHigh() == targetChipHigh;
    }
    
    private List<BalanceBot> getBots() {
	List<BalanceBot> bots = new ArrayList<>();
	for (String instruction : instructions) {
	    String[] botSplit = instruction.split("bot");
	    for (String str : botSplit) {
		putBot(bots, str.trim());
	    }
	}
	return bots;
    }

    private void setInitialChips() {
	for (String instruction : instructions) {
	    if (instruction.startsWith("val")) {
		int chipVal = Integer
			.parseInt(instruction.substring(instruction.indexOf("e") + 2, instruction.indexOf("g") - 1));
		int botId = Integer.parseInt(instruction.substring(instruction.indexOf("bot") + 4));

		bots.stream().filter(b -> b.id() == botId).findFirst().get().addChip(chipVal);
	    }
	}
	instructions.removeAll(instructions.stream().filter(s -> s.startsWith("value")).collect(Collectors.toList()));
    }

    private void putBot(List<BalanceBot> bots, String instruction) {
	if (instruction.length() > 0 && Character.isDigit(instruction.charAt(0))) {
	    if (instruction.contains(" ")) {
		BalanceBot bot = new BalanceBot(Integer.parseInt(instruction.substring(0, instruction.indexOf(" "))));
		if (!bots.contains(bot)) {
		    bots.add(bot);
		}
	    } else {
		BalanceBot bot = new BalanceBot(Integer.parseInt(instruction));
		if (!bots.contains(bot)) {
		    bots.add(bot);
		}
	    }
	}
    }

    public static void main(String[] args) {
	Day10 test = new Day10(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 10\\InputFile1.txt"));

	System.out.println(test.run(false));

    }

}
