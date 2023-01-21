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
import org.geogebra.common.kernel.algos.AlgoStats1D;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * Sum of Squares of a list
 * 
 * @author Michael Borcherds
 * @version 2008-02-18
 */

public class AlgoSigmaXX extends AlgoStats1D {

	public AlgoSigmaXX(Construction cons, GeoList geoList) {
		super(cons, geoList, AlgoStats1D.STATS_SIGMAXX);
	}

	public AlgoSigmaXX(Construction cons, GeoList geoList, GeoList freq) {
		super(cons, geoList, freq, AlgoStats1D.STATS_SIGMAXX);
	}

	@Override
	public Commands getClassName() {
		return Commands.SigmaXX;
	}
}
