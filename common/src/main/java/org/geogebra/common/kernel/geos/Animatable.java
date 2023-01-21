/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

*/

package org.geogebra.common.kernel.geos;

import org.geogebra.common.kernel.kernelND.GeoElementND;

/**
 * Geos that can be animated
 * 
 * @author Markus
 *
 */
public interface Animatable extends GeoElementND {

	/**
	 * Performs the next animation step for this GeoElement. This may change the
	 * value of this GeoElement but will NOT call update() or updateCascade().
	 * 
	 * @param frameRate
	 *            current frames/second used in animation
	 * @param parent
	 *            parent list
	 * @return null if nothing changed or changed element otherwise
	 */
	public GeoElementND doAnimationStep(double frameRate, GeoList parent);

	/**
	 * @return true when animation is on
	 */
	public boolean isAnimating();

}
