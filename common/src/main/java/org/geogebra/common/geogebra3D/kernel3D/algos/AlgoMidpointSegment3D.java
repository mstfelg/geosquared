/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoMidPoint.java
 *
 * Created on 24. September 2001, 21:37
 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoSegmentND;

/**
 *
 * @author mathieu
 */
public class AlgoMidpointSegment3D extends AlgoMidpoint3D {

	private GeoSegmentND segment;

	/**
	 * 
	 * @param cons
	 *            construction
	 * @param segment
	 *            segment
	 */
	AlgoMidpointSegment3D(Construction cons, GeoSegmentND segment) {
		super(cons, segment);
		this.segment = segment;

		setInputOutput();

		// compute M = (P + Q)/2
		compute();
	}

	// for AlgoElement
	@Override
	protected void setInputOutput() {
		input = new GeoElement[1];
		input[0] = (GeoElement) segment;

		setOnlyOutput(getPoint());
		setDependencies(); // done by AlgoElement
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlain("MidpointOfA",
				((GeoElement) segment).getLabel(tpl));

	}

}
