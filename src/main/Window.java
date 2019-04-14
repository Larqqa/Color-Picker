// Create JFrame & required components for the app.

package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
	private GraphicsDevice screenDevice;
	private GraphicsDevice currentDevice;
	private JList<String> list;
	private JTabbedPane tPane;
	private JScrollPane rgbListCont;
	private DefaultListModel<String> rgbList;
	private JScrollPane hsvListCont;
	private DefaultListModel<String> hsvList;
	private JScrollPane hexListCont;
	private DefaultListModel<String> hexList;
	
	private boolean active = false;
	
	public Window(App app) {
		this.app = app;
		
		// Initialize the window
		
		// Initialize the image for live preview
		image = new BufferedImage(app.getPicker().PRE_SIZE, app.getPicker().PRE_SIZE, BufferedImage.TYPE_INT_RGB);
		
		// Set dimensions of the canvas to the preview size set in ColorPicker
		Dimension s = new Dimension(app.getPicker().PRE_SIZE, app.getPicker().PRE_SIZE);

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
		
		tPane = new JTabbedPane();

		rgbList = new DefaultListModel<String>();
		rgbList.addElement("Colors in RGB:");
		list = new JList<String>(rgbList);
		list.setCellRenderer(new TextColorizer(0));
		rgbListCont = new JScrollPane(list);
	    rgbListCont.setPreferredSize(s);
	    
	    hsvList = new DefaultListModel<String>();
	    hsvList.addElement("Colors in HSV:");
		list = new JList<String>(hsvList);
		list.setCellRenderer(new TextColorizer(1));
		hsvListCont = new JScrollPane(list);
		
		hexList = new DefaultListModel<String>();
		hexList.addElement("Colors in HEX:");
		list = new JList<String>(hexList);
		list.setCellRenderer(new TextColorizer(2));
		hexListCont = new JScrollPane(list);
	    
	    tPane.addTab("RGB", rgbListCont);
	    tPane.addTab("HSV", hsvListCont);
	    tPane.addTab("HEX", hexListCont);
	    can.add(tPane, BorderLayout.SOUTH);
		
		// Add JPanel to JFrame
		frame.add(can);
		
		// Set Frame defaults
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// Check the initial display
		GraphicsDevice currentDevice = frame.getGraphicsConfiguration().getDevice();
		
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
	
	@SuppressWarnings("static-access")
	public void update(BufferedImage image, App app) {
		// Update preview image and check if toggle is active
		
		// Get current screen device
		GraphicsDevice screenDevice = frame.getGraphicsConfiguration().getDevice();
		
		if (screenDevice != currentDevice) {
			// If window changes monitors, recreate draw buffer
			g = bs.getDrawGraphics();
		}
		
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		image.flush();
		bs.show();
		
		// If toggle is active get focus when focus is lost
		// Slightly hacky way of doing this: minimize window, then return window
		// This allows for selection of multiple colors when active
		if (active) {
			if (frame.getFocusOwner() == null) {				
				// Set window to minimized
				frame.setExtendedState(JFrame.ICONIFIED);

				// Add 50ms delay so that user can still click on stuff
				try {
					app.getThread().sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// Set window to normal, which also grabs focus back to the window
				frame.setExtendedState(JFrame.NORMAL);
			}
		}
	}
	
	private class TextColorizer extends DefaultListCellRenderer {
		private int i;
		TextColorizer(int i) {
			this.i = i;
		}
		
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
            String[] tokens;
            String delims;
            Color clr;
            
            switch (i) {
			case 0:
				// Convert RGB string to RGB color
				if(index == 0) break;
				delims = "[ ,]+";
				tokens = value.toString().split(delims);
				clr = new Color(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
				
				setForeground(clr);
				break;
			case 1:
				// Convert HSV string to RGB color
				if(index == 0) break;
				delims = "[ ,]+";
				tokens = value.toString().split(delims);
				int rgb = Color.HSBtoRGB(Float.parseFloat(tokens[0])/360, Float.parseFloat(tokens[1])/100, Float.parseFloat(tokens[2])/100);
				clr = new Color(rgb);
				
				setForeground(clr);
				break;
			case 2:
				// Convert HEX string to RGB color
				if(index == 0) break;
				String colorStr = value.toString();
				clr = new Color(
			            		Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
			            		Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
			            		Integer.valueOf( colorStr.substring( 5, 7 ), 16 )
			            		);
				
				setForeground(clr);
				break;
				
			default:
				break;
			}
            return(this);
        }
    }
	
	public void getColor() {
		// When focus is lost, get the color of the pixel at pointers location
		
		// Get RGB color
		int r = app.getPicker().getColor().getRed();
		int g = app.getPicker().getColor().getGreen();
		int b = app.getPicker().getColor().getBlue();
		String color = Integer.toString(r) + ", " + Integer.toString(g) + ", " + Integer.toString(b);
		rgbList.addElement(color);
		
		
		// Convert to HSV
		float[] hsv = new float[3];
		hsv = Color.RGBtoHSB(r, g, b, hsv);
		// To get correct values, multiply the floats
		hsv[0] = hsv[0] * 360;
		hsv[1] = hsv[1] * 100;
		hsv[2] = hsv[2] * 100;
		color = Integer.toString((int)Math.round((double)hsv[0])) + ", " + Integer.toString((int)Math.round((double)hsv[1])) + ", " + Integer.toString((int)Math.round((double)hsv[2]));
		hsvList.addElement(color);
		
		// Convert to HEX color
		color = String.format("#%02X%02X%02X", r, g, b);
		hexList.addElement(color);
		
		frame.pack();
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

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		// Use focusLost event to get the color if toggle is active
		if (active) {
			getColor();
		}
	}
}
