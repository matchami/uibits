package com.jumbletree.uibits.wizard;


public interface WizardPage {

	/**
	 * Determines if the page should be shown as part of the wizard, or skipped
	 * @return true to show the page or false to skip it
	 */
	boolean isUsable();

	/**
	 * Called when the user presses the previous button, prior to acting on the request
	 * @return true if the wizard should continue as normal, false if the wizard should
	 * NOT process the action
	 */
	boolean previousPressed();

	/**
	 * Called when the user presses the next button, prior to acting on the request
	 * @return true if the wizard should continue as normal, false if the wizard should
	 * NOT process the action
	 */
	boolean nextPressed();

	/**
	 * Called immediately before this page is to be shown to the user.  Direction
	 * indicates whether the user clicked next (forward) or previous (backward) 
	 * to get here.
	 */
	ConfigurationResult configureWizard(Wizard wizard, Direction direction);

}
