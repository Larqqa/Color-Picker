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
	
	public Window(App app) {
		this.app = app;
		
		image = new BufferedImage(app.getPicker().getPRE_SIZE(), app.getPicker().getPRE_SIZE(), BufferedImage.TYPE_INT_RGB);
		frame = new JFrame(app.getTitle());
		
		canvas = new Canvas();
		Dimension s = new Dimension(app.getPicker().getPRE_SIZE(), app.getPicker().getPRE_SIZE());
		canvas.setPreferredSize(s);
		
		JPanel can = new JPanel();
		can.add(canvas, BorderLayout.CENTER);
		pick = new JToggleButton("Pick");
		pick.addActionListener(this);
		can.add(pick, BorderLayout.NORTH);
		frame.add(can);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		draw();

		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
		canvas.addFocusListener(this);
	}
	
	public void draw() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public void update(BufferedImage image, App app) {
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((JToggleButton)e.getSource()).isSelected()) {
			frame.setAlwaysOnTop(true);
		} else {
			frame.setAlwaysOnTop(false);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		System.out.println("gained");
	}

	@Override
	public void focusLost(FocusEvent e) {
		System.out.println("lost");
		frame.requestFocus();
		frame.repaint();
	}
}
