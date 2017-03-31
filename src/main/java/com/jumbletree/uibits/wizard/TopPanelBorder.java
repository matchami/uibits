package com.jumbletree.uibits.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class TopPanelBorder implements Border {

	private static TopPanelBorder instance = new TopPanelBorder();
	
	private Insets insets = new Insets(0, 0, 2, 0);
	
	public static TopPanelBorder get() {
		return instance;
	}
	
	private TopPanelBorder() {
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color base = c.getParent().getBackground();
		Color dark = base.darker();
		Color light = base.brighter();
		
		g.setColor(dark);
		g.drawLine(x, y+height-2, x+width, y+height-2);
		g.setColor(light);
		g.drawLine(x, y+height-1, x+width, y+height-1);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return insets;
	}
}
