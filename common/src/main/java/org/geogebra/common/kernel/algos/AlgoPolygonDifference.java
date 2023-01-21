package org.geogebra.common.kernel.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPolygon;

/**
 * @author thilina
 *
 */
public class AlgoPolygonDifference extends AlgoPolygonOperation {

	// input
	private final GeoBoolean exclusive;

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
	public AlgoPolygonDifference(Construction cons, String[] labels,
			GeoPolygon inPoly0, GeoPolygon inPoly1, GeoBoolean exclusive) {

		super(cons, labels, inPoly0, inPoly1);
		this.exclusive = exclusive;
		this.threeArgs = exclusive != null;

		this.initiatePolyOperation(getOp(exclusive));

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
	 * @param exclusive
	 *            whether this is XOR
	 * @param outputSizes
	 *            sizes of the results of the operation. consist of polygon
	 *            size, point size, and segment size
	 */
	public AlgoPolygonDifference(Construction cons, String[] labels,
			GeoPolygon inPoly0, GeoPolygon inPoly1, GeoBoolean exclusive,
			int[] outputSizes) {

		super(cons, labels, inPoly0, inPoly1, getOp(exclusive));
		this.exclusive = exclusive;
		this.threeArgs = exclusive != null;
		initialize(outputSizes);
	}

	/**
	 * @param exclusive
	 *            whether XOR should be returned
	 * @return XOR or DIFFERENCE
	 */
	public static PolyOperation getOp(GeoBoolean exclusive) {
		return (exclusive != null && exclusive.getBoolean()) ? PolyOperation.XOR
				: PolyOperation.DIFFERENCE;
	}

	@Override
	public void compute() {
		this.operationType = getOp(exclusive);
		super.compute();
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