package com.jumbletree.uibits;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class OneTimeMessage {

	private static final String KEY_START = "OneTimeMessage.";

	public static void resetAll(Component parent) {
		Preferences prefs = Preferences.userNodeForPackage(parent.getClass());
		try {
			for (String key : prefs.keys()) {
				if (key.startsWith(KEY_START)) {
					prefs.remove(key);
				}
			}
		} catch (BackingStoreException e) {
		}
	}

	public static void reset(Component parent, String key) {
		Preferences prefs = Preferences.userNodeForPackage(parent.getClass());
		prefs.remove(key);
	}

	private String key;
	private String message;
	private Class<?> keyClass;
	private Component parentComp;
	private JCheckBox hideButton;

	public OneTimeMessage(Component parent, String name, String message) {
		this(parent.getClass(), parent, name, message);
	} 
	
	
	public OneTimeMessage(Class<?> pkg, Component parent, String name, String message) {
		this.keyClass = pkg;
		this.parentComp = parent;
		this.key = KEY_START + name;
		this.message = message;
	}
	
	public void showMessage() {
		Preferences prefs = Preferences.userNodeForPackage(keyClass);
		if (prefs.get(key, null) != null)
			return;
		
		Window window = SwingUtilities.getWindowAncestor(parentComp);
		JDialog dialog = createDialog(window);
		dialog.pack();
		UIUtils.centerDialog(window, dialog);
		dialog.setVisible(true);
		
		//It's model!
		
		if (hideButton.isSelected()) {
			prefs.put(key, "hide");
		}
		
		dialog.dispose();
	}

	private JDialog createDialog(Window window) {
		JPanel content = new JPanel(new BorderLayout());

		final JDialog dialog = GradientHeading.createDialog(window, ModalityType.APPLICATION_MODAL, " ", content);

		JPanel buttons = new JPanel(new BorderLayout());
		buttons.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		buttons.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		
		createButtons(dialog, buttons);

		hideButton = new JCheckBox("Don't show me this message again");
		buttons.add(hideButton, BorderLayout.WEST);
		
		content.add(buttons, BorderLayout.SOUTH);
		
		
		JLabel message = new JLabel(this.message);
		message.setBorder(BorderFactory.createCompoundBorder(Hr90Border.get(SwingConstants.BOTTOM), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		content.add(message, BorderLayout.CENTER);
		
		return dialog;
	}

	protected void createButtons(final JDialog dialog, JPanel buttons) {
		JButton okButton = new JButton("OK");
		buttons.add(okButton, BorderLayout.EAST);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
	}
}
