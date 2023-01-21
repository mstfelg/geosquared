package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoDependentNumber;
import org.geogebra.common.kernel.algos.AlgoRandom;
import org.geogebra.common.kernel.algos.AlgoRandomFixed;
import org.geogebra.common.kernel.arithmetic.BooleanValue;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.main.MyError;
import org.geogebra.common.plugin.Operation;

/**
 * RandomBetween[a,b] RandomBetween[a,b,fixed]
 */
public class CmdRandom extends CommandProcessor {
	/**
	 * Creates new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdRandom(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;

		switch (n) {
		case 0:

			GeoNumeric num = new GeoNumeric(cons, 0);
			cons.addRandomGeo(num);
			num.setValue(app.getRandomNumber());
			AlgoDependentNumber adn = new AlgoDependentNumber(cons,
					new ExpressionNode(kernel, num,
							Operation.RANDOM, null),
					false);
			adn.getOutput(0).setLabel(c.getLabel());
			return adn.getOutput();
		case 3:
			arg = resArgs(c);
			if (arg[2] instanceof BooleanValue) {

				if (((BooleanValue) arg[2]).getBoolean()) {
					// don't pass (BooleanValue)arg[2] (dummy variable, always
					// true)

					AlgoRandomFixed algo = new AlgoRandomFixed(cons,
							c.getLabel(), (GeoNumberValue) arg[0],
							(GeoNumberValue) arg[1]);
					GeoElement[] ret = { algo.getResult() };

					return ret;
				}
				// else fall through to case 2:

			} else {
				throw argErr(c, arg[2]);
			}

			// fall through if arg[2] == false

		case 2:
			arg = resArgs(c);
			if ((arg[0] instanceof GeoNumberValue)
					&& (arg[1] instanceof GeoNumberValue)) {

				AlgoRandom algo = new AlgoRandom(cons, c.getLabel(),
						(GeoNumberValue) arg[0], (GeoNumberValue) arg[1]);
				GeoElement[] ret = { algo.getResult() };

				return ret;

			}
			throw argErr(c,
					arg[0] instanceof GeoNumberValue ? arg[1] : arg[0]);

		default:
			throw argNumErr(c);
		}
	}

}
