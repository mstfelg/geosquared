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
public class GeoGebraFrame3D extends AppFrame {

	private static final long serialVersionUID = 1L;
	
	public GeoGebraFrame3D() { super(); }
	
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
	public static synchronized AppFrame createNewWindow3D(String[] args) {
		return createNewWindow(args, new GeoGebraFrame3D());
	}

	@Override
	protected AppFrame copy() {
		return new GeoGebraFrame3D();
	}

}
