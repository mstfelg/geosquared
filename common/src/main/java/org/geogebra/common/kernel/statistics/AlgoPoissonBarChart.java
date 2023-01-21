/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.statistics;

import org.apache.commons.math3.util.Cloner;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoBarChart;
import org.geogebra.common.kernel.algos.DrawInformationAlgo;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoNumberValue;

/**
 * @author G. Sturr
 * @version 2011-06-21
 */

public class AlgoPoissonBarChart extends AlgoBarChart {

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param mean
	 *            mean
	 */
	public AlgoPoissonBarChart(Construction cons, String label,
			GeoNumberValue mean) {
		super(cons, label, mean, null, null, null,
				AlgoBarChart.TYPE_BARCHART_POISSON);
		cons.registerEuclidianViewCE(this);
	}

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param mean
	 *            mean
	 * @param isCumulative
	 *            cumulative?
	 */
	public AlgoPoissonBarChart(Construction cons, String label,
			GeoNumberValue mean, GeoBoolean isCumulative) {
		super(cons, label, mean, null, null, isCumulative,
				AlgoBarChart.TYPE_BARCHART_POISSON);
		cons.registerEuclidianViewCE(this);
	}

	private AlgoPoissonBarChart(GeoNumberValue mean, GeoBoolean isCumulative,
			GeoNumberValue a, GeoNumberValue b, double[] vals, double[] borders,
			int N) {
		super(mean, null, null, isCumulative,
				AlgoBarChart.TYPE_BARCHART_POISSON, a, b, vals, borders, N);
	}

	@Override
	public Commands getClassName() {
		return Commands.Poisson;
	}

	@Override
	public DrawInformationAlgo copy() {
		GeoBoolean b = (GeoBoolean) this.getIsCumulative();
		if (b != null) {
			b = b.copy();
		}
		return new AlgoPoissonBarChart(
				(GeoNumberValue) this.getP1().deepCopy(kernel), b,
				(GeoNumberValue) this.getA().deepCopy(kernel),
				(GeoNumberValue) this.getB().deepCopy(kernel),
				Cloner.clone(getValues()), Cloner.clone(getLeftBorder()),
				getIntervals());

	}

}
