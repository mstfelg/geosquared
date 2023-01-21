/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.statistics;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoNumberValue;

/**
 * 
 * @author Michael Borcherds
 */

public class AlgoZipf extends AlgoDistribution {

	public AlgoZipf(Construction cons, String label, GeoNumberValue a,
			GeoNumberValue b, GeoNumberValue c, GeoBoolean isCumulative) {
		super(cons, label, a, b, c, isCumulative);
	}

	@Override
	public Commands getClassName() {
		return Commands.Zipf;
	}

	@Override
	public final void compute() {

		if (input[0].isDefined() && input[1].isDefined()
				&& input[2].isDefined()) {
			int param = (int) a.getDouble();
			double param2 = b.getDouble();
			int val = (int) Math.round(c.getDouble());
			try {
				ZipfDistribution dist = getZipfDistribution(param, param2);
				if (isCumulative.getBoolean()) {
					num.setValue(dist.cumulativeProbability(val)); // P(X <=
																	// val)
				}
				else {
					num.setValue(dist.probability(val)); // P(X = val)
				}

			} catch (Exception e) {
				num.setUndefined();
			}
		} else {
			num.setUndefined();
		}
	}

}
