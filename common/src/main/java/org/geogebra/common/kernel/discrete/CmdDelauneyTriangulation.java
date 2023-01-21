package org.geogebra.common.kernel.discrete;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdOneListFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;

/**
 * DelauneyTriangulation[&lt;List ofPoints> ]
 * 
 * @author Michael
 *
 */
public class CmdDelauneyTriangulation extends CmdOneListFunction {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdDelauneyTriangulation(Kernel kernel) {
		super(kernel);
	}

	@Override
	final protected GeoElement doCommand(String a, GeoList b) {

		AlgoDelauneyTriangulation algo = new AlgoDelauneyTriangulation(cons, a,
				b);
		return algo.getResult();
	}

}
