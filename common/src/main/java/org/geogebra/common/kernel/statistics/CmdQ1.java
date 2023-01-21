package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoQ1;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * Q1[ list ]
 * 
 * @author Michael Borcherds
 * @version 2008-02-16
 */
public class CmdQ1 extends CmdOneListFunction {
	/**
	 * Creates new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdQ1(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoQ1 algo = new AlgoQ1(cons, b);
		algo.getQ1().setLabel(a);
		return algo.getQ1();
	}

	@Override
	final protected GeoElement doCommand(String a, Command c, GeoList list,
			GeoList freq) {
		AlgoQ1 algo = new AlgoQ1(cons, list, freq);
		algo.getQ1().setLabel(a);
		return algo.getQ1();
	}

}
