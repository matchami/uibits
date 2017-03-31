package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

public class ToolBar extends JToolBar implements Icon {

	private static final long serialVersionUID = 1L;
	private static BufferedImage hImage;
	private static BufferedImage vImage;

	public ToolBar() {
		super();
		setup();
	}

	public ToolBar(int orientation) {
		super(orientation);
		setup();
	}

	public ToolBar(String name, int orientation) {
		super(name, orientation);
		setup();
	}

	public ToolBar(String name) {
		super(name);
		setup();
	}

	private void setup() {
		UIDefaults overrides = new UIDefaults();
	    overrides.put("ToolBar.handleIcon", new IconUIResource(this));

	    putClientProperty("Nimbus.Overrides", overrides);
	    putClientProperty("Nimbus.Overrides.InheritDefaults", false);

	}

	@Override
	public int getIconHeight() {
		if (getOrientation() == VERTICAL) {
			return 12;
		} else 
			return getHeight();
	}

	@Override
	public int getIconWidth() {
		if (getOrientation() == VERTICAL) {
			return getWidth();
		} else 
			return 12;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = getIconWidth();
		int height = getIconHeight();
		int imageWidth = 18;
		g.translate(x, y);
		if (getOrientation() == VERTICAL) {
			g.setColor(((Color)UIManager.get("ToolBar.background")).darker());
			g.drawLine(0, height-2, width-1, height-2);
			g.drawImage(getVerticalImage(), (width - imageWidth)/2, 2, this);
		} else {
			g.setColor(((Color)UIManager.get("ToolBar.background")).darker());
			g.drawLine(width-2, 0, width-2, height-1);
			g.drawImage(getHorizontalImage(), 2, (height - imageWidth)/2, this);
		}
		
	}

	private Image getHorizontalImage() {
		if (hImage == null) try {
			hImage = ImageIO.read(getClass().getResource("toolbarHandleV.png"));
		} catch (Exception e) {
		}
		return hImage;
	}

	private Image getVerticalImage() {
		if (vImage == null) try {
			vImage = ImageIO.read(getClass().getResource("toolbarHandleH.png"));
		} catch (Exception e) {
		}
		return vImage;
	}

}
