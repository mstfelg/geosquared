package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * 
 * FitLineY[list of points] adapted from CmdLcm by Michael Borcherds 2008-01-14
 */
public class CmdFitLineY extends CmdOneListFunction {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdFitLineY(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoFitLineY algo = new AlgoFitLineY(cons, b);
		algo.getFitLineY().setLabel(a);
		return algo.getFitLineY();
	}

}
