package org.geogebra.desktop.geogebra3D.gui.dialogs;

import org.geogebra.common.gui.InputHandler;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.kernelND.GeoDirectionND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.desktop.gui.dialog.InputDialogRadiusD;
import org.geogebra.desktop.main.AppD;

/**
 * Dialog for 3D circle with center, direction and radius
 */
public class InputDialogCirclePointDirectionRadius extends InputDialogRadiusD {

	private final GeoPointND geoPoint;

	private final GeoDirectionND forAxis;

	/**
	 * @param app application
	 * @param title title
	 * @param handler input handler
	 * @param point center
	 * @param forAxis direction
	 * @param kernel kernel
	 */
	public InputDialogCirclePointDirectionRadius(AppD app, String title,
			InputHandler handler, GeoPointND point, GeoDirectionND forAxis,
			Kernel kernel) {
		super(app, title, handler, kernel);

		geoPoint = point;
		this.forAxis = forAxis;

	}

	@Override
	protected GeoElement createOutput(GeoNumberValue num) {

		return kernel.getManager3D().circle3D(null, geoPoint, num, forAxis);
	}

}
