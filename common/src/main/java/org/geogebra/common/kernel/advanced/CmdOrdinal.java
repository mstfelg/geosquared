package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.main.MyError;

/**
 * Ordinal[ &lt;Number> ]
 */
public class CmdOrdinal extends CommandProcessor {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdOrdinal(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;

		switch (n) {
		case 1:
			arg = resArgs(c);
			if (arg[0].isGeoNumeric()) {
				AlgoOrdinal algo = new AlgoOrdinal(cons, c.getLabel(),
						(GeoNumeric) arg[0]);

				GeoElement[] ret = { algo.getResult() };
				return ret;

			}
			throw argErr(c, arg[0]);

		default:
			throw argNumErr(c);
		}
	}

}
