package org.geogebra.common.kernel.cas;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.FunctionalNVar;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.commands.EvalInfo;
import org.geogebra.common.kernel.geos.CasEvaluableFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunctionable;
import org.geogebra.common.kernel.geos.GeoLocus;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.MyError;

/**
 * SolveODE
 */
public class CmdSolveODE extends CommandProcessor implements UsesCAS {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdSolveODE(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c, EvalInfo info) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;
		arg = resArgs(c);

		switch (n) {
		case 1:
			if (arg[0] instanceof CasEvaluableFunction) {

				AlgoSolveODECas algo = new AlgoSolveODECas(cons, c.getLabel(),
						(CasEvaluableFunction) arg[0], info);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			throw argErr(c, arg[0]);
		case 2:
			if ((ok[0] = arg[0] instanceof CasEvaluableFunction)
					&& (ok[1] = arg[1] instanceof GeoPointND)) {

				AlgoSolveODECas algo = new AlgoSolveODECas(cons, c.getLabel(),
						(CasEvaluableFunction) arg[0], (GeoPointND) arg[1]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));
		case 5:
			if ((ok[0] = arg[0] instanceof FunctionalNVar)
					&& (ok[1] = arg[1].isGeoNumeric())
					&& (ok[2] = arg[2].isGeoNumeric())
					&& (ok[3] = arg[3].isGeoNumeric())
					&& (ok[4] = arg[4].isGeoNumeric())) {
				GeoElement[] ret = {
						solveODE(c.getLabel(), (FunctionalNVar) arg[0], null,
								(GeoNumeric) arg[1], (GeoNumeric) arg[2],
								(GeoNumeric) arg[3], (GeoNumeric) arg[4]) };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));
		case 6:
			if ((ok[0] = arg[0] instanceof FunctionalNVar)
					&& (ok[1] = arg[1] instanceof FunctionalNVar)
					&& (ok[2] = arg[2].isGeoNumeric())
					&& (ok[3] = arg[3].isGeoNumeric())
					&& (ok[4] = arg[4].isGeoNumeric())
					&& (ok[5] = arg[5].isGeoNumeric())) {
				GeoElement[] ret = { solveODE(c.getLabel(),
						(FunctionalNVar) arg[0], (FunctionalNVar) arg[1],
						(GeoNumeric) arg[2], (GeoNumeric) arg[3],
						(GeoNumeric) arg[4], (GeoNumeric) arg[5]) };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));

		case 8: // second order ODE
			if ((ok[0] = arg[0].isRealValuedFunction())
					&& (ok[1] = arg[1].isRealValuedFunction())
					&& (ok[2] = arg[2].isRealValuedFunction())
					&& (ok[3] = arg[3].isGeoNumeric())
					&& (ok[4] = arg[4].isGeoNumeric())
					&& (ok[5] = arg[5].isGeoNumeric())
					&& (ok[6] = arg[6].isGeoNumeric())
					&& (ok[7] = arg[7].isGeoNumeric())) {

				AlgoSolveODE2 algo = new AlgoSolveODE2(cons, c.getLabel(),
						(GeoFunctionable) arg[0], (GeoFunctionable) arg[1],
						(GeoFunctionable) arg[2], (GeoNumeric) arg[3],
						(GeoNumeric) arg[4], (GeoNumeric) arg[5],
						(GeoNumeric) arg[6], (GeoNumeric) arg[7]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));

			// more than one argument
		default:
			throw argNumErr(c);
		}
	}

	final private GeoLocus solveODE(String label, FunctionalNVar f,
			FunctionalNVar g, GeoNumeric x, GeoNumeric y, GeoNumeric end,
			GeoNumeric step) {
		AlgoSolveODE algo = new AlgoSolveODE(cons, label, f, g, x, y, end,
				step);
		return algo.getResult();
	}

}
