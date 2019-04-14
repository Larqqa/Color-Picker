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
	private Color color;
	
	ColorPicker() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void getPickedColor(int mx, int my) {
		if (r.getPixelColor(mx,my) != null) {
			color = r.getPixelColor(mx,my);
		} else {
			color = Color.black;
		}
	}
	
	public void makeImage(int mx, int my) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
	
		Rectangle allScreenBounds = new Rectangle();
		for (GraphicsDevice screen : screens) {
		    Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
		    
		    allScreenBounds.x = mx - PRE_SIZE / 2;
		    allScreenBounds.y = my - PRE_SIZE / 2;
		    allScreenBounds.width = PRE_SIZE;
		    allScreenBounds.height = PRE_SIZE;
		}
	
		screen = r.createScreenCapture(allScreenBounds);
		for (int x = 0; x < PRE_SIZE / 10; x++) {
			int y = PRE_SIZE / 2;
			screen.setRGB((PRE_SIZE / 2 - (PRE_SIZE / 10 / 2)) + x, y, Color.RED.getRGB());	
		}
		for (int y = 0; y < PRE_SIZE / 10; y++) {
			int x = PRE_SIZE / 2;
			screen.setRGB(x, (PRE_SIZE / 2 - (PRE_SIZE / 10 / 2)) + y, Color.RED.getRGB());
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
