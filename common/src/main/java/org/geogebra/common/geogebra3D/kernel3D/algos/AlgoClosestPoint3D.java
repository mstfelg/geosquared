/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPoint3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Path;
import org.geogebra.common.kernel.algos.AlgoClosestPoint;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.kernelND.GeoPointND;

public class AlgoClosestPoint3D extends AlgoClosestPoint {

	/**
	 * @param c
	 *            construction
	 * @param path
	 *            path
	 * @param point
	 *            initial point
	 */
	public AlgoClosestPoint3D(Construction c, Path path, GeoPointND point) {
		super(c, path, point);
	}

	@Override
	protected void createOutputPoint(Construction cons1, Path path) {
		P = new GeoPoint3D(cons1);
		((GeoPoint3D) P).setPath(path);
	}

	@Override
	public Commands getClassName() {
		return Commands.ClosestPoint;
	}

	@Override
	protected void setCoords() {
		((GeoPoint3D) P).setCoords(point.getCoordsInD3(), false);
	}

	@Override
	protected void addIncidence() {
		// TODO
	}

}
