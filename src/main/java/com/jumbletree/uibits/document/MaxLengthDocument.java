package com.jumbletree.uibits.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MaxLengthDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int maxLength;

	public MaxLengthDocument(int size) {
		this.maxLength = size;
	}
	
	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str == null)
			return;

		if (offset > maxLength)
			throw new BadLocationException("Offset is greater than the maximum length (" + String.valueOf(offset) + ">" + String.valueOf(maxLength) + ")", offset);

		int lengthAvail = maxLength - getLength();
		if (lengthAvail == 0)
			return;

		super.insertString(offset, str.substring(0, Math.min(str.length(), lengthAvail)), a);
	}

	

}
