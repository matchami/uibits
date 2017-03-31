package com.jumbletree.uibits.wizard;

import java.awt.Cursor;

public interface Wizard {

	void setNextEnabled(boolean enabled);
	void setPreviousEnabled(boolean enabled);
	void setCancelEnabled(boolean enabled);
	void setNextText(String text);
	void setPrevoiusText(String text);
	void setHeading(String text);
	void setBriefDescription(String text);
	void setErrorMessage(String text);

	void clickNext();
	void clickPrevious();
	
	void setCursor(Cursor cursor);
}
