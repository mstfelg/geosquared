/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.CircularDefinitionException;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.prover.AlgoAreConcyclic;
import org.geogebra.common.main.MyError;

/**
 * AreConcyclic[&lt;Point>, &lt;Point>, &lt;Point> ]
 */
public class CmdAreConcyclic extends CommandProcessor {

	/**
	 * Creates new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdAreConcyclic(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c)
			throws MyError, CircularDefinitionException {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);
		if (n == 4) {
			if (!(arg[0] instanceof GeoPoint)) {
				throw argErr(c, arg[0]);
			}
			if (!(arg[1] instanceof GeoPoint)) {
				throw argErr(c, arg[1]);
			}
			if (!(arg[2] instanceof GeoPoint)) {
				throw argErr(c, arg[2]);
			}
			if (!(arg[3] instanceof GeoPoint)) {
				throw argErr(c, arg[3]);
			}

			AlgoAreConcyclic algo = new AlgoAreConcyclic(cons, c.getLabel(),
					(GeoPoint) arg[0], (GeoPoint) arg[1], (GeoPoint) arg[2],
					(GeoPoint) arg[3]);

			GeoElement[] ret = { algo.getResult() };
			return ret;
		}
		throw argNumErr(c);

	}

}
