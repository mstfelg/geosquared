package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;

/**
 * @author gabor
 * 
 *         interface for use in async calls
 *
 */
public interface CommandDispatcherInterface {

	/**
	 * @param c
	 *            Command
	 * @param kernel
	 *            Kernel
	 * @return CommandProcessor
	 */
	public CommandProcessor dispatch(Commands c, Kernel kernel);

}
