package com.jumbletree.uibits;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class OneTimeConfirm extends OneTimeMessage {

	boolean confirmed;

	public OneTimeConfirm(Class<?> pkg, Component parent, String name, String message) {
		super(pkg, parent, name, message);
		//Default state (if they've ticked don't show again) is confirm
		this.confirmed = true;
	}

	public OneTimeConfirm(Component parent, String name, String message) {
		super(parent, name, message);
		this.confirmed = true;
	}

	@Override
	protected void createButtons(final JDialog dialog, JPanel buttons) {
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder());
		panel.add(okButton, BorderLayout.WEST);
		panel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
		panel.add(cancelButton, BorderLayout.EAST);
		buttons.add(panel, BorderLayout.EAST);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OneTimeConfirm.this.confirmed = true;
				dialog.setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OneTimeConfirm.this.confirmed = false;
				dialog.setVisible(false);
			}
		});
	}

	public boolean confirm() {
		super.showMessage();
		return this.confirmed;
	}
}
