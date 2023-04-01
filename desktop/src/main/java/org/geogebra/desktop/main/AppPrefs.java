/* 
 GeoGebra - Dynamic Mathematics for Everyone
 http://www.geogebra.org

 This file is part of GeoGebra.

 This program is free software; you can redistribute it and/or modify it 
 under the terms of the GNU General Public License as published by 
 the Free Software Foundation.
 
 */

package org.geogebra.desktop.main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import org.geogebra.common.euclidian3D.Input3DConstants;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.util.UtilD;

/**
 * Stores user settings and options as preferences.
 * 
 * @author Markus Hohenwarter
 * @version May 16, 2007
 */

public class AppPrefs {
	public String configHome;
	public String layoutCfg;
	public String objCfg;

	public String dataHome;
	public String modPath;
	public static String propData;

	// Java Preferences
	public final static String PREF_ROOT = "/gsq";
	private Preferences ggbPrefs;

	// Picture export dialog
	public static String EXPORT_PIC_FORMAT = "export_pic_format";
	public static String EXPORT_PIC_DPI = "export_pic_dpi";
	// Print preview dialog
	public static String PRINT_ORIENTATION = "print_orientation";
	public static String PRINT_SHOW_SCALE = "print_show_scale";
	public static String PRINT_SHOW_SCALE2 = "print_show_scale_2";
	// Meta data
	public static String AUTHOR = "author";
	public static String VERSION = "version";
	public static String INPUT_3D = "input_3d";

	protected static final String XML_FACTORY_DEFAULT = "xml_factory_default";
	protected static final String TOOLS_FILE_GGT = "tools_file_ggt";
	protected static final String APP_LOCALE = "app_locale";
	protected static final String APP_CURRENT_IMAGE_PATH = "app_current_image_path";
	protected static final String APP_FILE_ = "app_file_";

	private static AppPrefs singleton;
	
	public AppPrefs() {
		this(null, null);
	}

	public AppPrefs(String cfg, String mod) {
		if (mod != null && !mod.equals(""))
			modPath = mod;
		if (cfg != null && !cfg.equals(""))
			objCfg = cfg;
		if (configHome == null)
			configHome = Paths.get(
					System.getenv("XDG_CONFIG_HOME"), "gsq"
				).toString();
		if (configHome == null)
			configHome = Paths.get(
					System.getProperty("user.home"), ".config/gsq/"
				).toString();
		layoutCfg = Paths.get(configHome, "layout.xml").toString();
		if (objCfg == null)
			objCfg = Paths.get(configHome, "defaults.xml").toString();

		if (dataHome == null)
			dataHome = Paths.get(
					System.getenv("XDG_DATA_HOME"), "gsq"
				).toString();
		if (dataHome == null)
			dataHome = Paths.get(
				System.getProperty("user.home"), ".local/share/gsq/"
			).toString();
		if (modPath == null)
			modPath = Paths.get(dataHome, "modules/").toString();
		propData = Paths.get(dataHome, "gsq.properties").toString();

		Log.debug(""
			+ "Reading config from: " + configHome
			+ "\n Reading modules from: " + modPath
			);

		try {
			ggbPrefs = Preferences.userRoot().node(PREF_ROOT);
		} catch (Exception e) {
			// Thrown when running unsigned JAR
			ggbPrefs = null;
		}
	}

	/** Set in geogebra.gui.app.GeoGebraFrame before first call to getPref() */
	public static void setPropertyFileName(String pfname) {
		propData = pfname;
	}

	/**
	 * @return preferences singleton
	 */
	public synchronized static AppPrefs getPref() {
		if (singleton == null) {
			singleton = new AppPrefs();
		}
		return singleton;
	}

	public String loadPreference(String key, String defaultValue) {
		if (ggbPrefs == null)
			return null;
		return ggbPrefs.get(key, defaultValue);
	}

	/**
	 * Save preference value
	 * @param key key
	 * @param value value
	 */
	public void savePreference(String key, String value) {
		if (key != null && value != null) {
			ggbPrefs.put(key, value);
		}
	}

	/**
	 * set 3D input used
	 * 
	 * @param type
	 *            type
	 */
	public void setInput3DType(String type) {
		getPref().savePreference(AppPrefs.INPUT_3D, type);
	}

	/**
	 * 
	 * @return 3D input type currently used, "none" if none
	 */
	public String getInput3DType() {
		return getPref().loadPreference(AppPrefs.INPUT_3D,
				Input3DConstants.PREFS_NONE);
	}

	/**
	 * @return the path of the first file in the file list
	 */
	public File getDefaultFilePath() {
		if (getPref() == null)
		return null;
		String fpath = getPref().loadPreference(APP_FILE_ + "1", "");
		if (fpath == null)
			return null;
		File file = new File(fpath);
		if (file == null || ! file.exists())
			return null;
		return file.getParentFile();
	}

	/**
	 * Returns the default image path
	 * 
	 * @return the image path
	 */
	public File getDefaultImagePath() {
		// image path
		String pathName = getPref().loadPreference(APP_CURRENT_IMAGE_PATH,
				null);
		if (pathName != null) {
			return new File(pathName);
		}
		return null;
	}

	/**
	 * Saves the currently set locale.
	 */
	public void saveDefaultImagePath(File imgPath) {
		try {
			if (imgPath != null) {
				getPref().savePreference(APP_CURRENT_IMAGE_PATH,
						imgPath.getCanonicalPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the default locale
	 * 
	 * @return the locale
	 */
	public Locale getDefaultLocale() {
		// language
		String strLocale = getPref().loadPreference(APP_LOCALE, null);
		if (strLocale != null) {
			return AppD.getLocale(strLocale);
		}
		return null;
	}

	/**
	 * Saves the currently set locale.
	 */
	public void saveDefaultLocale(Locale locale) {
		// save locale (language)
		getPref().savePreference(APP_LOCALE, locale.toString());
	}

	/**
	 * Inits factory default XML if there are no old preferences or if the
	 * version number changed. The default XML is the preferences XML of this
	 * virgin application.
	 */
	public void initDefaultXML(AppD app) {
	}

	/**
	 * Saves preferences by taking the application's current values.
	 */
	public void saveXMLPreferences(AppD app) {
		String userPrefsXML = app.getPreferencesXML();
		StringBuilder sb = new StringBuilder();
		app.getKernel().getConstruction().getConstructionDefaults()
				.getDefaultsXML(sb);
		String objectPrefsXML = sb.toString();

		// make sure folder exists
		UtilD.mkdirs(new File(configHome));

		UtilD.writeStringToFile(userPrefsXML, layoutCfg);
		UtilD.writeStringToFile(objectPrefsXML, objCfg);
		return;
	}

	/**
	 * @return XML preferences
	 */
	public String getXMLPreferences() {
		return null;
	}

	/**
	 * Loads XML preferences (empty construction with GUI and kernel settings)
	 * and sets application accordingly. This method clears the current
	 * construction in the application. Note: the XML string used is the same as
	 * for ggb files.
	 */
	public void applyTo(AppD app) {
		app.setWaitCursor();

		String layoutFile = UtilD.loadFileIntoString(layoutCfg);
		if (layoutFile != null) {
			app.setXML(layoutFile, false);
		}

		String objFile = UtilD.loadFileIntoString(objCfg);
		if (objFile != null) {
			boolean eda = app.getKernel().getElementDefaultAllowed();
			app.getKernel().setElementDefaultAllowed(true);
			app.getKernel().getConstruction().setIgnoringNewTypes(true);
			app.setXML(objFile, false);
			app.getKernel().getConstruction().setIgnoringNewTypes(false);
			app.getKernel().setElementDefaultAllowed(eda);
		}

		if (modPath != null) {
			Path root = Paths.get(modPath);
			try (Stream<Path> stream = Files.walk(root, 3)) {
				stream.forEach(p -> {
					if (Files.isRegularFile(p) && p.toString().endsWith(".gsq"))
							app.loadModule(p.toString());
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		app.updateToolBar();
		app.setDefaultCursor();
	}

	/**
	 * Clears all user preferences.
	 */
	public void clearPreferences(App app) {
		try {
			ggbPrefs.clear();
			ggbPrefs.flush();
		} catch (Exception e) {
			Log.debug(e + "");
		}
	}
}