package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * FitLog[&lt;List of points>]
 * 
 * @author Hans-Petter Ulven
 * @version 12.04.08
 */

public class CmdFitLog extends CmdOneListFunction {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdFitLog(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoFitLog algo = new AlgoFitLog(cons, b);
		algo.getFitLog().setLabel(a);
		return algo.getFitLog();
	}

}
