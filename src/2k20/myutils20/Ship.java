package myutils20;

import java.util.HashMap;
import java.util.Map;

// utility class for day 12
public class Ship {

    public enum Direction {
	NORTH('N'), EAST('E'), SOUTH('S'), WEST('W');
	
	private final char id;
	
	Direction(char id) {
	    this.id = id;
	}
	
	private static final Map<Character, Direction> directionMap = new HashMap<>();
	
	static {
	    for(Direction dir : Direction.values()) {
		directionMap.put(dir.id, dir);
	    }
	}
	
	@Override
	public String toString() {
	    return Character.toString(id);
	}
	
	public char id() {
	    return id;
	}
	
	public static Direction of(Character dir) {
	    Direction d = directionMap.get(dir);
	    if(d == null) {
		throw new IllegalArgumentException("No value mapped to, " + dir);
	    }
	    
	    return d;
	}
    }

    private Point2d position;
    private Direction direction;
    private int directionPointer;
    
    public Ship() {
	position = new Point2d(0, 0);
	direction = Direction.EAST;
	directionPointer = 1;
    }
    
    public Point2d pos() {
	return position;
    }
    
    public Direction dir() {
	return direction;
    }
    
    public void turn(char turnDirection, int value) {
	if(turnDirection == 'L') {
	    directionPointer = Math.floorMod(directionPointer - (value / 90), 4);
	    direction = Direction.of(Direction.values()[directionPointer].id());
	}
	else if (turnDirection == 'R') {
	    directionPointer = Math.floorMod(directionPointer + (value / 90), 4);
	    direction = Direction.of(Direction.values()[directionPointer].id());
	}
	else
	    throw new IllegalArgumentException("No turn direction mapped to, " + turnDirection);
    }
    
    public void moveForward(Direction direction, int units) {
	switch(direction) {
	case EAST:
	    position = new Point2d(position.x() + units, position.y());
	    break;
	case SOUTH:
	    position = new Point2d(position.x(), position.y() - units);
	    break;
	case WEST:
	    position = new Point2d(position.x() - units, position.y());
	    break;
	case NORTH:
	    position = new Point2d(position.x(), position.y() + units);
	    break;
	}
    }
    
    public void move(char dir, int units) {
	Direction direction = Direction.of(dir);
	moveForward(direction, units);
    }
    
    public void moveTowards(Point2d target, int units) {
	int dx = units * target.x();
	int dy = units * target.y();
	
	position = new Point2d(position.x() + dx, position.y() + dy);
    }

}
