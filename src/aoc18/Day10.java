package aoc18;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//jfreechart is needed to run this code
//you can get it at http://www.jfree.org/jfreechart/
public class Day10 extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<Point2D.Double> coordinates;
    private List<Velocity> velocities;
    private XYDataset dataset;
    private JFreeChart chart;
    private Timer animationTimer;
    private int speed;
    private int elapsedTime;

    public Day10() throws IOException {
	super("Day 10");
	elapsedTime = 0;
	speed = 100;
	coordinates = getCoordinates(
		new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 10\\InputFile.txt"));
	velocities = getVelocities(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2018\\Day 10\\InputFile.txt"));

	XYSeriesCollection collection = new XYSeriesCollection();
	XYSeries coordinateSeries = new XYSeries("coordinates");

	for (int i = 0; i < coordinates.size(); i++) {
	    System.out.println(coordinates.get(i).toString() + "   " + velocities.get(i).toString());
	    coordinateSeries.add(coordinates.get(i).x, coordinates.get(i).y);
	}

	collection.addSeries(coordinateSeries);

	dataset = collection;

	chart = ChartFactory.createScatterPlot("moving stars", "X", "Y", dataset);
	ChartPanel panel = new ChartPanel(chart);

	setContentPane(panel);
    }

    // part 1
    // this solution is, admittedly, not very intuitive but it works very
    // precisely
    // the coordinates move until something resembling characters appears on the
    // screen.
    // afterwards the speed of the animation (the moving coordinates) has to be
    // controlled
    // until the full set of characters appears.
    public void init() {
	this.setSize(1700, 1700);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// keylistener to stop the coordinates from "moving"
	this.addKeyListener(new KeyListener() {

	    @Override
	    public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == 's') {
		    animationTimer.stop();
		}
		if (arg0.getKeyChar() == 'd') {
		    animationTimer.start();
		}
		// keys to control the animation speed
		// can go forward and backward
		if (arg0.getKeyChar() == 'l') {
		    speed -= 10;
		}
		if (arg0.getKeyChar() == 'o') {
		    speed += 10;
		}
		if (arg0.getKeyChar() == 'j') {
		    speed += 1;
		}
		if (arg0.getKeyChar() == 'k') {
		    speed -= 1;
		}
	    }

	    @Override
	    public void keyReleased(KeyEvent arg0) {
	    }

	    @Override
	    public void keyTyped(KeyEvent arg0) {
	    }
	});

	// updates the coordinates every second
	animationTimer = new Timer(1000, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		updateCoordinates();
	    }
	});
	animationTimer.start();
	this.setVisible(true);
    }

    // moves coordinates x (speed * velocity) units in the current direction
    private void updateCoordinates() {
	elapsedTime += speed;
	System.out.println("Elapsed time (seconds): " + elapsedTime);
	XYSeries coordinateSeries = new XYSeries("coordinates");
	for (int i = 0; i < coordinates.size(); i++) {
	    coordinates.set(i, new Point2D.Double(coordinates.get(i).x + (speed * velocities.get(i).vX),
		    coordinates.get(i).y + (speed * velocities.get(i).vY)));
	    coordinateSeries.add(coordinates.get(i).x, coordinates.get(i).y);
	}
	XYSeriesCollection updatedCollection = new XYSeriesCollection(coordinateSeries);
	XYDataset updatedSet = updatedCollection;
	chart.getXYPlot().setDataset(updatedSet);
    }

    public static class Velocity {
	private double vX;
	private double vY;

	public Velocity(double vX, double vY) {
	    this.vX = vX;
	    this.vY = vY;
	}

	public double vX() {
	    return vX;
	}

	public double vY() {
	    return vY;
	}

	@Override
	public String toString() {
	    return vX + " " + vY;
	}
    }

    public static List<Point2D.Double> getCoordinates(File inputFile) throws IOException {
	List<Point2D.Double> coordinates = new ArrayList<Point2D.Double>();
	BufferedReader br = new BufferedReader(new FileReader(inputFile));
	String line = "";
	while ((line = br.readLine()) != null) {
	    String x = line.substring(line.indexOf("<") + 1, line.indexOf(","));
	    String y = line.substring(line.indexOf(",") + 1, line.indexOf(">"));
	    coordinates.add(new Point2D.Double(java.lang.Double.parseDouble(x), java.lang.Double.parseDouble(y)));
	}
	br.close();
	return coordinates;
    }

    public static List<Velocity> getVelocities(File inputFile) throws IOException {
	List<Velocity> velocities = new ArrayList<Velocity>();
	BufferedReader br = new BufferedReader(new FileReader(inputFile));
	String line = "";
	while ((line = br.readLine()) != null) {
	    String vx = line.substring(line.indexOf("<", line.indexOf("<") + 1) + 1,
		    line.indexOf(",", line.indexOf(",") + 1));
	    String vy = line.substring(line.indexOf(",", line.indexOf(",") + 1) + 2,
		    line.indexOf(">", line.indexOf(">") + 1));
	    velocities.add(new Velocity(java.lang.Double.parseDouble(vx), java.lang.Double.parseDouble(vy)));
	}
	br.close();
	return velocities;
    }

    public static void main(String[] args) throws IOException {
	Day10 test = new Day10();
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		test.init();
	    }
	});
    }
}