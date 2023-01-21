/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

*/

package org.geogebra.common.kernel.kernelND;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.algos.AlgoIntersectAbstract;

/**
 * Generic algo for intersection in n dimensions
 *
 */
public abstract class AlgoIntersectND extends AlgoIntersectAbstract {

	// gives the number of intersection algorithms
	// this algorithm is used by: see AlgoIntersectSingle
	private int numberOfUsers = 0;

	// used in setIntersectionPoint to remember all indices that have been set
	private boolean[] didSetIntersectionPointArray;

	/**
	 * @param c
	 *            construction
	 */
	public AlgoIntersectND(Construction c) {
		super(c);
	}

	/**
	 * @param c
	 *            construction
	 * @param addToConstructionList
	 *            whether to add this to XML
	 */
	public AlgoIntersectND(Construction c, boolean addToConstructionList) {
		super(c, addToConstructionList);
	}

	/**
	 * Avoids two intersection points at same position. This is only done as
	 * long as the second intersection point doesn't have a label yet.
	 */
	protected void avoidDoubleTangentPoint() {
		GeoPointND[] points = getIntersectionPoints();
		if (!points[1].isLabelSet()
				&& points[0].isEqual(points[1])) {
			points[1].setUndefined();
		}
	}

	/**
	 * @return false
	 */
	protected boolean showUndefinedPointsInAlgebraView() {
		return false;
	}

	/**
	 * Hide undefined points from algebra view, except first one
	 */
	protected void noUndefinedPointsInAlgebraView() {
		GeoPointND[] points = getIntersectionPoints();
		for (int i = 1; i < points.length; i++) {
			points[i].showUndefinedInAlgebraView(false);
		}
	}

	/**
	 * Increase number of users
	 */
	public void addUser() {
		numberOfUsers++;
	}

	/**
	 * Decrease number of users; if zero, remove this algo from kernel
	 */
	public void removeUser() {
		numberOfUsers--;

		if (numberOfUsers == 0 && !isPrintedInXML()) {
			// this algorithm has no users and no labeled output
			super.remove();
			kernel.getAlgoDispatcher().removeIntersectionAlgorithm(this);
		}
	}

	/** @return array of all intersection points */
	public abstract GeoPointND[] getIntersectionPoints();

	/** @return array of last defined intersection points */
	protected abstract GeoPointND[] getLastDefinedIntersectionPoints();

	/**
	 * Sets the index-th intersection point to the coords of p. This is needed
	 * when loading constructions from a file to make sure the intersection
	 * points remain at their saved positions.
	 * 
	 * @param index
	 *            index
	 * @param p
	 *            point
	 */
	public final void setIntersectionPoint(int index, GeoPointND p) {
		GeoPointND[] points = getIntersectionPoints();
		GeoPointND[] defpoints = getLastDefinedIntersectionPoints();

		if (!p.isDefined() || index >= points.length) {
			return;
		}

		// init didSetIntersectionPoint array
		if (didSetIntersectionPointArray == null) {
			didSetIntersectionPointArray = new boolean[points.length];
		} else if (didSetIntersectionPointArray.length < points.length) {
			boolean[] temp = new boolean[points.length];
			for (int i = 0; i < points.length; i++) {
				if (i < didSetIntersectionPointArray.length) {
					temp[i] = didSetIntersectionPointArray[i];
				} else {
					temp[i] = false;
				}
			}
			didSetIntersectionPointArray = temp;
		}

		// set coords of intersection point to those of p
		setCoords(points[index], p);
		if (defpoints != null) {
			setCoords(defpoints[index], p);
		}
		// we only remember setting the point if we used a defined point
		didSetIntersectionPointArray[index] = true;

		// all other intersection points should be set undefined
		// unless they have been set before
		for (int i = 0; i < points.length; i++) {
			if (!didSetIntersectionPointArray[i]) {
				points[i].setUndefined();
				if (defpoints != null) {
					defpoints[i].setUndefined();
				}
			}
		}

		// Application.debug("SET INTERSECTION POINT");
		// for (int i=0; i < points.length; i++) {
		// Application.debug(" point " + i + ": " + points[i] + ", defPoint " +
		// defpoints[i]);
		// }
	}

	/**
	 * set destination coords equals to source coords
	 * 
	 * @param destination
	 *            destination point
	 * @param source
	 *            source point
	 */
	abstract protected void setCoords(GeoPointND destination,
			GeoPointND source);
	// points[index].setCoords(p);

	/**
	 * Returns true if setIntersectionPoint was called for index-th point.
	 * 
	 * @param index
	 *            index of point
	 * @return true if setIntersectionPoint was called for index-th point.
	 */
	protected boolean didSetIntersectionPoint(int index) {
		return didSetIntersectionPointArray != null
				&& didSetIntersectionPointArray[index];
	}

	@Override
	public String toString(StringTemplate tpl) {
		// Michael Borcherds 2008-03-30
		// simplified to allow better Chinese translation
		return getLoc().getPlainDefault("IntersectionPointOfAB",
				"Intersection point of %0, %1",
				input[0].getLabel(tpl), input[1].getLabel(tpl));
	}

	@Override
	public void remove() {
		if (removed) {
			return;
		}
		if (numberOfUsers == 0) {
			// this algorithm has no users and no labeled output
			super.remove();
			kernel.getAlgoDispatcher().removeIntersectionAlgorithm(this);
		} else {
			// there are users of this algorithm, so we keep it
			// remove only output
			// delete dependent objects
			for (int i = 0; i < getOutputLength(); i++) {
				getOutput(i).doRemove();
			}
			setPrintedInXML(false);
		}
	}

}
