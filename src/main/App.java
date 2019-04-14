package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class App implements Runnable {
	private Thread thread;
	private Window window;
	private ColorPicker picker;
	private Input input;
	
	private String title;
	private boolean running = false;
	private final double UPDATE_CAP = 1.0 / 60.0;
	
	public static void main(String[] args) {
		App app = new App("ColorPicker");
	}
	
	App(String title) {
		this.picker = new ColorPicker();
		this.window = new Window(this);
		this.input = new Input(this);
		
		this.title = title;
		
		thread = new Thread(this);
		thread.run();
	}
	
	public void run() {
		running = true;
		boolean render = false;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		
		//Count fps
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		
		while(running) {
			render = false;
			
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;

			while(unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;

				if(frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					frames = 0;
					System.out.println(fps);
				}
			}
			
			if(render) {
				input.getMouseLocation();
				picker.makeImage(input.getM().x, input.getM().y);
				picker.getPickedColor(input.getM().x, input.getM().y);
				window.update(picker.getScreen(), this);

				frames++;
			} else {
				try {					
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public Window getWindow() {
		return window;
	}

	public ColorPicker getPicker() {
		return picker;
	}
}
