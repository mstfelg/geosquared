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
import java.util.Scanner;

import org.geogebra.desktop.geogebra3D.App3D;
import org.geogebra.desktop.gui.app.GeoGebraFrame3D;
import org.geogebra.desktop.util.LoggerD;
import org.geogebra.common.util.debug.Log;
import org.geogebra.common.util.debug.Log.LogDestination;
import org.geogebra.common.GeoGebraConstants;
import org.geogebra.desktop.main.AppPrefs;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class GeoGebra3D extends GeoGebra {

	// Defaults
	static boolean interactiveEh = true;
	static String logLevel = "debug";
	static String modPath;
	static String objCfg;
	static AppPrefs prefs;

	public static void main(String[] args) {
		LoggerD logger = new LoggerD();
		logger.setReading(true);
		Log.setLogger(logger);
		Log.setLogDestination(LogDestination.CONSOLE);
		Log.setLogLevel(logLevel);

		// Argument parsing
    	int c;
    	LongOpt[] longopts = new LongOpt[] {
    	    new LongOpt("config", LongOpt.REQUIRED_ARGUMENT, null, 'c'),
    	    new LongOpt("modules", LongOpt.REQUIRED_ARGUMENT, null, 'm'),
    	    new LongOpt("interactive", LongOpt.NO_ARGUMENT, null, 'i'),
    	    new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
    	    new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V'),
    	    new LongOpt("debug", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
    	};

    	Getopt g = new Getopt("GeoSquared", args, "Vhid:c:m:", longopts);
    	while ((c = g.getopt()) != -1) {
    	    switch (c) {
    	        case 'm':
					modPath = g.getOptarg();
    	        	break;
    	        case 'c':
					objCfg = g.getOptarg();
    	        	break;
    	        case 'i':
					interactiveEh = true;
					Log.setLogLevel("critical");
    	        	break;
    	        case 'd':
					System.out.println("Debug level: " + g.getOptarg());
					Log.setLogLevel(g.getOptarg());
    	        	break;
    	        case 'h':
					usage();
					return;
    	        case 'V':
					version();
					return;
    	        default:
    	        	System.out.println("Unknown option: " + g.getOptarg());
    	        	return;
    	    }
    	}

		// Positional arguments: file names
		String[] fileArgs = new String[5];
		boolean readStdinEh = false;
		for (int i = g.getOptind(); i < args.length; i++) {
			String fileName = args[i];
			readStdinEh = readStdinEh || fileName.equals("-");
			fileArgs[i] = args[i];
		}

		App3D app = appInstance(fileArgs);

		if (!interactiveEh)
			return;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            app.getFullGuiManager()
				.getFullAlgebraInput().sendCmd(line);
        }
        scanner.close();
	}

	public static App3D appInstance(String[] fileArgs) {
		GeoGebraFrame3D wnd = new GeoGebraFrame3D();
		App3D app = new App3D(fileArgs, wnd, new AppPrefs(objCfg, modPath));
		wnd.init(app);
		return app;
	}

	private static void version() {
		System.out.println(""
			+ "GeoSquared v" + GeoGebraConstants.VERSION_STRING + " "
							+ GeoGebraConstants.BUILD_DATE + "\n"
		);
	}

	private static void usage() {
		System.out.println(""
			+ "GeoSquared v" + GeoGebraConstants.VERSION_STRING + "\n"
			+ "Usage: gsq [OPTION] [FILE]\n"
					+ "Start GeoSquared with the specified OPTIONs and open the given FILE.\n"
					+ "  -h --help\t\tprint this message\n"
					+ "  -V --version\t\tprint version\n"
					+ "  -c --config=FILENAME\tread settings FILENAME\n"
					+ "  -m --modules=FILENAME\tread modules from FILENAME\n"
					+ "  -d --debug=LEVEL\tset logging level "
							+ "(EMERGENCY|ALERT|CRITICAL|ERROR|WARN|NOTICE|INFO|DEBUG|TRACE)\n"
		);
	}
}