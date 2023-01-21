package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPolygon3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoPolygonDifference;
import org.geogebra.common.kernel.algos.AlgoPolygonOperation.PolyOperation;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPolygon;

/**
 * AlgoElement class for finding difference (region difference) of two 3D
 * polygons
 * 
 * @author thilina
 *
 */
public class AlgoDifferencePolygons3D extends AlgoPolygonOperations3D {

	// input
	private GeoBoolean exclusive = null;

	private boolean threeArgs = false;

	/**
	 * 
	 * @param cons
	 *            construction
	 * @param labels
	 *            labels for the output
	 * @param inPoly0
	 *            first input polygon
	 * @param inPoly1
	 *            second input polygon
	 * @param exclusive
	 *            third input exclusive difference or not
	 */
	public AlgoDifferencePolygons3D(Construction cons, String[] labels,
			GeoPolygon inPoly0, GeoPolygon inPoly1, GeoBoolean exclusive) {

		super(cons, labels, inPoly0, inPoly1);
		this.exclusive = exclusive;
		this.threeArgs = true;

		initiatePolyOperation(AlgoPolygonDifference.getOp(exclusive));
	}

	/**
	 * 
	 * @param cons
	 *            construction
	 * @param labels
	 *            labels for the output
	 * @param inPoly0
	 *            first input polygon
	 * @param inPoly1
	 *            second input polygon
	 */
	public AlgoDifferencePolygons3D(Construction cons, String[] labels,
			GeoPolygon inPoly0, GeoPolygon inPoly1) {

		super(cons, labels, inPoly0, inPoly1, PolyOperation.DIFFERENCE);
		initialize(null);

	}

	/**
	 * 
	 * @param cons
	 *            construction
	 * @param labels
	 *            labels for the output
	 * @param inPoly0
	 *            first input polygon
	 * @param inPoly1
	 *            second input polygon
	 * @param outputSizes
	 *            sizes of the results of the operation. consist of polygon
	 *            size, point size, and segment size
	 */
	public AlgoDifferencePolygons3D(Construction cons, String[] labels,
			GeoPolygon3D inPoly0, GeoPolygon3D inPoly1, int[] outputSizes) {

		super(cons, labels, inPoly0, inPoly1, PolyOperation.DIFFERENCE);
		initialize(outputSizes);
	}

	@Override
	protected void compute(boolean useLabels) {
		this.operationType = AlgoPolygonDifference.getOp(exclusive);
		super.compute(useLabels);
	}

	@Override
	protected void setInputOutput() {

		if (this.threeArgs) {
			input = new GeoElement[3];
			input[0] = this.inPoly0;
			input[1] = this.inPoly1;
			input[2] = this.exclusive;
		} else {
			input = new GeoElement[2];
			input[0] = inPoly0;
			input[1] = inPoly1;
		}
		// set dependencies
		for (int i = 0; i < input.length; i++) {
			input[i].addAlgorithm(this);
		}
		cons.addToAlgorithmList(this);

		setDependencies();

	}

	@Override
	public GetCommand getClassName() {
		return Commands.Difference;
	}

}
