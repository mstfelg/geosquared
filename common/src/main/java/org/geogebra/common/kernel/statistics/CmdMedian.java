package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoMedian;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * Median[ list ] adapted from CmdLcm by Michael Borcherds 2008-02-16
 */
public class CmdMedian extends CmdOneListFunction {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdMedian(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoMedian algo = new AlgoMedian(cons, b);
		algo.getMedian().setLabel(a);
		return algo.getMedian();
	}

	@Override
	final protected GeoElement doCommand(String a, Command c, GeoList list,
			GeoList freq) {
		AlgoMedian algo = new AlgoMedian(cons, list, freq);
		algo.getMedian().setLabel(a);
		return algo.getMedian();
	}

}
