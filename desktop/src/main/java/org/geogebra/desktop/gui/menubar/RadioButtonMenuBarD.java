package org.geogebra.desktop.gui.menubar;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import org.geogebra.common.gui.menubar.MenuInterface;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.main.AppD;

public class RadioButtonMenuBarD extends JMenu implements MenuInterface {

	private static final long serialVersionUID = 1L;

	private AppD app;

	private ButtonGroup buttonGroup;

	/**
	 * @param application application
	 */
	public RadioButtonMenuBarD(App application) {
		super();
		app = (AppD) application;
	}

	/**
	 * @param alistener listener
	 * @param items items
	 * @param actionCommands commands
	 * @param selectedPos selected position
	 * @param changeText whether to translate the options
	 */
	public void addRadioButtonMenuItems(final OptionsMenuD alistener,
			String[] items, String[] actionCommands, int selectedPos,
			boolean changeText) {

		JRadioButtonMenuItem mi;
		buttonGroup = new ButtonGroup();
		// String label;

		for (int i = 0; i < items.length; i++) {
			if ("---".equals(items[i])) {
				addSeparator();
			} else {
				String text = changeText
						? app.getLocalization().getMenu(items[i]) : items[i];
				mi = new JRadioButtonMenuItem(text);
				mi.setFont(app.getFontCanDisplayAwt(text, false, Font.PLAIN,
						app.getGUIFontSize()));
				if (i == selectedPos) {
					mi.setSelected(true);
				}
				mi.setActionCommand(actionCommands[i]);
				mi.addActionListener(alistener);

				buttonGroup.add(mi);
				add(mi);
			}
		}
	}

	/**
	 * @param pos selected position
	 */
	public void setSelected(int pos) {

		if (pos == -1) { // unselect all
			buttonGroup.clearSelection();
		} else {
			Component item = getMenuComponent(pos);
			if (item instanceof JRadioButtonMenuItem) {
				((JRadioButtonMenuItem) item).setSelected(true);
			} else {
				Log.debug("Bad construction of radiobutton menu. "
						+ "All item must be an instance of JRadioButtonMenuItem.");
			}
		}
	}

}
