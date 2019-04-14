// Create JFrame & required components for the app.

package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class Window implements ActionListener, FocusListener {
	private JFrame frame;
	private JPanel panel;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	private JToggleButton pick;
	private App app;
	
	private boolean active = false;
	
	public Window(App app) {
		this.app = app;
		
		// Initialize the window
		
		// Initialize the image for live preview
		image = new BufferedImage(app.getPicker().getPRE_SIZE(), app.getPicker().getPRE_SIZE(), BufferedImage.TYPE_INT_RGB);
		
		Dimension s = new Dimension(app.getPicker().getPRE_SIZE(), app.getPicker().getPRE_SIZE());

		// Create containers and components
		frame = new JFrame(app.getTitle());
		canvas = new Canvas();
		JPanel can = new JPanel();
		pick = new JToggleButton("Pick");
		
		// Add canvas to JPanel
		canvas.setPreferredSize(s);
		can.add(canvas, BorderLayout.CENTER);
		
		// Add listeners to toggle & add toggle to jPanel 
		pick.addActionListener(this);
		pick.addFocusListener(this);
		can.add(pick, BorderLayout.NORTH);
		
		// Add JPanel to JFrame
		frame.add(can);
		
		// Set Frame defaults
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		draw();

		// Set BufferStrategy and graphics
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
	}
	
	public void draw() {
		frame.pack();
		frame.setVisible(true);	
	}
	
	public void update(BufferedImage image, App app) {
		// Update preview image and check if toggle is active
		
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
		
		// If toggle is active get focus when focus is lost
		// This allows for selection of multiple colors when active
		if (active) {
			if (frame.getFocusOwner() == null) {
				//System.out.println("not focus");
				
				// Re-get focus
				frame.setState(JFrame.ICONIFIED);
				frame.setState(JFrame.NORMAL);
			}
		}
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If active, set window to always on top and active
		
		if (((JToggleButton)e.getSource()).isSelected()) {
			frame.setAlwaysOnTop(true);
			active = true;
		} else {
			frame.setAlwaysOnTop(false);
			active = false;
		}
	}

	// Focus events might not be needed?
	
	@Override
	public void focusGained(FocusEvent e) {
		//System.out.println("gained");
	}

	@Override
	public void focusLost(FocusEvent e) {
		//System.out.println("lost");
	}
}
