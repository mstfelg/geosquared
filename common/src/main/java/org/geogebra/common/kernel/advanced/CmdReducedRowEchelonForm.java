package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * ReducedRowEchelonForm[ &lt;List> ]
 * 
 * @author Michael Borcherds
 */
public class CmdReducedRowEchelonForm extends CmdOneListFunction {
	/**
	 * Creates new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdReducedRowEchelonForm(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {
		AlgoReducedRowEchelonForm algo = new AlgoReducedRowEchelonForm(cons, a,
				b);
		return algo.getResult();
	}

}
