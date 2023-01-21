package org.geogebra.common.kernel.algos;

import org.geogebra.common.kernel.FixedPathRegionAlgo;
import org.geogebra.common.kernel.kernelND.GeoPointND;

public interface AlgoDynamicCoordinatesInterface extends FixedPathRegionAlgo {

	GeoPointND getParentPoint();

}
