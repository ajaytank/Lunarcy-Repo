package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ClientWelcomeScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	// Used ion the server textbox
	private final String EXAMPLESERVER = "eg 127.0.0.1";

	// Will be used to make a palette of size MAXCOLORS x MAXCOLORS
	private final int MAXCOLORS = 5;

	// Width of the textboxes
	private final int WIDTH = 200;

	// To dynamically update from colorChooser
	private JPanel spacesuitPanel;
	private Color chosenColor;
	private BufferedImage spacesuitImage;

	// So we can read the choice when start button is pushed
	private JComboBox mode;

	public ClientWelcomeScreen() {
		super("Player info");

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(350, 350));

		// Pre load our spacesuit image
		loadImage();

		// Setup the title
		addWelcomeText();

		// Setup name textbox
		addName();

		// Setup IP address entry
		addConnectionBox();

		// Setup color chooser
		addColorPallete();

		// Setup preview panel
		addColorPreview();

		// Add rendering option
		addRenderDropdown();

		// Setup start game button
		addStart();

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2, (size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);
	}

	private void addWelcomeText() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel title = new JLabel("Welcome to Lunarcy");
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));

		// Welcome label at 0,0 taking up two cells
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		add(title, c);
	}

	private void addName() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel nameLabel = new JLabel("Enter your name:");
		JTextField name = new JTextField("");
		// Width of 200, Height of the font size
		name.setPreferredSize(new Dimension(WIDTH, name.getFont().getSize() + 5));

		// Name label goes at 0,1
		c.gridx = 0;
		c.gridy = 1;

		add(nameLabel, c);

		// Name textbox goes at 1,1
		c.gridx = 1;
		c.gridy = 1;

		add(name, c);
	}

	private void addConnectionBox() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel addressLabel = new JLabel("Enter the server IP:");

		final JTextField address = new JTextField(EXAMPLESERVER);

		// Width of 200, height of the font height
		address.setPreferredSize(new Dimension(WIDTH, address.getFont().getSize() + 5));

		// When the textbox is clicked, clear the default text.
		address.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (address.getText().equals(EXAMPLESERVER)) {
					address.setText("");
				}
			}

		});

		// Address label goes at 0,2
		c.gridx = 0;
		c.gridy = 2;
		add(addressLabel, c);

		// Address textbox goes at 1,2
		c.gridx = 1;
		c.gridy = 2;
		add(address, c);
	}

	private void addColorPallete() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel colorLabel = new JLabel("Choose your colour:");

		JPanel colorPalette = new JPanel(new GridLayout(MAXCOLORS, MAXCOLORS));

		// Create labels for N colors,
		// Use random colors to lessen the chance of two clients selecting the
		// same colored player
		for (int i = 0; i < MAXCOLORS; i++) {
			for (int j = 0; j < MAXCOLORS; j++) {
				// Create a label of a random color
				JLabel label = makeLabel(
						new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
				colorPalette.add(label);
			}
		}
		colorPalette.setPreferredSize(new Dimension(spacesuitImage.getWidth(), spacesuitImage.getHeight()));

		// Color label at 0,3 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		add(colorLabel, c);

		// Color pallete at 0,4 with a width of 1
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		add(colorPalette, c);

	}

	private void addColorPreview() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		// Make a new JPanel, which will display our preview image
		spacesuitPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// First draw the image
				g.drawImage(spacesuitImage, 0, 0, this);

				// Next draw a tint over the image if a color has been chosen
				if (chosenColor != null) {
					tintPicture(g);
				}

			}

			private void tintPicture(Graphics g) {
				for (int x = 0; x < spacesuitImage.getWidth(); x++) {
					for (int y = 0; y < spacesuitImage.getHeight(); y++) {
						int pixel = spacesuitImage.getRGB(x, y);
						// If the pixel is not transparent
						if ((pixel >> 24) != 0x00) {
							// Draw a transparent rect in this pixels location
							g.setColor(new Color(chosenColor.getRed() / 255f, chosenColor.getGreen() / 255f,
									chosenColor.getBlue() / 255f, .1f));
							g.drawRect(x, y, 1, 1);
						}
					}
				}
			}

		};
		spacesuitPanel.setPreferredSize(new Dimension(spacesuitImage.getWidth(), spacesuitImage.getHeight()));

		// The panel is at cell 1,4
		c.gridx = 1;
		c.gridy = 4;
		add(spacesuitPanel, c);
	}

	private void addRenderDropdown() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		String[] options = new String[] { "Software", "Hardware" };
		mode = new JComboBox<>(options);
		JLabel title = new JLabel("Select your rendering mode:");

		// Title at 0,5 width a width of 2 cells
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;

		add(title, c);

		// Option dropdown at 0,6 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 6;

		add(mode, c);

	}

	private void addStart() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JButton start = new JButton();
		start.setText("Join game");

		// Open frame on button push
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Frame(Frame.createTestGameState1(20, 20),
						// Frame takes a true value for hardware, false for
						// software
						mode.getSelectedItem().equals("Hardware"));
			}
		});

		// Start button at 0,7 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		add(start, c);
	}

	private JLabel makeLabel(final Color col) {
		// final so we can access it from the MouseAdaptor below
		final JLabel label = new JLabel();

		// Set our labels background to the color
		label.setBackground(col);

		// Make sure its visible
		label.setOpaque(true);

		// Size the label
		label.setPreferredSize(new Dimension(15, 15));

		// Show a border on hover, and When clicked set the chosen color and
		// update preview
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setBorder(null);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				chosenColor = col;
				spacesuitPanel.repaint(); // Update our preview
			}
		});
		return label;
	}

	private void loadImage() {
		try {
			// TODO: Replace with creative commons image
			spacesuitImage = ImageIO.read(new File("assets/items/space_suit.png"));
		} catch (IOException e) {
			// Error loading image
			return;
		}
	}

	public static void main(String[] args) {
		new ClientWelcomeScreen();
	}

}
