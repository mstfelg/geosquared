package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.MyError;

/**
 * CrossRtio[&lt;Point>, &lt;Point>, &lt;Point>, &lt;Point>]
 * 
 * @author Victor Franco Espino
 */
public class CmdCrossRatio extends CommandProcessor {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdCrossRatio(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 4:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isGeoPoint()))
					&& (ok[1] = (arg[1].isGeoPoint()))
					&& (ok[2] = (arg[2].isGeoPoint()))
					&& (ok[3] = (arg[3].isGeoPoint()))) {

				AlgoCrossRatio cross = new AlgoCrossRatio(cons, c.getLabel(),
						(GeoPointND) arg[0], (GeoPointND) arg[1],
						(GeoPointND) arg[2], (GeoPointND) arg[3]);

				GeoElement[] ret = { cross.getResult() };
				return ret;
			}

			throw argErr(c, getBadArg(ok, arg));

		default:
			throw argNumErr(c);
		}
	}
}
