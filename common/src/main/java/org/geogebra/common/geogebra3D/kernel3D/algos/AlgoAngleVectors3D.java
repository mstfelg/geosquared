/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoAnglePoints.java
 *
 * Created on 30. August 2001, 21:37
 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoAngle3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoAngleVectorsND;
import org.geogebra.common.kernel.geos.GeoAngle;
import org.geogebra.common.kernel.kernelND.GeoDirectionND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoVectorND;
import org.geogebra.common.kernel.matrix.Coords;

/**
 *
 * @author mathieu
 */
public class AlgoAngleVectors3D extends AlgoAngleVectorsND {
	/** normal */
	protected Coords vn;
	private Coords o;
	private Coords v1;
	private Coords v2;

	/**
	 * @param cons
	 *            construction
	 * @param v
	 *            vector
	 * @param w
	 *            vector
	 */
	AlgoAngleVectors3D(Construction cons, GeoVectorND v,
			GeoVectorND w) {
		this(cons, v, w, null);
	}

	/**
	 * @param cons
	 *            construction
	 * @param v
	 *            vector
	 * @param w
	 *            vector
	 * @param orientation
	 *            orientation
	 */
	AlgoAngleVectors3D(Construction cons, GeoVectorND v,
			GeoVectorND w, GeoDirectionND orientation) {
		super(cons, v, w, orientation);

	}

	@Override
	protected GeoAngle newGeoAngle(Construction cons1) {
		GeoAngle ret = new GeoAngle3D(cons1);
		ret.setDrawable(true);
		return ret;
	}

	@Override
	public void compute() {

		// vectors directions
		v1 = getv().getCoordsInD3();
		v2 = getw().getCoordsInD3();

		// calc angle
		v1.calcNorm();
		double l1 = v1.getNorm();
		v2.calcNorm();
		double l2 = v2.getNorm();

		double c = v1.dotproduct(v2) / (l1 * l2); // cosinus of the angle

		getAngle().setValue(AlgoAnglePoints3D.acos(c));

		// normal vector
		vn = AlgoAnglePoints3D.forceNormalVector(v1, v2);

		// start point
		GeoPointND start = getStartPoint(getv());
		if (centerIsNotDrawable(start)) {
			o = Coords.UNDEFINED;
		} else {
			o = start.getInhomCoordsInD3();
		}

	}

	@Override
	public Coords getVn() {
		return vn;
	}

	@Override
	public boolean getCoordsInD3(Coords[] drawCoords) {

		if (!o.isDefined()) {
			return false;
		}

		drawCoords[0] = o;
		drawCoords[1] = v1;
		drawCoords[2] = v2;

		return true;
	}

}
