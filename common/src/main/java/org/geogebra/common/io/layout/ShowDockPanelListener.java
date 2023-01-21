package org.geogebra.common.io.layout;

import org.geogebra.common.gui.layout.DockPanel;

/**
 * Listens to opening new dock panels
 * 
 * @author Florian
 */
public interface ShowDockPanelListener {
	/**
	 * Notify this about opened dock panel
	 * 
	 * @param dp
	 *            opened dock panel
	 */
	void showDockPanel(DockPanel dp);
}
