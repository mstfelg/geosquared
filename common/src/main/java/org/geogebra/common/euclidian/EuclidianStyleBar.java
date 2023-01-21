package org.geogebra.common.euclidian;

import org.geogebra.common.kernel.geos.GeoElement;

/**
 * Style bar for Euclidian view
 */
public interface EuclidianStyleBar {

	// /** tooltip x location for buttons */
	// public static final int TOOLTIP_LOCATION_X = 0;
	// /** tooltip y location for buttons */
	// public static final int TOOLTIP_LOCATION_Y = -25;

	/**
	 * @param mode
	 *            euclidian view mode
	 */
	void setMode(int mode);

	/**
	 * Update tooltips
	 */
	void setLabels();

	/**
	 * Restore default properties
	 */
	void restoreDefaultGeo();

	/**
	 * Updates the state of the stylebar buttons and the defaultGeo field.
	 */
	void updateStyleBar();

	/**
	 * Update capture button
	 * 
	 * @param mode
	 *            euclidian view mode
	 */
	void updateButtonPointCapture(int mode);

	/**
	 * update the style bar if the geo is part of the active geo list
	 * 
	 * @param geo
	 *            geo
	 */
	void updateVisualStyle(GeoElement geo);

	/**
	 * @return index of selected point capturing mode
	 */
	int getPointCaptureSelectedIndex();

	void updateGUI();

	void hidePopups();

	/**
	 * reset "first paint", so that on first paint the GUI will be updated
	 */
	void resetFirstPaint();

	void reinit();

	void setVisible(boolean visible);
	
	boolean isVisible();
}
