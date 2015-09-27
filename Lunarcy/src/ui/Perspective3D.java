package ui;

import processing.core.*;
import saito.objloader.*;
import game.*;

/**
 * The view that displays the player perspective of the game world in 3D.
 * Renders onto a 3D PGrapihcs layer before drawing onto the parent canvas.
 * 
 * @author Jack & Kelly
 *
 */
public class Perspective3D extends DrawingComponent {

	// 3D graphics layer
	private PGraphics g;

	// temporary background image
	private Animation tempGifAnimation;

	// 3D world
	private PShape worldShape;
	private OBJModel worldModel;
	private Square[][] world;
	private final int SQUARE_SIZE = 250;

	// camera fields
	private PVector camEye;
	private PVector camCenter;
	private float rotationAngle = 0;
	private float elevationAngle = 0;

	public Perspective3D(PApplet p, GameState gameState, PGraphics g) {
		// public Perspective3D(PApplet p, GameState gameState) {
		super(p, gameState);

		// use the given graphics layer as the 3D renderer
		this.g = g;
		tempGifAnimation = new Animation("assets/animations/shrek/shrek_", 20);

		// camera setup
		camEye = new PVector(0, -100, 0);
		camCenter = new PVector(0, 0, 0);

		// world setup
		// worldShape = p.loadShape("assets/models/floor.obj");
		// worldShape.disableStyle();
		// worldShape.scale(100, 100, 100);
		// worldShape.rotateX(PApplet.PI / 2);

		worldModel = new OBJModel(p, "assets/models/floor.obj");
	}

	@Override
	public void update(GameState gameState) {
		world = gameState.getBoard();
	}

	@Override
	public void draw(float delta) {
		handleInput(delta);

		// allow drawing onto the graphics layer
		// g.beginDraw();
		((Canvas) p).drawOn(g);

		// push matrix and style information onto the stack
		g.pushMatrix();
		g.pushStyle();

		// draw the 3D perspective
		g.background(255);

		g.camera(camEye.x, camEye.y, camEye.z, camCenter.x, camCenter.y,
				camCenter.z, 0.0f, 1, 0);
		// float fov = PApplet.PI / 3.0f;
		// float cameraZ = (g.height / 2.0f) / PApplet.tan(fov / 2.0f);
		// float aspect = PApplet.parseFloat(g.width)
		// //PApplet.parseFloat(g.height);
		// //g.perspective(fov, aspect, cameraZ / 100.0f, cameraZ * 100.0f);

		// test image plane and gif
		g.pushMatrix();
		g.noStroke();
		g.fill(0, 255, 0);
		g.rotateX(PApplet.PI / 2);
		g.rect(10, 0, -20, -SQUARE_SIZE);
		g.fill(255, 0, 0);
		g.sphere(10);
		g.rotateX(-PApplet.PI / 2);
		g.rotateY(PApplet.radians(p.frameCount));
		tempGifAnimation.update(delta);
		tempGifAnimation.display(0, 0, SQUARE_SIZE, SQUARE_SIZE);
		g.popMatrix();

		// draw test board
		g.pushMatrix();
		g.stroke(0);
		g.strokeWeight(5);

		g.rotateX(PApplet.PI / 2);

		// VERY VERBOSE need to think of way to store map on construction!
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				Square s = world[x][y];
				if (s instanceof WalkableSquare) {
					WalkableSquare ws = (WalkableSquare) s;

					g.pushMatrix();
					g.translate(SQUARE_SIZE * x, SQUARE_SIZE * y);

					renderFloor();
					if (ws.isInside()) {
						renderCeiling();
					}

					if (ws.getWalls().get(Direction.North) instanceof SolidWall) {
						renderWall(0, 0, 1, 0);
					}

					if (ws.getWalls().get(Direction.East) instanceof SolidWall) {
						renderWall(1, 0, 1, 1);
					}

					if (ws.getWalls().get(Direction.South) instanceof SolidWall) {
						renderWall(0, 1, 1, 0);
					}

					if (ws.getWalls().get(Direction.West) instanceof SolidWall) {
						renderWall(0, 0, 1, 1);
					}
					g.popMatrix();
				}
			}
		}
		g.popMatrix();

		// pop matrix and style information from the stack
		g.popStyle();
		g.popMatrix();

		// finish drawing onto the graphics layer
		// g.endDraw();
		((Canvas) p).drawOff();

		// draw the 3D graphics layer onto the parent canvas
		p.image(g, 0, 0);
	}

	private void renderFloor() {
		g.fill(100);
		// g.rect(0, 0, SQUARE_SIZE, SQUARE_SIZE);
		// g.shape(worldShape);

		g.pushMatrix();
		g.translate(0, SQUARE_SIZE);
		g.scale(100, 100, 100);
		g.rotateX(-PApplet.PI / 2);
		worldModel.disableMaterial();
		worldModel.drawMode(OBJModel.POLYGON);
		worldModel.draw();
		g.popMatrix();
	}

	private void renderCeiling() {
		g.pushMatrix();
		g.translate(0, 0, SQUARE_SIZE);

		g.fill(100);
		g.rect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

		g.popMatrix();
	}

	private void renderWall(int transX, int transY, int rotateX, int rotateY) {
		g.pushMatrix();
		g.translate(SQUARE_SIZE * transX, SQUARE_SIZE * transY);
		g.rotateX(PApplet.PI / 2 * rotateX);
		g.rotateY(PApplet.PI / 2 * rotateY);

		g.fill(150);
		g.rect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

		g.popMatrix();
	}

	public class Animation {
		private PImage[] images;
		private int imageCount;
		private int frame;
		private float percent;
		private boolean halfRate = false;

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + PApplet.nf(i, 4) + ".jpg";
				images[i] = p.loadImage(filename);
			}
		}

		public void update(float delta) {
			halfRate = !halfRate;
			if (halfRate) {
				frame = ((int) (percent += delta)) % imageCount;
			}
		}

		public void display(float x, float y) {
			g.image(images[frame], x, y);
		}

		public void display(float x, float y, float w, float h) {
			g.image(images[frame], x, y, w, h);
		}
	}

	private void handleInput(float delta) {
		rotationAngle = PApplet
				.map(p.mouseX, 0, p.width, 0, PApplet.TWO_PI * 2);
		elevationAngle = PApplet.map(p.mouseY, 0, p.height, 0, PApplet.PI);
		PVector move = new PVector(0, 0);
		if (p.keyPressed) {
			if (p.key == 'w' || p.key == 'W') {
				move = new PVector(5 * delta, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'a' || p.key == 'A') {
				move = new PVector(0, -5 * delta);
				move.rotate(rotationAngle);
			}
			if (p.key == 's' || p.key == 'S') {
				move = new PVector(-5 * delta, 0);
				move.rotate(rotationAngle);
			}
			if (p.key == 'd' || p.key == 'D') {
				move = new PVector(0, 5 * delta);
				move.rotate(rotationAngle);
			}
		}
		updateCamera(move);
	}

	private void updateCamera(PVector move) {
		camEye.x += move.x;
		camEye.z += move.y;
		camCenter.x = PApplet.cos(rotationAngle) + camEye.x;
		camCenter.z = PApplet.sin(rotationAngle) + camEye.z;
		camCenter.y = -PApplet.cos(elevationAngle) + camEye.y;
	}
}
