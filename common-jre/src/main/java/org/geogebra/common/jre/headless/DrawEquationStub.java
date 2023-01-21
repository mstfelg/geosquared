package org.geogebra.common.jre.headless;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.awt.GDimension;
import org.geogebra.common.awt.GFont;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.euclidian.DrawEquation;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;

import com.himamis.retex.renderer.share.platform.graphics.Color;
import com.himamis.retex.renderer.share.platform.graphics.Image;

public class DrawEquationStub extends DrawEquation {

	@Override
	public GDimension drawEquation(App app, GeoElementND geo, GGraphics2D g2, int x, int y,
								   String text, GFont font, boolean serif, GColor fgColor,
								   GColor bgColor, boolean useCache, boolean updateAgain,
								   Runnable callback) {
		return AwtFactory.getPrototype().newDimension(0, 0);
	}

	@Override
	public Image getCachedDimensions(String text, GeoElementND geo, Color fgColor,
									 GFont font, int style, int[] ret) {
		return null;
	}

	@Override
	public void checkFirstCall(App app) {
		// unused
	}

	@Override
	public Color convertColor(GColor color) {
		return null;
	}

	@Override
	public GDimension measureEquation(App app, GeoElement geo0, String text, GFont font,
									  boolean serif) {
		return AwtFactory.getPrototype().newDimension(0, 0);
	}
}
