package ui;

import game.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Maintains the Swing window for the Lunarcy game. Handles the confirmation
 * dialog that appears when a user attempts to close their client. Contains the
 * Processing canvas component that handles all rendering of the game state.
 *
 * @author Jack & Ben
 *
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {

	// the initial size the frame should be drawn at
	public static final int INIT_WIDTH = 1280;
	public static final int INIT_HEIGHT = 720;

	// the maximum size the frame should be drawn at
	public static final int MAX_WIDTH = 1280;
	public static final int MAX_HEIGHT = 720;

	// the processing canvas
	private final Canvas canvas;

	public Frame(GameState gameState, boolean hardwareRenderer) {
		setTitle("Lunarcy");
		setSize(INIT_WIDTH, INIT_HEIGHT);

		// Catch exit with confirmation dialog
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane
						.showConfirmDialog(null, new JLabel(
								"Are you sure you want to exit Lunarcy?"),
								"Exit Game", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				// Only close if they confirm they want to close
				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		// setup processing canvas
		setLayout(new BorderLayout());
		canvas = new Canvas(MAX_WIDTH, MAX_HEIGHT, gameState, hardwareRenderer);
		add(canvas, BorderLayout.CENTER);

		// setup panel resize listener
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				// send the new window size to the canvas
				canvas.adjustScaling(getWidth(), getHeight());
			}
		});

		// Center align the frame on screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2,
				(size.height - getHeight()) / 2, getWidth(), getHeight());

		setResizable(true);
		setVisible(true);

		// run the processing canvas
		canvas.init();
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public static void main(String args[]) {
		new Frame(createTestGameState1(20, 20), true);
	}

	public static GameState createTestGameState1(int w, int h) {
		// test game state with w x h board
		GameState state = new GameState(w, h);

		// populate the state board

		// for (int x = 0; x < w; x++) {
		//
		// Wall east = null;
		// Wall west = null;
		//
		// if (x == 0) {
		// west = new SolidWall();
		// } else if (x == w - 1) {
		// east = new SolidWall();
		// }
		//
		// for (int y = 0; y < h; y++) {
		// Wall north = null;
		// Wall south = null;
		//
		// if (y == 0) {
		// north = new SolidWall();
		// } else if (y == h - 1) {
		// south = new SolidWall();
		// }
		//
		// state.setSquare(new Location(x, y), new WalkableSquare("test",
		// "test description", (Math.random() < 0.5), north, east,
		// south, west));
		// }
		// }

		// load the board state from the map.xml
		state.load();

		return state;
	}
}