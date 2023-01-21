package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.CircularDefinitionException;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoScriptAction;
import org.geogebra.common.main.App;
import org.geogebra.common.main.MyError;

/**
 * Common processor for scripting commands -- the execution is delayed
 * (GeoScriptAction is created and the command is not executed until you call
 * {@link GeoScriptAction#perform()}) so that they work nicely with If.
 * 
 * @author Zbynek
 *
 */
public abstract class CmdScripting extends CommandProcessor {
	/** array of arguments */
	// protected GeoElement[] arg;

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdScripting(Kernel kernel) {
		super(kernel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Perform the actual command
	 * 
	 * @param c
	 *            command
	 * @return elements that may be removed after this action
	 */
	protected abstract GeoElement[] perform(Command c);

	/**
	 * Perform the actual command and remove all unlabeled inputs
	 * 
	 * @param c
	 *            command
	 */
	public final void performAndClean(Command c) {
		GeoElement[] arg = perform(c);
		for (int i = 0; arg != null && i < arg.length; i++) {
			if (arg[i] != null && !arg[i].isLabelSet()
					&& !arg[i].isGeoCasCell()) {
				arg[i].remove();
			}
		}
	}

	@Override
	public final GeoElement[] process(Command c, EvalInfo info)
			throws MyError, CircularDefinitionException {
		GeoScriptAction sa = new GeoScriptAction(cons, this, c);
		return new GeoElement[] { sa };
	}

	/**
	 * @return app
	 */
	public App getApp() {
		return app;
	}

}
