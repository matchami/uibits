package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class ShadowBorder implements Border {

	public static final ShadowBorder instance = new ShadowBorder();
	
	@Override
	public Insets getBorderInsets(Component arg0) {
		return new Insets(1, 1, 3, 3);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Container parent = c.getParent();
		if (parent == null)
			return;
		
		Color background = parent.getBackground().darker();
		g.setColor(background);
		g.translate(x, y);
		
		g.drawLine(0, 0, width-3, 0);
		g.drawLine(0, 0, 0, height-3);
		g.drawLine(width-3, 0, width-3, height-3);
		g.drawLine(0, height-3, width-3, height-3);
		
		g.setColor(new Color(0, 0, 0, 128));
		g.fillRect(width-3, 2, 2, height-3);
		g.fillRect(2, height-3, width-5, 2);
		
		g.translate(-x, -y);
	}
}
