package com.jumbletree.uibits.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;

public class RightLayout implements LayoutManager {

	private static HashMap<Integer, RightLayout> cache = new HashMap<Integer, RightLayout>();
	
	public static RightLayout instance() {
		return instance(5);
	}
	
	public static RightLayout instance(int gap) {
		RightLayout layout = cache.get(gap);
		if (layout == null) {
			layout = new RightLayout(gap);
			cache.put(gap, layout);
		}
		return layout;
	}

	private int gap;

	public RightLayout(int gap) {
		this.gap = gap;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized(parent.getTreeLock()) {
			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			
			int x = size.width - insets.right;
			if (size.width >= preferredLayoutSize(parent).width) {
				x -= gap;
			}
			
			size.height -= (insets.top + insets.bottom);
			Component[] children = parent.getComponents();
			for (int i=children.length-1; i>=0; i--) {
				Component c = children[i];
				
				Dimension cSize = c.getPreferredSize();
				
				int dy = (size.height - cSize.height) / 2;
				
				x -= cSize.width;
				
				c.setBounds(x, insets.top + dy, cSize.width, cSize.height);
				
				x -= gap;
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		synchronized(parent.getTreeLock()) {
			int width = 0;
			int height = 0;
			
			for (Component c : parent.getComponents()) {
				Dimension size = c.getPreferredSize();
				width += size.width + gap;
				height = Math.max(height, size.height);
			}
			
			width -= gap;
			Insets insets = parent.getInsets();
			return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension min = minimumLayoutSize(parent);
		
		min.width += gap * 2;
		min.height += gap * 2;
		
		return min;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

}
