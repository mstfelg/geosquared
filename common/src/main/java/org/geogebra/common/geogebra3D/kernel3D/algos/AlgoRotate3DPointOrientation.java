/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoRotatePoint.java
 *
 * Created on 24. September 2001, 21:37
 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoCurveCartesian3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.kernelND.GeoCoordSys2D;
import org.geogebra.common.kernel.kernelND.GeoDirectionND;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 *
 * @author mathieu
 */
public class AlgoRotate3DPointOrientation extends AlgoRotate3D {

	private GeoPointND center;
	private GeoDirectionND orientation;

	AlgoRotate3DPointOrientation(Construction cons, String label, GeoElement in,
			GeoNumberValue angle, GeoPointND center,
			GeoDirectionND orientation) {
		this(cons, in, angle, center, orientation);
		((GeoElement) out).setLabel(label);
	}

	/**
	 * Creates new unlabeled point rotation algo
	 * 
	 * @param cons
	 *            construction
	 * @param in
	 *            rotated geo
	 * @param angle
	 *            angle
	 * @param center
	 *            center
	 * @param orientation
	 *            (axis) orientation
	 */
	public AlgoRotate3DPointOrientation(Construction cons, GeoElement in,
			GeoNumberValue angle, GeoPointND center,
			GeoDirectionND orientation) {

		super(cons, in, angle);

		this.center = center;
		this.orientation = orientation;

		setInputOutput();
		compute();
	}

	@Override
	public Commands getClassName() {
		return Commands.Rotate;
	}

	/*
	 * @Override public int getRelatedModeID() { return
	 * EuclidianConstants.MODE_ROTATE_BY_ANGLE; }
	 */

	// for AlgoElement
	@Override
	protected void setInputOutput() {

		input = new GeoElement[4];
		input[0] = inGeo;
		input[1] = angle.toGeoElement();
		input[2] = (GeoElement) center;
		input[3] = (GeoElement) orientation;

		setOutput();
	}

	// calc rotated point
	@Override
	public final void compute() {

		if (inGeo.isGeoList()) {
			transformList((GeoList) inGeo, (GeoList) outGeo);
			return;
		}

		if (inGeo instanceof GeoFunction) {
			AlgoTransformation3D.toGeoCurveCartesian(kernel,
					(GeoFunction) inGeo, (GeoCurveCartesian3D) outGeo);
		} else {
			setOutGeo();
		}

		if (!outGeo.isDefined()) {
			return;
		}

		out.rotate(angle, center, orientation);
		/*
		 * if(inGeo.isLimitedPath()) this.transformLimitedPath(inGeo, outGeo);
		 */
	}

	@Override
	final public String toString(StringTemplate tpl) {
		String s;
		if (orientation instanceof GeoCoordSys2D) { // axis perpendicular to
													// plane
			s = "ARotatedByAngleBAboutPlaneCThroughD";
		} else { // axis = orientation
			s = "ARotatedByAngleBAboutCThroughD";
		}
		return getLoc().getPlain(s, inGeo.getLabel(tpl),
				((GeoElement) angle).getLabel(tpl),
				((GeoElement) orientation).getLabel(tpl), center.getLabel(tpl));

	}

	@Override
	public double getAreaScaleFactor() {
		return 1;
	}

}
