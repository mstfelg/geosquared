/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoRadius.java
 *
 * Created on 30. August 2001, 21:37
 */

package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.HasHeight;

/**
 *
 * @author Mathieu
 */
public class AlgoOrientedHeight extends AlgoElement {

	private HasHeight c; // input
	private GeoNumeric num; // output

	/**
	 * @param cons
	 *            construction
	 * @param c
	 *            solid
	 */
	public AlgoOrientedHeight(Construction cons, HasHeight c) {
		super(cons);
		this.c = c;
		num = new GeoNumeric(cons);
		setInputOutput(); // for AlgoElement
		compute();
	}

	@Override
	public Commands getClassName() {
		return Commands.Height;
	}

	// for AlgoElement
	@Override
	protected void setInputOutput() {
		input = new GeoElement[1];
		input[0] = (GeoElement) c;

		super.setOutputLength(1);
		super.setOutput(0, num);
		setDependencies(); // done by AlgoElement
	}

	/**
	 * @return algebraic height (can be negative)
	 */
	public GeoNumeric getOrientedHeight() {
		return num;
	}

	@Override
	public final void compute() {
		if (!((GeoElement) c).isDefined()) {
			num.setUndefined();
		} else {
			num.setValue(c.getOrientedHeight());
		}
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlain("HeightOfA", ((GeoElement) c).getLabel(tpl));
	}

}
