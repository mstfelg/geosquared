package org.geogebra.common.kernel.scripting;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.MyError;

/**
 * Turn a turtle clockwise
 * 
 * TurtleRight[ &lt;Turtle>, &lt;Angle in radians> ]
 * 
 * @author arno
 */
public class CmdTurtleRight extends CmdTurtleCommand {

	/**
	 * @param kernel
	 *            the kernel
	 */
	public CmdTurtleRight(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected void performTurtleCommand(Command c, GeoElement[] args)
			throws MyError {
		getTurtle(args).turn(-getNumArg(c, args) * 180 / Math.PI);
	}

}
