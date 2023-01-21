package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.arithmetic.VectorValue;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoVector;

/**
 * Converts number, list, vector or point to complex number or polar
 * vector/point
 *
 */
public class AlgoToComplexPolar extends AlgoElement {
	private int coordStyle;
	private GeoPoint inPoint;
	private GeoVector inVector;
	private GeoPoint outPoint;
	private GeoList inList;
	private GeoVector outVector;
	private GeoNumberValue inNumber;

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param geoPoint
	 *            input point
	 * @param coordStyle
	 *            Kernel.COORD_COMPLEX or COORD_POLAR
	 */
	public AlgoToComplexPolar(Construction cons, String label,
			GeoPoint geoPoint, int coordStyle) {
		super(cons);
		inPoint = geoPoint;
		outPoint = new GeoPoint(cons);
		init(coordStyle, outPoint, label);
	}

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param geoList
	 *            input list
	 * @param coordStyle
	 *            Kernel.COORD_COMPLEX or COORD_POLAR
	 */
	public AlgoToComplexPolar(Construction cons, String label, GeoList geoList,
			int coordStyle) {
		super(cons);
		inList = geoList;
		outPoint = new GeoPoint(cons);
		init(coordStyle, outPoint, label);
	}

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param geoNum
	 *            input number
	 * @param coordStyle
	 *            Kernel.COORD_COMPLEX or COORD_POLAR
	 */
	public AlgoToComplexPolar(Construction cons, String label,
			GeoNumberValue geoNum, int coordStyle) {
		super(cons);
		inNumber = geoNum;
		outPoint = new GeoPoint(cons);
		init(coordStyle, outPoint, label);
	}

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param geoVector
	 *            input vector
	 * @param coordStyle
	 *            Kernel.COORD_COMPLEX or COORD_POLAR
	 */
	public AlgoToComplexPolar(Construction cons, String label,
			GeoVector geoVector, int coordStyle) {
		super(cons);
		inVector = geoVector;
		outVector = new GeoVector(cons);
		init(coordStyle, outVector, label);
	}

	private void init(int coordStyle1, GeoElement out, String label) {
		this.coordStyle = coordStyle1;
		setInputOutput();
		compute();
		((VectorValue) out).setMode(coordStyle1);
		out.setLabel(label);
	}

	@Override
	protected void setInputOutput() {
		if (inVector != null) {
			setOnlyOutput(outVector);
			input = new GeoElement[] { inVector };
		} else if (inNumber != null) {
			setOnlyOutput(outPoint);
			input = new GeoElement[] { inNumber.toGeoElement() };
		} else {
			setOnlyOutput(outPoint);
			input = new GeoElement[] { inPoint == null ? inList : inPoint };
		}
		setDependencies();
	}

	@Override
	public void compute() {
		if (inPoint != null) {
			outPoint.set(inPoint);
			outPoint.setMode(coordStyle);
			return;
		}
		if (inVector != null) {
			outVector.set(inVector);
			outVector.setMode(coordStyle);
			return;
		}
		if (inNumber != null) {
			outPoint.setCoords(inNumber.getDouble(), 0, 1);
			outPoint.setMode(coordStyle);
			return;
		}
		outPoint.setCoords(inList.get(0).evaluateDouble(),
				inList.get(1).evaluateDouble(), 1);
		outPoint.setMode(coordStyle);
	}

	@Override
	public Commands getClassName() {
		switch (coordStyle) {
		case Kernel.COORD_COMPLEX:
			return Commands.ToComplex;
		case Kernel.COORD_POLAR:
			return Commands.ToPolar;
		default:
			return Commands.ToPoint;
		}
	}

	/**
	 * @return resulting point/vector
	 */
	public GeoElement getResult() {
		return inVector == null ? outPoint : outVector;
	}

}
