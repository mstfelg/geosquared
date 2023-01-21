package org.geogebra.common.geogebra3D.kernel3D.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoPolygonOperation.PolyOperation;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoPoly;

/**
 * AlgoElement class for finding intersection path(region) between two 3D
 * polygons
 * 
 * @author thilina
 *
 */
public class AlgoIntersectPathPolygons3D extends AlgoPolygonOperations3D {

	/**
	 * common constructor
	 * 
	 * @param cons
	 *            construction
	 * @param labels
	 *            labels string array
	 * @param inPoly0
	 *            first input polygon
	 * @param inPoly1
	 *            second input polygon
	 */
	public AlgoIntersectPathPolygons3D(Construction cons, String[] labels,
			GeoPoly inPoly0, GeoPoly inPoly1) {

		super(cons, labels, inPoly0, inPoly1, PolyOperation.INTERSECTION);
		initialize(null);
	}

	@Override
	public GetCommand getClassName() {
		return Commands.IntersectPath;
	}

}
