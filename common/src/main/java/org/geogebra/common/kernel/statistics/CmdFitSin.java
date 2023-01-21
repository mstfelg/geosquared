package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * FitSin[&lt;List of points>]
 * 
 * @author Hans-Petter Ulven
 * @version 15.11.08
 */
public class CmdFitSin extends CmdOneListFunction {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdFitSin(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoFitSin algo = new AlgoFitSin(cons, b);
		algo.getFitSin().setLabel(a);
		return algo.getFitSin();
	}

}
