package org.geogebra.common.kernel;

import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Interface for GeoElements that have a start point (GeoText, GeoVector)
 */
public interface Locateable extends GeoElementND {
	/**
	 * @param p
	 *            start point
	 * @throws CircularDefinitionException
	 *             in case the start point depends on this object
	 */
	public void setStartPoint(GeoPointND p) throws CircularDefinitionException;

	/**
	 * Unregisters start point
	 * 
	 * @param p
	 *            start point to remove
	 */
	default void removeStartPoint(GeoPointND p) {
		if (p == getStartPoint()) {
			initStartPoint(p.copy(), 0);
		}
	}

	/**
	 * Returns (first) start point
	 * 
	 * @return start point
	 */
	public GeoPointND getStartPoint();

	/**
	 * @param p
	 *            start point
	 * @param number
	 *            index (GeoImage has three startPoints (i.e. corners))
	 * @throws CircularDefinitionException
	 *             in case the start point depends on this object
	 */
	public void setStartPoint(GeoPointND p, int number)
			throws CircularDefinitionException;

	default int getStartPointCount() {
		return 1;
	}

	default GeoElementND getStartPoint(int idx) {
		return getStartPoint();
	}

	/**
	 * Sets the startpoint without performing any checks. This is needed for
	 * macros.
	 * 
	 * @param p
	 *            start point
	 * @param number
	 *            index
	 */
	public void initStartPoint(GeoPointND p, int number);

	/**
	 * @return true iff the location is absolute
	 */
	public boolean hasStaticLocation();

	/**
	 * @return true iff object is always fixed
	 */
	public boolean isAlwaysFixed();

	/**
	 * Use this method to tell the locateable that its startpoint will be set
	 * soon. (This is needed during XML parsing, as startpoints are processed at
	 * the end of a construction, @see geogebra.io.MyXMLHandler)
	 */
	default void setWaitForStartPoint() {
		// only relevant for vectors
	}

	/**
	 * Update that does not change value, but only location
	 */
	public void updateLocation();

}
