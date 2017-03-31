package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.border.Border;

public class TransparentShadowBorder implements Border {

	private int xOffset;
	private Color color;
	private int yOffset;

	public TransparentShadowBorder(int xOffset, int yOffset, Color color) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.color = color;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 1, yOffset+1, xOffset+1);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics gr, int x, int y, int width, int height) {
		Graphics2D g = (Graphics2D)gr;
		g.setColor(color);
		g.drawRect(0, 0, width-1-xOffset, height-1-yOffset);
		
		
		
		Color from = new Color(color.getRed(), color.getGreen(), color.getBlue(), 196);
		Color to = new Color(color.getRed(), color.getGreen(), color.getBlue(), 64);
		
		Polygon side = new Polygon(new int[] {
			width-xOffset,
			width-xOffset,
			width-1,
			width-1
		}, new int[] {
			1, 
			height-yOffset,
			height,
			yOffset+1
		}, 4);
		
		
		GradientPaint paint = new GradientPaint(new Point(width-xOffset-1, 0), from, new Point(width-1, 0), to);
		g.setPaint(paint);
		g.fill(side);

		Polygon bottom = new Polygon(new int[] {
				1,
				width-xOffset,
				width,
				xOffset+1
			}, new int[] {
				height-yOffset,
				height-yOffset,
				height-1,
				height-1
			}, 4);
			
			
		paint = new GradientPaint(new Point(0, height-yOffset-1), from, new Point(0, height-1), to);
		g.setPaint(paint);
		g.fill(bottom);
		
		
	}

}
