package game;

import java.awt.Color;
import java.util.List;

/**
 * Represents a Player character and contains all the information about the
 * Player e.g orientation, position, inventory, identification
 * 
 * @author Robbie
 *
 */
public class Player {
	private final int id;
	private final String name;
	private Color color;

	private Location location;
	private Direction orientation;
	private int oxygen;
	private List<Item> inventory;

	public Player(int uniqueID, String name, Location location) {
		this.id = uniqueID;
		this.name = name;
		if(location==null){
			location = new Location(0,0);
		}
		this.location = location;
		this.oxygen = 200;
	}

	public void move(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException(
					"Parameter 'direction' may not be null");
		location = location.getAdjacent(direction);
	}

	public void modifyOxygen(int amount) {
		oxygen += amount;
		if (oxygen > 200) {
			oxygen = 200;
		} else if (oxygen < 0) {
			oxygen = 0;
		}
	}

	public int getOxygen() {
		return oxygen;
	}

	public Location getLocation() {
		return location;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public Direction getOrientation() {
		return orientation;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
