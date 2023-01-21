package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.main.MyError;

/**
 * Poisson Distribution
 */
public class CmdPoisson extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdPoisson(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {

		case 1:
			arg = resArgs(c);
			if (arg[0] instanceof GeoNumberValue) {
				AlgoPoissonBarChart algo = new AlgoPoissonBarChart(cons,
						c.getLabel(), (GeoNumberValue) arg[0]);

				GeoElement[] ret = { algo.getSum() };
				return ret;
			}
			throw argErr(c, arg[0]);

		case 2:
			arg = resArgs(c);
			if ((ok[0] = arg[0] instanceof GeoNumberValue)
					&& (ok[1] = arg[1].isGeoBoolean())) {

				AlgoPoissonBarChart algo = new AlgoPoissonBarChart(cons,
						c.getLabel(), (GeoNumberValue) arg[0],
						(GeoBoolean) arg[1]);

				GeoElement[] ret = { algo.getSum() };
				return ret;

			} else if ((ok[0] = arg[0] instanceof GeoNumberValue)
					&& (ok[1] = arg[1].isGeoList())) {
				AlgoPoissonDistList algo = new AlgoPoissonDistList(cons,
						c.getLabel(), (GeoNumberValue) arg[0],
						(GeoList) arg[1]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			} else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else {
				throw argErr(c, arg[1]);
			}

		case 3:
			arg = resArgs(c);
			if ((ok[0] = arg[0] instanceof GeoNumberValue)
					&& (ok[1] = arg[1] instanceof GeoNumberValue)
					&& (ok[2] = arg[2].isGeoBoolean())) {

				AlgoPoisson algo = new AlgoPoisson(cons, c.getLabel(),
						(GeoNumberValue) arg[0], (GeoNumberValue) arg[1],
						(GeoBoolean) arg[2]);

				GeoElement[] ret = { algo.getResult() };
				return ret;

			} else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else if (!ok[1]) {
				throw argErr(c, arg[1]);
			} else {
				throw argErr(c, arg[2]);
			}

		default:
			throw argNumErr(c);
		}
	}
}
