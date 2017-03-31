package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {
	

	private static final long serialVersionUID = 1L;
	
	protected Color fromColour;
	protected Color toColour;
	
	public GradientPanel(Color fromColour, Color toColour) {
		super();
		setOpaque(false);  // unfortunately required to disable automatic bg painting
		setBackground(fromColour); // in case colors are derived from background
		this.fromColour = fromColour;
		this.toColour = toColour;
	}
	
	public GradientPanel() {
		this(Color.blue.darker().darker(), Color.lightGray);
	}
	
	public void setFromColor(Color color) {
	   	Color oldColour = fromColour;
		this.fromColour = color;
		firePropertyChange("fromColour", oldColour, color);
	}
	
	public void setToColor(Color color) {
	   	Color oldColour = toColour;
		this.toColour = color;
		firePropertyChange("toColour", oldColour, color);
	}
  
	@Override
	protected void paintComponent(Graphics g) {
		Dimension size = getSize();
		GradientPaint paint = new GradientPaint(new Point(size.width / 10, size.height / 6), fromColour, new Point(size.width, size.height/6*5), toColour);
		((Graphics2D)g).setPaint(paint);
		g.fillRect(0, 0, size.width, size.height);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new GradientPanel());
		frame.setSize(400, 100);
		frame.setVisible(true);
	}
}
