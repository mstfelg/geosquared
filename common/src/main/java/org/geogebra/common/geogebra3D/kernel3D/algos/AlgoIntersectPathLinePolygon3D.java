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

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPoint3D;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoSegment3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoIntersectPathLinePolygon;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPolygon;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.kernelND.GeoRayND;
import org.geogebra.common.kernel.kernelND.GeoSegmentND;
import org.geogebra.common.kernel.matrix.Coords;

/**
 * Algo for intersection of a line with the interior of a polygon
 * 
 * @author matthieu
 */
public class AlgoIntersectPathLinePolygon3D
		extends AlgoIntersectPathLinePolygon {

	/**
	 * common constructor
	 * 
	 * @param c
	 *            construction
	 * @param labels
	 *            output labels
	 * @param geo
	 *            line
	 * @param p
	 *            polygon
	 */
	public AlgoIntersectPathLinePolygon3D(Construction c, String[] labels,
			GeoElement geo, GeoElement p) {

		super(c, labels, geo, p);

	}

	/**
	 * common constructor
	 * 
	 * @param c
	 *            construction
	 * @param geo
	 *            line
	 * @param p
	 *            polygon
	 */
	public AlgoIntersectPathLinePolygon3D(Construction c, GeoElement geo,
			GeoElement p) {

		super(c, geo, p);

	}

	/**
	 * @param c
	 *            construction
	 */
	public AlgoIntersectPathLinePolygon3D(Construction c) {
		super(c);
	}

	@Override
	protected OutputHandler<GeoElement> createOutputSegments() {
		return new OutputHandler<>(new ElementFactory<GeoElement>() {
			@Override
			public GeoSegment3D newElement() {
				GeoSegment3D a = new GeoSegment3D(cons);
				GeoPoint3D aS = new GeoPoint3D(cons);
				aS.setCoords(0, 0, 0, 1);
				GeoPoint3D aE = new GeoPoint3D(cons);
				aE.setCoords(0, 0, 0, 1);
				a.setPoints(aS, aE);
				a.setParentAlgorithm(AlgoIntersectPathLinePolygon3D.this);
				setSegmentVisualProperties(a);
				return a;
			}
		});
	}

	@Override
	protected void addCoords(double parameter, Coords coords,
			GeoElementND geo) {
		newCoords.put(parameter, coords.copyVector());
	}

	@Override
	protected void addStartEndPoints() {
		if (g instanceof GeoSegmentND) {
			newCoords.put(0.0, g.getStartInhomCoords());
			newCoords.put(1.0, g.getEndInhomCoords());
		} else if (g instanceof GeoRayND) {
			newCoords.put(0d, g.getStartInhomCoords());
		}
	}

	@Override
	protected boolean checkMidpoint(GeoPolygon poly, Coords a, Coords b) {
		Coords midpoint = poly
				.getNormalProjection(a.copy().addInside(b).mulInside(0.5))[1];
		return poly.isInRegion(midpoint.getX(), midpoint.getY());
	}

}
