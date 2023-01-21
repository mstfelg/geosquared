package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.CircularDefinitionException;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoSpline;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunctionNVar;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoCurveCartesianND;
import org.geogebra.common.main.MyError;
import org.geogebra.common.plugin.GeoClass;

/**
 * 
 * Spline [&lt;list of points>]
 * 
 * @author Giuliano Bellucci
 * 
 */
public class CmdSpline extends CommandProcessor {

	/**
	 * @param kernel
	 *            kernel
	 */
	public CmdSpline(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c)
			throws MyError, CircularDefinitionException {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);
		switch (n) {
		case 0:
			throw argNumErr(c);
		case 1:
			arg = resArgs(c);
			if (arg[0].isGeoList() && arePoint((GeoList) arg[0])) {
				GeoElement[] ret = { spline(c.getLabel(), (GeoList) arg[0]) };
				return ret;
			}
			throw argErr(c, arg[0]);
		case 2:
			arg = resArgs(c);
			if (arg[0].isGeoList() && arePoint((GeoList) arg[0])) {
				int degree = (int) c.getArgument(1).evaluateDouble();
				if (Double.isNaN(degree) || degree > ((GeoList) arg[0]).size()
						|| degree < 3) {
					throw argErr(c, c.getArgument(1));
				}
				GeoNumberValue degreeNum = (GeoNumberValue) arg[1];
				AlgoSpline algo = new AlgoSpline(cons, c.getLabel(),
						(GeoList) arg[0], degreeNum, null);
				GeoCurveCartesianND list = algo.getSpline();
				GeoElement[] ret = { list };
				return ret;
			}
			throw argErr(c, arg[0]);
		case 3:
			arg = resArgs(c);
			if (!arg[2].isGeoFunctionNVar()) {
				throw argErr(c, arg[2]);
			}
			if (arg[0].isGeoList() && arePoint((GeoList) arg[0])) {
				int degree = (int) c.getArgument(1).evaluateDouble();
				if (Double.isNaN(degree) || degree > ((GeoList) arg[0]).size()
						|| degree < 3) {
					throw argErr(c, c.getArgument(1));
				}
				GeoNumberValue degreeNum = (GeoNumberValue) arg[1];
				AlgoSpline algo = new AlgoSpline(cons, c.getLabel(),
						(GeoList) arg[0], degreeNum, (GeoFunctionNVar) arg[2]);
				GeoCurveCartesianND list = algo.getSpline();
				GeoElement[] ret = { list };
				return ret;
			}
			throw argErr(c, arg[0]);
		default:
			GeoList list = wrapInList(kernel, arg, arg.length, GeoClass.POINT);
			if (list != null) {
				GeoElement[] ret = { spline(c.getLabel(), list) };
				return ret;
			}

			throw argNumErr(c);
		}
	}

	private GeoCurveCartesianND spline(String label, GeoList list) {
		AlgoSpline algo = new AlgoSpline(cons, label, list,
				new GeoNumeric(cons, 3), null);
		return algo.getSpline();
	}

	private static boolean arePoint(GeoList geoList) {
		for (int i = 0; i < geoList.size() - 1; i++) {
			if (!geoList.get(i).isGeoPoint()) {
				return false;
			}
		}
		return true;
	}

}
