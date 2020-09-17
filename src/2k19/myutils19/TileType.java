package myutils19;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    EMPTY(0, "Empty"),
    WALL(1, "Wall"),
    BLOCK(2, "Block"),
    PADDLE(3, "Paddle"),
    BALL(4, "Ball");
    
    private static final Map<Integer, TileType> tileTypes = new HashMap<>(values().length, 1);

    static {
	for(TileType tiletype : values())
	    tileTypes.put(tiletype.id, tiletype);
    }
    
    private final int id;
    private final String name;
    
    TileType(int id, String name) {
	this.id = id;
	this.name = name;
    }
    
    @Override
    public String toString() {
	return name;
    }
    
    public int id() {
	return id;
    }
    
    public static TileType of(int id) {
	TileType tiletype = tileTypes.get(id);
	if(tiletype == null) {
	    throw new IllegalArgumentException("Tile ID " + id + " is not mapped to any tile type");
	}
	return tiletype;
    }

}
