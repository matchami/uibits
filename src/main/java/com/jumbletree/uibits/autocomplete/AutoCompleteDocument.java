package com.jumbletree.uibits.autocomplete;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class AutoCompleteDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private AutoCompleteModel driver;
	private JTextField target;

	public AutoCompleteDocument(AutoCompleteModel driver, JTextField target) {
		this.driver = driver;
		this.target = target;
	}
	
	@Override
	public void replace(int i, int j, String s, AttributeSet attributeset) throws BadLocationException {
		super.remove(i, j);
		insertString(i, s, attributeset);
	}

	@Override
	public void insertString(int i, String s, AttributeSet attributeset) throws BadLocationException {
		if (s == null || "".equals(s))
			return;
		String enteredText = getText(0, i);
		String newText = driver.getMatch(enteredText + s);
		int j = (i + s.length()) - 1;
		if (driver.isStrict() && newText == null) {
			newText = driver.getMatch(enteredText);
			j--;
		} else if (!driver.isStrict() && newText == null) {
			super.insertString(i, s, attributeset);
			return;
		}
		super.remove(0, getLength());
		super.insertString(0, newText, attributeset);
		target.setSelectionStart(j + 1);
		target.setSelectionEnd(getLength());
	}

	@Override
	public void remove(int i, int j) throws BadLocationException {
		int k = target.getSelectionStart();
		if (k > 0)
			k--;
		String s = driver.getMatch(getText(0, k));
		if (!driver.isStrict() && s == null) {
			super.remove(i, j);
		} else {
			super.remove(0, getLength());
			super.insertString(0, s, null);
		}
		try {
			target.setSelectionStart(k);
			target.setSelectionEnd(getLength());
		} catch (Exception exception) {
		}
	}

	public AutoCompleteModel getModel() {
		return driver;
	}

}
