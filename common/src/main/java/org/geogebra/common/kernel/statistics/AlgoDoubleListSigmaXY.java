/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * Sigma(xy) of 2 lists
 * 
 * @author Michael Borcherds
 * @version 2008-02-23
 */

public class AlgoDoubleListSigmaXY extends AlgoStats2D {

	public AlgoDoubleListSigmaXY(Construction cons, String label,
			GeoList geoListx, GeoList geoListy) {
		super(cons, label, geoListx, geoListy, AlgoStats2D.STATS_SIGMAXY);
	}

	@Override
	public Commands getClassName() {
		return Commands.SigmaXY;
	}
}
