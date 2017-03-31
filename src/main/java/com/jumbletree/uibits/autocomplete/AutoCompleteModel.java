package com.jumbletree.uibits.autocomplete;

public interface AutoCompleteModel {

	/**
	 * Whether this model allows entry ONLY from the list of matches (strict) or also allows other content
	 * @return
	 */
	boolean isStrict();

	/**
	 * A match for the given text, or null if no such match exists
	 * @param text
	 * @return
	 */
	String getMatch(String text);

	/**
	 * Returns the object that is currently chosen.
	 * If the model is strict, then this method will return null if the current text in the component doesn't match
	 * any appropriate value.  If the model is not strict, then the method will return the currently entered text as
	 * a String if it does not match any appropriate value.
	 * @return
	 */
	Object getSelectedItem();

}
