
// Skeletal program for the "Draw a Clock" assignment
// Written by:  Minglun Gong

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

// Main class
public class DrawClock extends Frame {
	GridCanvas grid;
	Label infor;
	Timer timer = new Timer();
	// Constructor
	public DrawClock(int span) {
		super("Analog Clock");
		// create & add a grid canvas
		grid = new GridCanvas(span);
		add("Center", grid);
		// add an information bar
		infor = new Label();
		infor.setAlignment(infor.CENTER);
		add("South", infor);
		addWindowListener(new ExitListener());
		timer.scheduleAtFixedRate((new UpdateClock()), 0, 1000);
	}
	// Exit listener
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			timer.cancel();
			System.exit(0);
		}
	}
	// TimerTask for updating the colck
	class UpdateClock extends TimerTask {
		public void run() {
			Calendar time = Calendar.getInstance();
			int hour = time.get(Calendar.HOUR_OF_DAY);
			int minute = time.get(Calendar.MINUTE);
			int second = time.get(Calendar.SECOND);
			// display the current time
			infor.setText("Current time: " + hour + (minute>9?":"+minute:":0"+minute) + (second>9?":"+second:":0"+second));
			grid.setTime(hour, minute, second);
		}
	}

	public static void main(String[] args) {
		int span = 10;
		if ( args.length == 1 )
			span = Integer.parseInt(args[0]);
		DrawClock window = new DrawClock(span);
		window.setSize(400, 450);
		window.setResizable(true);
		window.setVisible(true);
	}
}

// Canvas with grid shown
class GridCanvas extends Canvas {
	// parameter that controls the span of the grid
	int span, width, height, xoff, yoff;
	int hour, minute, second;
	// Initialize the grid size;
	public GridCanvas(int s) {
		span = s;
	}
	public void setTime(int h, int m, int s) {
		hour = h; minute = m; second = s;
		repaint();
	}
	// Draw the grids
	public void drawGrid(Graphics2D g2D) {
		g2D.setColor(Color.lightGray);
		for ( int x=-width ; x<=width ; x++ )
			g2D.draw(new Line2D.Float(new Dot(x, -height).toCoord(), new Dot(x, height).toCoord()));
		for ( int y=-height ; y<=height ; y++ )
			g2D.draw(new Line2D.Float(new Dot(-width, y).toCoord(), new Dot(width, y).toCoord()));
	}

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		
		// calculate the number of cells to be shown
		width = (getWidth() / span - 1) / 2;
		height = (getHeight() / span - 1) / 2;
		xoff = getWidth() / 2;
		yoff = getHeight() / 2;
		drawGrid(g2D);
		
		// example of how to display a dot
		g2D.setColor(Color.red);
		g2D.fill(new Dot(0, 0));
		g2D.fill(new Dot(second%width, minute%height));
	}

	// Represent a dot at given coordinate
	class Dot extends Ellipse2D.Float {
		public Dot(int x, int y) {
			super(x*span-span/2+xoff, -y*span-span/2+yoff, span, span);
		}
		public Point toCoord() {
			return new Point((int)x+span/2, (int)y+span/2);
		}
	}
}