/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.desktop;

import java.awt.Frame;
import java.awt.Toolkit;
import java.net.URL;

import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.main.GeoGebraPreferencesXML;
import org.geogebra.common.util.Util;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.gui.app.GeoGebraFrame;
import org.geogebra.desktop.main.AppD;
import org.geogebra.desktop.main.GeoGebraServer;

public class GeoGebra {
	public static void main(String[] cmdArgs) {
		CommandLineArguments args = new CommandLineArguments(cmdArgs);
		GeoGebraFrame.main(args);
	}
}