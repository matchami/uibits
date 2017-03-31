package com.jumbletree.uibits.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class IntegerDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str.equals("-")) {
			if (offset == 0)
				super.insertString(offset, str, a);
			else 
				return;
		}
		try {
			Integer.parseInt(str);
			super.insertString(offset, str, a);
		} catch (NumberFormatException e) {
			StringBuilder builder = new StringBuilder();
			for (int i=0; i<str.length(); i++) {
				char ch = str.charAt(i);
				if ((ch >= '0' && ch <= '9') || (offset == 0 && i == 0 && ch == '-'))
					builder.append(ch);
			}
			if (builder.length() > 0) {
				super.insertString(offset, builder.toString(), a);
			}
		}
	}

}
