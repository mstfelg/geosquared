package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.barycentric.CmdBarycenter;
import org.geogebra.common.kernel.barycentric.CmdCubic;
import org.geogebra.common.kernel.barycentric.CmdKimberling;
import org.geogebra.common.kernel.barycentric.CmdTriangleCurve;
import org.geogebra.common.kernel.barycentric.CmdTrilinear;
import org.geogebra.common.kernel.discrete.CmdConvexHull;
import org.geogebra.common.kernel.discrete.CmdDelauneyTriangulation;
import org.geogebra.common.kernel.discrete.CmdHull;
import org.geogebra.common.kernel.discrete.CmdMinimumSpanningTree;
import org.geogebra.common.kernel.discrete.CmdShortestDistance;
import org.geogebra.common.kernel.discrete.CmdTravelingSalesman;
import org.geogebra.common.kernel.discrete.CmdVoronoi;

/**
 * class to split off some CmdXXX classes into another jar (for faster applet
 * loading)
 *
 */
public class CommandDispatcherDiscrete implements CommandDispatcherInterface {
	@Override
	public CommandProcessor dispatch(Commands c, Kernel kernel) {
		switch (c) {

		case TriangleCenter:
			return new CmdKimberling(kernel);
		case Barycenter:
			return new CmdBarycenter(kernel);
		case Trilinear:
			return new CmdTrilinear(kernel);
		case Cubic:
			return new CmdCubic(kernel);
		case TriangleCurve:
			return new CmdTriangleCurve(kernel);

		case Voronoi:
			return new CmdVoronoi(kernel);
		case Hull:
			return new CmdHull(kernel);
		case ConvexHull:
			return new CmdConvexHull(kernel);
		case MinimumSpanningTree:
			return new CmdMinimumSpanningTree(kernel);
		case DelaunayTriangulation:
		case DelauneyTriangulation:
			return new CmdDelauneyTriangulation(kernel);
		case TravelingSalesman:
			return new CmdTravelingSalesman(kernel);
		case ShortestDistance:
			return new CmdShortestDistance(kernel);
		default:
			break;

		}
		return null;
	}
}
