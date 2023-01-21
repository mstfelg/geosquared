package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CmdTwoNumFunction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;

/**
 * RandomBinomial[ &lt;Number>, &lt;Number> ]
 */
public class CmdRandomBinomial extends CmdTwoNumFunction {

	/**
	 * Creates new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdRandomBinomial(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected GeoElement doCommand(String a, GeoNumberValue b,
			GeoNumberValue c) {
		AlgoRandomBinomial algo = new AlgoRandomBinomial(cons, a, b, c);
		return algo.getResult();
	}

}
