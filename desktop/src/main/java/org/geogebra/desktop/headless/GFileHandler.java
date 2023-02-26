package org.geogebra.desktop.headless;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.geogebra.common.jre.headless.AppDI;
import org.geogebra.common.jre.io.MyXMLioJre;
import org.geogebra.common.jre.util.Base64;
import org.geogebra.common.main.App;
import org.geogebra.common.main.MyError;
import org.geogebra.common.util.Charsets;

public class GFileHandler {
	/**
	 * @param app
	 *            app
	 * @param is
	 *            stream
	 * @param isMacroFile
	 *            macro?
	 * @return whether successfully loaded
	 * @throws Exception
	 *             for invalid XML; MyErrors are ignored
	 */

	public static boolean loadXML(App app, InputStream is, boolean isMacroFile) throws Exception {
		return loadXML(app, is, isMacroFile, false);
	}
	public static boolean loadXML(App app, InputStream is, boolean isMacroFile, boolean isGsq)
			throws Exception {
		try {
			if (!isMacroFile) {
				app.setMoveMode();
			}

			// store current location of the window
			((AppDI) app).storeFrameCenter();

			// make sure objects are displayed in the correct View
			app.setActiveView(App.VIEW_EUCLIDIAN);

			// reset unique id (for old files, in case they don't have one)
			app.resetUniqueId();

			BufferedInputStream bis = new BufferedInputStream(is);
			if (isGsq)
				((MyXMLioJre) app.getXMLio()).readFromInputStream(bis);
			else
				((MyXMLioJre) app.getXMLio()).readZipFromInputStream(bis, isMacroFile);

			is.close();
			bis.close();

			if (!isMacroFile) {
				app.getKernel().initUndoInfo();
				app.setSaved();
				app.resetCurrentFile();
			}

			// command list may have changed due to macros
			app.updateCommandDictionary();

			return true;
		} catch (MyError err) {
			app.resetCurrentFile();
			app.showError(err);
			return false;
		}
	}
}
