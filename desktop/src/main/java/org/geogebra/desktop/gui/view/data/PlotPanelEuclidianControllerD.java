package org.geogebra.desktop.gui.view.data;

import org.geogebra.common.awt.GPoint;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.desktop.euclidian.EuclidianControllerD;

public class PlotPanelEuclidianControllerD extends EuclidianControllerD {

	public PlotPanelEuclidianControllerD(Kernel kernel) {
		super(kernel);
	}

	@Override
	public void showDrawingPadPopup(GPoint mouseLoc) {
		// do nothing
	}
}