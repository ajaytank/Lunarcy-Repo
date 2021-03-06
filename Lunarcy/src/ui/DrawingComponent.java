package ui;

import processing.core.*;
import game.GameState;

/**
 * Represents a display component that is rendered onto the canvas.
 * 
 * @author Jack
 *
 */
public abstract class DrawingComponent {

	// the parent processing canvas
	protected PApplet p;

	public DrawingComponent(PApplet p, GameState gameState) {
		// set the parent canvas and graphics layer
		this.p = p;

		// set the initial game state
		update(gameState);
	}

	/**
	 * Update the component given an update game state.
	 */
	public abstract void update(GameState gameState);

	/**
	 * Draws the component.
	 * 
	 * @param delta
	 *            The delta time for the current draw tick.
	 */
	public abstract void draw(float delta);
}
