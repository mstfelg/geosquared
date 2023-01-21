package org.geogebra.common.euclidian;

public class ScreenReaderSilent implements ScreenReaderAdapter {

	/**
	 * Singleton instance
	 */
	public static final ScreenReaderSilent INSTANCE = new ScreenReaderSilent();

	@Override
	public void readText(String text) {
		// silent
	}

	@Override
	public void readDelayed(String text) {
		// silent
	}
}
