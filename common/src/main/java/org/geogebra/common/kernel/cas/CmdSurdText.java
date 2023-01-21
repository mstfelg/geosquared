package org.geogebra.common.kernel.cas;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.MyError;

/**
 * SurdText
 */
public class CmdSurdText extends CommandProcessor implements UsesCAS {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdSurdText(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);

		switch (n) {
		case 1:

			if (arg[0] instanceof GeoNumberValue) {

				AlgoSurdText algo = new AlgoSurdText(cons, c.getLabel(),
						(GeoNumberValue) arg[0], null);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			} else if (arg[0].isGeoPoint()) {

				AlgoSurdTextPoint algo = new AlgoSurdTextPoint(cons,
						c.getLabel(), (GeoPointND) arg[0]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}

			throw argErr(c, arg[0]);

		case 2:

			boolean ok0;
			if ((ok0 = arg[0] instanceof GeoNumberValue)
					&& arg[1].isGeoList()) {

				AlgoSurdText algo = new AlgoSurdText(cons, c.getLabel(),
						(GeoNumberValue) arg[0], (GeoList) arg[1]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}

			throw argErr(c, arg[ok0 ? 0 : 1]);

		default:
			throw argNumErr(c);
		}
	}
}
