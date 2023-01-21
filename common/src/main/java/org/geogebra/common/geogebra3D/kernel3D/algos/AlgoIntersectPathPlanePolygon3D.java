/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoIntersectLines.java
 *
 * Created on 30. August 2001, 21:37
 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPlane3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.matrix.CoordMatrixUtil;
import org.geogebra.common.kernel.matrix.Coords;

/**
 * Algo for intersection of a line with the interior of a polygon
 * 
 * @author matthieu
 */
public class AlgoIntersectPathPlanePolygon3D
		extends AlgoIntersectPathLinePolygon3D {
	/** plane */
	protected GeoPlane3D plane;

	/**
	 * common constructor
	 * 
	 * @param c
	 *            construction
	 * @param labels
	 *            labels
	 * @param plane
	 *            plane
	 * @param p
	 *            polygon
	 */
	public AlgoIntersectPathPlanePolygon3D(Construction c, String[] labels,
			GeoPlane3D plane, GeoElement p) {

		super(c, labels, plane, p);

	}

	/**
	 * common constructor
	 * 
	 * @param c
	 *            construction
	 * @param plane
	 *            plane
	 * @param p
	 *            polygon
	 */
	public AlgoIntersectPathPlanePolygon3D(Construction c, GeoPlane3D plane,
			GeoElement p) {

		super(c, plane, p);

	}

	/**
	 * @param c
	 *            construction
	 */
	public AlgoIntersectPathPlanePolygon3D(Construction c) {
		super(c);
	}

	@Override
	protected void setFirstInput(GeoElement geo) {
		this.plane = (GeoPlane3D) geo;
	}

	@Override
	protected GeoElement getFirstInput() {
		return plane;
	}

	@Override
	protected void addStartEndPoints() {
		// no start/end points
	}

	@Override
	protected void setIntersectionLine() {

		Coords[] intersection = CoordMatrixUtil.intersectPlanes(
				plane.getCoordSys().getMatrixOrthonormal(),
				p.getCoordSys().getMatrixOrthonormal());

		o1 = intersection[0];
		d1 = intersection[1];

		// if (d1.isZero())
		// Log.debug("\np: "+p+"\no1=\n"+o1+"\nd1=\n"+d1);
	}

	@Override
	protected boolean checkParameter(double t1) {
		return true; // nothing to check here
	}

}
