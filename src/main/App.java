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
	// Cap frames to 60fps
	private final double UPDATE_CAP = 1.0 / 60.0;
	
	public static void main(String[] args) {
		App app = new App();
	}
	
	App() {
		// Initialize the app
		this.title = "ColorPicker V1.0";

		this.picker = new ColorPicker();
		this.window = new Window(this);
		this.input = new Input();
		
		
		thread = new Thread(this);
		thread.run();
	}
	
	public void run() {
		// Thread is used to get video feed for the preview
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
			
			// Get time at start of loop
			firstTime = System.nanoTime() / 1000000000.0;
			
			// Get how much time has passed from last frame to this frame
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			// Add how much time has passed to unprocessed time (counter)
			unprocessedTime += passedTime;
			frameTime += passedTime;

			// UnprocessedTime checks if the desired amount of time has passed to render a frame
			// Id unprocessedTime < UPDATE_CAP the loop wont render and will advance time
			// This way the fps can be capped to desired amounts
			
			// While unprocessedTime is higher than the desired fps, subtract the UPDATE_CAP from the unprocessedTime and render
			// unprocessedTime -= UPDATE_CAP: this lets the program render skipped frames, as it renders each loop as long as the unprocessedTime !>= UPDATE_CAP
			while(unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;

				// After 1s has passed, check how many frames have rendered and reset timer
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
				window.update(picker.getScreen(), this);

				// To count how many times render has happened
				frames++;
			} else {
				// If render = false, sleep for 1ms to advance the timers
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

	public Thread getThread() {
		return thread;
	}

	public Input getInput() {
		return input;
	}
}
