package com.jumbletree.uibits.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Takes a single component and lays it out in the center of the container
 *
 */
public class CenterLayout implements LayoutManager {

	private static CenterLayout instance = new CenterLayout();
	
	public static CenterLayout instance() {
		return instance;
	}
	
	private CenterLayout() {
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized(parent.getTreeLock()) {
			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			
			Component c = parent.getComponent(0);
			Dimension child = c.getPreferredSize();
			
			size.width -= (insets.left + insets.right);
			size.height -= (insets.top + insets.bottom);
			
			int dx = (size.width - child.width) / 2;
			int dy = (size.height - child.height) / 2;
			
			c.setBounds(insets.left + dx, insets.top + dy, child.width, child.height);
		}
	}

	/**
	 * Returns the size of the child component
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		synchronized(parent.getTreeLock()) {
			Component c = parent.getComponent(0);
			Insets i = parent.getInsets();
			Dimension size = new Dimension(c.getPreferredSize());
			
			size.width += i.left + i.right;
			size.height += i.top + i.bottom;
			
			return size;
		}
	}

	/**
	 * Returns the size of the child component, plus a five pixel margin
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension min = minimumLayoutSize(parent);
		min.width += 10;
		min.height += 10;
		return min;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

}
