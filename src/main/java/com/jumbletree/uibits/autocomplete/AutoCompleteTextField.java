package com.jumbletree.uibits.autocomplete;

import javax.swing.JTextField;

public class AutoCompleteTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	public AutoCompleteTextField(AutoCompleteModel model) {
		super();
		setDocument(new AutoCompleteDocument(model, this));
	}

	public AutoCompleteTextField(AutoCompleteModel model, int columns) {
		super(columns);
		setDocument(new AutoCompleteDocument(model, this));
	}

	public AutoCompleteTextField(AutoCompleteModel model, String text, int columns) {
		super(text, columns);
		setDocument(new AutoCompleteDocument(model, this));
	}

	public AutoCompleteTextField(AutoCompleteModel model, String text) {
		super(text);
		setDocument(new AutoCompleteDocument(model, this));
	}

}
