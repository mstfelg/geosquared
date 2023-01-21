/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.algos;

import java.util.ArrayList;

import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.MyDouble;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoPolygon;
import org.geogebra.common.kernel.kernelND.GeoDirectionND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoSegmentND;

/**
 * Creates a regular Polygon for two points and the number of vertices.
 * 
 * @author Markus Hohenwarter
 */
public abstract class AlgoPolygonRegularND extends AlgoElement
		implements PolygonAlgo {
	/** first input point */
	protected final GeoPointND A;
	/** second input point */
	protected final GeoPointND B;
	/** input: number of vertices */
	protected GeoNumberValue num;
	/** number of vertices at last compute */
	protected int numOld = 2;
	/** output handler */
	protected OutputHandler<GeoPolygon> outputPolygon;
	/** output points */
	protected OutputHandler<GeoElement> outputPoints;
	/** output segments */
	protected OutputHandler<GeoElement> outputSegments;
	/** center */
	protected GeoPointND centerPoint;
	/** angle */
	protected MyDouble rotAngle;
	/**
	 * whether points should be labeled: depends on whether inputs are labeled
	 */
	protected boolean labelPointsAndSegments;
	/** whether new segment labels should be visible */
	boolean showNewSegmentsLabels;
	/** whether new point labels should be visible */
	boolean showNewPointsLabels;
	private boolean labelsNeedIniting;
	private double alpha;
	private int n;

	/**
	 * Creates a new regular polygon algorithm
	 * 
	 * @param c
	 *            construction
	 * @param labels
	 *            labels[0] for polygon, then labels for segments and then for
	 *            points
	 * @param A1
	 *            first input point
	 * @param B1
	 *            second input point
	 * @param num
	 *            number of vertices
	 * @param direction
	 *            polygon orientation in space
	 */
	public AlgoPolygonRegularND(Construction c, String[] labels, GeoPointND A1,
			GeoPointND B1, GeoNumberValue num, GeoDirectionND direction) {
		super(c);

		labelsNeedIniting = true;

		this.A = A1;
		this.B = B1;
		this.num = num;
		setDirection(direction);

		// labels given by user or loaded from file
		int labelsLength = labels == null ? 0 : labels.length;

		// set labels for segments only when points have labels
		labelPointsAndSegments = A.isLabelSet() || B.isLabelSet()
				|| labelsLength > 1;
		showNewSegmentsLabels = false;
		showNewPointsLabels = false;

		// temp center point of regular polygon
		centerPoint = (GeoPointND) newGeoPoint(c);
		rotAngle = new MyDouble(kernel);

		outputPolygon = new OutputHandler<>(
				new ElementFactory<GeoPolygon>() {
					@Override
					public GeoPolygon newElement() {
						GeoPolygon p = newGeoPolygon(cons);
						p.setParentAlgorithm(AlgoPolygonRegularND.this);
						// p.setInitLabelsCalled(true);
						return p;
					}
				});

		outputSegments = new OutputHandler<>(
				new ElementFactory<GeoElement>() {
					@Override
					public GeoElement newElement() {
						int size = outputSegments.size();
						GeoPolygon polygon =  outputPolygon.getElement(0);
						GeoElement segment = (GeoElement) (size == 0
								? polygon.createSegment(cons, A, B, true)
								: polygon.createSegmentOwnDimension(cons, A, B, true));
						segment.setAuxiliaryObject(true);
						boolean segmentsVisible = false;
						if (size > 0) { // check if at least one segment is
										// visible
							for (int i = 0; i < size && !segmentsVisible; i++) {
								segmentsVisible = segmentsVisible
										|| outputSegments.getElement(i)
												.isEuclidianVisible();
							}
						} else { // no segment yet
							segmentsVisible = true;
						}
						segment.setEuclidianVisible(segmentsVisible);
						segment.setLabelVisible(showNewSegmentsLabels);
						segment.setViewFlags(((GeoElement) A).getViewSet());
						segment.setVisibleInView3D((GeoElement) A);
						segment.setVisibleInViewForPlane((GeoElement) A);
						return segment;
					}
				});

		if (!labelPointsAndSegments) {
			outputSegments.removeFromHandler(); // no segments has output
		}

		outputPoints = new OutputHandler<>(
				new ElementFactory<GeoElement>() {
					@Override
					public GeoElement newElement() {
						GeoElement newPoint = newGeoPoint(cons);
						newPoint.setParentAlgorithm(AlgoPolygonRegularND.this);
						newPoint.setAuxiliaryObject(true);
						((GeoPointND) newPoint).setPointSize(A.getPointSize());
						newPoint.setEuclidianVisible(A.isEuclidianVisible()
								|| B.isEuclidianVisible());
						newPoint.setAuxiliaryObject(true);
						newPoint.setLabelVisible(showNewPointsLabels);
						newPoint.setViewFlags(((GeoElement) A).getViewSet());
						newPoint.setVisibleInView3D((GeoElement) A);
						newPoint.setVisibleInViewForPlane((GeoElement) A);
						GeoBoolean conditionToShow = ((GeoElement) A)
								.getShowObjectCondition();
						if (conditionToShow == null) {
							conditionToShow = ((GeoElement) B)
									.getShowObjectCondition();
						}
						if (conditionToShow != null) {
							try {
								newPoint.setShowObjectCondition(
										conditionToShow);
							} catch (Exception e) {
								// circular exception -- do nothing
							}
						}
						return newPoint;
					}
				});

		if (!labelPointsAndSegments) {
			outputPoints.removeFromHandler(); // no segments has output
		}

		// create polygon
		outputPolygon.adjustOutputSize(1);

		// create 2 first segments
		outputSegments.augmentOutputSize(2, false);
		outputSegments.getElement(0).setAuxiliaryObject(false);
		((GeoSegmentND) outputSegments.getElement(1)).modifyInputPoints(B, A);

		// for AlgoElement
		setInputOutput();

		GeoPolygon poly = getPoly();

		// set that the poly output can have different points length
		poly.setNotFixedPointsLength(true);

		// compute poly
		if (labelsLength > 1) {
			compute((labelsLength + 1) / 2); // create maybe undefined outputs
			poly.setLabel(labels[0]);
			int d = 1;
			for (int i = 0; i < outputSegments.size(); i++) {
				outputSegments.getElement(i).setLabel(labels[d + i]);
			}
			d += outputSegments.size();
			for (int i = 0; i < outputPoints.size(); i++) {
				outputPoints.getElement(i).setLabel(labels[d + i]);
			}
		} else if (labelsLength == 1) {
			poly.setLabel(labels[0]);
		} else {
			poly.setLabel(null);
		}

		labelsNeedIniting = false;

		update();

		/*
		 * if (labelPointsAndSegments) { //poly.initLabels(labels); } else if
		 * (labelsLength == 1) { poly.setLabel(labels[0]); } else {
		 * poly.setLabel(null); }
		 * 
		 * 
		 * labelsNeedIniting = false;
		 */
		// make sure that we set all point and segment labels when needed
		// updateSegmentsAndPointsLabels(points.length);
	}

	/**
	 * 
	 * @param cons1
	 *            construction for the new polygon
	 * @return new GeoPolygon 2D/3D
	 */
	protected abstract GeoPolygon newGeoPolygon(Construction cons1);

	/**
	 * 
	 * @param cons1
	 *            construction for the new point
	 * @return new GeoPoint 2D/3D
	 */
	protected abstract GeoElement newGeoPoint(Construction cons1);

	/**
	 * set the direction (only for 3D)
	 * 
	 * @param direction
	 *            direction
	 */
	protected abstract void setDirection(GeoDirectionND direction);

	@Override
	public Commands getClassName() {
		return Commands.Polygon;
	}

	@Override
	public int getRelatedModeID() {
		return EuclidianConstants.MODE_REGULAR_POLYGON;
	}

	/**
	 * 
	 * @return resulting polygon
	 */
	public final GeoPolygon getPoly() {
		return outputPolygon.getElement(0);
	}

	/**
	 * Computes points of regular polygon
	 */
	@Override
	public final void compute() {

		// check points and number
		double nd = num.getDouble();

		if (Double.isNaN(nd)) {
			nd = 2;
		}

		compute((int) Math.round(nd));
	}

	/**
	 * set the center point coords
	 * @param beta
	 *            internal angle
	 */
	protected abstract void setCenterPoint(double beta);

	/**
	 * @param vertices
	 *            n
	 * @param centralAngle
	 *            alpha
	 */
	protected void rotatePoints(int vertices, double centralAngle) {
		// now we have the center point of the polygon and
		// the center angle alpha between two neighbouring points
		// let's create the points by rotating A around the center point
		for (int k = 0; k < vertices - 2; k++) {
			// rotate point around center point
			outputPoints.getElement(k).set(A);
			rotAngle.set((k + 2) * centralAngle);
			rotate((GeoPointND) outputPoints.getElement(k));
		}
	}

	/**
	 * rotate the point regarding current parameters
	 * 
	 * @param point
	 *            point
	 */
	protected abstract void rotate(GeoPointND point);

	/**
	 * 
	 * @param nCurrent
	 *            current number of vertices
	 * @return true if undefined
	 */
	protected boolean checkUnDefined(int nCurrent) {
		if (nCurrent < 3 || !A.isDefined() || !B.isDefined()) {
			getPoly().setUndefined();
			numOld = nCurrent;
			return true;
		}

		return false;
	}

	/**
	 * @param nd
	 *            number of vertices
	 */
	public final void compute(int nd) {
		// get integer number of vertices n
		this.n = Math.max(2, nd);

		// if number of points changed, we need to update the
		// points array and the output array
		updateOutput(n);

		// check if regular polygon is defined
		if (checkUnDefined(n)) {
			return;
		}

		this.alpha = Kernel.PI_2 / n; // center angle ACB
		double beta = (Math.PI - alpha) / 2; // base angle CBA = BAC

		setCenterPoint(beta);
		rotatePoints(n, alpha);

		GeoPointND[] points = new GeoPointND[n];
		points[0] = A;
		points[1] = B;
		for (int i = 2; i < n; i++) {
			points[i] = (GeoPointND) outputPoints.getElement(i - 2);
		}

		// update new segments
		for (int i = numOld - 1; i < n; i++) {
			// Log.debug(i+": "+points[i]+" , "+points[(i+1)%n]);
			((GeoSegmentND) outputSegments.getElement(i))
					.modifyInputPoints(points[i], points[(i + 1) % n]);
		}
		GeoPolygon poly = getPoly();
		// update polygon
		poly.setPoints(points, null, false); // don't create segments
		GeoSegmentND[] segments = new GeoSegmentND[n];
		for (int i = 0; i < n; i++) {
			segments[i] = (GeoSegmentND) outputSegments.getElement(i);
		}
		poly.setSegments(segments);

		// compute area of poly
		calcArea();

		// update region coordinate system
		poly.updateRegionCSWithFirstPoints();

		numOld = n;
	}

	/**
	 * 
	 * @return current points length
	 */
	public int getCurrentPointsLength() {
		return numOld;
	}

	/**
	 * Ensures that the pointList holds n points.
	 * 
	 * @param vertices
	 *            number of vertices
	 */
	private void updateOutput(int vertices) {

		int nOld = outputPoints.size() + 2;

		// App.error("nOld="+nOld+", n="+n);

		if (nOld == vertices) {
			return;
		}

		// update points and segments
		if (vertices > nOld) {
			showNewPointsLabels = labelPointsAndSegments
					&& (A.isEuclidianVisible() && A.isLabelVisible()
							|| B.isEuclidianVisible() && B.isLabelVisible());
			outputPoints.augmentOutputSize(vertices - nOld, false);
			if (labelPointsAndSegments && !labelsNeedIniting) {
				outputPoints.updateLabels();
			}

			showNewSegmentsLabels = false;
			for (int i = 0; i < outputSegments.size(); i++) {
				showNewSegmentsLabels = showNewSegmentsLabels
						|| outputSegments.getElement(i).isLabelVisible();
			}
			outputSegments.augmentOutputSize(vertices - nOld, false);
			if (labelPointsAndSegments && !labelsNeedIniting) {
				outputSegments.updateLabels();
			}
		} else {
			for (int i = vertices; i < nOld; i++) {
				outputPoints.getElement(i - 2).setUndefined();
				outputSegments.getElement(i).setUndefined();
			}
			// update last segment
			if (vertices > 2) {
				((GeoSegmentND) outputSegments.getElement(vertices - 1))
						.modifyInputPoints(
								(GeoPointND) outputPoints.getElement(vertices - 3), A);
			} else {
				((GeoSegmentND) outputSegments.getElement(vertices - 1))
						.modifyInputPoints(B, A);
			}
		}

	}

	private void removePoint(GeoElement oldPoint) {

		// remove dependent algorithms (e.g. segments) from update sets of
		// objects further up (e.g. polygon) the tree
		ArrayList<AlgoElement> algoList = oldPoint.getAlgorithmList();
		for (int k = 0; k < algoList.size(); k++) {
			AlgoElement algo = algoList.get(k);
			for (int j = 0; j < input.length; j++) {
				input[j].removeFromUpdateSets(algo);
			}
		}

		// remove old point
		oldPoint.setParentAlgorithm(null);

		// remove dependent segment algorithm that are part of this polygon
		// to make sure we don't remove the polygon as well
		GeoPolygon poly = getPoly();
		for (int k = 0; k < algoList.size(); k++) {
			AlgoElement algo = algoList.get(k);
			// make sure we don't remove the polygon as well
			if (algo instanceof AlgoJoinPointsSegmentInterface
					&& ((AlgoJoinPointsSegmentInterface) algo)
							.getPoly() == poly) {
				continue;
			}
			algo.remove();

		}

		algoList.clear();
		// remove point
		oldPoint.doRemove();

	}

	/**
	 * Calls doRemove() for all output objects of this algorithm except for
	 * keepGeo.
	 */
	@Override
	public void removeOutputExcept(GeoElement keepGeo) {
		for (int i = 0; i < super.getOutputLength(); i++) {
			GeoElement geo = super.getOutput(i);
			if (geo != keepGeo) {
				if (geo.isGeoPoint()) {
					removePoint(geo);
				} else {
					geo.doRemove();
				}
			}
		}
	}

	@Override
	public void calcArea() {

		// more accurate method for 2D
		if (A instanceof GeoPoint && B instanceof GeoPoint
				&& centerPoint instanceof GeoPoint) {

			// area = 1/2 | det(P[i], P[i+1]) |
			double area = GeoPoint.det((GeoPoint) A, (GeoPoint) B);
			area += GeoPoint.det((GeoPoint) B, (GeoPoint) this.centerPoint);
			area += GeoPoint.det((GeoPoint) this.centerPoint, (GeoPoint) A);
			area = area * this.n / 2;

			getPoly().setArea(area);

			return;
		}

		// TODO: more accurate method should be possible for 3D too
		double radius = A.distance(centerPoint);

		// 1/2 a b sin(C)
		getPoly().setArea(n * radius * radius * Math.sin(alpha) / 2.0);
	}

}
