package myutils2k17;

public class SecurityLayer {
    private boolean[] occupiedRanges;
    private int scannerIndex;
    private final int DEPTH;
    private boolean movingDown;

    public SecurityLayer(int range, int depth) {
	occupiedRanges = new boolean[range];
	occupiedRanges[0] = true;
	scannerIndex = 0;
	this.DEPTH = depth;
	movingDown = true;
    }

    public void advanceScanner() {
	occupiedRanges[scannerIndex] = false;

	if(scannerIndex == 0 && !movingDown) {
	    movingDown = true;
	}
	else if(scannerIndex == range() - 1 && movingDown) {
	    movingDown = false;
	}

	if(movingDown) {
	    scannerIndex++;
	}
	else {
	    scannerIndex--;
	}



	occupiedRanges[scannerIndex] = true;
    }

    public boolean intrusionDetected(int indexOfIntruder) {
	return occupiedRanges[indexOfIntruder];
    }

    public int intrusionSeverity() {
	return occupiedRanges.length * DEPTH;
    }

    public int range() {
	return occupiedRanges.length;
    }

    public int depth() {
	return DEPTH;
    }

}
