package com.jumbletree.uibits;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A simple internal tabbed pane - kind of re-inventing the wheel but the TabbedPaneUI is so 
 * complicated that this is easier.
 * 
 * L&F is based on JGoodies Looks.
 * @author me
 *
 */
public class InternalTabbedPane extends JPanel implements Icon, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> names;
	private ArrayList<JComponent> components;
	private int activeTab;
	private int[] widths;

	public InternalTabbedPane() {
		super(new BorderLayout());
		JLabel label = new JLabel(this);
		label.setBorder(BorderFactory.createEmptyBorder());
		label.addMouseListener(this);
		add(label, BorderLayout.SOUTH);
		add(new JPanel(), BorderLayout.CENTER);
		this.names = new ArrayList<String>();
		this.components = new ArrayList<JComponent>();
		this.activeTab = 0;
	}
	
	public void addTab(String name, JComponent component) {
		this.names.add(name);
		this.components.add(component);
		if (this.names.size() == 1) {
			//This is the first tab
			setActiveTab(0, true);
		}
	}

	public void setActiveTab(int i) {
		setActiveTab(i, false);
	}
	
	public int getActiveTab() {
		return activeTab;
	}
	
	private void setActiveTab(int i, boolean force) {
		if (!force && i == activeTab)
			return;
		
		this.activeTab = i;
		remove(1);
		add(components.get(i), BorderLayout.CENTER);
		revalidate();
		//Redraw the label
		repaint();
//		getComponent(1).repaint();
//		getComponent(0).repaint();
		fireStateChanged();
	}

	@Override
	public int getIconHeight() {
		return 19;
	}

	@Override
	public int getIconWidth() {
		Insets insets = getInsets();
		return getWidth() - insets.left - insets.right;
	}

	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		//Space the labels
		widths = new int[names.size()];
		FontMetrics metrics = g.getFontMetrics();
		for (int i=0; i<names.size(); i++) {
			widths[i] = metrics.stringWidth(names.get(i));
		}
		
		//Draw the labels
		int offset = 12;
		g.setColor(GradientHeading.text);
		
		for (int i=0; i<names.size(); i++) {
			g.drawString(names.get(i), offset, 14);
			offset += 24 + widths[i];
		}
		
		//Line across the top and tab highlights
		g.setColor(GradientHeading.shadow);
		int width = getIconWidth();
		g.drawLine(0, 0, width, 0);
		
		offset = 0;
		int height = getIconHeight();
		for (int i=0; i<names.size()-1; i++) {
			offset += 24 + widths[i];
			g.drawLine(offset, 0, offset, height-1);
		}
		//Also finish off if it's the last one
		if (activeTab == widths.length-1) {
			offset += 24 + widths[widths.length-1];
			g.drawLine(offset, 0, offset, height-1);
		}
		//Draw the highlight
		offset = 0;
		for (int i=0; i<activeTab; i++) {
			offset += 24 + widths[i];
		}
		g.setColor(GradientHeading.highlight);
		g.drawLine(offset+1, 1, offset+1, height-3);
		g.drawLine(offset+2, 1, offset+2, height-3);
		int end = offset + 24 + widths[activeTab];
		g.drawLine(offset+2, height-3, end-2, height-3);
		g.drawLine(offset+2, height-2, end-2, height-2);
		g.drawLine(end-1, 1, end-1, height-3);
		g.drawLine(end-2, 1, end-2, height-3);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public synchronized void mousePressed(MouseEvent e) {
		int x = e.getX();
		int offset = 0;
		for (int i=0; i<widths.length; i++) {
			int end = offset + 24 + widths[i];
			if (offset <= x && x <= end) {
				setActiveTab(i);
				break;
			}
			offset = end;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public void addChangeListener(ChangeListener list) {
		listenerList.add(ChangeListener.class, list);
	}

	public void removeChangeListener(ChangeListener list) {
		listenerList.remove(ChangeListener.class, list);
	}
	
	protected void fireStateChanged() {
	     Object[] listeners = listenerList.getListenerList();
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==ChangeListener.class) {
	             ((ChangeListener)listeners[i+1]).stateChanged(new ChangeEvent(this));
	         }
	     }
	 }

}
