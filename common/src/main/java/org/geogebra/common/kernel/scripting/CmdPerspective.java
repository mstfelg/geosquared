package org.geogebra.common.kernel.scripting;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CmdScripting;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoText;

/**
 * Sets perspective
 *
 */
public class CmdPerspective extends CmdScripting {
	/**
	 * @param kernel
	 *            kernel
	 */
	public CmdPerspective(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected GeoElement[] perform(Command c) {
		GeoElement[] args = resArgs(c);
		if (args.length != 1) {
			throw this.argNumErr(c);
		}
		if (args[0] instanceof GeoText || args[0] instanceof GeoNumberValue) {
			String code = args[0].toValueString(StringTemplate.defaultTemplate);
			app.getGgbApi().setPerspective(code);
			return args;
		}

		throw this.argErr(c, args[0]);
	}

}
