// awt.Robot is used to cast a preview screen of the location with zoom for accurate selection
// The color picker takes the center most pixels color from this preview screen, under the pointer

package main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ColorPicker {
	private Robot r;
	private BufferedImage screen;
	public final int PRE_SIZE = 200;
	private final int ZOOM = 3;
	private final int P_SIZE = 10;
	private Color color;
	private Rectangle preview = new Rectangle();
	
	ColorPicker() {
		// Initialize Robot
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		// Set window size and zoom
		// Window is rendered as PRE_SIZE so to zoom the preview image, it can be / by a value.
		preview.width = PRE_SIZE / ZOOM;
		preview.height = PRE_SIZE / ZOOM;
		
		// Initialize preview
		makeImage(0,0);
	}
	
	public void makeImage(int mx, int my) {
		// Create preview image
	
		// Center of the window = mouse point - ((Window size / 2) / zoom)
		preview.x = mx - ((PRE_SIZE / 2) / ZOOM);
		preview.y = my - ((PRE_SIZE / 2) / ZOOM);
	
		// Get the preview window with Robot at mouse location
		screen = r.createScreenCapture(preview);
		
		// Get the RGB value of the center most pixel of the preview window
		color = new Color(
							screen.getRaster().getSample(screen.getWidth() / 2, screen.getHeight() / 2, 0),
							screen.getRaster().getSample(screen.getWidth() / 2, screen.getHeight() / 2, 1),
							screen.getRaster().getSample(screen.getWidth() / 2, screen.getHeight() / 2, 2)
		);
		
		// Draw pointer at the center of the image
		// Subtract the RGB value of the pointers pixels from white (0xffffff) to get the negative color for the pointer
		for (int x = 0; x <= preview.width / P_SIZE; x++) {
			int y = preview.height / 2;
			screen.setRGB((preview.width / 2 - ((preview.width / P_SIZE) / 2)) + x, y, 0xffffff - screen.getRGB((preview.width / 2 - ((preview.width / P_SIZE) / 2)) + x,y));
		}
		for (int y = 0; y <= preview.width / P_SIZE; y++) {
			int x = preview.height / 2;
			screen.setRGB(x, (preview.height / 2 - ((preview.height / P_SIZE) / 2)) + y, 0xffffff - screen.getRGB(x,(preview.width / 2 - ((preview.width / P_SIZE) / 2)) + y));
		}
	}

	public BufferedImage getScreen() {
		return screen;
	}

	public Color getColor() {
		return color;
	}
}
