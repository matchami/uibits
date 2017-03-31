package com.jumbletree.uibits;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class GradientHeading extends JPanel implements Colours, Serializable {
	final class BorderBorder implements Border, Serializable {
		private static final long serialVersionUID = 1L;
		private final HashMap<Rectangle, String> commands;
		private final HashMap<Rectangle, ActionListener[]> actions;

		BorderBorder(HashMap<Rectangle, String> commands, HashMap<Rectangle, ActionListener[]> actions) {
			this.commands = commands;
			this.actions = actions;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.translate(x, y);
			paintGradient(g, new Dimension(width, getHeight(g)));
			g.setFont(getComponent(0).getFont());
			FontMetrics fm = g.getFontMetrics();
			g.setColor(text);
			g.drawString(((JLabel)getComponent(0)).getText(), 10, 2 + fm.getAscent());
			
			if (getComponentCount() > 2) {
				//Has actions
				
				int gap = 6;
				int xpos = width - gap/2;
				int topHeight = getComponent(0).getPreferredSize().height;
				for (int i=getComponentCount()-1; i>=2; i--) {
					Component child = getComponent(i);
					if (!(child instanceof JButton))
						continue;
					JButton button = (JButton)getComponent(i);
					Icon icon = button.getIcon();
					int iconWidth = icon.getIconWidth();
					int iconHeight = icon.getIconHeight();
					xpos -= iconWidth;
					int ypos = (topHeight-iconHeight)/2;
					icon.paintIcon(c, g, xpos, ypos);
					Rectangle rectangle = new Rectangle(x+xpos, y+ypos, iconWidth, iconHeight);
					actions.put(rectangle, button.getActionListeners());
					commands.put(rectangle, button.getActionCommand());
					xpos -= gap;
				}
			}
			g.translate(-x, -y);
		}

		private int getHeight(Graphics g) {
			FontMetrics fm = g.getFontMetrics(getComponent(0).getFont());
			return fm.getHeight() + 4;
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(getComponent(0).getPreferredSize().height, 0, 0, 0);
		}
	}

	final class BorderMouseListener extends MouseAdapter implements Serializable {
		private static final long serialVersionUID = 1L;
		private final HashMap<Rectangle, ActionListener[]> actions;
		private final JComponent target;
		private final HashMap<Rectangle, String> commands;
		private Rectangle triggered;

		BorderMouseListener(HashMap<Rectangle, ActionListener[]> actions, JComponent target, HashMap<Rectangle, String> commands) {
			this.actions = actions;
			this.target = target;
			this.commands = commands;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point pt = e.getPoint();
			for (Map.Entry<Rectangle, ActionListener[]> entry : actions.entrySet()) {
				if (entry.getKey().contains(pt)) {
					triggered = entry.getKey();
					return;
				}
			}
			triggered = null;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point pt = e.getPoint();
			for (Map.Entry<Rectangle, ActionListener[]> entry : actions.entrySet()) {
				if (entry.getKey().contains(pt)) {
					if (entry.getKey().equals(triggered)) {
						ActionEvent event = new ActionEvent(target, ActionEvent.ACTION_PERFORMED, commands.get(entry.getKey()));
						for (ActionListener listener : entry.getValue()) {
							listener.actionPerformed(event);
						}
					}
					break;
				}
			}
			triggered = null;
			return;
		}
	}

	public static class ResizeListener extends MouseAdapter implements Serializable {
	
		enum ResizeType {
			HORIZONTAL_W,
			HORIZONTAL_E,
			VERTICAL_N,
			VERTICAL_S,
			NW_CORNER,
			NE_CORNER,
			SW_CORNER,
			SE_CORNER,
			NONE;
		}

		private static final long serialVersionUID = 1L;
		
		private Point trigger;
		private ResizeType triggerType;

		public ResizeListener(DialogHeaderListener listener) {
			listener.installOverride(this);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			JComponent source = (JComponent)e.getSource();
			
			Point pt = e.getPoint();
			Dimension size = source.getSize();
			ResizeType type = isInResize(pt, source.getInsets(), size);
			if (type != ResizeType.NONE) {
				trigger = e.getPoint();
				triggerType = type;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			trigger = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (trigger == null)
				return;
			Point pt = e.getPoint();
			int dx = pt.x - trigger.x, dy = pt.y - trigger.y;

			Window dialog = (Window)SwingUtilities.getAncestorNamed(GRADIENT_HEADING_DIALOG, (Component)e.getSource());
			Dimension size = dialog.getSize();
			Point location = dialog.getLocation();
			int lx = location.x, ly = location.y, sx = size.width, sy = size.height;
			Point newTrigger = e.getPoint();
			int east = 1, west = 2, north = 4, south = 8, where = 0;
			switch (triggerType) {
				case NW_CORNER:
//					dialog.setSize(size.width - dx, size.height - dy);
//					dialog.setLocation(location.x + dx, location.y + dy);
//					return;
//					
					where += north;
				case HORIZONTAL_W:
					where += west;
					break;
				case NE_CORNER:
					where += east;
				case VERTICAL_N:
					where += north;
					break;
				case SE_CORNER:
					where += south;
				case HORIZONTAL_E:
					where += east;
					break;
				case SW_CORNER:
					where += west;
				case VERTICAL_S:
					where += south;
					break;
			}
			if ((where & east) > 0) {
				sx += dx;
			} 
			if ((where & west) > 0) {
				sx -= dx;
				lx += dx;
				newTrigger.x = trigger.x;
			} 
			if ((where & north) > 0) {
				sy -= dy;
				ly += dy;
				newTrigger.y = trigger.y;
			} 
			if ((where & south) > 0) {
				sy += dy;
			}
//			switch (triggerType) {
//				case HORIZONTAL_W:
//					lx -= dx;
//					dx = -dx;
//				case HORIZONTAL_E:
//					sx += dx;
//					break;
//				case VERTICAL_N:
//					ly -= dy;
//					dy = -dy;
//				case VERTICAL_S:
//					sy += dy;
//					break;
//				case NW_CORNER:
//				case NE_CORNER:
//				case SW_CORNER:
//				case SE_CORNER:
//
//			}
//			if (triggerType != ResizeType.VERTICAL)
//				dx = pt.x - trigger.x;
//			if (triggerType != ResizeType.HORIZONTAL)
//				dy = pt.y - trigger.y;
			
			dialog.setSize(sx, sy);
			dialog.setLocation(lx, ly);
			trigger = newTrigger;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			JComponent source = (JComponent)e.getSource();
			
			Point pt = e.getPoint();
			Dimension size = source.getSize();
			ResizeType type = isInResize(pt, source.getInsets(), size);
			
			switch (type) {
				case NW_CORNER:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					break;
				case NE_CORNER:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					break;
				case SW_CORNER:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					break;
				case SE_CORNER:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					break;
				case HORIZONTAL_E:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					break;
				case HORIZONTAL_W:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					break;
				case VERTICAL_N:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
					break;
				case VERTICAL_S:
					source.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
					break;
				default:
					source.setCursor(Cursor.getDefaultCursor());
					break;
			}
		}

		ResizeType isInResize(Point pt, Insets insets, Dimension size) {
			if (pt.x < 2) {
				//Left
				if (pt.y < 2) {
					return ResizeType.NW_CORNER;
				} else if (pt.y > size.height-2) {
					return ResizeType.SW_CORNER;
				}
				return ResizeType.HORIZONTAL_W;
			} 
			if (pt.y < 2) {
				if (pt.x > size.width-2)
					return ResizeType.NE_CORNER;
				return ResizeType.VERTICAL_N;
			}
			
			pt.x -= size.width-8-insets.left;
			pt.y -= size.height-8-insets.top;
			
			boolean inCorner = (pt.x > 0 && pt.y > 0) || (pt.x > 0 && -pt.y <= pt.x) || (pt.y > 0 && -pt.x <= pt.y);
			if (inCorner)
				return ResizeType.SE_CORNER;
			
			pt.x -= 6;
			if (pt.x > 0)
				return ResizeType.HORIZONTAL_E;
			
			pt.y -= 6;
			if (pt.y > 0)
				return ResizeType.VERTICAL_S;
			
			return ResizeType.NONE;
		}

	}

	public static class DialogHeaderListener implements MouseListener, MouseMotionListener, Serializable {

	 	private static final long serialVersionUID = 1L;
		
	 	private Point trigger;

		private ResizeListener resize;

		//These listeners facilitate movement of the content by dragging the header
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		/**
		 * Makes sure that this listener does not try to interpret the same mouseDown as the resize listener
		 * @param resizeListener
		 */
		public void installOverride(ResizeListener resizeListener) {
			this.resize = resizeListener;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//trigger = null;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			JComponent source = (JComponent)e.getSource();
			if (resize != null && resize.isInResize(e.getPoint(), source.getInsets(), source.getSize()) != ResizeListener.ResizeType.NONE)
				return;
			trigger = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			trigger = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (trigger == null)
				return;
			Point pt = e.getPoint();
			int dx = pt.x - trigger.x;
			int dy = pt.y - trigger.y;
			
			Window dialog = (Window)SwingUtilities.getAncestorNamed(GRADIENT_HEADING_DIALOG, (Component)e.getSource());
			Point currentLocation = dialog.getLocation();
			dialog.setLocation(currentLocation.x + dx, currentLocation.y + dy);
			//trigger = pt;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			JComponent source = (JComponent)e.getSource();
			if (resize != null && resize.isInResize(e.getPoint(), source.getInsets(), source.getSize()) != ResizeListener.ResizeType.NONE)
				return;
			source.setCursor(Cursor.getDefaultCursor());
		}

	}

	private static final String GRADIENT_HEADING_DIALOG = "grd.hd.dlg";

	public static JPanel createTitlePanel(String heading, Component content, JButton ... actions) {
		return createTitlePanel(heading, content, null, actions);
	}

	public static JPanel createTitlePanel(String heading, Component content, int padding, JButton ... actions) {
		return createTitlePanel(heading, content, new Insets(padding, padding, padding, padding), actions);
	}
	
	public static JPanel createTitlePanel(String heading, Component content, Insets padding, JButton ... actions) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(padding == null ? ShadowBorder.instance : BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right), ShadowBorder.instance));
		panel.add(new GradientHeading(heading, actions), BorderLayout.NORTH);
		panel.add(content, BorderLayout.CENTER);
		if (content.getName() != null) {
			panel.setName("Heading Panel for " + content.getName());
		}
		return panel;
	}

	/**
	 * Sets the heading.  content must be either the JPanel created by createTitlePanel, or 
	 * the content passed to the same method.
	 * @param gradientPanel
	 * @param heading
	 */
	public static void setHeading(JComponent content, String heading) {
		try {
			//If the argument is a valid gradient heading'd panel then this will work
			
			if (!(content.getComponent(0) instanceof GradientHeading)) {
				content = (JComponent)content.getParent();
			}
			GradientHeading headingPanel = (GradientHeading)content.getComponent(0);
			JLabel label = (JLabel)headingPanel.getComponent(0);
			label.setText(heading);
			label.repaint();
		} catch (Exception e) {
			throw new IllegalArgumentException("Not a gradient heading panel", e);
		}
	}
	
	public static JDialog createDialog(Window parent, ModalityType modality, String heading, Container content) {
		return createDialog(parent, modality, heading, content, false);
	}
	
	public static JDialog createDialog(Window parent, ModalityType modality, String heading, Container content, boolean resizable) {
		return createDialog(parent, modality, heading, content, resizable, (JButton[])null);
	}
	
	public static JDialog createDialog(Window parent, ModalityType modality, String heading, Container content, boolean resizable, JButton ... actions) {
		JPanel panel = actions == null ? createTitlePanel(heading, content) : createTitlePanel(heading, content, actions);
		if (resizable) {
			panel.setBorder(BorderFactory.createCompoundBorder(panel.getBorder(), new ResizeBorder()));
		}
		setupDialogListeners(panel, resizable);
		
		JDialog dialog = new JDialog(parent, modality);
		dialog.setName(GRADIENT_HEADING_DIALOG);
		dialog.setContentPane(panel);
		dialog.setUndecorated(true);
		return dialog;
	}

	public static JFrame createFrame(String heading, Container content, boolean resizable, JButton ... actions) {
		JPanel panel = actions == null ? createTitlePanel(heading, content) : createTitlePanel(heading, content, actions);
		if (resizable) {
			panel.setBorder(BorderFactory.createCompoundBorder(panel.getBorder(), new ResizeBorder()));
		}
		setupDialogListeners(panel, resizable);
		
		JFrame dialog = new JFrame();
		
		dialog.setName(GRADIENT_HEADING_DIALOG);
		dialog.setContentPane(panel);
		dialog.setUndecorated(true);
		return dialog;
	}

	private static void setupDialogListeners(JPanel panel, boolean resizable) {
		GradientHeading headingPanel = (GradientHeading)panel.getComponent(0);
		
		DialogHeaderListener listener = new DialogHeaderListener();
		headingPanel.addMouseListener(listener);
		headingPanel.addMouseMotionListener(listener);
		
		if (resizable) {
			ResizeListener rListener = new ResizeListener(listener);
			panel.addMouseListener(rListener);
			panel.addMouseMotionListener(rListener);
		}
	}
	
	public static void decorateDialog(JDialog dialog, boolean resizable) {
		//Make sure it's not showing
		boolean was = dialog.isVisible();
		dialog.setVisible(false);
		dialog.dispose();
		
		Container content = dialog.getContentPane();
		
		JPanel panel = createTitlePanel(dialog.getTitle(), content);
		setupDialogListeners(panel, resizable);

		dialog.setContentPane(panel);
		dialog.setUndecorated(true);	
		
		if (was)
			dialog.setVisible(was);
		
		System.out.println(dialog.getContentPane());
	}
	
	public Border asBorder(final JComponent target) {
		final HashMap<Rectangle, ActionListener[]> actions = new HashMap<Rectangle, ActionListener[]>();
		final HashMap<Rectangle, String> commands = new HashMap<Rectangle, String>();
		if (getComponentCount() > 2) {
			MouseAdapter mouse = new BorderMouseListener(actions, target, commands);
			target.addMouseListener(mouse);
			target.addMouseMotionListener(mouse);
		}
		return BorderFactory.createCompoundBorder(ShadowBorder.instance, new BorderBorder(commands, actions));
	}
	
	private static final long serialVersionUID = 1L;
	
	public GradientHeading(String heading, JButton ... actions) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
		setOpaque(false);
		JLabel label = new JLabel(heading);
		label.setForeground(text);
		label.setHorizontalAlignment(JLabel.LEADING);
		label.setFont(deriveFont(label));
		add(label);
		
		add(Box.createHorizontalGlue());
		
		for (JButton action : actions) {
			//Remove all borders etc
			action.setBorderPainted(false);
			//action.setBorder(BorderFactory.createEmptyBorder());
			action.setContentAreaFilled(false);
			action.setBorder(BorderFactory.createEmptyBorder());
			
			add(action);
			add(Box.createHorizontalStrut(5));
		}
	}

	protected Font deriveFont(JLabel label) {
		return label.getFont().deriveFont(Font.BOLD, 17f);
	}
	     
 	@Override
 	protected void paintComponent(Graphics g) {
 		Dimension size = getSize();
 		paintGradient(g, size);
 	}

	protected void paintGradient(Graphics g, Dimension size) {
		GradientPaint paint = new GradientPaint(new Point(0, 1), gradientTop, new Point(0, size.height-2), gradientBase);
 		((Graphics2D)g).setPaint(paint);
 		g.fillRect(0, 0, size.width, size.height);
 		g.setColor(highlight);
 		g.drawLine(0, 0, size.width-1, 0);
 		g.setColor(shadow);
 		g.drawLine(0, size.height-1, size.width-1, size.height-1);
	}
	
	public static JButton createDialogCloseButton() {
		final JButton button = new JButton(new ImageIcon(GradientHeading.class.getResource("close.gif")));
		button.setRolloverEnabled(true);
		button.setRolloverIcon(new ImageIcon(GradientHeading.class.getResource("closeOver.gif")));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog parent = (JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, button);
				parent.setVisible(false);
				parent.dispose();
			}
		});
		return button;
	}
	
	public enum ButtonType {
		EDIT,
		DELETE,
		ADD;
	}
	
	public static JButton createButton(ButtonType type, ActionListener listener) {
		final JButton button = new JButton(new ImageIcon(GradientHeading.class.getResource(type.toString().toLowerCase() + ".gif")));
		button.setDisabledIcon(new ImageIcon(GradientHeading.class.getResource(type.toString().toLowerCase() + "Disabled.gif")));
		button.addActionListener(listener);
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}
	
	public static void main(String[] args) {
		JLabel label = new JLabel("Hello World");
		
		JDialog dialog = GradientHeading.createDialog(new JFrame(), ModalityType.APPLICATION_MODAL, "Blah", label, true);
		
		dialog.pack();
		dialog.setVisible(true);
	}
}
