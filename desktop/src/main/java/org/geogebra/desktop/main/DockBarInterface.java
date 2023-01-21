package org.geogebra.desktop.main;

/**
 * move DockBar out of App so that minimal applets work
 * 
 * @author michael
 *
 */
public interface DockBarInterface {

	boolean isEastOrientation();

	void setVisible(boolean b);

	void setLabels();

	void showPopup();

	boolean isShowButtonBar();

	void setEastOrientation(boolean selected);

	void setShowButtonBar(boolean selected);

	void hidePopup();

}
