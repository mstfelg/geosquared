package org.geogebra.desktop.gui.app;

import javax.swing.JFrame;

import org.geogebra.desktop.geogebra3D.App3D;
import org.geogebra.desktop.main.AppD;

/**
 * Frame for geogebra 3D.
 * 
 * @author Mathieu
 *
 */
public class GeoGebraFrame3D extends GeoGebraFrame {

	private static final long serialVersionUID = 1L;
	
	public GeoGebraFrame3D() { super(); }
	public GeoGebraFrame3D(String[] args) { super(args); }
	
	public static synchronized void main(String[] args) {
		createNewWindow3D(args);
	}

	@Override
	protected AppD createApplication(String[] args, JFrame frame) {
		return new App3D(args, frame);
	}

	/**
	 * Create a new 3D geogebra window
	 * 
	 * @param args
	 *            command line arguments
	 * @return new geogebra window
	 */
	public static synchronized GeoGebraFrame createNewWindow3D(String[] args) {
		return createNewWindow(args, new GeoGebraFrame3D());
	}

	@Override
	protected GeoGebraFrame copy() {
		return new GeoGebraFrame3D();
	}

}
