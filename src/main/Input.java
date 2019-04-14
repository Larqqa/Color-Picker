// Get mouse inputs and mouse location

package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class Input {	
	private PointerInfo mouseLoc;
	Point m;

	Input(){
		m = new Point(0,0);
	}
	
	public void getMouseLocation() {
		mouseLoc = MouseInfo.getPointerInfo();
		m = mouseLoc.getLocation();
	}

	public PointerInfo getMouseLoc() {
		return mouseLoc;
	}

	public Point getM() {
		return m;
	}
}