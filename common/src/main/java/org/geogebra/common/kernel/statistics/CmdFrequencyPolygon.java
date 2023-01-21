package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoPolyLine;
import org.geogebra.common.main.MyError;

/**
 * Histogram[ &lt;List>, &lt;List> ]
 */
public class CmdFrequencyPolygon extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdFrequencyPolygon(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 2:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoList()))
					&& (ok[1] = (arg[1].isGeoList()))) {

				AlgoFrequencyPolygon algo = new AlgoFrequencyPolygon(cons,
						(GeoList) arg[0], (GeoList) arg[1]);

				return output(algo, c);
			} else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else {
				throw argErr(c, arg[1]);
			}

		case 3:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoList())) && (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoBoolean()))) {
				GeoElement[] ret = {
						frequencyPolygon(c.getLabel(), (GeoList) arg[0],
								(GeoList) arg[1], (GeoBoolean) arg[2], null) };
				return ret;
			} else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else if (!ok[1]) {
				throw argErr(c, arg[1]);
			} else {
				throw argErr(c, arg[2]);
			}

		case 4:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoList())) && (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoBoolean()))
					&& (ok[3] = (arg[3].isGeoNumeric()))) {
				GeoElement[] ret = { frequencyPolygon(c.getLabel(),
						(GeoList) arg[0], (GeoList) arg[1], (GeoBoolean) arg[2],
						(GeoNumeric) arg[3]) };
				return ret;
			}

			else if ((ok[0] = (arg[0].isGeoBoolean()))
					&& (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoList()))
					&& (ok[3] = (arg[3].isGeoBoolean()))) {

				AlgoFrequencyPolygon algo = new AlgoFrequencyPolygon(cons,
						(GeoBoolean) arg[0], (GeoList) arg[1],
						(GeoList) arg[2], null, (GeoBoolean) arg[3], null);

				return output(algo, c);
			}

			else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else if (!ok[1]) {
				throw argErr(c, arg[1]);
			} else if (!ok[2]) {
				throw argErr(c, arg[2]);
			} else {
				throw argErr(c, arg[3]);
			}

		case 5:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoBoolean()))
					&& (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoList()))
					&& (ok[3] = (arg[3].isGeoBoolean()))
					&& (ok[4] = (arg[4].isGeoNumeric()))) {

				AlgoFrequencyPolygon algo = new AlgoFrequencyPolygon(cons,
						(GeoBoolean) arg[0], (GeoList) arg[1],
						(GeoList) arg[2], null, (GeoBoolean) arg[3],
						(GeoNumeric) arg[4]);

				return output(algo, c);
			}

			else if ((ok[0] = (arg[0].isGeoBoolean()))
					&& (ok[1] = (arg[1].isGeoList()))
					&& (ok[2] = (arg[2].isGeoList()))
					&& (ok[3] = (arg[3].isGeoList()))
					&& (ok[4] = (arg[4].isGeoBoolean()))) {

				AlgoFrequencyPolygon algo = new AlgoFrequencyPolygon(cons,
						(GeoBoolean) arg[0], (GeoList) arg[1],
						(GeoList) arg[2], (GeoList) arg[3], (GeoBoolean) arg[4],
						null);
				return output(algo, c);

			} else if (!ok[0]) {
				throw argErr(c, arg[0]);
			} else if (!ok[1]) {
				throw argErr(c, arg[1]);
			} else if (!ok[2]) {
				throw argErr(c, arg[2]);
			} else if (!ok[3]) {
				throw argErr(c, arg[3]);
			} else {
				throw argErr(c, arg[4]);
			}

		default:
			throw argNumErr(c);
		}

	}

	private static GeoElement[] output(AlgoFrequencyPolygon algo, Command c) {
		algo.getResult().setLabel(c.getLabel());
		return new GeoElement[] { algo.getResult() };
	}

	/**
	 * FrequencyPolygon with density scale factor (no cumulative parameter)
	 */
	final private GeoPolyLine frequencyPolygon(String label, GeoList list1,
			GeoList list2, GeoBoolean useDensity, GeoNumeric density) {
		AlgoFrequencyPolygon algo = new AlgoFrequencyPolygon(cons, null,
				list1, list2, null, useDensity, density);
		GeoPolyLine result = algo.getResult();
		result.setLabel(label);
		return result;
	}
}
