package aoc15;

import java.util.HashMap;
import java.util.Map;

public class Day20 {
    
    private final int inputPresentAmount = 29_000_000;
    
    public int run1() {
	int houseNum = 1;
	while(numOfPresents(houseNum) < inputPresentAmount) {
	    houseNum++;
	}
	
	return houseNum;
    }
    
    public int run2() {
	Map<Integer, Integer> houseNumberToDeliveriesLeftMapping = new HashMap<>();
	int houseNum = 1;
	while(numOfPresentsPt2(houseNum, houseNumberToDeliveriesLeftMapping) < inputPresentAmount) {
	    houseNum++;
	}
	
	return houseNum;
    }
    
    private long numOfPresents(int houseNumber) {
	long sum = 0;
	int root = (int) Math.sqrt(houseNumber);
	for(int i = 1; i <= root; i++) {
	    if(houseNumber % i == 0) {
		if(houseNumber / i == i) {
		    sum += (10 * i);
		} else {
		    sum += (10 * i) + (10 * (houseNumber / i));
		}
	    }
	}
	
	return sum;
    }
    
    // same as pt1 above, only this time, we check how many times we already delivered a present
    private long numOfPresentsPt2(int houseNumber, Map<Integer, Integer> deliveriesLeftMapping) {
	long sum = 0;
	int root = (int) Math.sqrt(houseNumber);
	for(int i = 1; i <= root; i++) {
	    if(houseNumber % i == 0) {
		deliveriesLeftMapping.putIfAbsent(i, 50);
		deliveriesLeftMapping.putIfAbsent(houseNumber / i, 50);
		if(houseNumber / i == i) {
		    if(deliveriesLeftMapping.get(i) > 0) {
			sum += (11 * i);
			deliveriesLeftMapping.put(i, deliveriesLeftMapping.get(i) - 1);
		    }
		    
		} else {
		    if(deliveriesLeftMapping.get(i) > 0) {
			sum += (11 * i);
			deliveriesLeftMapping.put(i, deliveriesLeftMapping.get(i) - 1);
		    }
		    if(deliveriesLeftMapping.get(houseNumber / i) > 0) {
			 sum += (11 * (houseNumber / i));
			 deliveriesLeftMapping.put(houseNumber / i, deliveriesLeftMapping.get(houseNumber / i) - 1);
		    }
		}
	    }
	}
	
	return sum;
    }

    public static void main(String[] args) {
	Day20 test = new Day20();
	System.out.println(test.run1());
	System.out.println(test.run2());
	
    }

}
