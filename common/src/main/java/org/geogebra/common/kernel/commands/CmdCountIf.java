package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoCountIf;
import org.geogebra.common.kernel.algos.AlgoCountIf3;
import org.geogebra.common.kernel.arithmetic.ValidExpression;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * CountIf[ &lt;GeoBoolean>, &lt;GeoList> ]
 */
public class CmdCountIf extends CmdKeepIf {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdCountIf(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected GeoElement[] getResult2(ValidExpression c, GeoFunction booleanFun,
			GeoElement[] args) {
		AlgoCountIf algo = new AlgoCountIf(cons, c.getLabel(), booleanFun,
				(GeoList) args[1]);
		GeoElement[] ret = { algo.getResult() };

		return ret;
	}

	@Override
	protected GeoElement[] getResult3(ValidExpression c, GeoBoolean arg,
			GeoElement[] vars, GeoList[] over) {
		AlgoCountIf3 algo = new AlgoCountIf3(cons, c.getLabel(), arg, vars[0],
				over[0]);
		GeoElement[] ret = { algo.getResult() };

		return ret;
	}
}
