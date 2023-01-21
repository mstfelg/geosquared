package org.geogebra.common.geogebra3D.kernel3D.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdLocus;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Locus command
 *
 */
public class CmdLocus3D extends CmdLocus {
	/**
	 * @param kernel
	 *            Kernel
	 */
	public CmdLocus3D(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected GeoElement locus(String label, GeoPointND p1, GeoPointND p2) {

		if (p1.isGeoElement3D() || p2.isGeoElement3D()) {
			return kernel.getManager3D().locus3D(label, p1, p2);
		}

		return super.locus(label, p1, p2);
	}

	@Override
	protected GeoElement locus(String label, GeoPointND p, GeoNumeric slider) {
		if (p.isGeoElement3D()) {
			return kernel.getManager3D().locus3D(label, p, slider);
		}
		return super.locus(label, p, slider);
	}

}
