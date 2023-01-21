package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPlane3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoCoordSys2D;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.matrix.CoordSys;
import org.geogebra.common.kernel.matrix.Coords;

/**
 * @author ggb3D
 *
 */
public class AlgoPlaneThreePoints extends AlgoElement3D {

	/** the 2D coord sys created */
	protected GeoCoordSys2D cs;

	/** 3D points */
	private GeoPointND A;
	private GeoPointND B;
	private GeoPointND C;

	/**
	 * create a plane joining points, with label.
	 * 
	 * @param c
	 *            construction
	 * @param label
	 *            label of the plane
	 * @param A
	 *            first point
	 * @param B
	 *            second point
	 * @param C
	 *            third point
	 */
	public AlgoPlaneThreePoints(Construction c, String label, GeoPointND A,
			GeoPointND B, GeoPointND C) {
		this(c, A, B, C);

		((GeoElement) cs).setLabel(label);

	}

	/**
	 * create a plane joining points, with label.
	 * 
	 * @param c
	 *            construction
	 * @param A
	 *            first point
	 * @param B
	 *            second point
	 * @param C
	 *            third point
	 */
	public AlgoPlaneThreePoints(Construction c, GeoPointND A, GeoPointND B,
			GeoPointND C) {
		super(c);

		this.A = A;
		this.B = B;
		this.C = C;

		cs = new GeoPlane3D(c);

		// set input and output
		setInputOutput(new GeoElement[] { (GeoElement) A, (GeoElement) B,
				(GeoElement) C }, new GeoElement[] { (GeoElement) cs });

	}

	@Override
	public void compute() {

		CoordSys coordsys = cs.getCoordSys();

		if ((!A.isDefined()) || (!B.isDefined()) || (!C.isDefined())) {
			coordsys.setUndefined();
			return;
		}

		// recompute the coord sys
		coordsys.resetCoordSys();

		Coords cA = A.getInhomCoordsInD3();
		Coords cB = B.getInhomCoordsInD3();
		Coords cC = C.getInhomCoordsInD3();
		coordsys.addPoint(cA);
		coordsys.addPoint(cB);
		coordsys.addPoint(cC);

		if (coordsys.makeOrthoMatrix(false, false)) {
			if (coordsys.isDefined()) {
				coordsys.setEquationVector(cA, cB, cC);
			}
		}

		// Application.debug(cs.getCoordSys().getMatrixOrthonormal().toString());

	}

	/**
	 * return the cs
	 * 
	 * @return the cs
	 */
	public GeoCoordSys2D getCoordSys() {
		return cs;
	}

	@Override
	public Commands getClassName() {
		return Commands.Plane;
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlain("PlaneThroughABC", A.getLabel(tpl),
				B.getLabel(tpl), C.getLabel(tpl));

	}

}
