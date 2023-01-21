package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.main.MyError;

/**
 * LogNormal distribution
 */
public class CmdLogNormal extends CommandProcessor {
	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdLogNormal(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean ok;
		GeoElement[] arg;

		GeoBoolean cumulative = null; // default for n=3 (false)
		arg = resArgs(c);

		switch (n) {
		case 4:

			if (arg[3].isGeoBoolean()) {
				cumulative = (GeoBoolean) arg[3];
			} else {
				throw argErr(c, arg[3]);
			}

			// fall through
		case 3:
			if ((ok = arg[0] instanceof GeoNumberValue)
					&& (arg[1] instanceof GeoNumberValue)) {
				if (arg[2].isGeoFunction() && ((GeoFunction) arg[2])
						.toString(StringTemplate.defaultTemplate).equals("x")) {

					AlgoLogNormalDF algo = new AlgoLogNormalDF(cons, (GeoNumberValue) arg[0],
							(GeoNumberValue) arg[1],
							forceBoolean(cumulative, true));
					algo.getResult().setLabel(c.getLabel());
					return algo.getResult().asArray();

				} else if (arg[2] instanceof GeoNumberValue) {
					AlgoLogNormal algo = new AlgoLogNormal(cons,
							(GeoNumberValue) arg[0], (GeoNumberValue) arg[1],
							(GeoNumberValue) arg[2], cumulative);
					algo.getResult().setLabel(c.getLabel());
					return algo.getResult().asArray();

				} else {
					throw argErr(c, arg[2]);
				}
			}
			throw argErr(c, ok ? arg[1] : arg[0]);

		default:
			throw argNumErr(c);
		}
	}
}
