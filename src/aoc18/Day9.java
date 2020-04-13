package aoc18;

import java.util.ArrayList;
import java.util.Map;

import myutils.DoublyLinkedList;
import myutils.DoublyLinkedList.Node;

public class Day9 {
    private int playerCount;
    private int lastMarble;
    private long[] playerScores;

    public Day9(int playerCount, int lastMarble) {
	this.playerCount = playerCount;
	this.lastMarble = lastMarble;
	playerScores = new long[playerCount + 1];
    }

    // part 1
    // too slow for part 2 due to array resizing of ArrayList data structure
    public void run() {
	ArrayList<Integer> marbles = new ArrayList<Integer>();
	marbles.add(0);

	int currentMarbleIndex = 0;
	int currentPlayer = 1;

	for (int i = 1; i <= lastMarble; i++) {
	    currentPlayer = currentPlayer > playerCount ? 1 : currentPlayer;
	    int marbleToInsert = i;

	    // not a multiple of 23
	    if (marbleToInsert % 23 != 0) {
		if (marbles.size() == 1) {
		    marbles.add(marbleToInsert);
		    currentMarbleIndex++;
		}

		else {
		    if (currentMarbleIndex < marbles.size() + 1) {
			for (int j = 0; j < 2; j++) {
			    if (currentMarbleIndex == marbles.size()) {
				currentMarbleIndex = 0;
			    }
			    currentMarbleIndex++;
			}
		    } else {
			currentMarbleIndex += 2;
		    }

		    marbles.add(currentMarbleIndex, marbleToInsert);
		}
	    }
	    // current marble is a multiple of 23
	    else {
		// add the current marble to score of current player
		playerScores[currentPlayer] = playerScores[currentPlayer] + marbleToInsert;

		if (currentMarbleIndex < 7) {
		    for (int j = 0; j < 7; j++) {
			currentMarbleIndex--;
			if (currentMarbleIndex == -1) {
			    currentMarbleIndex = marbles.size() - 1;
			}
		    }
		} else {
		    currentMarbleIndex -= 7;
		}

		playerScores[currentPlayer] = playerScores[currentPlayer] + marbles.remove(currentMarbleIndex);
		currentMarbleIndex = currentMarbleIndex == marbles.size() ? 0 : currentMarbleIndex;
	    }
	    currentPlayer++;
	}
    }

    public long getHighestScore(Map<Integer, Long> playerScores) {
	long highestScore = 0;

	for (Map.Entry<Integer, Long> entry : playerScores.entrySet()) {
	    if (entry.getValue() > highestScore) {
		highestScore = entry.getValue();
	    }
	}

	return highestScore;
    }

    // part 2 using custom doubly linked list
    // takes ~4 seconds compared to ~3 hours of method above that uses an
    // ArrayList to store the marbles
    public void run2() {
	DoublyLinkedList marbles = new DoublyLinkedList();
	marbles.addLast(0);
	Node currentMarble = marbles.getHead();

	int currentPlayer = 1;
	for (int i = 1; i <= lastMarble; i++) {
	    currentPlayer = currentPlayer > playerCount ? 1 : currentPlayer;
	    int marbleToInsert = i;

	    if (marbleToInsert % 23 != 0) {
		if (marbles.size() == 1) {
		    marbles.addLast(marbleToInsert);
		    currentMarble = currentMarble.next;
		} else {
		    for (int j = 0; j < 2; j++) {
			if (currentMarble == null) {
			    currentMarble = marbles.getHead();
			}
			currentMarble = currentMarble.next;
		    }
		    if (currentMarble == null) {
			marbles.addLast(marbleToInsert);
			currentMarble = marbles.getTail();
		    } else {
			marbles.addToCurrentPos(currentMarble, marbleToInsert);
			currentMarble = currentMarble.previous;
		    }

		}
	    }
	    // current marble is a multiple of 23
	    else {
		// add current marble to score of current player
		playerScores[currentPlayer] = playerScores[currentPlayer] + marbleToInsert;

		for (int j = 0; j < 7; j++) {
		    if (currentMarble == null) {
			currentMarble = marbles.getTail();
		    }
		    currentMarble = currentMarble.previous;
		}
		playerScores[currentPlayer] = playerScores[currentPlayer] + currentMarble.data;
		Node marbleToRemove = currentMarble;
		currentMarble = currentMarble.next == null ? marbles.getHead() : currentMarble.next;
		marbles.removeNode(marbleToRemove);
	    }
	    currentPlayer++;
	}
    }

    public long getHighscore(long[] playerScores) {
	long highScore = 0;
	for (int i = 0; i < playerScores.length; i++) {
	    if (playerScores[i] > highScore) {
		highScore = playerScores[i];
	    }
	}
	return highScore;
    }

    private static int getPlayerCount(String input) {
	return Integer.parseInt(input.substring(0, input.indexOf(" ")));
    }

    private static int getMarbleCount(String input) {
	return Integer
		.parseInt(input.substring(input.indexOf("h") + 2, input.indexOf("p", input.indexOf("p") + 1) - 1));
    }

    public static void main(String[] args) {
	String myInput = "412 players; last marble is worth 71646 points";
	// String testCase1 = "10 players; last marble is worth 1618 points";
	// String testCase2 = "13 players; last marble is worth 7999 points";
	// String testCase3 = "17 players; last marble is worth 1104 points";

	int playerCount = getPlayerCount(myInput);
	int lastMarble = getMarbleCount(myInput);

	// part 2
	// remove the 100 to get solution of part 1
	Day9 test = new Day9(playerCount, lastMarble * 100);
	// Day9 test = new Day9(9, 25);
	test.run2();
	long highest = test.getHighscore(test.playerScores);
	System.out.println(highest);
    }
}