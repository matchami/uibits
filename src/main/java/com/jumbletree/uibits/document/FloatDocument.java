package com.jumbletree.uibits.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FloatDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (getLength() == 0 && str.length() == 1) {
			switch (str.charAt(0)) {
				case '.':
				case '-':
					super.insertString(offset, str, a);
					return;
			}
		}
		String whatItWillBe = getText(0, offset) + str + (offset < getLength() ? getText(offset, getLength()) : "");
		
		try {
			Float.parseFloat(whatItWillBe);
			super.insertString(offset, str, a);
		} catch (NumberFormatException e) {
			//Insert failed - track back from the end until it doesn't
			String substr = str;
			for (int i=str.length()-1; i>0; i--) {
				substr = str.substring(0, i);
				whatItWillBe = getText(0, offset) + substr + (offset < getLength() ? getText(offset, getLength()) : "");
				try {
					Float.parseFloat(whatItWillBe);
					super.insertString(offset, str, a);
					break;
				} catch (NumberFormatException _e) {
					//fail...keep on trying!
				}
			}
		}
	}
}
