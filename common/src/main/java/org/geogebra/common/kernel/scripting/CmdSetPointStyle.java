package org.geogebra.common.kernel.scripting;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.commands.CmdScripting;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.PointProperties;
import org.geogebra.common.main.MyError;

/**
 * SetPointStyle
 */
public class CmdSetPointStyle extends CmdScripting {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdSetPointStyle(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected final GeoElement[] perform(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean ok;
		switch (n) {
		case 2:
			GeoElement[] arg = resArgs(c);

			if ((ok = arg[0] instanceof PointProperties)
					&& arg[1] instanceof NumberValue) {

				PointProperties point = (PointProperties) arg[0];

				int style = (int) arg[1].evaluateDouble();

				point.setPointStyle(style);
				point.updateRepaint();

				return arg;
			} else if (!ok) {
				throw argErr(c, arg[0]);
			} else {
				throw argErr(c, arg[1]);
			}

		default:
			throw argNumErr(c);
		}
	}
}
