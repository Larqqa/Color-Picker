package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class Input {
	private App app;
	private PointerInfo mouseLoc;
	Point m;

	Input(App app){
		this.app = app;
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