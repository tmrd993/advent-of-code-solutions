package aoc20;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import myutils20.StaticUtils;

public class Day22 {

    private List<String> rawData;
    private Queue<Integer> pDeck1;
    private Queue<Integer> pDeck2;

    public Day22(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public int run1() {
	initDecks();
	while (!pDeck1.isEmpty() && !pDeck2.isEmpty()) {
	    int p1 = pDeck1.poll();
	    int p2 = pDeck2.poll();

	    if (p1 > p2) {
		pDeck1.offer(p1);
		pDeck1.offer(p2);
	    } else {
		pDeck2.offer(p2);
		pDeck2.offer(p1);
	    }
	}

	return winningScore(pDeck1.isEmpty() ? pDeck2 : pDeck1);
    }

    private int run2() {
	initDecks();
	recursiveBattle(0, 0, pDeck1, pDeck2, 0);
	return winningScore(pDeck1.isEmpty() ? pDeck2 : pDeck1);
    }

    // return the number of the winning player
    private int recursiveBattle(int nP1, int nP2, Queue<Integer> pDeck1, Queue<Integer> pDeck2, int gameNum) {
	Set<Queue<Integer>> visitedStatesP1 = new HashSet<>();
	Set<Queue<Integer>> visitedStatesP2 = new HashSet<>();

	while (!pDeck1.isEmpty() && !pDeck2.isEmpty()) {
	    
	    // player 1 wins
	    if (visitedStatesP1.contains(pDeck1) || visitedStatesP2.contains(pDeck2)) {
		return nP1;
	    }
	    
	    Queue<Integer> cpyDeck1 = new LinkedList<>(pDeck1);
	    Queue<Integer> cpyDeck2 = new LinkedList<>(pDeck2);

	    visitedStatesP1.add(cpyDeck1);
	    visitedStatesP2.add(cpyDeck2);
	  
	    int p1 = pDeck1.poll();
	    int p2 = pDeck2.poll();

	    // can't recurse, play this round with normal rules
	    if (p1 > pDeck1.size() || p2 > pDeck2.size()) {

		if (p1 > p2) {
		    pDeck1.offer(p1);
		    pDeck1.offer(p2);
		} else {
		    pDeck2.offer(p2);
		    pDeck2.offer(p1);
		}

		// winner of this round gets decided by recursion
	    } else {

		Queue<Integer> nextDeck1 = new LinkedList<>();
		Queue<Integer> nextDeck2 = new LinkedList<>();
		
		pDeck1.stream().limit(p1).forEach(n -> nextDeck1.add(n));
		pDeck2.stream().limit(p2).forEach(n -> nextDeck2.add(n));
		
		int winningNumber = recursiveBattle(p1, p2, nextDeck1, nextDeck2, gameNum +1);

		// player 1 wins
		if (p1 == winningNumber) {
		    pDeck1.offer(p1);
		    pDeck1.offer(p2);
		} else {
		    pDeck2.offer(p2);
		    pDeck2.offer(p1);
		}
	    }
	}

	// return the number of the winning player
	return pDeck1.isEmpty() ? nP2 : nP1;
    }

    private final int winningScore(Queue<Integer> winner) {

	int cardScore = winner.size();
	int finalScore = 0;
	while (!winner.isEmpty()) {
	    finalScore += (cardScore-- * winner.poll());
	}

	return finalScore;
    }

    // set both decks to their initial states
    private void initDecks() {
	pDeck1 = new LinkedList<>();
	pDeck2 = new LinkedList<>();

	boolean p2Reached = false;
	for (int i = 1; i < rawData.size(); i++) {
	    String line = rawData.get(i);
	    if (line.equals("Player 2:")) {
		p2Reached = true;
		continue;
	    }

	    if (line.length() > 0) {
		if (p2Reached) {
		    pDeck2.offer(Integer.parseInt(line));
		} else {
		    pDeck1.offer(Integer.parseInt(line));
		}
	    }
	}
    }

    public static void main(String[] args) {
	Day22 test = new Day22(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 22\\InputFile1.txt"));
	System.out.println(test.run2());

    }

}
