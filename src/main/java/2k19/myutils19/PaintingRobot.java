package myutils19;

public class PaintingRobot {
    
    public enum Direction {UP, DOWN, LEFT, RIGHT};
    public enum TurnDirection {LEFT, RIGHT};
    
    private Point2d pos;
    private Direction direction;
    
    public PaintingRobot(Point2d startingPos) {
	pos = startingPos;
	direction = Direction.UP;
    }
    
    public Point2d pos() {
	return pos;
    }
    
    public Direction direction() {
	return direction;
    }
    
    public void turn90Degrees(TurnDirection td) {
	switch(direction) {
	case UP:
	    direction = td == TurnDirection.LEFT ? Direction.LEFT : Direction.RIGHT;
	    break;
	case DOWN:
	    direction = td == TurnDirection.LEFT ? Direction.RIGHT : Direction.LEFT;
	    break;
	case LEFT:
	    direction = td == TurnDirection.LEFT ? Direction.DOWN : Direction.UP;
	    break;
	case RIGHT:
	    direction = td == TurnDirection.LEFT ? Direction.UP : Direction.DOWN;
	    break;
	}
    }
    
    public Point2d move() {
	switch(direction) {
	case UP:
	    pos = new Point2d(pos.x(), pos.y() - 1);
	    break;
	case DOWN:
	    pos = new Point2d(pos.x(), pos.y() + 1);
	    break;
	case LEFT:
	    pos = new Point2d(pos.x() - 1, pos.y());
	    break;
	case RIGHT:
	    pos = new Point2d(pos.x() + 1, pos.y());
	    break;
	}
	return pos;
    }
    

}
