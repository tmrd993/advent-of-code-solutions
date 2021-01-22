package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day4 {

    private static Comparator<String> timeAscending = new Comparator<String>() {
	@Override
	public int compare(String str1, String str2) {
	    Calendar c1 = Calendar.getInstance();
	    Calendar c2 = Calendar.getInstance();
	    c1.set(getYear(str1), getMonth(str1), getDay(str1), getHour(str1), getMinute(str1));
	    c2.set(getYear(str2), getMonth(str2), getDay(str2), getHour(str2), getMinute(str2));

	    return c1.compareTo(c2);
	}
    };

    private static int getYear(String record) {
	return Integer.parseInt(record.substring(1, record.indexOf("-")));
    }

    private static int getMonth(String record) {
	return Integer
		.parseInt(record.substring(record.indexOf("-") + 1, record.indexOf("-", record.indexOf("-") + 1)));
    }

    private static int getDay(String record) {
	return Integer
		.parseInt(record.substring(record.indexOf("-", record.indexOf("-") + 1) + 1, record.indexOf(" ")));
    }

    private static int getHour(String record) {
	return Integer.parseInt(record.substring(record.indexOf(" ") + 1, record.indexOf(":")));
    }

    private static int getMinute(String record) {
	return Integer.parseInt(record.substring(record.indexOf(":") + 1, record.indexOf("]")));
    }

    private static String getId(String record) {
	return record.substring(record.indexOf("#") + 1, record.indexOf(" ", record.indexOf("#")));
    }

    // returns a list of guard records in chronological order
    private static List<List<String>> getGuardRecordsByGuard(List<String> guardRecords) {
	guardRecords.sort(timeAscending);
	List<List<String>> guardRecordsSplitByGuard = new ArrayList<List<String>>();
	for (int i = 0; i < guardRecords.size(); i++) {
	    if (guardRecords.get(i).contains("Guard")) {
		List<String> guardRecord = new ArrayList<String>();
		guardRecord.add(guardRecords.get(i));
		int j = i + 1;
		while (j < guardRecords.size()) {
		    if (guardRecords.get(j).contains("Guard"))
			break;
		    guardRecord.add(guardRecords.get(j));
		    j++;
		}
		guardRecordsSplitByGuard.add(guardRecord);
		i = j - 1;
	    }
	}
	return guardRecordsSplitByGuard;
    }

    // returns mapping of days + id (as a string, example: 11-09:8574) with a
    // map of integer-boolean values which represent the minutes (0-60)
    // which are spent sleeping from 00:00 to 01:00 AM by the guard with the id
    // contained in the key of the returned map
    public static Map<String, Map<Integer, Boolean>> getSleepCyclesByDay(List<List<String>> guardRecordsSplitByGuard) {
	// string = month and day + id example: date -> 11-09:1234 <- Id
	// map<int, bool> = 0 - 60 mapped to either true (sleeps) or false (is
	// awake)
	Map<String, Map<Integer, Boolean>> sleepCyclePerDay = new HashMap<String, Map<Integer, Boolean>>();

	for (List<String> currentList : guardRecordsSplitByGuard) {
	    // ignore guards who stay awake
	    if (currentList.size() < 2)
		continue;
	    String currentRecord = currentList.get(0);
	    String key = getMonth(currentList.get(1)) + "-" + getDay(currentList.get(1)) + ":" + getId(currentRecord);
	    Map<Integer, Boolean> currentDayCycle = new HashMap<Integer, Boolean>();

	    // initialize every minute to false (guard awake)
	    for (int i = 0; i <= 60; i++) {
		currentDayCycle.put(i, false);
	    }

	    for (int i = 1; i < currentList.size() - 1; i += 2) {
		int sleepsFrom = getMinute(currentList.get(i));
		int wakesAt = getMinute(currentList.get(i + 1));

		for (int j = sleepsFrom; j < wakesAt; j++) {
		    currentDayCycle.put(j, true);
		}
	    }
	    sleepCyclePerDay.put(key, currentDayCycle);
	}
	return sleepCyclePerDay;
    }

    public static String getIdWithHighestSleeptime(Map<String, Map<Integer, Boolean>> sleepCyclePerDay, String[] ids) {
	String maxId = "";
	int maxValue = Integer.MIN_VALUE;
	for (int i = 0; i < ids.length; i++) {
	    int countMinutes = 0;
	    for (Map.Entry<String, Map<Integer, Boolean>> e : sleepCyclePerDay.entrySet()) {
		if (e.getKey().substring(e.getKey().indexOf(":") + 1).equals(ids[i])) {
		    for (Map.Entry<Integer, Boolean> entry : e.getValue().entrySet()) {
			if (entry.getValue() == true)
			    countMinutes++;
		    }
		}
	    }
	    if (countMinutes > maxValue) {
		maxValue = countMinutes;
		maxId = ids[i];
	    }
	}
	return maxId;
    }

    public static int getOptimalTime(Map<String, Map<Integer, Boolean>> sleepCyclePerDay, String targetId) {
	Map<Integer, Integer> countMinutes = new HashMap<Integer, Integer>();

	for (Map.Entry<String, Map<Integer, Boolean>> e : sleepCyclePerDay.entrySet()) {
	    if (e.getKey().substring(e.getKey().indexOf(":") + 1, e.getKey().length()).equals(targetId)) {
		for (Map.Entry<Integer, Boolean> entry : e.getValue().entrySet()) {
		    int minute = entry.getKey();
		    if (entry.getValue() == true) {
			if (countMinutes.containsKey(minute)) {
			    // int v = countMinutes.get(minute) + 1;
			    countMinutes.put(minute, countMinutes.get(minute) + 1);
			} else
			    countMinutes.put(minute, 1);
		    }
		}
	    }
	}

	int maxMin = Integer.MIN_VALUE;
	int maxKey = 0;
	for (Map.Entry<Integer, Integer> entry : countMinutes.entrySet()) {
	    if (entry.getValue() > maxMin) {
		maxMin = entry.getValue();
		maxKey = entry.getKey();
	    }

	}
	return maxKey;
    }

    public static String[] getIds(List<String> guardRecords) {
	Set<String> guardIds = new HashSet<String>();
	for (String st : guardRecords) {
	    if (st.contains("G")) {
		guardIds.add(getId(st));
	    }
	}
	return guardIds.toArray(new String[guardIds.size()]);
    }

    // part 1 solution
    public static int getOptimalMinuteTimesId() throws IOException {
	// get the records line by line
	List<String> guardRecords = aoc18.Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 4\\InputFile.txt"));
	// get chronological list of lists for each day
	List<List<String>> guardRecordsSplitByGuard = getGuardRecordsByGuard(guardRecords);

	// Map dates and ids (spliced together) with an integer-boolean map that
	// contains a boolean value (true = guard sleeps, false = guard is
	// awake)
	// for each minute (integer, 61 values) of the hour between 00:00 to
	// 01:00
	// string = month and day + id example: date -> 11-09:1234 <- Id
	// map<int, bool> = 0 - 60 minutes mapped to either true (sleeps) or
	// false (is awake)
	Map<String, Map<Integer, Boolean>> sleepCyclePerDay = getSleepCyclesByDay(guardRecordsSplitByGuard);

	// array of all (unique) ids
	String[] ids = getIds(guardRecords);
	String idOfLongestSleeper = getIdWithHighestSleeptime(sleepCyclePerDay, ids);

	int optimalTime = getOptimalTime(sleepCyclePerDay, idOfLongestSleeper);

	return optimalTime * Integer.parseInt(idOfLongestSleeper);
    }

    // part 2 solution
    public static int mostFrequentSleeperTimesMinute() throws IOException {
	// get the records line by line
	List<String> guardRecords = aoc18.Day1
		.inputFileToList(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 4\\InputFile.txt"));
	// get chronological list of lists for each day
	List<List<String>> guardRecordsSplitByGuard = getGuardRecordsByGuard(guardRecords);

	// Map dates and ids (spliced together) with an integer-boolean map that
	// contains a boolean value (true = guard sleeps, false = guard is
	// awake)
	// for each minute (integer, 61 values) of the hour between 00:00 to
	// 01:00
	// string = month and day + id example: date -> 11-09:1234 <- Id
	// map<int, bool> = 0 - 60 minutes mapped to either true (sleeps) or
	// false (is awake)
	Map<String, Map<Integer, Boolean>> sleepCyclePerDay = getSleepCyclesByDay(guardRecordsSplitByGuard);

	// unique ID's
	String[] ids = getIds(guardRecords);

	int maxValue = Integer.MIN_VALUE;
	int maxKey = 0;
	String maxId = "";

	for (int i = 0; i < ids.length; i++) {
	    String currentID = ids[i];
	    Map<Integer, Integer> countMinutes = new HashMap<Integer, Integer>();
	    for (Map.Entry<String, Map<Integer, Boolean>> e : sleepCyclePerDay.entrySet()) {
		if (e.getKey().substring(e.getKey().indexOf(":") + 1, e.getKey().length()).equals(currentID)) {
		    for (Map.Entry<Integer, Boolean> entry : e.getValue().entrySet()) {
			if (entry.getValue() == true) {
			    if (countMinutes.containsKey(entry.getKey())) {
				countMinutes.put(entry.getKey(), countMinutes.get(entry.getKey()) + 1);
			    } else {
				countMinutes.put(entry.getKey(), 1);
			    }
			}
		    }
		}
	    }

	    for (Map.Entry<Integer, Integer> e : countMinutes.entrySet()) {
		if (e.getValue() > maxValue) {
		    maxValue = e.getValue();
		    maxKey = e.getKey();
		    maxId = ids[i];
		}
	    }
	}
	return Integer.parseInt(maxId) * maxKey;
    }

    /*
     * 02-05:123 04-05:321 05-05:312 07-05:321
     */

    public static void main(String[] args) throws IOException {

	System.out.println(mostFrequentSleeperTimesMinute());
    }
}