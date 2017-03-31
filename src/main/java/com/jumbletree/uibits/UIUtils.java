package com.jumbletree.uibits;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JEditorPane;
import javax.swing.UIDefaults;

public class UIUtils {

	/**
	 * Centers the dialog, relative to the parent window.
	 * @param parentWindow
	 * @param dialog
	 */
	public static void centerDialog(Window parentWindow, Window dialog) {
		Rectangle parentBounds = parentWindow.getBounds();
		Dimension size = dialog.getSize();
		Point pt = new Point(parentBounds.x + (parentBounds.width-size.width)/2, parentBounds.y + (parentBounds.height - size.height)/3);
	
		dialog.setLocation(pt);
	}

	/**
	 * Centers the dialog, relative to the default screen device.
	 * @param parentWindow
	 * @param dialog
	 */
	public static void centerDialog(Window dialog) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle parentBounds = env.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		Dimension size = dialog.getSize();
		Point pt = new Point(parentBounds.x + (parentBounds.width-size.width)/2, parentBounds.y + (parentBounds.height - size.height)/3);
	
		dialog.setLocation(pt);
	}
	
	/**
	 * Fixes a bug in JDK: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6789980
	 */
	public static void setEditorPaneBackground(JEditorPane pane, Color bgColor) {
		UIDefaults defaults = new UIDefaults();
    defaults.put("EditorPane[Enabled].backgroundPainter", bgColor);
    defaults.put("EditorPane[Disabled].backgroundPainter", bgColor);
    pane.putClientProperty("Nimbus.Overrides", defaults);
    pane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
    pane.setBackground(bgColor);
	}

}
