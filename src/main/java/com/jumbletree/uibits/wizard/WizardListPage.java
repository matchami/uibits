package com.jumbletree.uibits.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.jumbletree.uibits.UIUtils;

public class WizardListPage<T> extends JPanel implements WizardPage {

	private static final long serialVersionUID = 1L;

	public static class ListItem<T> {
		private String text;
		private String description;
		private T object;
		public ListItem(String text, String description, T object) {
			this.text = text;
			this.description = description;
			this.object = object;
		}
		public String getText() {
			return text;
		}
		public String getDescription() {
			return description;
		}
		public T getObject() {
			return object;
		}
	}

	public static class ScrollablePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = 1L;

		public ScrollablePanel(LayoutManager layout) {
			super(layout);
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return orientation == SwingConstants.HORIZONTAL ? visibleRect.width : visibleRect.height / 5;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return orientation == SwingConstants.HORIZONTAL ? visibleRect.width : visibleRect.height / 20;
		}
	}

	AtomicReference<T> result;
	private String heading;
	private String description;
	private JEditorPane blurb;
	private ScrollablePanel inside;

	public WizardListPage(String heading, String description, AtomicReference<T> result, String blurb, @SuppressWarnings("unchecked") ListItem<T> ... items) {
		this(heading, description, result);
		setup(blurb, items);
	}

	/**
	 * Constructor without setting up the list.  setup() must be called prior to display
	 * @param result
	 */
	public WizardListPage(String heading, String description, AtomicReference<T> result) {
		this.result = result;
		this.heading = heading;
		this.description = description;
	}

	public void setup(String blurb, @SuppressWarnings("unchecked") ListItem<T> ... items) {
		setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));	
		inside = new ScrollablePanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		
		JScrollPane pane = new JScrollPane(inside);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setBorder(BorderFactory.createEmptyBorder());
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		
		inside.add(this.blurb = new JEditorPane("text/html", blurb == null ? "" : blurb), c);
		UIUtils.setEditorPaneBackground(this.blurb, getBackground());
		ButtonGroup group = new ButtonGroup();

		boolean first = true;
		for (ListItem<T> item : items) {
			createButton(inside, item, group, first, c);
			if (first) {
				first = false;
				result.set(item.getObject());
			}
		}
	}

	private void createButton(ScrollablePanel inside, final ListItem<T> item, ButtonGroup group, boolean selected, GridBagConstraints c) {
		final JRadioButton button = new JRadioButton(item.getText(), selected);
		button.setFont(button.getFont().deriveFont(Font.BOLD));
		inside.add(button, c);
		group.add(button);
		button.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result.set(item.getObject());
			}
		});

		JEditorPane pane = new JEditorPane("text/html", "<html>" + item.getDescription() + "</html>");
		UIUtils.setEditorPaneBackground(pane, getBackground());
		pane.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				button.setSelected(true);
				result.set(item.getObject());
			}
		});
		inside.add(pane, c);
	}

	@Override
	public ConfigurationResult configureWizard(Wizard wizard, Direction direction) {
		wizard.setHeading(heading);
		wizard.setBriefDescription(description);
		return ConfigurationResult.CONFIGURED;
	}

	@Override
	public boolean nextPressed() {
		return true;
	}

	@Override
	public boolean previousPressed() {
		return true;
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	protected void setBlurb(String string) {
		this.blurb.setText(string);
	}

	public void setEnabled(int index, boolean enabled) {
		index = index * 2 + 1;
		inside.getComponent(index).setEnabled(enabled);
		inside.getComponent(index+1).setEnabled(enabled);
		if (!enabled) {
			if (((JRadioButton)inside.getComponent(index)).isSelected()) {
				//Try and select the next one
				boolean success = false;
				for (int i=index+2; i<inside.getComponentCount(); i+=2) {
					if (inside.getComponent(i).isEnabled()) {
						((JRadioButton)inside.getComponent(i)).doClick();
						success = true;
						break;
					}
				}
				if (!success) {
					//Going forward didn't work, so go backwards
					for (int i=index-2; i>0; i-=2) {
						if (inside.getComponent(i).isEnabled()) {
							((JRadioButton)inside.getComponent(i)).doClick();
							success = true;
							break;
						}
					}
					if (!success) {
						//Just unselect it
						((JRadioButton)inside.getComponent(index)).setSelected(false);
					}
				}
			}
		}
	}
}