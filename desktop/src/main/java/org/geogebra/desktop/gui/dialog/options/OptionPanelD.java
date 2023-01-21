package org.geogebra.desktop.gui.dialog.options;

import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Interface for option panels
 * 
 * @author mathieu
 *
 */
public interface OptionPanelD {
	/**
	 * Update the GUI to take care of new settings which were applied.
	 */
	public void updateGUI();

	/**
	 * JPanel method
	 */
	public void revalidate();

	/**
	 * JPanel method
	 * 
	 * @param border
	 *            border
	 */
	public void setBorder(Border border);

	/**
	 * @return the wrapped JPanel for Desktop
	 */
	public JPanel getWrappedPanel();

	/**
	 * apply modifications. should be called when the panel is hidden.
	 */
	public void applyModifications();

	/**
	 * update the font
	 */
	public void updateFont();

	/**
	 * set if this panel is selected or not (used for update)
	 * 
	 * @param flag
	 *            selected or not
	 */
	public void setSelected(boolean flag);

}
