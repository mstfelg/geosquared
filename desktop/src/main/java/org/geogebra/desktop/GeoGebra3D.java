/* 
 GeoGebra - Dynamic Mathematics for Everyone
 
 http://www.geogebra.org

 This file is part of GeoGebra.

 This program is free software; you can redistribute it and/or modify it 
 under the terms of the GNU General Public License as published by 
 the Free Software Foundation.

 */

/**
 * GeoGebra Application
 *
 * @author Markus Hohenwarter
 */

package org.geogebra.desktop;

import java.util.logging.Logger;

import org.geogebra.desktop.gui.app.GeoGebraFrame3D;
import org.geogebra.desktop.util.LoggerD;
import org.geogebra.common.util.debug.Log;
import org.geogebra.common.util.debug.Log.LogDestination;

public class GeoGebra3D extends GeoGebra {
	public static void main(String[] cmdArgs) {
		CommandLineArguments args = new CommandLineArguments(cmdArgs);

		if (args != null && !args.containsArg("silent")) {
			LoggerD logger = new LoggerD();
			logger.setReading(true);
			Log.setLogger(logger);
			Log.setLogDestination(LogDestination.CONSOLE);
			if (args.containsArg("logLevel")) {
				Log.setLogLevel(args.getStringValue("logLevel"));
			}
			if (args.containsArg("logFile")) {
				Log.setLogDestination(LogDestination.FILE);
				logger.setLogFileImpl(args.getStringValue("logFile"));
			}
			if (args.containsArg("logShowCaller")) {
				Log.setCallerShown(args.getBooleanValue("logShowCaller", true));
			}
			if (args.containsArg("logShowTime")) {
				LoggerD.setTimeShown(args.getBooleanValue("logShowTime", true));
			}
			if (args.containsArg("logShowLevel")) {
				Log.setLevelShown(args.getBooleanValue("logShowLevel", true));
			}
		}

		GeoGebraFrame3D.main(args);
	}
}