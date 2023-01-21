/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoNumeric;

public abstract class AlgoIntervalAbstract extends AlgoElement {

	protected GeoFunction interval; // input
	protected GeoNumeric result; // output

	AlgoIntervalAbstract(Construction cons, String label,
			GeoFunction interval) {
		this(cons, interval);

		result.setLabel(label);
	}

	AlgoIntervalAbstract(Construction cons, GeoFunction interval) {
		super(cons);
		this.interval = interval;

		result = new GeoNumeric(cons);

		setInputOutput();
		compute();
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[1];
		input[0] = interval;

		super.setOutputLength(1);
		super.setOutput(0, result);
		setDependencies(); // done by AlgoElement
	}

	public GeoNumeric getResult() {
		return result;
	}

}
