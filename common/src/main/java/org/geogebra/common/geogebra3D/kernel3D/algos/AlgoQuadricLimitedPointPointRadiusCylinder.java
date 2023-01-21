package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoQuadricNDConstants;
import org.geogebra.common.kernel.matrix.Coords;

/**
 * Algo for cylinder between two end points and given radius.
 * 
 * @author mathieu
 *
 */
public class AlgoQuadricLimitedPointPointRadiusCylinder
		extends AlgoQuadricLimitedPointPointRadius {

	/**
	 * 
	 * @param c
	 *            construction
	 * @param labels
	 *            output labels
	 * @param origin
	 *            center of bottom
	 * @param secondPoint
	 *            center of top
	 * @param r
	 *            radius
	 */
	public AlgoQuadricLimitedPointPointRadiusCylinder(Construction c,
			String[] labels, GeoPointND origin, GeoPointND secondPoint,
			GeoNumberValue r) {
		super(c, labels, origin, secondPoint, r,
				GeoQuadricNDConstants.QUADRIC_CYLINDER);

	}

	@Override
	protected AlgoQuadricEnds createEnds() {
		AlgoQuadricEnds algo2 = new AlgoQuadricEnds(cons, getQuadric(), true);
		bottom = algo2.getSection1();
		top = algo2.getSection2();
		return algo2;
	}

	@Override
	protected void setOutput() {
		setOutput(new GeoElement[] { getQuadric(), getQuadric().getBottom(),
				getQuadric().getTop(), getQuadric().getSide() });
	}

	@Override
	protected void setQuadric(Coords o1, Coords o2, Coords d, double r,
			double min, double max) {
		getQuadric().setCylinder(o1, d, r, min, max);
	}

	@Override
	public Commands getClassName() {
		return Commands.Cylinder;
	}

	// //////////////////////
	// ALGOTRANSFORMABLE
	// //////////////////////

	@Override
	protected AlgoElement getTransformedAlgo(String[] labels, GeoPointND p1,
			GeoPointND p2, GeoNumeric r) {
		return new AlgoQuadricLimitedPointPointRadiusCylinder(this.cons, labels,
				p1, p2, r);
	}

}
