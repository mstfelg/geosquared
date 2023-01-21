/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * VectorValue.java
 *
 * Created on 03. Oktober 2001, 10:09
 */

package org.geogebra.common.kernel.arithmetic;

import org.geogebra.common.kernel.geos.GeoVec2D;

/**
 *
 * @author Markus
 */
public interface VectorValue extends VectorNDValue {
	/**
	 * @return this vector value as GeoVec2D
	 */
	@Override
	public GeoVec2D getVector();

	/**
	 * Returns coord mode POLAR, COMPLEX or CARTESIAN
	 * 
	 * @return Kernel.COORD_*
	 */
	@Override
	public int getToStringMode();

	/**
	 * Sets coord mode
	 * 
	 * @param mode
	 *            Kernel.COORD_*
	 */
	@Override
	public void setMode(int mode);
}
