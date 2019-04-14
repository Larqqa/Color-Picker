// The color picker functions with a Robot that gets the pixel color at the mouse's location
// The Robot is also used to cast a preview screen of the location with zoom for accurate selection

package main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ColorPicker {
	private Robot r;
	private BufferedImage screen;
	private final int PRE_SIZE = 200;
	private final int ZOOM = 3;
	private Color color;
	
	ColorPicker() {
		// Initialize Robot
		
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void getPickedColor(int mx, int my) {
		// Get the color at the mouse location
		// If color == null, the color = black to avoid errors
		
		if (r.getPixelColor(mx,my) != null) {
			color = r.getPixelColor(mx,my);
		} else {
			color = Color.black;
		}
	}
	
	public void makeImage(int mx, int my) {
		// Create preview image
		
		// Set GE and get screens to support multiple screens
		// This might not be needed, but can help with problem with canvas and multiple screens
		//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//GraphicsDevice[] screens = ge.getScreenDevices();
	
		// Set the window size
		Rectangle preview = new Rectangle();
		
		// Center of the window = mouse point - ((Window size / 2) / zoom)
		preview.x = mx - ((PRE_SIZE / 2) / ZOOM);
		preview.y = my - ((PRE_SIZE / 2) / ZOOM);
		
		// Set zoom
		preview.width = PRE_SIZE / ZOOM;
		preview.height = PRE_SIZE / ZOOM;
	
		// Get the preview window with Robot at mouse location
		screen = r.createScreenCapture(preview);
		
		// Draw pointer at the center of the image
		// Currently red, should be made the negative of the color underneath the pixel (WIP)
		for (int x = 0; x <= preview.width / 10; x++) {
			int y = preview.height / 2;
			screen.setRGB((preview.width / 2 - ((preview.width / 10) / 2)) + x, y, Color.RED.getRGB());	
		}
		for (int y = 0; y <= preview.width / 10; y++) {
			int x = preview.height / 2;
			screen.setRGB(x, (preview.height / 2 - ((preview.height / 10) / 2)) + y, Color.RED.getRGB());
		}
	}

	public BufferedImage getScreen() {
		return screen;
	}

	public int getPRE_SIZE() {
		return PRE_SIZE;
	}

	public Color getColor() {
		return color;
	}
}
