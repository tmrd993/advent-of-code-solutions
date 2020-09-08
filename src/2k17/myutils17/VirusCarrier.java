package myutils2k17;

public class VirusCarrier {

    public enum Direction{UP, DOWN, LEFT, RIGHT};
    private Point2d position;
    private Direction direction;

    public VirusCarrier(Point2d position) {
	this.position = position;
	this.direction = Direction.UP;
    }

    public Point2d position() {
	return position;
    }

    public void move() {
	switch(direction) {
	case UP:
	    position = new Point2d(position.x(), position.y() - 1);
	    break;
	case DOWN:
	    position = new Point2d(position.x(), position.y() + 1);
	    break;
	case LEFT:
	    position = new Point2d(position.x() - 1, position.y());
	    break;
	case RIGHT:
	    position = new Point2d(position.x() + 1, position.y());
	    break;
	}
    }

    public void turnLeft() {
	switch(direction) {
	case UP:
	    direction = Direction.LEFT;
	    break;
	case DOWN:
	    direction = Direction.RIGHT;
	    break;
	case LEFT:
	    direction = Direction.DOWN;
	    break;
	case RIGHT:
	    direction = Direction.UP;
	    break;
	}
    }

    public void turnRight() {
	switch(direction) {
	case UP:
	    direction = Direction.RIGHT;
	    break;
	case DOWN:
	    direction = Direction.LEFT;
	    break;
	case LEFT:
	    direction = Direction.UP;
	    break;
	case RIGHT:
	    direction = Direction.DOWN;
	    break;
	}
    }

}
