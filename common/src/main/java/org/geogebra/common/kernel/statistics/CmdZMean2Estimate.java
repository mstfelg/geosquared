package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.main.MyError;

/**
 * ZProportionTest
 */
public class CmdZMean2Estimate extends CommandProcessor {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdZMean2Estimate(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;
		arg = resArgs(c);

		switch (n) {

		case 5:
			if ((ok[0] = arg[0].isGeoList()) && (ok[1] = arg[1].isGeoList())
					&& (ok[2] = arg[2].isGeoNumeric())
					&& (ok[3] = arg[3].isGeoNumeric())
					&& (ok[4] = arg[4].isGeoNumeric())) {

				AlgoZMean2Estimate algo = new AlgoZMean2Estimate(cons,
						c.getLabel(), (GeoList) arg[0], (GeoList) arg[1],
						(GeoNumeric) arg[2], (GeoNumeric) arg[3],
						(GeoNumeric) arg[4]);

				GeoElement[] ret = { algo.getResult() };
				return ret;

			}

			throw argErr(c, getBadArg(ok, arg));

		case 7:
			if ((ok[0] = arg[0].isGeoNumeric())
					&& (ok[1] = arg[1].isGeoNumeric())
					&& (ok[2] = arg[2].isGeoNumeric())
					&& (ok[3] = arg[3].isGeoNumeric())
					&& (ok[4] = arg[4].isGeoNumeric())
					&& (ok[5] = arg[5].isGeoNumeric())
					&& (ok[6] = arg[6].isGeoNumeric())) {

				AlgoZMean2Estimate algo = new AlgoZMean2Estimate(cons,
						(GeoNumeric) arg[0], (GeoNumeric) arg[1],
						(GeoNumeric) arg[2], (GeoNumeric) arg[3],
						(GeoNumeric) arg[4], (GeoNumeric) arg[5],
						(GeoNumeric) arg[6]);
				algo.getResult().setLabel(c.getLabel());
				GeoElement[] ret = { algo.getResult() };
				return ret;

			}

			throw argErr(c, getBadArg(ok, arg));

		default:
			throw argNumErr(c);
		}
	}
}
