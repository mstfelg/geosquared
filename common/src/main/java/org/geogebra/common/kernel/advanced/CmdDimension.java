package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoVectorND;
import org.geogebra.common.main.MyError;

/**
 * Dimension[&lt;Object>]
 * 
 * @author zbynek
 *
 */
public class CmdDimension extends CommandProcessor {

	/**
	 * @param kernel
	 *            kernel
	 */
	public CmdDimension(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);
		switch (n) {

		case 1:
			if (arg[0].isGeoList()) {

				AlgoDimension algo = new AlgoDimension(cons, c.getLabel(),
						(GeoList) arg[0]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			if (arg[0] instanceof GeoPointND || arg[0] instanceof GeoVectorND) {

				AlgoDimension algo = new AlgoDimension(cons, c.getLabel(),
						arg[0]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			throw argErr(c, arg[0]);
		default:
			throw argNumErr(c);
		}
	}

}
