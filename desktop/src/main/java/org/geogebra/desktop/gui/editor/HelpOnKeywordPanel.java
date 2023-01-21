/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.
This code has been written initially for Scilab (http://www.scilab.org/).

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.desktop.gui.editor;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;

import org.geogebra.common.main.Localization;
import org.geogebra.desktop.main.AppD;

/**
 * Class to create a textarea containing help on command. This is a singleton
 * class.
 * 
 * @author Calixte DENIZET
 * 
 */
public class HelpOnKeywordPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static HelpOnKeywordPanel instance;
	private JTextArea textarea;

	/**
	 * Default constructor
	 */
	private HelpOnKeywordPanel() {
		super();
		setLayout(new BorderLayout());
		textarea = new JTextArea();
		LookAndFeel.installBorder(this, "ToolTip.border");
		LookAndFeel.installColors(textarea, "ToolTip.background",
				"ToolTip.foreground");
		textarea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		textarea.setEditable(false);
		add(textarea);
	}

	/**
	 * Returns the unique instance
	 * 
	 * @param app
	 *            the application
	 * @param command
	 *            the command
	 * @return the instance
	 */
	public static HelpOnKeywordPanel getInstance(AppD app, String command) {
		if (instance == null) {
			instance = new HelpOnKeywordPanel();
		}

		instance.init(app, command);

		return instance;
	}

	private void init(AppD app, String command) {
		String help = app.getLocalization().getCommand(
				app.getReverseCommand(command) + Localization.syntaxStr);
		String[] lines = help.split("\n");
		int cols = 1;
		for (int i = 0; i < lines.length; i++) {
			cols = Math.max(cols, lines[i].length());
		}
		textarea.setText(help);
		textarea.setFont(app.getPlainFont());
		textarea.setRows(lines.length);
		textarea.setColumns(cols);
		setPreferredSize(textarea.getMinimumSize());
		setSize(getPreferredSize());

	}
}
