package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPoint3D;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Single intersection point
 */
public class AlgoIntersectSingle3D extends AlgoIntersect3D {

	// input
	private AlgoIntersect3D algo;
	private int index; // index of point in algo
	private GeoPointND refPoint;

	// output
	private GeoPoint3D point;

	private GeoPoint3D[] parentOutput;

	/**
	 * intersection point is the (a) nearest to refPoint
	 * 
	 * @param label
	 *            output label
	 * @param algo
	 *            generic intersect algo
	 * @param refPoint
	 *            closest point
	 */
	AlgoIntersectSingle3D(String label, AlgoIntersect3D algo,
			GeoPointND refPoint) {
		super(algo.getConstruction());
		this.algo = algo;
		algo.addUser(); // this algorithm is a user of algo
		this.refPoint = refPoint;

		point = new GeoPoint3D(algo.getConstruction());

		setInputOutput();
		initForNearToRelationship();
		compute();
		point.setLabel(label);
	}

	/**
	 * intersection point is index-th intersection point of algo
	 * 
	 * @param label
	 *            output label
	 * @param algo
	 *            intersect algo
	 * @param index0
	 *            output index
	 */
	AlgoIntersectSingle3D(String label, AlgoIntersect3D algo, int index0) {
		super(algo.getConstruction());
		this.algo = algo;
		algo.addUser(); // this algorithm is a user of algo

		// check index
		if (index0 < 0) {
			this.index = 0;
		} else {
			this.index = index0;
		}

		point = new GeoPoint3D(algo.getConstruction());

		setInputOutput();
		initForNearToRelationship();
		compute();
		point.setLabel(label);
	}

	@Override
	protected boolean showUndefinedPointsInAlgebraView() {
		return true;
	}

	@Override
	public Commands getClassName() {
		return Commands.Intersect;
	}

	@Override
	public int getRelatedModeID() {
		return EuclidianConstants.MODE_INTERSECT;
	}

	// for AlgoElement
	@Override
	public void setInputOutput() {
		if (refPoint == null) {
			input = new GeoElement[3];
			input[0] = algo.getInput()[0];
			input[1] = algo.getInput()[1];
			// dummy value to store the index of the intersection point
			// index + 1 is used here to let numbering start at 1
			input[2] = new GeoNumeric(cons, index + 1);
		} else {
			input = new GeoElement[3];
			input[0] = algo.getInput()[0];
			input[1] = algo.getInput()[1];
			input[2] = (GeoElement) refPoint;
		}

		setOutputLength(1);
		this.setOutput(0, point);

		setDependencies(); // done by AlgoElement
	}

	/**
	 * @return intersection point
	 */
	public GeoPoint3D getPoint() {
		return point;
	}

	@Override
	public GeoPoint3D[] getIntersectionPoints() {
		return (GeoPoint3D[]) getOutput();
	}

	@Override
	protected GeoPoint3D[] getLastDefinedIntersectionPoints() {
		return null;
	}

	@Override
	public boolean isNearToAlgorithm() {
		return true;
	}

	@Override
	public final void initForNearToRelationship() {
		parentOutput = algo.getIntersectionPoints();

		// tell parent algorithm about the loaded position;
		// this is needed for initing the intersection algo with
		// the intersection point stored in XML files
		algo.initForNearToRelationship();
		algo.setIntersectionPoint(index, point);
		algo.compute();
	}

	@Override
	public void compute() {
		parentOutput = algo.getIntersectionPoints();

		if (refPoint != null) {
			if (refPoint.isDefined()) {
				index = algo.getClosestPointIndex(refPoint);
			}
		}

		if (input[0].isDefined() && input[1].isDefined()
				&& index < parentOutput.length) {
			// get coordinates from helper algorithm
			point.setCoords(parentOutput[index].getCoords());
		} else {
			point.setUndefined();
		}
	}

	@Override
	public void remove() {
		super.remove();
		algo.removeUser(); // this algorithm was a user of algo
	}

}
