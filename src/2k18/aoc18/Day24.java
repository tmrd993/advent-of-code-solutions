package aoc18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myutils.ImmuneSystemGroup.Type;
import myutils.ImmuneSystemGroup;

public class Day24 {

    private List<ImmuneSystemGroup> allGroups;
    private final File inputFile;

    public Day24(File inputFile) throws IOException {
	this.inputFile = inputFile;
	allGroups = getAllGroups(inputFile, 0);
    }

    // part 1
    /**
     *
     * @return sum of all surviving units of the winning side
     */
    public int run() {
	List<ImmuneSystemGroup> winners = getWinners();
	System.out.println(winners.stream().findAny().get().type() + " " + winners);
	return winners.stream().mapToInt(n -> n.units()).sum();
    }

    // part 2
    // runs in O(log(n) * (max - min)), could be improved but the performance is
    // fine for the given input
    /**
     *
     * @return units after immune system wins for the first time
     * @throws IOException
     */
    public int run2() throws IOException {
	int immuneSystemBoost = 1;

	int min = 0;
	int max = immuneSystemBoost;
	// get lower and upper bound
	while (!isImmuneSystem(getWinners())) {
	    min = max;
	    max *= 2;
	    allGroups = getAllGroups(inputFile, max);
	}

	// target is within min ... max
	List<ImmuneSystemGroup> winners = null;
	for (int i = min + 1; i <= max; i++) {
	    allGroups = getAllGroups(inputFile, i);
	    winners = getWinners();
	    if (winners != null)
		if (isImmuneSystem(winners)) {
		    break;
		}
	}

	return winners.stream().mapToInt(n -> n.units()).sum();
    }

    private boolean isImmuneSystem(List<ImmuneSystemGroup> winners) {
	if (winners == null)
	    return false;

	return winners.stream().findAny().get().type() == Type.IMMUNE_SYSTEM;
    }

    /**
     *
     * @return list containing the surviving units of the winning side
     */
    public List<ImmuneSystemGroup> getWinners() {

	List<ImmuneSystemGroup> activeImmuneSystem = new ArrayList<ImmuneSystemGroup>();
	List<ImmuneSystemGroup> activeInfection = new ArrayList<ImmuneSystemGroup>();

	for (ImmuneSystemGroup group : allGroups) {
	    if (group.type() == Type.IMMUNE_SYSTEM) {
		activeImmuneSystem.add(group);
	    } else
		activeInfection.add(group);
	}

	while (!activeImmuneSystem.isEmpty() && !activeInfection.isEmpty()) {

	    allGroups.sort(new TargetSelectionComparator());
	    activeImmuneSystem.sort(new TargetSelectionComparator());
	    activeInfection.sort(new TargetSelectionComparator());

	    // select targets for each group
	    ImmuneSystemGroup[] attackers = new ImmuneSystemGroup[activeImmuneSystem.size() + activeInfection.size()];

	    Map<ImmuneSystemGroup, ImmuneSystemGroup> attackerAndTargets = new HashMap<ImmuneSystemGroup, ImmuneSystemGroup>();

	    int index = 0;
	    for (ImmuneSystemGroup group : allGroups) {
		if (group.units() > 0 && (activeImmuneSystem.contains(group) || activeInfection.contains(group))) {
		    attackers[index++] = group;
		}
	    }

	    Set<ImmuneSystemGroup> unavailableTargets = new HashSet<ImmuneSystemGroup>();

	    for (int i = 0; i < attackers.length; i++) {
		if (attackers[i].type() == Type.IMMUNE_SYSTEM) {
		    attackerAndTargets.put(attackers[i], getTarget(attackers[i], unavailableTargets, activeInfection));
		} else {
		    attackerAndTargets.put(attackers[i],
			    getTarget(attackers[i], unavailableTargets, activeImmuneSystem));
		}
	    }

	    Arrays.sort(attackers, new AttackOrderComparator());

	    int deadUnits = 0;
	    for (ImmuneSystemGroup attacker : attackers) {
		ImmuneSystemGroup target = attackerAndTargets.get(attacker);

		if (attacker.units() > 0 && target != null && target.units() > 0) {
		    deadUnits += attacker.attack(target);
		    if (target.units() <= 0) {
			if (target.type() == Type.IMMUNE_SYSTEM)
			    activeImmuneSystem.remove(target);
			else
			    activeInfection.remove(target);
		    }
		}

	    }
	    // stalemate
	    if (deadUnits == 0)
		break;

	}

	// stalemate
	if (activeInfection.size() > 0 && activeImmuneSystem.size() > 0) {
	    return null;
	}

	List<ImmuneSystemGroup> survivors = activeInfection.size() > 0 ? activeInfection : activeImmuneSystem;

	return survivors;
    }

    private ImmuneSystemGroup getTarget(ImmuneSystemGroup attacker, Set<ImmuneSystemGroup> markedTargets,
	    List<ImmuneSystemGroup> targets) {
	ImmuneSystemGroup target = null;
	int maxDamage = 0;
	for (ImmuneSystemGroup group : targets) {
	    if (!markedTargets.contains(group) && !group.immunities().contains(attacker.attackType())) {
		int damageTo = attacker.getEffPower();
		if (group.weaknesses().contains(attacker.attackType())) {
		    damageTo *= 2;
		}

		if (damageTo > maxDamage) {
		    target = group;
		    maxDamage = damageTo;
		}
	    }
	}

	markedTargets.add(target);
	return target;

    }

    // returns a list of all groups
    /**
     *
     * @param inputFile
     *            input file containing the unit descriptions
     * @param immuneSystemBoost
     *            integer value that gets added to the attack power of all
     *            immune system groups. should be set to 0 for part 1
     * @return allGroups List containing all groups
     * @throws IOException
     */
    private List<ImmuneSystemGroup> getAllGroups(File inputFile, int immuneSystemBoost) throws IOException {
	List<ImmuneSystemGroup> allGroups = new ArrayList<ImmuneSystemGroup>();

	BufferedReader br = new BufferedReader(new FileReader(inputFile));
	String line = " ";

	Type currentType = Type.IMMUNE_SYSTEM;
	while ((line = br.readLine()) != null) {
	    if (line.length() == 0) {
		continue;
	    }
	    if (line.contains("Infection")) {
		currentType = Type.INFECTION;
		continue;
	    } else if (line.contains("Immune System")) {
		currentType = Type.IMMUNE_SYSTEM;
		continue;
	    }

	    int units = Integer.parseInt(line.substring(0, line.indexOf('u') - 1));

	    int hitpoints = Integer.parseInt(line.substring(line.indexOf("with") + 5, line.indexOf("hit") - 1));

	    Set<String> weakTo = new HashSet<String>();
	    Set<String> immuneTo = new HashSet<String>();

	    // get weaknesses and immunities
	    if (line.indexOf('(') != -1) {
		String[] weaknessImmunity = line.substring(line.indexOf('(') + 1, line.indexOf(')')).split(" ");
		String type = "";
		for (String str : weaknessImmunity) {
		    str = str.replaceAll("[^a-zA-Z]+", "");
		    if (str.equals("immune") || str.equals("weak")) {
			type = str;
		    } else if (!str.equals("to")) {
			if (type.equals("immune")) {
			    immuneTo.add(str);
			} else if (type.endsWith("weak"))
			    weakTo.add(str);
		    }
		}
	    }

	    String[] atkTypeInitiative = line.substring(line.indexOf("does") + 4, line.length()).trim().split(" ");

	    int attackPower = Integer.parseInt(atkTypeInitiative[0]);
	    String attackType = atkTypeInitiative[1];
	    int initiative = Integer.parseInt(atkTypeInitiative[atkTypeInitiative.length - 1]);

	    if (currentType == Type.IMMUNE_SYSTEM) {
		attackPower += immuneSystemBoost;
	    }

	    allGroups.add(new ImmuneSystemGroup(currentType, units, hitpoints, weakTo, immuneTo, attackType,
		    attackPower, initiative));
	}

	br.close();

	return allGroups;
    }

    public static void main(String[] args) throws IOException {

	Day24 test = new Day24(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 24\\InputFile1.txt"));

	int res2 = test.run2();
	System.out.println(res2);

    }

    public class TargetSelectionComparator implements Comparator<ImmuneSystemGroup> {

	@Override
	public int compare(ImmuneSystemGroup i0, ImmuneSystemGroup i1) {
	    if (i0.getEffPower() < i1.getEffPower()) {
		return 1;
	    }
	    if (i0.getEffPower() == i1.getEffPower()) {
		if (i0.initiative() < i1.initiative()) {
		    return 1;
		}
		return -1;
	    }
	    return -1;
	}

    }

    public class AttackOrderComparator implements Comparator<ImmuneSystemGroup> {

	@Override
	public int compare(ImmuneSystemGroup i0, ImmuneSystemGroup i1) {

	    if (i0.initiative() < i1.initiative()) {
		return 1;
	    }
	    if (i0.initiative() == i1.initiative())
		return 0;

	    return -1;
	}

    }

}
