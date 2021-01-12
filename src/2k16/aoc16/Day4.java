package aoc16;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import myutils16.MathUtils;
import myutils16.StaticUtils;

public class Day4 {

    List<String> rooms;

    public Day4(File input) {
	rooms = StaticUtils.inputToList(input);
    }

    public int run1() {
	int sum = 0;
	for (String room : rooms) {
	    String[] splitInput = room.split("-");
	    String name = Arrays.stream(Arrays.copyOfRange(splitInput, 0, splitInput.length - 1))
		    .collect(Collectors.joining());
	    String secIdChecksum = splitInput[splitInput.length - 1];
	    String secId = secIdChecksum.substring(0, secIdChecksum.indexOf("["));
	    String checksum = secIdChecksum.substring(secIdChecksum.indexOf("[") + 1, secIdChecksum.indexOf("]"));

	    if (isRealRoom(name, checksum)) {
		sum += Integer.parseInt(secId);
	    }
	}

	return sum;
    }
    
    public int run2() {
	List<Character> alphabet = StaticUtils.alphabet;
	int alphabetLen = alphabet.size();
	for(String room : rooms) {
	    String[] splitInput = room.split("-");
	    StringBuilder name = new StringBuilder();
	    int i = 0;
	    while(!Character.isDigit(room.charAt(i))) {
		name.append(room.charAt(i++));
	    }
	    String secIdChecksum = splitInput[splitInput.length - 1];
	    int secId = Integer.parseInt(secIdChecksum.substring(0, secIdChecksum.indexOf("[")));
	    
	    StringBuilder decodedMsg = new StringBuilder();
	    for(int j = 0; j < name.length(); j++) {
		if(name.charAt(j) == '-')
		    decodedMsg.append(" ");
		else {
		    decodedMsg.append(alphabet.get((alphabet.indexOf(Character.toUpperCase(name.charAt(j))) + secId) % alphabetLen));
		}
		   
	    }
	    if(decodedMsg.toString().contains("NORTHPOLE"))
		return secId;
	}
	return -1;
    }

    private static boolean isRealRoom(String name, String checksum) {
	Map<Character, Integer> chFreq = new LinkedHashMap<>();
	for (int i = 0; i < name.length(); i++) {

	    chFreq.putIfAbsent(name.charAt(i), 0);
	    chFreq.put(name.charAt(i), chFreq.get(name.charAt(i)) + 1);

	}

	for (int i = 0; i < checksum.length(); i++) {
	    if (!chFreq.containsKey(checksum.charAt(i))) {
		return false;
	    }
	}
	
	Map<Integer, List<Character>> groupingByFreq = new LinkedHashMap<>();
	for (Map.Entry<Character, Integer> e : chFreq.entrySet()) {
	    groupingByFreq.putIfAbsent(e.getValue(), new ArrayList<>());
	    if (checksum.indexOf(e.getKey()) != -1)
		groupingByFreq.get(e.getValue()).add(e.getKey());
	}
	
	Set<Character> marked = new HashSet<>();

	for (int i = 0; i < checksum.length(); i++) {
	    char currentCh = checksum.charAt(i);
	    if (marked.contains(currentCh))
		continue;

	    int max = groupingByFreq.entrySet().stream().mapToInt(s -> s.getKey()).max().getAsInt();
	    int occurenceOfCurrentCh = chFreq.get(checksum.charAt(i));

	    if (max > occurenceOfCurrentCh) {
		return false;
	    }

	    List<Character> group = groupingByFreq.get(occurenceOfCurrentCh);
	    if (group.size() == 1) {
		name.replace(currentCh + "", "");
		chFreq.remove(currentCh);
		groupingByFreq.remove(occurenceOfCurrentCh);
	    } else {
		List<Character> sameFreqChars = new ArrayList<>();
		for(int j = 0; j < checksum.length(); j++) {
		    if(group.contains(checksum.charAt(j)) && !sameFreqChars.contains(checksum.charAt(j))) {
			sameFreqChars.add(checksum.charAt(j));
		    }
			    
		}
		if (!MathUtils.isAlphabeticSorted(sameFreqChars)) {
		    return false;
		}
		for (char c : group) {
		    name.replace(c + "", "");
		    chFreq.remove(c);
		    marked.add(c);
		}
		groupingByFreq.remove(occurenceOfCurrentCh);

	    }
	}
	return true;
    }

    public static void main(String[] args) {
	Day4 test = new Day4(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2016\\Day 4\\InputFile1.txt"));
	System.out.println(test.run2());
    }
}
