package bots;

import java.util.List;

import game.Location;
import game.Player;

/**
 * Track Movement: Follow a Player around the map, computing the shortest path
 * to them and trying to reach them. Will give up after a set amount of time.
 * 
 * @author b
 *
 */
public class TrackMovement extends ShortestPathMover {

	Player target; // The player we are chasing

	public TrackMovement(Player target) {
		this.target = target;
	}

	/**
	 * Returns true if any of the following conditions hold
	 * 
	 * 1. The paths end location does not equal the target players location 2.
	 * The path is null 3. The path is empty
	 * 
	 * Otherwise returns false
	 */
	@Override
	protected boolean mustUpdate(List<Location> path) {
		if (path == null || path.isEmpty())
			return true;

		// Return true if the end location does not equal the
		// target location, false otherwise
		return !path.get(path.size() - 1).equals(target.getLocation());
	}

	/**
	 * Finds the shortest path from currentLocation, to the target field.
	 * Returns this path if one was found, or null.
	 * 
	 * @param currentLocation:
	 *            Where the rover is currently located
	 * @return the path
	 */
	public List<Location> move(Location currentLocation) {
		if (currentLocation == null || target == null)
			return null;

		return findPath(currentLocation, target.getLocation());
	}

}
