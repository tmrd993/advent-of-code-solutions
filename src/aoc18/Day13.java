package aoc18;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day13 {
    private int gridHeight;
    private int gridWidth;
    private char[][] grid;

    public Day13(File input) throws IOException {
	gridWidth = getWidth(input);
	gridHeight = getHeight(input);
	grid = initiateGrid(input);
    }

    // part 1
    public void run() {
	List<Cart> carts = getCarts(grid);
	boolean crashOccured = false;
	while (!crashOccured) {
	    carts.sort(new CartComparator());
	    for (Cart cart : carts) {
		if (isOnIntersection(cart)) {
		    moveIntersection(cart);
		    cart.updateIntersectionMemory();
		} else if (isOnCurve(cart)) {
		    moveCurve(cart);
		} else {
		    moveStraight(cart);
		}

		// check for collision
		for (int i = 0; i < carts.size(); i++) {
		    for (int j = i + 1; j < carts.size(); j++) {
			if (carts.get(i).x == carts.get(j).x && carts.get(i).y == carts.get(j).y) {
			    System.out.println(carts.get(i).x + " " + carts.get(i).y);
			    crashOccured = true;
			    return;
			}
		    }
		}
	    }
	}
    }

    // part 2
    public void run2() {

	List<Cart> carts = getCarts(grid);
	List<Cart> tmpCarts = new ArrayList<Cart>(carts);

	while (tmpCarts.size() > 1) {
	    tmpCarts.sort(new CartComparator());

	    for (Cart cart : carts) {
		if (tmpCarts.contains(cart)) {
		    if (isOnIntersection(cart)) {
			moveIntersection(cart);
			cart.updateIntersectionMemory();
		    } else if (isOnCurve(cart)) {
			moveCurve(cart);
		    } else {
			moveStraight(cart);
		    }

		    // check for collision, remove carts if found
		    for (int i = 0; i < carts.size(); i++) {
			for (int j = i + 1; j < carts.size(); j++) {
			    if (tmpCarts.contains(carts.get(i)) && tmpCarts.contains(carts.get(j))
				    && carts.get(i).x == carts.get(j).x && carts.get(i).y == carts.get(j).y) {
				tmpCarts.remove(carts.get(i));
				tmpCarts.remove(carts.get(j));
			    }
			}
		    }
		}
	    }
	}
	System.out.println(tmpCarts.get(0).x + " " + tmpCarts.get(0).y);
    }

    // moves the cart based on the direction assuming the cart occupies an
    // intersection
    public void moveIntersection(Cart current) {
	int i = current.y;
	int j = current.x;
	if (current.direction == '>') {
	    switch (current.intersectionMemory) {
	    case 'l':
		current.direction = '^';
		current.currentTrack = grid[i - 1][j];
		current.x = j;
		current.y = i - 1;
		break;
	    case 's':
		current.currentTrack = grid[i][j + 1];
		current.x = j + 1;
		current.y = i;
		break;
	    case 'r':
		current.direction = 'v';
		current.currentTrack = grid[i + 1][j];
		current.x = j;
		current.y = i + 1;
		break;
	    }
	} else if (current.direction == '<') {
	    switch (current.intersectionMemory) {
	    case 'l':
		current.direction = 'v';
		current.currentTrack = grid[i + 1][j];
		current.x = j;
		current.y = i + 1;
		break;
	    case 's':
		current.currentTrack = grid[i][j - 1];
		current.x = j - 1;
		current.y = i;
		break;
	    case 'r':
		current.direction = '^';
		current.currentTrack = grid[i - 1][j];
		current.x = j;
		current.y = i - 1;
		break;
	    }
	} else if (current.direction == '^') {
	    switch (current.intersectionMemory) {
	    case 'l':
		current.direction = '<';
		current.currentTrack = grid[i][j - 1];
		current.x = j - 1;
		current.y = i;
		break;
	    case 's':
		current.currentTrack = grid[i - 1][j];
		current.x = j;
		current.y = i - 1;
		break;
	    case 'r':
		current.direction = '>';
		current.currentTrack = grid[i][j + 1];
		current.x = j + 1;
		current.y = i;
		break;
	    }
	} else if (current.direction == 'v') {
	    switch (current.intersectionMemory) {
	    case 'l':
		current.direction = '>';
		current.currentTrack = grid[i][j + 1];
		current.x = j + 1;
		current.y = i;
		break;
	    case 's':
		current.currentTrack = grid[i + 1][j];
		current.x = j;
		current.y = i + 1;
		break;
	    case 'r':
		current.direction = '<';
		current.currentTrack = grid[i][j - 1];
		current.x = j - 1;
		current.y = i;
		break;
	    }
	}
    }

    // moves the cart based on the direction assuming the cart occupies a curve
    public void moveCurve(Cart current) {
	int i = current.y;
	int j = current.x;
	if (current.currentTrack == '/') {
	    switch (current.direction) {
	    case '>':
		current.direction = '^';
		current.currentTrack = grid[i - 1][j];
		current.x = j;
		current.y = i - 1;
		break;
	    case 'v':
		current.direction = '<';
		current.currentTrack = grid[i][j - 1];
		current.x = j - 1;
		current.y = i;
		break;
	    case '<':
		current.direction = 'v';
		current.currentTrack = grid[i + 1][j];
		current.x = j;
		current.y = i + 1;
		break;
	    case '^':
		current.direction = '>';
		current.currentTrack = grid[i][j + 1];
		current.x = j + 1;
		current.y = i;
		break;
	    }

	} else if (current.currentTrack == '\\') {
	    switch (current.direction) {
	    case '<':
		current.direction = '^';
		current.currentTrack = grid[i - 1][j];
		current.x = j;
		current.y = i - 1;
		break;
	    case 'v':
		current.direction = '>';
		current.currentTrack = grid[i][j + 1];
		current.x = j + 1;
		current.y = i;
		break;
	    case '>':
		current.direction = 'v';
		current.currentTrack = grid[i + 1][j];
		current.x = j;
		current.y = i + 1;
		break;
	    case '^':
		current.direction = '<';
		current.currentTrack = grid[i][j - 1];
		current.x = j - 1;
		current.y = i;
		break;
	    }
	}
    }

    // moves the cart one tick based on the direction
    public void moveStraight(Cart current) {
	int j = current.x;
	int i = current.y;
	switch (current.direction) {
	case '^':
	    current.currentTrack = grid[i - 1][j];
	    current.x = j;
	    current.y = i - 1;
	    break;
	case 'v':
	    current.currentTrack = grid[i + 1][j];
	    current.x = j;
	    current.y = i + 1;
	    break;
	case '>':
	    current.currentTrack = grid[i][j + 1];
	    current.x = j + 1;
	    current.y = i;
	    break;
	case '<':
	    current.currentTrack = grid[i][j - 1];
	    current.x = j - 1;
	    current.y = i;
	    break;
	}
    }

    // returns a list with all carts
    private List<Cart> getCarts(char[][] grid) {
	List<Cart> carts = new ArrayList<Cart>();
	for (int i = 0; i < gridHeight; i++) {
	    for (int j = 0; j < gridWidth; j++) {
		char currentPosition = grid[i][j];
		if (isCart(currentPosition)) {
		    char direction = currentPosition;
		    carts.add(new Cart(j, i, direction));
		}
	    }
	}
	return carts;
    }

    public boolean isOnIntersection(Cart cart) {
	return cart.currentTrack == '+';
    }

    public boolean isOnCurve(Cart cart) {
	return cart.currentTrack == '\\' || cart.currentTrack == '/';
    }

    // returns true if the current grid coordinate is a cart
    private boolean isCart(char val) {
	if (val == '<' || val == '>' || val == 'v' || val == '^') {
	    return true;
	}
	return false;
    }

    // returns the height of the grid (y-Max)
    private int getHeight(File input) throws IOException {
	int height = 0;
	Scanner sc = new Scanner(input);
	while (sc.hasNextLine()) {
	    sc.nextLine();
	    height++;

	}
	sc.close();
	return height;
    }

    // returns the width of the grid (x-Max)
    private int getWidth(File input) throws IOException {
	int width = 0;
	Scanner sc = new Scanner(input);
	String line = sc.nextLine();
	width = line.length();
	sc.close();
	return width;
    }

    // fills the grid with the values from the input file
    private char[][] initiateGrid(File input) throws IOException {
	char[][] grid = new char[gridHeight][gridWidth];

	Scanner sc = new Scanner(input);

	for (int i = 0; i < gridHeight; i++) {
	    String currentLine = sc.nextLine();
	    for (int j = 0; j < gridWidth; j++) {
		grid[i][j] = currentLine.charAt(j);
	    }
	}
	sc.close();
	return grid;
    }



    private static class Cart {
	// current position of the cart
	private int x;
	private int y;
	private char direction;
	// stores information about the next turn
	private char intersectionMemory;
	// currently occupied track. straight line, curve or intersection (-, |,
	// \, /, +);
	private char currentTrack;

	public Cart(int x, int y, char direction) {
	    this.x = x;
	    this.y = y;
	    this.direction = direction;
	    intersectionMemory = 'l';
	    if (direction == '>' || direction == '<') {
		currentTrack = '-';
	    } else if (direction == '^' || direction == 'v') {
		currentTrack = '|';
	    }
	}

	// l == left, s == straight, r == right
	public void updateIntersectionMemory() {
	    if (intersectionMemory == 'l') {
		intersectionMemory = 's';
	    } else if (intersectionMemory == 's') {
		intersectionMemory = 'r';
	    } else if (intersectionMemory == 'r') {
		intersectionMemory = 'l';
	    }
	}
    }

    class CartComparator implements Comparator<Cart> {
	@Override
	public int compare(Cart cart1, Cart cart2) {
	    if (cart1.y == cart2.y) {
		if (cart1.x == cart2.x)
		    return 0;
		if (cart1.x < cart2.x)
		    return -1;
		return 1;
	    }

	    if (cart1.y < cart2.y)
		return -1;
	    return 1;
	}
    }

    public static void main(String[] args) throws IOException {
	Day13 test = new Day13(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 13\\InputFile.txt"));
	//test.run();
	test.run2();
    }
}