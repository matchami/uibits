package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class ResizeBorder implements Border {

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 15, 0);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color darker = c.getBackground().darker();
		Color brighter = c.getBackground().brighter();
		
		try {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} catch (ClassCastException e) {
		}
		
		g.translate(x+width-2, y+height-2);
		
		g.setColor(brighter);
		g.drawLine(0, -2, -2, 0);
		g.drawLine(0, -5, -5, 0);
		g.drawLine(0, -8, -8, 0);
		g.drawLine(0, -11, -11, 0);

		g.setColor(darker);
		g.drawLine(0, -3, -3, 0);
		g.drawLine(0, -6, -6, 0);
		g.drawLine(0, -9, -9, 0);
		g.drawLine(0, -12, -12, 0);

			
		g.translate(-(x+width-2), -(y+height-2));
	}

}
