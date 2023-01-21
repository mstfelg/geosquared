package org.geogebra.common.kernel;

import org.geogebra.common.kernel.algos.AlgoRotate;
import org.geogebra.common.kernel.algos.AlgoRotatePoint;
import org.geogebra.common.kernel.algos.AlgoTransformation;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Rotation
 * 
 * @author Zbynek
 * 
 */
public class TransformRotate extends Transform {
	/** center of rotation */
	protected GeoPointND center;
	/** angle of rotation */
	protected GeoNumberValue angle;

	/**
	 * @param cons
	 *            construction
	 * @param angle
	 *            rotation angle
	 */
	public TransformRotate(Construction cons, GeoNumberValue angle) {
		this.angle = angle;
		this.cons = cons;
	}

	/**
	 * @param cons
	 *            construction
	 * @param angle
	 *            rotation angle
	 * @param center
	 *            rotation center
	 */
	public TransformRotate(Construction cons, GeoNumberValue angle,
			GeoPointND center) {
		this.angle = angle;
		this.center = center;
		this.cons = cons;
	}

	@Override
	protected AlgoTransformation getTransformAlgo(GeoElement geo) {
		AlgoTransformation algo = null;
		if (center == null) {
			algo = new AlgoRotate(cons, geo, angle);
		} else {
			algo = new AlgoRotatePoint(cons, geo, angle, center);
		}
		return algo;
	}

}
