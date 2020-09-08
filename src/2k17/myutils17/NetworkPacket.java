package myutils17;

public class NetworkPacket {

    public enum Direction {
	UP, DOWN, LEFT, RIGHT
    };

    private Direction direction;
    private Point2d position;

    public NetworkPacket(Point2d startPosition) {
	position = startPosition;
	direction = Direction.DOWN;
    }

    public Direction getDirection() {
	return direction;
    }

    public void setDirection(Direction direction) {
	this.direction = direction;
    }

    public Point2d getPosition() {
	return position;
    }

    public void setPosition(Point2d position) {
	this.position = position;
    }

    /**
     * moves the network packet one step based on the current direction
     */
    public void move() {
	switch (direction) {
	case UP:
	    setPosition(new Point2d(position.x(), position.y() - 1));
	    break;
	case DOWN:
	    setPosition(new Point2d(position.x(), position.y() + 1));
	    break;
	case LEFT:
	    setPosition(new Point2d(position.x() - 1, position.y()));
	    break;
	case RIGHT:
	    setPosition(new Point2d(position.x() + 1, position.y()));
	    break;
	}
    }
}
