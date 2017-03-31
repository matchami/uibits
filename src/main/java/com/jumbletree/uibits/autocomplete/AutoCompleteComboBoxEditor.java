package com.jumbletree.uibits.autocomplete;

import java.awt.Component;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;

public class AutoCompleteComboBoxEditor extends AutoCompleteTextField implements ComboBoxEditor {

	private static final long serialVersionUID = 1L;

	private AutoCompleteComboModel model;
	
	public AutoCompleteComboBoxEditor(AutoCompleteComboModel model, int columns) {
		super(model, columns);
		this.model = model;
	}

	public AutoCompleteComboBoxEditor(AutoCompleteComboModel model, String text, int columns) {
		super(model, text, columns);
		this.model = model;
	}

	public AutoCompleteComboBoxEditor(AutoCompleteComboModel model, String text) {
		super(model, text);
		this.model = model;
	}

	public AutoCompleteComboBoxEditor(AutoCompleteComboModel model) {
		super(model);
		this.model = model;
	}

	@Override
	public Component getEditorComponent() {
		return this;
	}

	@Override
	public void setItem(Object anObject) {
		setText(anObject == null ? "" : anObject.toString());
	}

	@Override
	public Object getItem() {
		return ((AutoCompleteDocument)getDocument()).getModel().getSelectedItem();
	}
	
	public void bindTo(JComboBox box) {
		box.setEditor(this);
		box.setEditable(true);
		
		box.setModel(model);
	}

}
