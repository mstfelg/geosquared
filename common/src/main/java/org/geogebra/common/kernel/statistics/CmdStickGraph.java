package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoStickGraph;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.main.MyError;

/**
 * Stick Graph
 * 
 * @author G. Sturr
 * 
 */
public class CmdStickGraph extends CommandProcessor {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdStickGraph(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 1:

			// StickGraph[ <list of points> ]
			arg = resArgs(c);
			if (arg[0].isGeoList()) {

				AlgoStickGraph algo = new AlgoStickGraph(cons, c.getLabel(),
						(GeoList) arg[0]);
				GeoElement[] ret = { algo.getSum() };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));

		case 2:

			// StickGraph[ <x List>, <y list> ]
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoList()))
					&& (ok[1] = (arg[1].isGeoList()))) {

				AlgoStickGraph algo = new AlgoStickGraph(cons, c.getLabel(),
						(GeoList) arg[0], (GeoList) arg[1]);
				GeoElement[] ret = { algo.getSum() };
				return ret;

				// StickGraph[ <list of points>, <isHorizontal> ]
			} else if ((ok[0] = (arg[0].isGeoList()))
					&& (ok[1] = (arg[1].isGeoBoolean()))) {

				AlgoStickGraph algo = new AlgoStickGraph(cons, c.getLabel(),
						(GeoList) arg[0], (GeoBoolean) arg[1]);
				GeoElement[] ret = { algo.getSum() };
				return ret;
			}

			throw argErr(c, getBadArg(ok, arg));

		case 3:

			// StickGraph[ <x List>, <y list>, <isHorizontal> ]
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoList())) && (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoBoolean()))) {

				AlgoStickGraph algo = new AlgoStickGraph(cons, c.getLabel(),
						(GeoList) arg[0], (GeoList) arg[1],
						(GeoBoolean) arg[2]);
				GeoElement[] ret = { algo.getSum() };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));

		default:
			throw argNumErr(c);
		}
	}

}