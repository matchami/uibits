package com.jumbletree.uibits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.jumbletree.uibits.wizard.ConfigurationResult;
import com.jumbletree.uibits.wizard.Direction;
import com.jumbletree.uibits.wizard.TopPanelBorder;
import com.jumbletree.uibits.wizard.Wizard;
import com.jumbletree.uibits.wizard.WizardPage;

public class WizardDialog implements Wizard {

	private static String finishLabel = "Finish";
	private static String nextLabel = "Next >";
	private static String previousLabel = "< Prev";
	
	//Global configuration
	public static void setNextLabel(String label) {
		nextLabel = label;
	}
	
	public static void setPreviousLabel(String label) {
		previousLabel = label;
	}
	
	public static void setFinishLabel(String label) {
		finishLabel = label;
	}

	public class TopPanelLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			int width = size.width;
			Insets insets = parent.getInsets();
			if (icon != null) {
				iconLabel.setBounds(size.width-insets.right-70, insets.top, 70, 70);
				width -= 70;
			}
			
			int labelHeight = headerLabel.getPreferredSize().height;
			headerLabel.setBounds(insets.left, insets.top, width-insets.left-insets.right, labelHeight);
			briefDescLabel.setBounds(insets.left + 20, insets.top + labelHeight + 2, width-insets.left-insets.right-20, labelHeight);
			errorLabel.setBounds(insets.left, size.height-insets.bottom-labelHeight, width-insets.left-insets.right, labelHeight);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(size.width, 70);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(size.width, 70);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

	}

	Icon icon;
	private List<WizardPage> pages;
	Dimension size;
	int currentPage = 0;
	
	
	JLabel iconLabel;
	JLabel headerLabel;
	JLabel briefDescLabel;
	JLabel errorLabel;
	private JButton previousButton;
	private JButton nextButton;
	private JButton cancelButton;
	private JPanel content;
	private boolean cancelled;
	private ImageIcon errorIcon;

	/**
	 * Creates a wizard dialog.  The icon is the icon to be shown at the top right
	 * of the dialog, and may be null
	 * @param icon
	 */
	public WizardDialog(Icon icon) {
		this(icon, new Dimension(450, 350));
	}
	
	/**
	 * Creates a wizard dialog with the given icon and given size.  Note that the actual content area will be the given
	 * size less a 72 pixel header and 72 pixel footer.  The icon must be no more than 70 pixels square.
	 * @param icon
	 * @param size
	 */
	public WizardDialog(Icon icon, Dimension size) {
		this.pages = new ArrayList<WizardPage>();
		this.icon = icon;
		this.size = size;
	}
	
	public <C extends JComponent & WizardPage> void addPage(C page) {
		this.pages.add(page);
	}
	
	public boolean showWizard(JComponent parent) {
		Window window = SwingUtilities.getWindowAncestor(parent);
		JDialog dialog = createDialog(window);
		if (dialog == null) {
			//Nothing to show
			return true;
		}
		UIUtils.centerDialog(window, dialog);
		dialog.setVisible(true);
		//Modal
		return !cancelled;
	}
	
	public boolean showWizard() {
		JDialog dialog = createDialog(new JFrame());
		if (dialog == null) {
			//Nothing to show
			return true;
		}
		UIUtils.centerDialog(dialog);
		dialog.setVisible(true);
		//Modal
		return !cancelled;
	}

	private JDialog createDialog(Window window) {		
		JPanel topPanel = new JPanel(new WizardDialog.TopPanelLayout());
		topPanel.setBorder(BorderFactory.createCompoundBorder(TopPanelBorder.get(), BorderFactory.createEmptyBorder(7, 10, 5, 10)));
		topPanel.setBackground(Color.white);
		if (icon != null) {
			topPanel.add(iconLabel = new JLabel(icon));
		}
		topPanel.add(headerLabel = new JLabel("Header"));
		headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
		topPanel.add(briefDescLabel = new JLabel("Brief Description"));
		topPanel.add(errorLabel = new JLabel(""));
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(Hr90Border.get(SwingConstants.TOP), BorderFactory.createEmptyBorder(7, 10, 7, 10)));

		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(previousButton = new JButton(previousLabel));
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(nextButton = new JButton(nextLabel));
		buttonPanel.add(Box.createHorizontalStrut(15));
		buttonPanel.add(cancelButton = new JButton("Cancel"));
		
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPrevious();
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				doNext();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doFinished(true);	
			}
		});
		
		
		content = new JPanel(new BorderLayout());
		
		content.add(buttonPanel, BorderLayout.SOUTH);
		content.add(topPanel, BorderLayout.NORTH);
		content.add(new JPanel(), BorderLayout.CENTER);
		
		
		JDialog dialog = GradientHeading.createDialog(window, ModalityType.APPLICATION_MODAL, " ", content);
		
		dialog.setSize(size);
		dialog.setResizable(false);
		
		//Make next the default
		dialog.getRootPane().setDefaultButton(nextButton);
		
		//Show the first page
		if (!showPage(0)) {
			return null;
		}
		
		return dialog;
	}

	protected void doFinished(boolean cancelled) {
		this.cancelled = cancelled;
		SwingUtilities.getAncestorOfClass(JDialog.class, briefDescLabel).setVisible(false);
	}

	protected void doPrevious() {
		if (!pages.get(currentPage).previousPressed())
			return;
		
		int requestedPage = currentPage - 1;
		while (!pages.get(requestedPage).isUsable()) {
			--requestedPage;
			if (requestedPage < 0) {
				//Could happen if a configuration fails
				previousButton.setEnabled(false);
				return;
			}
		}

		if (!showPage(requestedPage))
			doPrevious();
	}

	
	protected boolean showPage(int pageIndex) {
		//Configuration
		boolean prevEnabled = false;
		for (int i=pageIndex-1; !prevEnabled && i>=0; i--)
			prevEnabled |= pages.get(i).isUsable();
		
		previousButton.setEnabled(prevEnabled);
		
		boolean notFinished = false;
		for (int i=pageIndex+1; !notFinished && i<pages.size(); i++)
			notFinished |= pages.get(i).isUsable();
		
		nextButton.setText(notFinished ? nextLabel : finishLabel);
		
		WizardPage page = pages.get(pageIndex);
		ConfigurationResult result = page.configureWizard(this, pageIndex > currentPage ? Direction.FORWARD : Direction.BACKWARD);
		
		currentPage = pageIndex;

		if (result == ConfigurationResult.NOT_USABLE) {
			//They've indicated that the page is not usable at the last minute ..
			return false;
		}
		
		content.remove(2);
		content.add((JComponent)page);
		
		content.validate();
		content.repaint();
		return true;
	}

	@Override
	public void setBriefDescription(String text) {
		briefDescLabel.setText(text);
	}

	@Override
	public void setCancelEnabled(boolean enabled) {
		cancelButton.setEnabled(enabled);
	}

	@Override
	public void setErrorMessage(String text) {
		if (text == null || text.equals("")) {
			errorLabel.setText("");
			errorLabel.setIcon(null);
		} else {
			errorLabel.setText(text);
			errorLabel.setIcon(getErrorIcon());
		}
	}

	private Icon getErrorIcon() {
		if (errorIcon == null) {
			errorIcon = new ImageIcon(getClass().getResource("alert.gif"));
		}
		return errorIcon;
	}

	@Override
	public void setHeading(String text) {
		headerLabel.setText(text);
	}

	@Override
	public void setNextEnabled(boolean enabled) {
		nextButton.setEnabled(enabled);
	}

	@Override
	public void setNextText(String text) {
		nextButton.setText(text);
	}

	@Override
	public void setPreviousEnabled(boolean enabled) {
		previousButton.setEnabled(enabled);
	}

	@Override
	public void setPrevoiusText(String text) {
		previousButton.setText(text);
	}

	protected void doNext() {
		if (!pages.get(currentPage).nextPressed())
			return;
		
		int requestedPage = currentPage + 1;
		if (requestedPage == pages.size()) {
			//Will happen only if a configuration fails
			doFinished(false);
			return;
		}
		
		while (!pages.get(requestedPage).isUsable()) {
			++requestedPage;
			if (requestedPage == pages.size()) {
				doFinished(false);
				return;
			}
		}
				
		if (!showPage(requestedPage))
			doNext();
	}

	@Override
	public void clickNext() {
		nextButton.doClick();
	}

	@Override
	public void clickPrevious() {
		previousButton.doClick();
	}

	@Override
	public void setCursor(Cursor cursor) {
		content.setCursor(cursor);
	}
}
