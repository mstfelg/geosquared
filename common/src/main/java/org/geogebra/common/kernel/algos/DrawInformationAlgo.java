package org.geogebra.common.kernel.algos;

/**
 * Algos that contain information needed for object drawing
 * 
 * @author Zbynek
 *
 */
public interface DrawInformationAlgo {

	/**
	 * Make a placeholder for this algo containing all info necessary for
	 * drawing
	 * 
	 * @return algo placeholder
	 */
	public DrawInformationAlgo copy();

}
