package game;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a square on the board, the base class has very little
 * functionality as blank squares cannot contain items/players or be entered.
 * 
 * @author Robbie
 *
 */
public abstract class Square {
	protected Map<Direction, Wall> walls;
	public abstract boolean canEnter(Player player, Direction direction);
	public abstract boolean addPlayer(Player player);
	public abstract void removePlayer(Player player);
	
	public Square(){
		walls = new HashMap<Direction, Wall>();
	}
	
	/**
	 * Toggles a wall on or off. Used for the map builder.
	 *
	 * @param dir
	 * @author Kelly
	 */
	public void toggleWall(Direction dir) {
		Wall currentWall = walls.get(dir);
		if (currentWall instanceof SolidWall) {
			walls.put(dir, new EmptyWall());
		} else {
			walls.put(dir, new SolidWall());
		}
	}

	public Map<Direction, Wall> getWalls() {
		return walls;
	}
}
