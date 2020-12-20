package myutils20;

import java.util.Objects;

public class PuzzlePiece {

    private final int id;
    private char[][] grid;
    private boolean leftConnected;
    private boolean rightConnected;
    private boolean bottomConnected;
    private boolean topConnected;
    private int leftConnection;
    private int rightConnection;
    private int topConnection;
    private int bottomConnection;
    private int connections;
    private char[][] initialState;
    // flag signalizing that this puzzle piece should not be rotated OR flipped
    // again if false
    private boolean isFlippable;

    public PuzzlePiece(char[][] grid, int id) {
	this.id = id;
	this.grid = grid;
	this.initialState = grid;
	isFlippable = true;
    }

    public int id() {
	return id;
    }
    
    public int getConnections() {
	return connections;
    }

    public char[][] grid() {
	return grid;
    }

    public void resetToInitialState() {
	grid = initialState;
    }

    public void setInitialState(char[][] grid) {
	this.initialState = grid;
    }

    public String connections() {
	return "Connections: " + connections + ", Top: " + topConnection + ", Bottom: " + bottomConnection + ", Left: "
		+ leftConnection + ", Right: " + rightConnection;
    }

    public void setFlippable(boolean flag) {
	this.isFlippable = flag;
    }

    public boolean canConnect(PuzzlePiece piece) {

	for (int i = 0; i < 4; i++) {

	    if (this.canConnectBottom(piece) || this.canConnectLeft(piece) || this.canConnectRight(piece)
		    || this.canConnectTop(piece)) {
		// piece.resetToInitialState();
		return true;
	    }
	    if (piece.isFlippable) {
		piece.rotate90CW();
	    }
	}
	if (piece.isFlippable) {
	    piece.horizontalFlip();
	}

	for (int i = 0; i < 4; i++) {

	    if (this.canConnectBottom(piece) || this.canConnectLeft(piece) || this.canConnectRight(piece)
		    || this.canConnectTop(piece)) {
		// piece.resetToInitialState();
		return true;
	    }

	    if (piece.isFlippable) {
		piece.rotate90CW();
	    }
	}
	//piece.resetToInitialState();
	return false;
    }

    // check if the top of this piece can be connected to the bottom of topPiece
    public boolean canConnectTop(PuzzlePiece topPiece) {

	for (int i = 0; i < grid.length; i++) {
	    if (grid[0][i] != topPiece.grid[grid.length - 1][i]) {
		return false;
	    }
	}

	if (this.topConnection == 0 && topPiece.bottomConnection == 0) {
	    topPiece.isFlippable = false;
	    this.isFlippable = false;
	    // System.out.println(topPiece.id + " now unflippable ");

	    this.topConnection = topPiece.id;
	    topPiece.bottomConnection = this.id;
	    this.topConnected = true;
	    topPiece.bottomConnected = true;
	    this.connections++;
	    topPiece.connections++;
	}
	return true;
    }

    public boolean canConnectBottom(PuzzlePiece bottomPiece) {

	for (int i = 0; i < grid.length; i++) {
	    if (grid[grid.length - 1][i] != bottomPiece.grid[0][i]) {
		return false;
	    }
	}
	if (this.bottomConnection == 0 && bottomPiece.topConnection == 0) {
	    bottomPiece.isFlippable = false;
	    this.isFlippable = false;
	    // System.out.println(bottomPiece.id + " now unflippable ");
	    this.bottomConnection = bottomPiece.id;
	    bottomPiece.topConnection = this.id;
	    this.bottomConnected = true;
	    bottomPiece.topConnected = true;
	    this.connections++;
	    bottomPiece.connections++;
	}

	return true;
    }

    public boolean canConnectLeft(PuzzlePiece leftPiece) {

	for (int i = 0; i < grid.length; i++) {
	    if (grid[i][0] != leftPiece.grid[i][grid.length - 1]) {
		return false;
	    }
	}

	if (this.leftConnection == 0 && leftPiece.rightConnection == 0) {
	    leftPiece.isFlippable = false;
	    this.isFlippable = false;
	    // System.out.println(leftPiece.id + " now unflippable ");
	    this.leftConnection = leftPiece.id;
	    leftPiece.rightConnection = this.id;
	    this.leftConnected = true;
	    leftPiece.rightConnected = true;
	    this.connections++;
	    leftPiece.connections++;
	}

	return true;
    }

    public boolean canConnectRight(PuzzlePiece rightPiece) {

	for (int i = 0; i < grid.length; i++) {
	    if (grid[i][grid.length - 1] != rightPiece.grid[i][0]) {
		return false;
	    }
	}

	if (this.rightConnection == 0 && rightPiece.leftConnection == 0) {
	    rightPiece.isFlippable = false;
	    this.isFlippable = false;
	    // System.out.println(rightPiece.id + " now unflippable ");
	    this.rightConnection = rightPiece.id;
	    rightPiece.leftConnection = this.id;
	    this.rightConnected = true;
	    rightPiece.leftConnected = true;
	    this.connections++;
	    rightPiece.connections++;
	}

	return true;
    }

    @Override
    public String toString() {
	String piece = "";
	System.out.println("ID: " + id);
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		piece = piece + grid[i][j];
	    }
	    piece = piece + "\n";
	}
	return piece;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null | !(o instanceof PuzzlePiece)) {
	    return false;
	}

	PuzzlePiece tmp = (PuzzlePiece) o;

	return this.id == tmp.id;
    }

    @Override
    public int hashCode() {
	return Objects.hash(id);
    }

    public void rotate90CW() {
	final int M = grid.length;
	final int N = grid[0].length;
	char[][] ret = new char[N][M];
	for (int r = 0; r < M; r++) {
	    for (int c = 0; c < N; c++) {
		ret[c][M - 1 - r] = grid[r][c];
	    }
	}
	grid = ret;
    }

    public void horizontalFlip() {
	char[][] out = new char[grid.length][grid[0].length];
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[i].length; j++) {
		out[i][grid[i].length - j - 1] = grid[i][j];
	    }
	}
	grid = out;
    }

    public boolean isLeftConnected() {
	return leftConnected;
    }

    public boolean isRightConnected() {
	return rightConnected;
    }

    public boolean isBottomConnected() {
	return bottomConnected;
    }

    public boolean isTopConnected() {
	return topConnected;
    }

    public int getLeftConnection() {
	return leftConnection;
    }

    public int getRightConnection() {
	return rightConnection;
    }

    public int getTopConnection() {
	return topConnection;
    }

    public int getBottomConnection() {
	return bottomConnection;
    }

    public char[][] getInitialState() {
	return initialState;
    }

}
