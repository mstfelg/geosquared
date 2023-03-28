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
import java.util.Scanner;

import org.geogebra.desktop.gui.app.GeoGebraFrame3D;
import org.geogebra.desktop.util.LoggerD;
import org.geogebra.common.util.debug.Log;
import org.geogebra.common.util.debug.Log.LogDestination;
import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.GeoGebraConstants.Platform;
import org.geogebra.desktop.gui.inputbar.AlgebraInputD;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class GeoGebra3D extends GeoGebra {
	
	static boolean interactiveEh = true;

	public static void main(String[] args) {
    	int c;
    	String arg;
    	LongOpt[] longopts = new LongOpt[] {
    	    new LongOpt("interactive", LongOpt.NO_ARGUMENT, null, 'i'),
    	    new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
    	    new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V'),
    	    new LongOpt("debug", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
    	};

    	Getopt g = new Getopt("GeoSquared", args, "vhid:", longopts);
    	while ((c = g.getopt()) != -1) {
    	    switch (c) {
    	        case 'i':
					interactiveEh = true;
    	        	break;
    	        case 'd':
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

		// App breaks if removed
		LoggerD logger = new LoggerD();
		logger.setReading(true);
		Log.setLogger(logger);
		Log.setLogDestination(LogDestination.CONSOLE);

		// Positional arguments: file names
		CommandLineArguments clArgs = new CommandLineArguments(null);
		boolean readStdinEh = false;
		for (int i = g.getOptind(); i < args.length; i++) {
			String fileName = args[i];
			readStdinEh = readStdinEh || fileName.equals("-");
			clArgs.addFile(fileName);
		}
	
		GeoGebraFrame3D wnd = new GeoGebraFrame3D(clArgs);

		if (!interactiveEh)
			return;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            wnd.getApplication().getFullGuiManager()
				.getFullAlgebraInput().sendCmd(line);
        }
        scanner.close();
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
			+ "Usage: java -jar geogebra.jar [OPTION] [FILE]\n"
					+ "Start GeoSquared with the specified OPTIONs and open the given FILE.\n"
					+ "  --help\t\tprint this message\n"
					+ "  --v\t\tprint version\n"
					+ "  --config=PATH|FILENAME\tread settings from another file\n"
					+ "  --clean\tRun with default config\n"
					+ "  --debug=LEVEL\tset logging level "
							+ "(EMERGENCY|ALERT|CRITICAL|ERROR|WARN|NOTICE|INFO|DEBUG|TRACE)\n"
		);
	}

}
