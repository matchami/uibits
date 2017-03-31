package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Hr90Border implements Border, SwingConstants {

	private static Hr90Border topInstance = new Hr90Border(TOP);
	private static Hr90Border bottomInstance = new Hr90Border(BOTTOM);
	
	private boolean top;

	private Hr90Border(int location) {
		this.top = location == TOP;
	}
	
	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(top ? 2 : 0, 0, top ? 0 : 2, 0);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color darker = c.getBackground().darker();
		Color brighter = c.getBackground().brighter();
		
		g.setColor(darker);
		int lineY = top ? 0 : y + height - 2;
		g.drawLine(x + 15, lineY, x + width-15, lineY);
		//g.drawLine(x + width/20, y + height - 2, x + width/20*19, y + height - 2);
		g.setColor(brighter);
		lineY = top ? 1 : y + height - 1;
		g.drawLine(x + 15, lineY, x + width-15, lineY);
		//g.drawLine(x + width/20, y + height - 1, x + width/20*19, y + height - 1);
	}

	public static Hr90Border get(int location) {
		switch (location) {
			case TOP:
				return topInstance;
			case BOTTOM:
				return bottomInstance;
			default:
				throw new IllegalArgumentException();
		}
	}

}
